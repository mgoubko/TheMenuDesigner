package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.*;
import ru.lavila.menudesigner.models.events.ElementChangeEvent;
import ru.lavila.menudesigner.models.events.ItemsListChangeEvent;
import ru.lavila.menudesigner.models.events.ItemsListListener;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TablePresenter extends AbstractTableModel implements ItemsListListener
{
    private final ItemsList itemsList;

    public TablePresenter(ItemsList itemsList)
    {
        this.itemsList = itemsList;
        itemsList.addModelListener(this);
    }

    public int getRowCount()
    {
        return this.itemsList.size();
    }

    public int getColumnCount()
    {
        return 2 + itemsList.getTaxonomies().size();
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Item item = itemsList.get(rowIndex);
        if (columnIndex == 0)
        {
            return item.getName();
        }
        else if (columnIndex == 1)
        {
            return String.format("%.5f", item.getPopularity());
        }
        else
        {
            return getPathToItem(itemsList.getTaxonomies().get(columnIndex - 2), item);
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        Item item = itemsList.get(rowIndex);
        if (columnIndex == 0)
        {
            item.setName(value.toString());
        }
        else if (columnIndex == 1)
        {
            try
            {
                item.setPopularity(Double.parseDouble(value.toString()));
            }
            catch (NumberFormatException e)
            {
                
            }
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex < 2;
    }

    @Override
    public String getColumnName(int column)
    {
        if (column == 0)
        {
            return "Name";
        }
        else if (column == 1)
        {
            return "Popularity";
        }
        else
        {
            return itemsList.getTaxonomies().get(column - 2).getRoot().getName();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return String.class;
    }

    public void itemChanged(ElementChangeEvent event)
    {
        if (event.getElement() instanceof Item)
        {
            int row = itemsList.indexOf((Item) event.getElement());
            fireTableRowsUpdated(row, row);
        }
    }

    public void listChanged(ItemsListChangeEvent event)
    {
        int firstRow = getRowCount();
        int lastRow = -1;
        for (int index : event.getIndexes())
        {
            if (index < firstRow) firstRow = index;
            if (index > lastRow) lastRow = index;
        }

        //todo: collect separate intervals
        if (lastRow != -1)
        {
            switch (event.getType())
            {
                case ELEMENTS_ADDED:
                    fireTableRowsInserted(firstRow, lastRow);
                    break;
                case ELEMENTS_REMOVED:
                    fireTableRowsDeleted(firstRow, lastRow);
                    break;
            }
        }
    }

    public List<Element> getSelectedItems(int[] selectedRows)
    {
        List<Element> selectedElements = new ArrayList<Element>();
        for (int selectedRow : selectedRows)
        {
            selectedElements.add(itemsList.get(selectedRow));
        }
        return selectedElements;
    }

    private String getPathToItem(Hierarchy hierarchy, Item item)
    {
        String result = "";
        for (Category current = hierarchy.getElementCategory(item); current != hierarchy.getRoot(); current = hierarchy.getElementCategory(current))
        {
            result = " / " + current.getName() + result;
        }
        return result.length() == 0 ? result : result.substring(3);
    }
}
