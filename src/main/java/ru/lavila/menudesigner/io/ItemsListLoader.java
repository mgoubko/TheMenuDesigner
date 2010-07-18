package ru.lavila.menudesigner.io;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.models.impl.ItemsListImpl;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ItemsListLoader
{
    public ItemsList loadItemsList(String filename)
    {
        try
        {
            return loadItemsList(new FileInputStream(filename));
        }
        catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public ItemsList loadItemsList(InputStream inputStream)
    {
        try
        {
            return loadItemsListInt(inputStream);
        }
        catch (LoaderException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    private ItemsList loadItemsListInt(InputStream inputStream) throws LoaderException
    {
        try
        {
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            ItemsList itemsList = new ItemsListImpl();
            Row titleRow = sheet.getRow(0);
            Map<Hierarchy, Map<String, Category>> taxonomies = new LinkedHashMap<Hierarchy, Map<String, Category>>();
            for (int index = 2; titleRow.getCell(index) != null; index++)
            {
                Hierarchy taxonomy = itemsList.newHierarchy(titleRow.getCell(index).getStringCellValue(), true);
                HashMap<String, Category> categories = new HashMap<String, Category>();
                categories.put("", taxonomy.getRoot());
                taxonomies.put(taxonomy, categories);
            }
            for (Row row : sheet)
            {
                if (row.getRowNum() == 0) continue;
                Cell nameCell = row.getCell(0);
                Cell popularityCell = row.getCell(1);
                if (nameCell == null || nameCell.getCellType() != HSSFCell.CELL_TYPE_STRING)
                {
                    throw new LoaderException("Cell in " + LoaderException.getCellIdentifier(row.getRowNum(), 0) +" must contain string name");
                }
                if (popularityCell == null || popularityCell.getCellType() != HSSFCell.CELL_TYPE_NUMERIC)
                {
                    throw new LoaderException("Cell in " + LoaderException.getCellIdentifier(row.getRowNum(), 1) +" must contain numeric popularity");
                }
                Item item = itemsList.newItem(nameCell.getStringCellValue(), popularityCell.getNumericCellValue());
                int index = 2;
                for (Map.Entry<Hierarchy, Map<String, Category>> taxonomy : taxonomies.entrySet())
                {
                    Cell categoryCell = row.getCell(index++);
                    if (categoryCell == null) continue;
                    if (categoryCell.getCellType() != HSSFCell.CELL_TYPE_STRING)
                    {
                        throw new LoaderException("Cell in " + LoaderException.getCellIdentifier(categoryCell) +" must contain string category name");
                    }
                    taxonomy.getKey().add(getCategory(taxonomy.getKey(), taxonomy.getValue(), categoryCell.getStringCellValue()), item);
                }
            }
            itemsList.normalizePopularities();
            return itemsList;
        }
        catch (IOException e)
        {
            throw new LoaderException("Unexpected error while reading data", e);
        }
    }

    private Category getCategory(Hierarchy hierarchy, Map<String, Category> categories, String name)
    {
        Category category = categories.get(name);
        if (category == null)
        {
            int separatorIndex = name.lastIndexOf(Hierarchy.LEVEL_SEPARATOR);
            if (separatorIndex == -1)
            {
                category = hierarchy.newCategory(hierarchy.getRoot(), name);
            }
            else
            {
                category = hierarchy.newCategory(getCategory(hierarchy, categories, name.substring(0, separatorIndex).trim()), name.substring(separatorIndex + 1).trim());
            }
            categories.put(name, category);
        }
        return category;
    }
}
