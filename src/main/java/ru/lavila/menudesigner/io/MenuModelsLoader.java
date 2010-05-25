package ru.lavila.menudesigner.io;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.lavila.menudesigner.math.MenuModel;
import ru.lavila.menudesigner.math.UserDefinedMenuModel;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuModelsLoader
{
    public List<MenuModel> loadMenuModels(String filename)
    {
        try
        {
            return loadMenuModels(new FileInputStream(filename));
        }
        catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public List<MenuModel> loadMenuModels(InputStream inputStream)
    {
        try
        {
            return loadMenuModelsInt(inputStream);
        }
        catch (LoaderException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return Collections.emptyList();
        }
    }

    private List<MenuModel> loadMenuModelsInt(InputStream inputStream) throws LoaderException
    {
        try
        {
            Workbook workbook = new HSSFWorkbook(inputStream);
            List<MenuModel> models = new ArrayList<MenuModel>();
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++)
            {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                String name = sheet.getSheetName();
                if (name.equals("Parameters")) continue;
                List<double[]> values = new ArrayList<double[]>();
                for (Row row : sheet)
                {
                    int total = row.getRowNum() + 1;
                    double[] rowValues = new double[total];
                    for (int index = 0; index < total; index++)
                    {
                        Cell cell = row.getCell(index);
                        if (cell == null || (cell.getCellType() != Cell.CELL_TYPE_NUMERIC && cell.getCellType() != Cell.CELL_TYPE_FORMULA))
                        {
                            throw new LoaderException("Cell " + LoaderException.getCellIdentifier(row.getRowNum(), index) + " should contain valid number or formula");
                        }
                        rowValues[index] = cell.getNumericCellValue();
                    }
                    values.add(rowValues);
                }
                models.add(new UserDefinedMenuModel(name, values.toArray(new double[values.size()][])));
            }
            return models;
        }
        catch (IOException e)
        {
            throw new LoaderException("Unexpected error while reading data", e);
        }
    }
}
