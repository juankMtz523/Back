package com.gtim.service_orders.notification.impl;

import com.gtim.service_orders.notification.ExcelService;

import com.gtim.service_orders.dto.ColaboradorDTO;
import com.gtim.service_orders.dto.CoordinadoresDTO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.stereotype.Service;

@Service
public class ExcelServiceImpl implements ExcelService{

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "Errores";

    @Override
    public ByteArrayInputStream crearReporteErroresCoordinador(List<CoordinadoresDTO> data) throws IOException {
        int rowNumber = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(SHEET);

        XSSFCellStyle hdr = this.hdrStyle(workbook);
        XSSFCell cellHdr;

        XSSFRow rowHeader = sheet.createRow(rowNumber);

        cellHdr = rowHeader.createCell(0);
        cellHdr.setCellValue("Nombre");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(1);
        cellHdr.setCellValue("Apellidos");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(2);
        cellHdr.setCellValue("Correo");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(3);
        cellHdr.setCellValue("Télefono");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(4);
        cellHdr.setCellValue("Área");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(5);
        cellHdr.setCellValue("Jefe Inmediato");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(6);
        cellHdr.setCellValue("Correo Jefe Inmediato");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(7);
        cellHdr.setCellValue("Resultado");
        cellHdr.setCellStyle(hdr);

        rowNumber++;

        XSSFCell cell;

        for (CoordinadoresDTO cDto : data) {
            XSSFRow row = sheet.createRow(rowNumber);

            cell = row.createCell(0);
            cell.setCellValue(cDto.getNombres() != null ? cDto.getNombres() : "");
            cell = row.createCell(1);
            cell.setCellValue(cDto.getApellidos() != null ? cDto.getApellidos() : "");
            cell = row.createCell(2);
            cell.setCellValue(cDto.getCorreo() != null ? cDto.getCorreo() : "");
            cell = row.createCell(3);
            cell.setCellValue(cDto.getTelefono() != null ? cDto.getTelefono() : "");
            cell = row.createCell(4);
            cell.setCellValue(cDto.getArea() != null ? cDto.getArea() : "");
            cell = row.createCell(5);
            cell.setCellValue(cDto.getCoordinador() != null ? cDto.getCoordinador() : "");
            cell = row.createCell(6);
            cell.setCellValue(cDto.getCorreoCoordinador() != null ? cDto.getCorreoCoordinador() : "");
            cell = row.createCell(7);
            cell.setCellValue(cDto.getResultado() != null ? cDto.getResultado() : "");

            rowNumber++;
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    @Override
    public ByteArrayInputStream crearReporteErroresColaborador(List<ColaboradorDTO> data) throws IOException {
        int rowNumber = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(SHEET);

        XSSFCellStyle hdr = this.hdrStyle(workbook);
        XSSFCell cellHdr;

        XSSFRow rowHeader = sheet.createRow(rowNumber);

        cellHdr = rowHeader.createCell(0);
        cellHdr.setCellValue("Nombre");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(1);
        cellHdr.setCellValue("Apellidos");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(2);
        cellHdr.setCellValue("Correo");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(3);
        cellHdr.setCellValue("Télefono");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(4);
        cellHdr.setCellValue("Área");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(5);
        cellHdr.setCellValue("Rol GTIM");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(6);
        cellHdr.setCellValue("Coordinador");
        cellHdr.setCellStyle(hdr);
        cellHdr = rowHeader.createCell(7);
        cellHdr.setCellValue("Resultado");
        cellHdr.setCellStyle(hdr);

        rowNumber++;

        XSSFCell cell;

        for (ColaboradorDTO cDto : data) {
            XSSFRow row = sheet.createRow(rowNumber);

            cell = row.createCell(0);
            cell.setCellValue(cDto.getNombres() != null ? cDto.getNombres() : "");
            cell = row.createCell(1);
            cell.setCellValue(cDto.getApellidos() != null ? cDto.getApellidos() : "");
            cell = row.createCell(2);
            cell.setCellValue(cDto.getCorreo() != null ? cDto.getCorreo() : "");
            cell = row.createCell(3);
            cell.setCellValue(cDto.getTelefono() != null ? cDto.getTelefono() : "");
            cell = row.createCell(4);
            cell.setCellValue(cDto.getArea() != null ? cDto.getArea() : "");
            cell = row.createCell(5);
            cell.setCellValue(cDto.getRolGtim() != null ? cDto.getRolGtim() : "");
            cell = row.createCell(6);
            cell.setCellValue(cDto.getCoodrinador() != null ? cDto.getCoodrinador() : "");
            cell = row.createCell(7);
            cell.setCellValue(cDto.getResultado() != null ? cDto.getResultado() : "");

            rowNumber++;
        }

        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }

    private XSSFCellStyle hdrStyle(XSSFWorkbook workbook) {
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setColor(IndexedColors.WHITE.getIndex());
        fontHeader.setBold(true);
        XSSFColor customColor = new XSSFColor(new java.awt.Color(116, 114, 114), workbook.getStylesSource().getIndexedColors());
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFillBackgroundColor(customColor);
        cellStyle.setFillForegroundColor(customColor);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setLeftBorderColor(IndexedColors.WHITE.getIndex());
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFont(fontHeader);

        return cellStyle;
    }
}
