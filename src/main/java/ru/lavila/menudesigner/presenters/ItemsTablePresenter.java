package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.Item;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ItemsTablePresenter extends AbstractTableModel
{
    private final List<Item> items;

    public ItemsTablePresenter(Hierarchy hierarchy)
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
        return columnIndex == 0 ? item.getName() : PopularityFormatter.format(item.getPopularity());
    }

    @Override
    public String getColumnName(int column)
    {
        return column == 0 ? "Name" : "Popularity";
    }
}
