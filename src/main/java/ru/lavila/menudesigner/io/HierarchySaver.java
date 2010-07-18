package ru.lavila.menudesigner.io;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HierarchySaver
{
    public void saveHierarchy(String filename, Hierarchy hierarchy, ItemsList itemsList)
    {
        Workbook workbook = new HSSFWorkbook();
        new WorkbookWriter(workbook, itemsList).write(hierarchy);
        try
        {
            FileOutputStream outputStream = new FileOutputStream(filename);
            workbook.write(outputStream);
            outputStream.close();
            JOptionPane.showMessageDialog(null, "Menu structure successfully saved to '" + filename + "'", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class WorkbookWriter
    {
        private final Sheet dataSheet;
        private final Sheet structureSheet;
        private int dataIndex;
        private int structureIndex;
        private final List<Hierarchy> taxonomies;

        private WorkbookWriter(Workbook workbook, ItemsList itemsList)
        {
            dataSheet = workbook.createSheet("Categories");
            structureSheet = workbook.createSheet("Structure");
            dataIndex = 0;
            structureIndex = 0;
            this.taxonomies = itemsList.getTaxonomies();
        }

        public void write(Hierarchy hierarchy)
        {
            Row dataRow = dataSheet.createRow(dataIndex++);
            dataRow.createCell(0).setCellValue("Name");
            dataRow.createCell(1).setCellValue("Popularity");
            dataRow.createCell(2).setCellValue("Target Menu");
            int index = 3;
            for (Hierarchy taxonomy : taxonomies)
            {
                dataRow.createCell(index++).setCellValue(taxonomy.getName());
            }
            writeCategory(hierarchy.getRoot(), Collections.<Category>emptyList());
        }

        private void writeCategory(Category category, List<Category> path)
        {
            for (Element element : category.getElements())
            {
                Row structureRow = structureSheet.createRow(structureIndex++);
                structureRow.createCell(0).setCellValue(pathIndent(path) + element.getName());
                if (element instanceof Category)
                {
                    List<Category> childPath = new ArrayList<Category>(path);
                    Category childCategory = (Category) element;
                    childPath.add(childCategory);
                    writeCategory(childCategory, childPath);
                }
                else
                {
                    Row dataRow = dataSheet.createRow(dataIndex++);
                    dataRow.createCell(0).setCellValue(element.getName());
                    dataRow.createCell(1).setCellValue(element.getPopularity());
                    dataRow.createCell(2).setCellValue(pathToString(path));
                    int index = 3;
                    for (Hierarchy taxonomy : taxonomies)
                    {
                        dataRow.createCell(index++).setCellValue(taxonomy.getPathTo(element));
                    }
                }
            }
        }

        private static String pathIndent(List<Category> path)
        {
            String result = "";
            for (Category category : path)
            {
                result += "  ";
            }
            return result;
        }

        private static String pathToString(List<Category> path)
        {
            if (path.isEmpty()) return "";
            String result = "";
            for (Category category : path)
            {
                result += " > " + category.getName();
            }
            return result.substring(3);
        }
    }
}
