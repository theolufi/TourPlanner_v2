package com.project.tourplanner.tour;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.io.IOException;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TourService {
    @Autowired
    private TourDtoMapper tourDtoMapper;

    public TourDto getTours() {
        Tour tour = Tour.builder()
                .name("This is a tour")
                .tourDescription("Description")
                .tourFrom("From") // Changed from "fromm" to "tourFrom"
                .tourTo("To")     // Changed from "too" to "tourTo"
                .transportType("Train")
                .tourDistance("2km")
                .estTime("2h")    // Changed from "estimatedTime" to "estTime"
                .routeInfo("Route Info") // Added routeInfo if needed
                .build();

        TourDto tourDto = tourDtoMapper.mapTourToTourDto(tour);
        return tourDto;
    }

    public static byte[] summarizeReport(List<Tour> tourList) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        document.add(new Paragraph("Tours PDF Generated Report").setBold().setFontSize(20));

        for (Tour tour : tourList) {
            document.add(new Paragraph("Tour:").setBold());
            document.add(new Paragraph("ID: " + tour.getID())); // Assuming getID() is your getter method
            document.add(new Paragraph("Name: " + tour.getName()));
            document.add(new Paragraph("Tour Description: " + tour.getTourDescription()));
            document.add(new Paragraph("From: " + tour.getTourFrom()));
            document.add(new Paragraph("To: " + tour.getTourTo()));
            document.add(new Paragraph("Transport Type: " + tour.getTransportType()));
            document.add(new Paragraph("Tour Distance: " + tour.getTourDistance()));
            document.add(new Paragraph("Estimated Time: " + tour.getEstTime()));
            document.add(new Paragraph("Route Info: " + tour.getRouteInfo())); // Assuming you want to include routeInfo
            document.add(new Paragraph("\n"));
        }

        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] tourReport(Tour tour) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        document.add(new Paragraph("Tour PDF Generated Report").setBold().setFontSize(20));

        document.add(new Paragraph("Tour:").setBold());
        document.add(new Paragraph("ID: " + tour.getID())); // Assuming getID() is your getter method
        document.add(new Paragraph("Name: " + tour.getName()));
        document.add(new Paragraph("Tour Description: " + tour.getTourDescription()));
        document.add(new Paragraph("From: " + tour.getTourFrom()));
        document.add(new Paragraph("To: " + tour.getTourTo()));
        document.add(new Paragraph("Transport Type: " + tour.getTransportType()));
        document.add(new Paragraph("Tour Distance: " + tour.getTourDistance()));
        document.add(new Paragraph("Estimated Time: " + tour.getEstTime()));
        document.add(new Paragraph("Route Info: " + tour.getRouteInfo())); // Assuming you want to include routeInfo

        document.close();
        return byteArrayOutputStream.toByteArray();
    }
}
