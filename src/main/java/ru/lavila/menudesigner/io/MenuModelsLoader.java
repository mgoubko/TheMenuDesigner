package ru.lavila.menudesigner.io;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.lavila.menudesigner.math.menumodels.UserDefinedMenuModel;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

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
                double[] proportion = null;
                for (Row row : sheet)
                {
                    if (row.getRowNum() != sheet.getLastRowNum())
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
                    else
                    {
                        List<Double> proportionValues = new ArrayList<Double>();
                        for (Cell cell : row)
                        {
                            if (cell.getCellType() != Cell.CELL_TYPE_NUMERIC && cell.getCellType() != Cell.CELL_TYPE_FORMULA)
                            {
                                throw new LoaderException("Cell " + LoaderException.getCellIdentifier(cell) + " should contain valid number or formula");
                            }
                            proportionValues.add(cell.getNumericCellValue());
                        }
                        proportion = new double[proportionValues.size()];
                        double sum = 0;
                        for (int index = 0; index < proportionValues.size(); index++)
                        {
                            proportion[index] = proportionValues.get(index);
                            sum += proportion[index];
                        }
                        if (Math.abs(sum - 1) > 0.01)
                        {
                            throw new LoaderException("Sum of optimal proportion elements should equal to 1");
                        }
                    }
                }
                models.add(new UserDefinedMenuModel(name, values.toArray(new double[values.size()][]), proportion));
            }
            return models;
        }
        catch (IOException e)
        {
            throw new LoaderException("Unexpected error while reading data", e);
        }
    }
}
