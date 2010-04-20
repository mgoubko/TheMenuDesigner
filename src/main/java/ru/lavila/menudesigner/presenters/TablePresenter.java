package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;
import ru.lavila.menudesigner.presenters.PopularityPresenter;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TablePresenter extends AbstractTableModel
{
    private final List<Item> items;

    public TablePresenter(Hierarchy hierarchy)
    {
        this.items = hierarchy.getItems();
    }

    public int getRowCount()
    {
        return items.size();
    }

    public int getColumnCount()
    {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Item item = items.get(rowIndex);
        return columnIndex == 0 ? item.getName() : new PopularityPresenter(item.getPopularity());
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        Item item = items.get(rowIndex);
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
}
