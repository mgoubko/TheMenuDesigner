package ru.lavila.menudesigner.io;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ExcelWorkbookFilter extends FileFilter
{
    @Override
    public boolean accept(File f)
    {
        return f.isDirectory() || "xls".equals(getExtension(f));
    }

    @Override
    public String getDescription()
    {
        return "Excel Workbook";
    }

    public static String getExtension(File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1)
        {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
