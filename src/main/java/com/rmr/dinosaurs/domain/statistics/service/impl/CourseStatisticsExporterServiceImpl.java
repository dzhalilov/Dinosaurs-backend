package com.rmr.dinosaurs.domain.statistics.service.impl;

import static com.rmr.dinosaurs.domain.statistics.exception.errorcode.StatisticsErrorCode.EXPORT_ERROR;

import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.statistics.model.CourseLinkTransition;
import com.rmr.dinosaurs.domain.statistics.service.CourseStatisticsExporterService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CourseStatisticsExporterServiceImpl implements CourseStatisticsExporterService {


  @Override
  public void exportToExcel(List<CourseLinkTransition> courseLinkTransitions,
      HttpServletResponse response) {
    XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
    var sheet = writeHeaderLine(xssfWorkbook, "Course Link Transitions");
    int iter = 2;
    var defaultCellStyle = getDefaultCellStyle(xssfWorkbook);
    for (CourseLinkTransition clt : courseLinkTransitions) {
      Row row = sheet.createRow(iter++);
      int columnCount = 0;
      createCell(sheet, row, columnCount++, clt.getUser().getEmail(), defaultCellStyle);
      createCell(sheet, row, columnCount++, clt.getCourse().getTitle(), defaultCellStyle);
      createCell(sheet, row, columnCount++, clt.getTransitionedAt(), defaultCellStyle);
    }

    try (ServletOutputStream outputStream = response.getOutputStream()) {
      xssfWorkbook.write(outputStream);
    } catch (IOException e) {
      log.debug("Error while exporting to excel. {}", e.getMessage());
      throw new ServiceException(EXPORT_ERROR);
    }
  }

  private CellStyle getDefaultCellStyle(XSSFWorkbook xssfWorkbook) {
    XSSFCellStyle defaultCellStyle = xssfWorkbook.createCellStyle();
    XSSFFont font = xssfWorkbook.createFont();
    font.setFontHeight(14);
    defaultCellStyle.setFont(font);
    return defaultCellStyle;
  }


  private void createCell(XSSFSheet sheet, Row row, int columnCount, Object value,
      CellStyle style) {
    sheet.autoSizeColumn(columnCount);
    Cell cell = row.createCell(columnCount);
    if (value instanceof Long l) {
      cell.setCellValue(l);
    } else if (value instanceof LocalDateTime localDateTime) {
      cell.setCellValue(localDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    } else {
      cell.setCellValue(String.valueOf(value));
    }
    cell.setCellStyle(style);
  }

  private XSSFSheet writeHeaderLine(XSSFWorkbook xssfWorkbook, String sheetName) {
    XSSFFont font = xssfWorkbook.createFont();
    font.setBold(true);
    font.setFontHeight(20);
    CellStyle cellStyle = xssfWorkbook.createCellStyle();
    cellStyle.setFont(font);
    cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    XSSFSheet xssfSheet = xssfWorkbook.createSheet(sheetName);
    Row row = xssfSheet.createRow(0);
    createCell(xssfSheet, row, 0, sheetName, cellStyle);
    xssfSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
    font.setFontHeightInPoints((short) (10));

    row = xssfSheet.createRow(1);
    font.setBold(true);
    font.setFontHeight(16);
    cellStyle.setFont(font);
    createCell(xssfSheet, row, 0, "User email", cellStyle);
    createCell(xssfSheet, row, 1, "Course title", cellStyle);
    createCell(xssfSheet, row, 2, "Transitioned at", cellStyle);
    return xssfSheet;
  }

}
