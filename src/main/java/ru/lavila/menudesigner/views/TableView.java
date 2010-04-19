package ru.lavila.menudesigner.views;

import javax.swing.*;
import javax.swing.table.TableModel;

public class TableView extends JTable
{
    public TableView(TableModel tableModel)
    {
        super(tableModel);
    }
}
