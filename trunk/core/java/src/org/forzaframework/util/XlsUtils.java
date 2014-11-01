/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.util;

import net.sf.json.JSONObject;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: gabriel.chulim
 * Date: 17/01/12
 * Time: 08:44 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({"unchecked"})
public class XlsUtils {
    static private List<Map<String, Object>> getRowGroups(Map<String, Object> model, String key) {
        List<Map<String, Object>> rows;
        //Obtenmos el elmento del map y verificamos que sea una instancia de la clase Map o List
        Object object = model.get(key);
        if (object instanceof Map) {
            //si es un Map representa una fila y lo agregamos a la lista de filas
            Map<String, Object> row = (Map<String, Object>) object;
            rows = new ArrayList<Map<String, Object>>();
            rows.add(row);
        }else {
            //Representa varias filas
            rows = (List<Map<String, Object>>) object;

        }

        return rows;
    }

    static public void modelToExcelSheet(HSSFWorkbook wb, String sheetName, Map<String, Object> model) {
        List<Map<String, Object>> headers = getRowGroups(model, "header");
        List<Map<String, Object>> data = getRowGroups(model, "data");
        List<Map<String, Object>> footers = getRowGroups(model, "totals");
        //Obtenemos los datos que mostraremos en la hoja
//        Map<String, Object> header = (Map<String, Object>) model.get("header");
//        headers.add(header);
//        List<Map<String, Object>> data = (List<Map<String, Object>>) model.get("data");
//        Map<String, Object> totals = (Map<String, Object>) model.get("totals");
//        footers.add(totals);

        modelToExcelSheet(wb, sheetName, headers, data, footers);
    }

    static public void modelToExcelSheet(HSSFWorkbook wb, String sheetName, Map<String, Object> model, Integer freezePane) {
        List<Map<String, Object>> headers = getRowGroups(model, "header");
        List<Map<String, Object>> data = getRowGroups(model, "data");
        List<Map<String, Object>> footers = getRowGroups(model, "totals");

        modelToExcelSheet(wb, sheetName, headers, data, footers, freezePane);
    }

    static public void modelToExcelSheet(HSSFWorkbook wb, String sheetName, Map<String, Object> model, Boolean defaultFormat) {
        List<Map<String, Object>> headers = getRowGroups(model, "header");
        List<Map<String, Object>> data = getRowGroups(model, "data");
        List<Map<String, Object>> footers = getRowGroups(model, "totals");

        modelToExcelSheet(wb, sheetName, headers, data, footers, null, defaultFormat, true, null, null, true, true);

    }

    static public void modelToExcelSheet(HSSFWorkbook wb, String sheetName, List<Map<String, Object>> headers, List<Map<String, Object>> data, Boolean defaultFormat, Boolean createNewSheet) {
        modelToExcelSheet(wb, sheetName, headers, data, null, null, defaultFormat, createNewSheet, 0, null, true, true);

    }

    static public void modelToExcelSheet(HSSFWorkbook wb, String sheetName, List<Map<String, Object>> headers, List<Map<String, Object>> data, Boolean defaultFormat, Integer freezePane) {
        modelToExcelSheet(wb, sheetName, headers, data, null, freezePane, defaultFormat, true, null, null, true, true);

    }

    static public void modelToExcelSheet(HSSFWorkbook wb, String sheetName, List<Map<String, Object>> headers, List<Map<String, Object>> data, List<Map<String, Object>> footers) {
        modelToExcelSheet(wb, sheetName, headers, data, footers, null, true, true, null, null, true, true);

    }

    static public void modelToExcelSheet(HSSFWorkbook wb, String sheetName, List<Map<String, Object>> headers, List<Map<String, Object>> data, List<Map<String, Object>> footers, Boolean defaultFormat, Boolean createNewSheet) {
        modelToExcelSheet(wb, sheetName, headers, data, footers, null, defaultFormat, createNewSheet, null, null, true, true);

    }

    static public void modelToExcelSheet(HSSFWorkbook wb, String sheetName, List<Map<String, Object>> headers, List<Map<String, Object>> data, List<Map<String, Object>> footers, Integer freezePane, Boolean defaultFormat, Boolean createNewSheet) {
        modelToExcelSheet(wb, sheetName, headers, data, footers, freezePane, defaultFormat, createNewSheet, null, null, true, true);

    }

    static public void modelToExcelSheet(HSSFWorkbook wb, String sheetName, List<Map<String, Object>> headers, List<Map<String, Object>> data, List<Map<String, Object>> footers, Integer freezePane) {
        modelToExcelSheet(wb, sheetName, headers, data, footers, freezePane, true, true, null, null, true, true);
    }

