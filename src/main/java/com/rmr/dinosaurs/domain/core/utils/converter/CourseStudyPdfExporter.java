package com.rmr.dinosaurs.domain.core.utils.converter;


import static com.rmr.dinosaurs.domain.core.exception.errorcode.ApplicationErrorCode.INTERNAL_SERVER_ERROR;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
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

  private final Color greenColor = new Color(0, 125, 92);

  private void writeTableHeader(PdfPTable table, List<CourseStudyInfoResponseDto> listCourseStudy) {
    PdfPCell cell = new PdfPCell();
    cell.setBackgroundColor(greenColor);
    cell.setPadding(5);
    cell.setHorizontalAlignment(1);

    Font font = FontFactory.getFont(FontFactory.HELVETICA);
    font.setSize(10);
    font.setColor(Color.WHITE);

    cell.setPhrase(new Phrase("Name Surname", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("E-mail", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Course title", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Finished", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Finish date", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Score", font));
    table.addCell(cell);

    cell.setPhrase(new Phrase("Professions", font));
    table.addCell(cell);
  }

  private void writeTableData(PdfPTable table, List<CourseStudyInfoResponseDto> listCourseStudy) {
    Font font = FontFactory.getFont("/font/arialmt.ttf", "cp1251",
        BaseFont.EMBEDDED, 9);

    for (CourseStudyInfoResponseDto dto : listCourseStudy) {
      final String professions = dto.getProfessions()
          .stream()
          .reduce(" ", String::concat)
          .trim();
      table.addCell(new Phrase(dto.getUserInfoNameAndSurname(), font));
      table.addCell(new Phrase(dto.getEmail(), font));
      table.addCell(new Phrase(dto.getCourseTitle(), font));
      PdfPCell isFinished = new PdfPCell(new Phrase(String.valueOf(dto.isCourseFinished()), font));
      isFinished.setHorizontalAlignment(1);
      table.addCell(isFinished);
      PdfPCell date = new PdfPCell(
          new Phrase(dto.getFinishedAt().toLocalDate().format(DateTimeFormatter.ISO_DATE), font));
      date.setHorizontalAlignment(1);
      table.addCell(date);
      PdfPCell score = new PdfPCell(new Phrase(String.valueOf(dto.getScore()), font));
      score.setHorizontalAlignment(1);
      table.addCell(score);
      table.addCell(new Phrase(professions, font));
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
    font.setColor(Color.BLACK);

    Paragraph p = new Paragraph("List of Course study info", font);
    p.setAlignment(Paragraph.ALIGN_CENTER);

    document.add(p);

    PdfPTable table = new PdfPTable(7);
    table.setWidthPercentage(100f);
    table.setWidths(new float[]{6.0f, 6.0f, 8.0f, 2.5f, 5.0f, 2.5f, 6.0f});
    table.setSpacingBefore(10);

    writeTableHeader(table, listCourseStudy);
    writeTableData(table, listCourseStudy);

    document.add(table);

    document.close();

  }
}