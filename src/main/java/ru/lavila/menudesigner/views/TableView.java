package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.presenters.TablePresenter;

import javax.swing.*;
import java.util.List;

public class TableView extends JTable
{
    public TableView(TablePresenter tablePresenter)
    {
        super(tablePresenter);
    }

    public List<Element> getSelectedElements()
    {
        return ((TablePresenter) getModel()).getSelectedItems(getSelectedRows());
    }
}