    static public void modelToExcelSheet(HSSFWorkbook wb, String sheetName, List<Map<String, Object>> headers, List<Map<String, Object>> data, List<Map<String, Object>> footers, Integer freezePane, Boolean defaultFormat, Boolean createNewSheet, Integer indexSheet, Integer startInRow, Boolean printHeader, Boolean autoSizeColumns) {
        HSSFSheet sheet = getSheet(wb, sheetName, createNewSheet, indexSheet);
        HSSFCellStyle headerCellStyle = getDefaultHeaderCellStyle(wb, defaultFormat);
        HSSFCellStyle titlesCellStyle = null;
        if (defaultFormat != null && defaultFormat) {
            titlesCellStyle = wb.createCellStyle();
            //Creamos el tipo de fuente
            HSSFFont titleFont = wb.createFont();
//            headerFont.setFontName(HSSFFont.FONT_ARIAL);
            titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            titleFont.setColor(HSSFFont.COLOR_NORMAL);
            titleFont.setFontHeightInPoints((short)8);
            titlesCellStyle.setFont(titleFont);
        }


        Integer col = 0;
        Integer row = 0;
        if (startInRow != null) {
            row = startInRow;
        }
        Map<Integer, Integer > columnWidthMap = new HashMap<Integer, Integer>();
        //Indice de la fila donde empieza los encabezados de titulo de cada columna
        Integer principalHeaderIndex = headers.size() - 1;
        if (printHeader != null && printHeader) {
            //Armamos el encabezado
            for (Map<String, Object> header : headers) {
                for (Map.Entry<String, Object> entry : header.entrySet()) {
                    HSSFCell cell = getCell( sheet, row, col);
                    if (defaultFormat != null && defaultFormat) {
                        if (principalHeaderIndex.equals(row)) {
                            //Colocamos el formato de la celda
                            cell.setCellStyle(headerCellStyle);
                        } else {
                            cell.setCellStyle(titlesCellStyle);
                        }
                    }
                    setValue(cell, entry.getValue());
                    //Especificamos el ancho que tendra la columna
                    if (autoSizeColumns != null && autoSizeColumns) {
                        columnWidthMap.put(col, entry.getValue().toString().length());
                    }
                    col++;
                }
                row++;
                col = 0;
            }
            //Ponemos la altura del encabezado
            setRowHeight(sheet, row-1, (short)420);
        }


        HSSFCellStyle detailCellStyle = getDefaultDetailCellStyle(wb, defaultFormat);

        Map<String, Object> principalHeader = headers.get(principalHeaderIndex);
        // datos
        for (Map<String, Object> map : data) {
            for (Map.Entry<String, Object> entry : principalHeader.entrySet()) {
                Object value = map.get(entry.getKey());
                buildCellAndCalculateColumnWidth(sheet, value, col, row, detailCellStyle, columnWidthMap, autoSizeColumns);
                col++;
            }
            col = 0;
            row++;
        }
        HSSFCellStyle totalCellStyle = null;
        if (defaultFormat != null && defaultFormat) {
            //Armamos el formato los totales
            totalCellStyle = wb.createCellStyle();
            HSSFFont totalFont = wb.createFont();
            totalFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            totalFont.setColor(HSSFFont.COLOR_NORMAL);
            totalFont.setFontHeightInPoints((short)8);
            totalCellStyle.setFont(totalFont);
        }

        if(footers != null) {
            for (Map<String, Object> footer : footers) {
                for (Map.Entry<String, Object> entry : principalHeader.entrySet()) {
                    HSSFCell cell = getCell( sheet, row, col++);
                    if (totalCellStyle != null) {
                        //Colocamos el formato de la celda
                        cell.setCellStyle(totalCellStyle);
                    }

                    Object object = footer.get(entry.getKey());
                    if(object != null) {
                        setValue(cell, object);
                    } else{
                        setText(cell, "");
                    }
                }
            }
        }

        if (autoSizeColumns != null && autoSizeColumns) {
            setColumnsWidth(sheet, columnWidthMap, principalHeader.size());
        }


        if (freezePane != null && freezePane > 0) {
            //Colocamos la columna estatica y las filas del encabezado estaticas
            sheet.createFreezePane(freezePane, headers.size());
        }
    }

