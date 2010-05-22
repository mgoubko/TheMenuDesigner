package ru.lavila.menudesigner.io;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.models.impl.ItemsListImpl;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ItemsListLoader
{
    public ItemsList loadItemsList(String filename)
    {
        try
        {
            return loadItemsListInt(filename);
        }
        catch (LoaderException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private ItemsList loadItemsListInt(String filename) throws LoaderException
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(filename);
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            ItemsList itemsList = new ItemsListImpl();
            for (Row row : sheet)
            {
                if (row.getRowNum() == 0) continue;
                Cell nameCell = row.getCell(0);
                Cell popularityCell = row.getCell(1);
                if (nameCell.getCellType() != HSSFCell.CELL_TYPE_STRING)
                {
                    throw new LoaderException("Cell in " + getCellIdentifier(nameCell) +" must contain string name");
                }
                if (popularityCell.getCellType() != HSSFCell.CELL_TYPE_NUMERIC)
                {
                    throw new LoaderException("Cell in " + getCellIdentifier(popularityCell) +" must contain numeric popularity");
                }
                itemsList.newItem(nameCell.getStringCellValue(), popularityCell.getNumericCellValue());
            }
            return itemsList;
        }
        catch (FileNotFoundException e)
        {
            throw new LoaderException("File '" + filename + "' not found", e);
        }
        catch (IOException e)
        {
            throw new LoaderException("Unexpected error while reading file '" + filename + "'", e);
        }
    }

    private String getCellIdentifier(Cell cell)
    {
        return "row " + (cell.getRowIndex() + 1) + " column " + (cell.getColumnIndex() + 1);
    }

    private static class LoaderException extends Exception
    {
        private LoaderException(String message)
        {
            super(message);
        }

        private LoaderException(String message, Throwable cause)
        {
            super(message, cause);
        }
    }
}
