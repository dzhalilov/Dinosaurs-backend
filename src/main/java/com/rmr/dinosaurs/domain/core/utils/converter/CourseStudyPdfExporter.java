package com.rmr.dinosaurs.domain.core.utils.converter;


import static com.rmr.dinosaurs.domain.core.exception.errorcode.ApplicationErrorCode.INTERNAL_SERVER_ERROR;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyInfoResponseDto;
import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CourseStudyPdfExporter {

  private void writeTableHeader(PdfPTable table, List<CourseStudyInfoResponseDto> listCourseStudy) {
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(Color.BLUE);
    cell.setPadding(5);

    Font font = FontFactory.getFont(FontFactory.HELVETICA);
    font.setColor(Color.WHITE);

    cell.setPhrase(new Phrase("Name Surname", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("E-mail", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Course title", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Ss finished", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Finish date", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Score", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Professions", font));
    table.addCell(cell);
  }

  private void writeTableData(PdfPTable table, List<CourseStudyInfoResponseDto> listCourseStudy) {
    for (CourseStudyInfoResponseDto dto : listCourseStudy) {
      table.addCell(dto.getUserInfoNameAndSurname());
      table.addCell(dto.getEmail());
      table.addCell(dto.getCourseTitle());
      table.addCell(String.valueOf(dto.isCourseFinished()));
      table.addCell(dto.getFinishedAt().format(DateTimeFormatter.ISO_DATE_TIME));
      table.addCell(String.valueOf(dto.getScore()));
      table.addCell(dto.getProfessions()
          .stream()
          .reduce(" ", String::concat));
    }
  }

  public void export(HttpServletResponse response,
      List<CourseStudyInfoResponseDto> listCourseStudy) {
    Document document = new Document(PageSize.A4);
    try {
      PdfWriter.getInstance(document, response.getOutputStream());
    } catch (Exception e) {
      throw new ServiceException(INTERNAL_SERVER_ERROR);
    }

    document.open();
    Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    font.setSize(18);
    font.setColor(Color.BLUE);

    Paragraph p = new Paragraph("List of Course study info", font);
    p.setAlignment(Paragraph.ALIGN_CENTER);

    document.add(p);

    PdfPTable table = new PdfPTable(5);
    table.setWidthPercentage(100f);
    table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
    table.setSpacingBefore(10);

    writeTableHeader(table, listCourseStudy);
    writeTableData(table, listCourseStudy);

    document.add(table);

    document.close();

  }
}