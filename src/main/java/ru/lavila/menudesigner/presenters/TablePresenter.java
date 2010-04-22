package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.*;
import ru.lavila.menudesigner.models.events.ElementChangeEvent;
import ru.lavila.menudesigner.models.events.HierarchyListener;
import ru.lavila.menudesigner.models.events.StructureChangeEvent;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TablePresenter extends AbstractTableModel implements HierarchyListener
{
    private final Hierarchy hierarchy;
    private List<Item> items;

    public TablePresenter(Hierarchy hierarchy)
    {
        this.hierarchy = hierarchy;
        hierarchy.addModelListener(this);
    }

    private List<Item> getItems()
    {
        if (items == null)
        {
            items = hierarchy.getItems();
        }
        return items;
    }

    public int getRowCount()
    {
        return getItems().size();
    }

    public int getColumnCount()
    {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Item item = getItems().get(rowIndex);
        return columnIndex == 0 ? item.getName() : new PopularityPresenter(item.getPopularity());
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        Item item = getItems().get(rowIndex);
        if (columnIndex == 0)
        {
            item.setName(value.toString());
        }
        else if (columnIndex == 1)
        {
            item.setPopularity(((PopularityPresenter) value).value);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    @Override
    public String getColumnName(int column)
    {
        return column == 0 ? "Name" : "Popularity";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnIndex == 0 ? String.class : PopularityPresenter.class;
    }

    public void modelChanged(ElementChangeEvent event)
    {
        if (event.getElement() instanceof Item)
        {
            int row = getItems().indexOf(event.getElement());
            fireTableRowsUpdated(row, row);
        }
    }

    public void structureChanged(StructureChangeEvent event)
    {
        switch (event.getType())
        {
            case ELEMENTS_ADDED:
                elementsAdded(event.getCategorizedElements());
                break;
            case ELEMENTS_REMOVED:
                elementsRemoved(event.getCategorizedElements());
                break;
        }
    }

    private void elementsAdded(CategorizedElements categorizedElements)
    {
        items = null;

        int firstRow = getRowCount();
        int lastRow = -1;
        for (Element element : categorizedElements.getAllElements())
        {
            if (element instanceof Item)
            {
                int index = getItems().indexOf(element);
                if (index < firstRow) firstRow = index;
                if (index > lastRow) lastRow = index;
            }
        }

        //todo: collect separate intervals
        if (lastRow != -1) fireTableRowsInserted(firstRow, lastRow);
    }

    private void elementsRemoved(CategorizedElements categorizedElements)
    {
        int firstRow = getRowCount();
        int lastRow = -1;
        for (Element element : categorizedElements.getAllElements())
        {
            if (element instanceof Item)
            {
                int index = getItems().indexOf(element);
                if (index < firstRow) firstRow = index;
                if (index > lastRow) lastRow = index;
            }
        }

        items = null;

        //todo: collect separate intervals
        if (lastRow != -1) fireTableRowsDeleted(firstRow, lastRow);
    }

    public List<Element> getSelectedItems(int[] selectedRows)
    {
        List<Element> selectedElements = new ArrayList<Element>();
        for (int selectedRow : selectedRows)
        {
            selectedElements.add(getItems().get(selectedRow));
        }
        return selectedElements;
    }
}