    public static void buildCellAndCalculateColumnWidth(HSSFSheet sheet, Object value, Integer col, Integer row, HSSFCellStyle detailCellStyle, Map<Integer, Integer> columnWidthMap, Boolean autoSizeColumns) {
        HSSFCell cell = getCell(sheet, row, col);

        if (detailCellStyle != null) {
            //Le damos formato a la celda
            cell.setCellStyle(detailCellStyle);
        }
        if(value != null){
            setValue(cell, value);
            //Verificamos que la columna tenga el ancho correcto
            if (autoSizeColumns != null && autoSizeColumns) {
                Integer objectLenght = value.toString().length();
                if (columnWidthMap.get(col) < objectLenght){
                    columnWidthMap.put(col, objectLenght);
                }
            }
        }else{
            setText(cell, "");
        }
    }

    public static void setColumnsWidth(HSSFSheet sheet, Map<Integer, Integer> columnWidthMap, Integer numberOfColumns) {
        //Colocamos las columnas con el ancho correcto
        for(Integer i=0 ; i < numberOfColumns ; i++) {
            //Obtenemos el maximo numero de caracteres de la columna
            Integer columnWidth = columnWidthMap.get(i) + 1;
            columnWidth = columnWidth > 100 ? 100 : columnWidth;
            //multiplicamos por 256 porque es lo que representa un caranter en excel
            sheet.setColumnWidth(i, columnWidth * 256);
            //TODO: Esta es otra forma dar el ancho de la columna correctamente, probar si es mas optimo
//            sheet.autoSizeColumn((short)i);
//            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 256);
        }
    }

    public static HSSFCellStyle getDefaultDetailCellStyle(HSSFWorkbook wb, Boolean defaultFormat) {
        HSSFCellStyle detailCellStyle = null;
        if (defaultFormat != null && defaultFormat) {
            //Armamos el formato para los datos del detalle
            detailCellStyle = wb.createCellStyle();
            HSSFFont detailFont = wb.createFont();
            detailFont.setFontHeightInPoints((short)8);
            detailCellStyle.setFont(detailFont);
        }
        return detailCellStyle;
    }

    public static void setRowHeight(HSSFSheet sheet, Integer row, Short height) {
        HSSFRow headerRow = sheet.getRow(row);
        if (headerRow != null) {
            headerRow.setHeight(height);
        }
    }

    public static HSSFCellStyle getDefaultHeaderCellStyle(HSSFWorkbook wb, Boolean defaultFormat) {
        HSSFCellStyle headerCellStyle = null;
        if (defaultFormat != null && defaultFormat) {
            //Le damos formato a los encabezados
            headerCellStyle = wb.createCellStyle();
            headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
            headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
            headerCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            //Creamos el tipo de fuente
            HSSFFont headerFont = wb.createFont();
//            headerFont.setFontName(HSSFFont.FONT_ARIAL);
            headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            headerFont.setColor(HSSFFont.COLOR_NORMAL);
            headerFont.setFontHeightInPoints((short)8);
            headerCellStyle.setFont(headerFont);
        }
        return headerCellStyle;
    }

    public static HSSFSheet getSheet(HSSFWorkbook wb, String sheetName, Boolean createNewSheet, Integer indexSheet) {
        HSSFSheet sheet = null;//Revisamos si vamos a crear una hoja nueva o con una ya existente.
        if ((createNewSheet != null && createNewSheet) || wb.getNumberOfSheets() == 0) {
            //Creamos una hoja nueva
            if (sheetName != null) {
                sheet = wb.createSheet(sheetName);
            } else {
                sheet = wb.createSheet();
            }
        } else {
            //Revisamos si existe la hoja con el nombre especificado
            if (indexSheet == null && sheetName != null) {
                sheet = wb.getSheet(sheetName);
            }

            if (sheet == null) {
                //Trabajamos con una hoja ya existente
                if (indexSheet == null) {
                    indexSheet = 0;
                }
                if (sheetName != null) {
                    wb.setSheetName(indexSheet, sheetName);
                }
                sheet = wb.getSheetAt(indexSheet);
            }
        }
        return sheet;
    }

    static public HSSFCell getCell(HSSFSheet sheet, int row, int col) {
        HSSFRow sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        HSSFCell cell = sheetRow.getCell(col);
        if (cell == null) {
            cell = sheetRow.createCell(col);
        }
        return cell;
    }

    static public void setText(HSSFCell cell, String text) {
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(text);
    }

