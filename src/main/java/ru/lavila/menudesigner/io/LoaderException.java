package ru.lavila.menudesigner.io;

import org.apache.poi.ss.usermodel.Cell;

class LoaderException extends Exception
{
    LoaderException(String message)
    {
        super(message);
    }

    LoaderException(String message, Throwable cause)
    {
        super(message, cause);
    }

    static String getCellIdentifier(Cell cell)
    {
        return getCellIdentifier(cell.getRowIndex(), cell.getColumnIndex());
    }

    static String getCellIdentifier(int row, int column)
    {
        return "row " + (row + 1) + " column " + (column + 1);
    }
}
