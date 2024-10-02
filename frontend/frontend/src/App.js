import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polyline } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import axios from 'axios';
import { Icon } from 'leaflet';

const API_KEY = '5b3ce3597851110001cf62485d0023abcb8645f5956de13109fcdfff';
const GEOCODING_API_KEY = 'f400a7228cef45b1ad825f7e4f4d20a8'; // Replace with your OpenCage Data API key

const customIcon = new Icon({
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41]
});

function App() {
    const [tours, setTours] = useState([]);
    const [tourLogs, setTourLogs] = useState([]);
    const [showAddForm, setShowAddForm] = useState(false);
    const [showRemoveForm, setShowRemoveForm] = useState(false);
    const [showImportModal, setShowImportModal] = useState(false);
    const [showReportModal, setShowReportModal] = useState(false);
    const [formType, setFormType] = useState('Tour'); // to differentiate between Tour and Tour Log forms
    const [newTour, setNewTour] = useState({
        name: '',
        tourDescription: '',
        tourFrom: '',
        tourTo: '',
        transportType: ''
    });
    const [newTourLog, setNewTourLog] = useState({
        tourID: '',
        date: '',
        time: '',
        comment: '',
        difficulty: '',
        totalDistance: '',
        totalTime: '',
        rating: ''
    });
    const [removeId, setRemoveId] = useState('');
    const [mapMarkers, setMapMarkers] = useState([]);
    const [pathCoordinates, setPathCoordinates] = useState([]);
    const [activeTab, setActiveTab] = useState('Tours');
    const [importFile, setImportFile] = useState(null);
    const [reportTourId, setReportTourId] = useState('');
    const [dropdownVisible, setDropdownVisible] = useState(false);
    const [reportDropdownVisible, setReportDropdownVisible] = useState(false);

    useEffect(() => {
        fetch('http://localhost:8080/api/tours')
            .then(response => response.json())
            .then(data => setTours(data))
            .catch(error => console.error('Error fetching tours:', error));

        fetch('http://localhost:8080/api/tourlogs')
            .then(response => response.json())
            .then(data => setTourLogs(data))
            .catch(error => console.error('Error fetching tour logs:', error));
    }, []);

    const mapWrapperStyle = {
        position: 'relative',
        width: '100%',
        height: '400px',
        overflow: "hidden"
    };

    const mapContainerStyle = {
        width: '100%',
        height: '100%'
    };

    const handleAddTour = () => {
        setFormType('Tour');
        setShowAddForm(true);
        setShowRemoveForm(false);
    };

    const handleRemoveTour = () => {
        setFormType('Tour');
        setShowRemoveForm(true);
        setShowAddForm(false);
    };

    const handleAddTourLog = () => {
        setFormType('TourLog');
        setShowAddForm(true);
        setShowRemoveForm(false);
    };

    const handleRemoveTourLog = () => {
        setFormType('TourLog');
        setShowRemoveForm(true);
        setShowAddForm(false);
    };

    const handleAddFormChange = (e) => {
        const { name, value } = e.target;
        setNewTour({ ...newTour, [name]: value });
    };

    const handleAddTourLogFormChange = (e) => {
        const { name, value } = e.target;
        setNewTourLog({ ...newTourLog, [name]: value });
    };

    const handleAddFormSubmit = async (e) => {
        e.preventDefault();
    
        // Input Validation
        if (!newTour.tourFrom.trim() || !newTour.tourTo.trim()) {
            alert('Please provide both "From" and "To" locations.');
            return;
        }
    
        try {
            const fromResponse = await axios.get(`https://api.opencagedata.com/geocode/v1/json?q=${encodeURIComponent(newTour.tourFrom)}&key=${GEOCODING_API_KEY}`);
            if (!fromResponse.data.results.length) {
                throw new Error('Invalid "From" location.');
            }
            const fromCoords = fromResponse.data.results[0].geometry;
    
            const toResponse = await axios.get(`https://api.opencagedata.com/geocode/v1/json?q=${encodeURIComponent(newTour.tourTo)}&key=${GEOCODING_API_KEY}`);
            if (!toResponse.data.results.length) {
                throw new Error('Invalid "To" location.');
            }
            const toCoords = toResponse.data.results[0].geometry;
    
            console.log(fromCoords, toCoords);
    
            const orsResponse = await axios.get(`https://api.openrouteservice.org/v2/directions/driving-car`, {
                params: {
                    api_key: API_KEY,
                    start: `${fromCoords.lng},${fromCoords.lat}`,
                    end: `${toCoords.lng},${toCoords.lat}`
                }
            });
    
            const { distance, duration } = orsResponse.data.features[0].properties.segments[0];
    
            const distanceMeters = `${distance.toFixed(2)} m`;
            const durationMinutes = Math.floor(duration / 60);
            const durationSeconds = (duration % 60).toFixed(2);
            const durationFormatted = `${durationMinutes} min ${durationSeconds} s`;
    
            const tourData = {
                ...newTour,
                tourDistance: distanceMeters,
                estTime: durationFormatted,
                routeInfo: null
            };
    
            const response = await fetch('http://localhost:8080/api/tours/createTour', { // Added 'http://'
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(tourData)
            });
    
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to create tour.');
            }
    
            const data = await response.json();
            setTours([...tours, data]);
            setNewTour({
                name: '',
                tourDescription: '',
                tourFrom: '',
                tourTo: '',
                transportType: ''
            });
            setShowAddForm(false);
    
            setMapMarkers([
                { coords: fromCoords, name: newTour.tourFrom },
                { coords: toCoords, name: newTour.tourTo }
            ]);
    
            const pathCoords = orsResponse.data.features[0].geometry.coordinates.map(coord => ({
                lat: coord[1],
                lng: coord[0]
            }));
            setPathCoordinates(pathCoords);
        } catch (error) {
            console.error('Error adding new tour:', error);
            alert(`Error adding new tour: ${error.message}`);
        }
    };
    

    const handleAddTourLogFormSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/api/tourlogs/createTourLog', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newTourLog)
            });

            const data = await response.json();
            setTourLogs([...tourLogs, data]);
            setNewTourLog({
                tourID: '',
                date: '',
                time: '',
                comment: '',
                difficulty: '',
                totalDistance: '',
                totalTime: '',
                rating: ''
            });
            setShowAddForm(false);
        } catch (error) {
            console.error('Error adding new tour log:', error);
        }
    };

    const handleRemoveFormChange = (e) => {
        setRemoveId(e.target.value);
    };

    const handleRemoveFormSubmit = (e) => {
        e.preventDefault();
        const url = formType === 'Tour' ? `http://localhost:8080/api/tours/deleteTour/${removeId}` : `http://localhost:8080/api/tourLogs/deleteTourLog/${removeId}`;
        fetch(url, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    if (formType === 'Tour') {
                        setTours(tours.filter(tour => tour.id !== parseInt(removeId)));
                    } else {
                        setTourLogs(tourLogs.filter(log => log.id !== parseInt(removeId)));
                    }
                    setShowRemoveForm(false);
                } else {
                    throw new Error('Failed to delete');
                }
            })
            .catch(error => console.error('Error deleting:', error));
    };

    const handleFileChange = (e) => {
        setImportFile(e.target.files[0]);
    };

    const handleImportSubmit = async () => {
        if (!importFile) return;

        const reader = new FileReader();
        reader.onload = async (e) => {
            const fileContent = e.target.result;

            try {
                const response = await axios.post('http://localhost:8080/api/tours/createTour', JSON.parse(fileContent), {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

                const data = await response.data;
                setTours([...tours, data]);
                setShowImportModal(false);
            } catch (error) {
                console.error('Error importing tour:', error);
            }
        };

        reader.readAsText(importFile);
    };

    const handleExportTours = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/tours');
            const toursData = response.data;

            const blob = new Blob([JSON.stringify(toursData, null, 2)], { type: 'application/json' });
            const url = URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = 'exported_tours.json';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        } catch (error) {
            console.error('Error exporting tours:', error);
        }
    };

    const handleGenerateReport = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/tours/tourReport/${reportTourId}`, { responseType: 'blob' });
            const blob = new Blob([response.data], { type: 'application/pdf' });
            const url = URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = `tour_report_${reportTourId}.pdf`;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            setShowReportModal(false);
        } catch (error) {
            console.error('Error generating report:', error);
        }
    };

    const handleSummarizeReport = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/tours/summarizeReport`, { responseType: 'blob' });
            const blob = new Blob([response.data], { type: 'application/pdf' });
            const url = URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = `tour_report.pdf`;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            setShowReportModal(false);
        } catch (error) {
            console.error('Error generating report:', error);
        }
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
            <nav style={{ backgroundColor: '#333', padding: '10px', display: 'flex' }}>
                <div style={{ position: 'relative' }}>
                    <button id="file" style={buttonStyle} onClick={() => setDropdownVisible(!dropdownVisible)}>File</button>
                    {dropdownVisible && (
                        <div style={dropdownContentStyle}>
                            <button onClick={() => { setShowImportModal(true); setDropdownVisible(false); }} style={dropdownItemStyle}>Import Tour</button>
                            <button onClick={handleExportTours} style={dropdownItemStyle}>Export Tour</button>
                            <div style={{ position: 'relative' }}>
                                <button onClick={() => setReportDropdownVisible(!reportDropdownVisible)} style={dropdownItemStyle}>Generate Report</button>
                                {reportDropdownVisible && (
                                    <div style={dropdownContentStyle}>
                                        <button onClick={() => { setShowReportModal(true); setReportDropdownVisible(false); }} style={dropdownItemStyle}>Tour Report</button>
                                        <button onClick={handleSummarizeReport} style={dropdownItemStyle}>Summarize Report</button>
                                    </div>
                                )}
                            </div>
                        </div>
                    )}
                </div>
                <button id="edit" style={buttonStyle}>Edit</button>
                <button id="options" style={buttonStyle}>Options</button>
                <button id="help" style={buttonStyle}>Help</button>
            </nav>

            <div style={{ display: 'flex', flex: 1 }}>
                <div style={{ flex: 1, padding: '20px' }}>
                    <h2 style={{ marginBottom: '10px' }}>Tour List</h2>
                    <ul style={{ listStyle: 'none', padding: 0, marginBottom: '20px' }}>
                        {tours.map((tour, index) => (
                            <li key={index}>{tour.name}</li>
                        ))}
                    </ul>
                </div>

                <div style={{ flex: 2, display: 'flex', flexDirection: 'column' }}>
                    <div style={{ flex: 1, padding: '20px', border: '1px solid #ccc', marginRight: '10px' }}>
                        <div className="map-container" style={mapContainerStyle}>
                            <h2>Map</h2>
                            <div style={{ height: '400px', width: '100%', ...mapWrapperStyle }}>
                                <MapContainer center={[48.2082, 16.3738]} zoom={13} style={{ height: '100%', width: '100%' }}>
                                    <TileLayer
                                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                                    />
                                    {mapMarkers.map((marker, index) => (
                                        <Marker
                                            key={index}
                                            position={[marker.coords.lat, marker.coords.lng]}
                                            icon={customIcon}
                                        >
                                            <Popup>{marker.name}</Popup>
                                        </Marker>
                                    ))}
                                    {pathCoordinates.length > 0 && (
                                        <Polyline positions={pathCoordinates} color="blue" />
                                    )}
                                </MapContainer>
                            </div>
                        </div>
                    </div>

                    <div style={{flex: 1, padding: '20px', border: '1px solid #ccc', marginBottom: '10px'}}>
                        <div>
                            <button onClick={() => setActiveTab('Tours')} style={tabButtonStyle}>
                                Tours
                            </button>
                            <button onClick={() => setActiveTab('Tour Logs')} style={tabButtonStyle}>
                                Tour Logs
                            </button>
                        </div>
                        <div style={{display: 'flex', justifyContent: 'flex-end', marginTop: '10px'}}>
                            {activeTab === 'Tours' && (
                                <>
                                    <button id="add-tour-button" onClick={handleAddTour} style={addRemoveButton}>
                                        <span>+</span></button>
                                    <button id="remove-tour-button" onClick={handleRemoveTour} style={addRemoveButton}>
                                        <span>-</span></button>
                                </>
                            )}
                            {activeTab === 'Tour Logs' && (
                                <>
                                    <button id="add-tourlog-button" onClick={handleAddTourLog} style={addRemoveButton}>
                                        <span>+</span></button>
                                    <button id="remove-tourlog-button" onClick={handleRemoveTourLog}
                                            style={addRemoveButton}><span>-</span></button>
                                </>
                            )}
                        </div>
                        {activeTab === 'Tours' && (
                            <div>
                                <h2>Tours</h2>
                                <table style={{width: '100%', borderCollapse: 'collapse'}}>
                                    <thead>
                                    <tr>
                                        <th style={tableHeaderStyle}>Tour ID</th>
                                        <th style={tableHeaderStyle}>Tour Name</th>
                                        <th style={tableHeaderStyle}>Tour Description</th>
                                        <th style={tableHeaderStyle}>From</th>
                                        <th style={tableHeaderStyle}>To</th>
                                        <th style={tableHeaderStyle}>Transport Type</th>
                                        <th style={tableHeaderStyle}>Tour Distance</th>
                                        <th style={tableHeaderStyle}>Duration</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {tours.map((tour, index) => (
                                        <tr key={index}>
                                            <td style={tableCellStyle}>{tour.id}</td>
                                            <td style={tableCellStyle}>{tour.name}</td>
                                            <td style={tableCellStyle}>{tour.tourDescription}</td>
                                            <td style={tableCellStyle}>{tour.fromm}</td>
                                            <td style={tableCellStyle}>{tour.too}</td>
                                            <td style={tableCellStyle}>{tour.transportType}</td>
                                            <td style={tableCellStyle}>{tour.tourDistance}</td>
                                            <td style={tableCellStyle}>{tour.estimatedTime}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                        {activeTab === 'Tour Logs' && (
                            <div>
                                <h2>Tour Logs</h2>
                                <table style={{width: '100%', borderCollapse: 'collapse'}}>
                                    <thead>
                                    <tr>
                                        <th style={tableHeaderStyle}>ID</th>
                                        <th style={tableHeaderStyle}>TourID</th>
                                        <th style={tableHeaderStyle}>Date</th>
                                        <th style={tableHeaderStyle}>Time</th>
                                        <th style={tableHeaderStyle}>Comment</th>
                                        <th style={tableHeaderStyle}>Difficulty</th>
                                        <th style={tableHeaderStyle}>Total Distance</th>
                                        <th style={tableHeaderStyle}>Total Time</th>
                                        <th style={tableHeaderStyle}>Rating</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {tourLogs.map((log, index) => (
                                        <tr key={index}>
                                            <td style={tableCellStyle}>{log.id}</td>
                                            <td style={tableCellStyle}>{log.tourid}</td>
                                            <td style={tableCellStyle}>{log.date}</td>
                                            <td style={tableCellStyle}>{log.time}</td>
                                            <td style={tableCellStyle}>{log.comment}</td>
                                            <td style={tableCellStyle}>{log.difficulty}</td>
                                            <td style={tableCellStyle}>{log.totalDistance}</td>
                                            <td style={tableCellStyle}>{log.totalTime}</td>
                                            <td style={tableCellStyle}>{log.rating}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                </div>
            </div>

            {showAddForm && formType === 'Tour' && (
    <div style={{
        position: 'absolute',
        bottom: '20px',
        left: '20px',
        padding: '20px',
        border: '1px solid #ccc',
        backgroundColor: '#f9f9f9'
    }}>
        <h2>Add New Tour</h2>
        <form onSubmit={handleAddFormSubmit}>
            <div style={{marginBottom: '10px'}}>
                <label htmlFor="name" style={{marginRight: '10px'}}>Name:</label>
                <input type="text" id="name" name="name" value={newTour.name}
                       onChange={handleAddFormChange}/>
            </div>
            <div style={{marginBottom: '10px'}}>
                <label htmlFor="tourDescription" style={{marginRight: '10px'}}>Tour Description:</label>
                <input type="text" id="tourDescription" name="tourDescription"
                       value={newTour.tourDescription} onChange={handleAddFormChange}/>
            </div>
            <div style={{ marginBottom: '10px' }}>
                <label htmlFor="tourFrom" style={{ marginRight: '10px' }}>From:</label>
                <input type="text" id="tourFrom" name="tourFrom" value={newTour.tourFrom} onChange={handleAddFormChange} />
            </div>
            <div style={{ marginBottom: '10px' }}>
                <label htmlFor="tourTo" style={{ marginRight: '10px' }}>To:</label>
                <input type="text" id="tourTo" name="tourTo" value={newTour.tourTo} onChange={handleAddFormChange} />
            </div>
            <div style={{marginBottom: '10px'}}>
                <label htmlFor="transportType" style={{marginRight: '10px'}}>Transport Type:</label>
                <select id="transportType" name="transportType" value={newTour.transportType} onChange={handleAddFormChange}>
                    <option value="">Select</option> {/* Added default option */}
                    <option value="Foot">Foot</option>
                    <option value="Car">Car</option>
                    <option value="Bicycle">Bicycle</option>
                </select>
            </div>
            <div>
                <button type="submit" style={submitButtonStyle}>Send</button>
                <button type="button" onClick={() => setShowAddForm(false)} style={cancelButtonStyle}>Cancel</button>
            </div>
        </form>
    </div>
)}


            {showAddForm && formType === 'TourLog' && (
                <div style={{ position: 'absolute', bottom: '20px', left: '20px', padding: '20px', border: '1px solid #ccc', backgroundColor: '#f9f9f9' }}>
                    <h2>Add New Tour Log</h2>
                    <form onSubmit={handleAddTourLogFormSubmit}>
                        <div style={{ marginBottom: '10px' }}>
                            <label htmlFor="tourid" style={{ marginRight: '10px' }}>Tour ID:</label>
                            <input type="text" id="tourid" name="tourid" value={newTourLog.tourid} onChange={handleAddTourLogFormChange} />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label htmlFor="date" style={{ marginRight: '10px' }}>Date:</label>
                            <input type="text" id="date" name="date" value={newTourLog.date} onChange={handleAddTourLogFormChange} />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label htmlFor="time" style={{ marginRight: '10px' }}>Time:</label>
                            <input type="text" id="time" name="time" value={newTourLog.time} onChange={handleAddTourLogFormChange} />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label htmlFor="comment" style={{ marginRight: '10px' }}>Comment:</label>
                            <input type="text" id="comment" name="comment" value={newTourLog.comment} onChange={handleAddTourLogFormChange} />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label htmlFor="difficulty" style={{ marginRight: '10px' }}>Difficulty:</label>
                            <input type="text" id="difficulty" name="difficulty" value={newTourLog.difficulty} onChange={handleAddTourLogFormChange} />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label htmlFor="totalDistance" style={{ marginRight: '10px' }}>Total Distance:</label>
                            <input type="text" id="totalDistance" name="totalDistance" value={newTourLog.totalDistance} onChange={handleAddTourLogFormChange} />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label htmlFor="totalTime" style={{ marginRight: '10px' }}>Total Time:</label>
                            <input type="text" id="totalTime" name="totalTime" value={newTourLog.totalTime} onChange={handleAddTourLogFormChange} />
                        </div>
                        <div style={{ marginBottom: '10px' }}>
                            <label htmlFor="rating" style={{ marginRight: '10px' }}>Rating:</label>
                            <input type="text" id="rating" name="rating" value={newTourLog.rating} onChange={handleAddTourLogFormChange} />
                        </div>
                        <div>
                            <button type="submit" style={submitButtonStyle}>Send</button>
                            <button type="button" onClick={() => setShowAddForm(false)} style={cancelButtonStyle}>Cancel</button>
                        </div>
                    </form>
                </div>
            )}

            {showRemoveForm && (
                <div style={{ position: 'absolute', bottom: '20px', left: '20px', padding: '20px', border: '1px solid #ccc', backgroundColor: '#f9f9f9' }}>
                    <h2>Remove {formType === 'Tour' ? 'Tour' : 'Tour Log'}</h2>
                    <form onSubmit={handleRemoveFormSubmit}>
                        <div style={{ marginBottom: '10px' }}>
                            <label htmlFor="removeId" style={{ marginRight: '10px' }}>ID:</label>
                            <input type="text" id="removeId" name="removeId" value={removeId} onChange={handleRemoveFormChange} />
                        </div>
                        <div>
                            <button type="submit" style={submitButtonStyle}>Send</button>
                            <button type="button" onClick={() => setShowRemoveForm(false)} style={cancelButtonStyle}>Cancel</button>
                        </div>
                    </form>
                </div>
            )}

            {showImportModal && (
                <div style={importModalStyle}>
                    <h2>Import Tour</h2>
                    <p>Here you can import a tour of your own. Upload a file in JSON format with the same attribute structure as the table of tours.</p>
                    <input type="file" onChange={handleFileChange} />
                    <button onClick={handleImportSubmit} style={submitButtonStyle}>Upload</button>
                    <button onClick={() => setShowImportModal(false)} style={cancelButtonStyle}>Cancel</button>
                </div>
            )}

            {showReportModal && (
                <div style={importModalStyle}>
                    <h2>Generate a Tour Report</h2>
                    <p>Enter the Tour ID of the Tour to generate a report for:</p>
                    <input
                        type="text"
                        value={reportTourId}
                        onChange={(e) => setReportTourId(e.target.value)}
                        style={{ width: '100%', marginBottom: '10px' }}
                    />
                    <button onClick={handleGenerateReport} style={submitButtonStyle}>Generate</button>
                    <button onClick={() => setShowReportModal(false)} style={cancelButtonStyle}>Cancel</button>
                </div>
            )}
        </div>
    );
}

const buttonStyle = {
    background: 'none',
    border: 'none',
    color: 'white',
    padding: '5px 10px',
    marginRight: '10px',
    cursor: 'pointer'
};

const addRemoveButton = {
    background: 'white',
    border: 'black 1px solid',
    color: 'black',
    padding: '5px 10px',
    marginRight: '10px',
    cursor: 'pointer'
};

const tableHeaderStyle = {
    borderBottom: '1px solid #333',
    padding: '8px',
    textAlign: 'left'
};

const tableCellStyle = {
    borderBottom: '1px solid #ccc',
    padding: '8px',
    textAlign: 'left'
};

const tabButtonStyle = {
    padding: '10px',
    cursor: 'pointer',
    border: '1px solid #ccc',
    marginRight: '5px',
    background: '#f0f0f0'
};

const dropdownContentStyle = {
    position: 'absolute',
    backgroundColor: '#f9f9f9',
    boxShadow: '0px 8px 16px 0px rgba(0,0,0,0.2)',
    zIndex: 1,
    display: 'flex',
    flexDirection: 'column',
    marginTop: '5px'
};

const dropdownItemStyle = {
    color: 'black',
    padding: '12px 16px',
    textDecoration: 'none',
    cursor: 'pointer'
};

const submitButtonStyle = {
    backgroundColor: '#4CAF50',
    color: 'white',
    padding: '10px 15px',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer'
};

const cancelButtonStyle = {
    backgroundColor: '#f44336',
    color: 'white',
    padding: '10px 15px',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    marginLeft: '10px'
};

const importModalStyle = {
    position: 'fixed',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    padding: '20px',
    border: '1px solid #ccc',
    backgroundColor: '#f9f9f9',
    zIndex: 1000
};

export default App;