    static public void setValue(HSSFCell cell, Object object) {
        if (object instanceof Double) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue((Double) object);
        } else if(object instanceof Integer) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((Integer)object).doubleValue());
        } else if(object instanceof BigDecimal) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((BigDecimal)object).doubleValue());
        } else if(object instanceof String) {
            String cellValue = (String) object;
            if (!"".equals(cellValue)) {
                cell.setCellValue(cellValue);
            }
        } else if(object instanceof Date) {
            cell.setCellValue(DateUtils.getString ((Date)object));
        } else if(object instanceof Boolean) {
            cell.setCellValue((Boolean)object ? "Si" : "No");
        } else {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(object.toString());
        }
    }

    static public void setNumeric(HSSFCell cell, Double value) {
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
    }

    static public void setNumeric(HSSFSheet sheet, int row, int col, Double value) {
        HSSFCell cell = getCell(sheet, row, col);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
    }

    static public void setText(HSSFSheet sheet, int row, int col, String text) {
        HSSFCell cell = getCell(sheet, row, col);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(text);
    }

    static public Integer addModelToExcelSheet(HSSFWorkbook wb, String sheetName, Map<String, Object> model, Integer freezePane, Integer startInRow, Boolean printHeader, Boolean autoSizeColumns, Boolean printFooter) {
        Integer totalRows = 0;
        List<Map<String, Object>> headers = getRowGroups(model, "header");
        List<Map<String, Object>> data = getRowGroups(model, "data");
        List<Map<String, Object>> footers = getRowGroups(model, "totals");
        if (printHeader != null && printHeader && headers != null) {
            totalRows += headers.size();
        }

        if (data != null) {
            totalRows += data.size();
        }

        if (printFooter != null && printFooter && footers != null){
            totalRows += footers.size();
        } else {
            footers = null;
        }

        modelToExcelSheet(wb, sheetName, headers, data, footers, freezePane, true, null,null, startInRow, printHeader, autoSizeColumns);

        return totalRows;
    }

    static public void jsonToExcelSheet(HSSFWorkbook wb, List<JSONObject> jsonRecordList, String sheetName, Integer freezePane) {
        jsonToExcelSheet(wb, null, jsonRecordList, sheetName, freezePane);
    }

    static public void jsonToExcelSheet(HSSFWorkbook wb, List<JSONObject> jsonHeaderList, List<JSONObject> jsonRecordList, String sheetName, Integer freezePane) {
        HSSFSheet sheet = getSheet(wb, sheetName, true, 0);
        HSSFCellStyle headerCellStyle = getDefaultHeaderCellStyle(wb, true);

        Integer col = 0;
        Integer row = 0;
        Map<Integer, Integer > columnWidthMap = new HashMap<Integer, Integer>();

        JSONObject principalHeader = null;
        if (jsonHeaderList != null && !jsonHeaderList.isEmpty()) {
            LinkedList<JSONObject> headerList = new LinkedList<JSONObject>(jsonHeaderList);
            principalHeader = headerList.getLast();
            headerList.removeLast();
            for (JSONObject header : headerList) {
                Collection headerValues = header.values();
                for (Object o : headerValues) {
                    HSSFCell cell = getCell( sheet, row, col);
                    setValue(cell, o);
                    col++;
                }
                col = 0;
                row++;
            }

        }

        List<JSONObject> datas;
        if (principalHeader == null) {
            principalHeader = jsonRecordList.get(0);
            datas = jsonRecordList.subList(1, jsonRecordList.size());
        } else {
            datas = jsonRecordList;
        }
        Collection headerValues = principalHeader.values();
        for (Object o : headerValues) {
            HSSFCell cell = getCell( sheet, row, col);
            cell.setCellStyle(headerCellStyle);
            setValue(cell, o);
            columnWidthMap.put(col, o.toString().length());
            col++;
        }
        col = 0;
        row++;

        setRowHeight(sheet, row-1, (short)420);

        HSSFCellStyle detailCellStyle = getDefaultDetailCellStyle(wb, true);
        for (JSONObject jsonObject : datas) {
            for (Object value : jsonObject.values()) {
                buildCellAndCalculateColumnWidth(sheet, value, col, row, detailCellStyle, columnWidthMap, true);
                col++;
            }
            row++;
            col = 0;
        }

        setColumnsWidth(sheet, columnWidthMap, headerValues.size());
        if (freezePane != null && freezePane > 0) {
            //Colocamos la columna estatica y las filas del encabezado estaticas
            if (jsonHeaderList != null) {
                sheet.createFreezePane(freezePane, jsonHeaderList.size());
            } else {
                sheet.createFreezePane(freezePane, 1);
            }
        }
    }
}
