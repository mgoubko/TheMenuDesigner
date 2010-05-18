package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.presenters.TablePresenter;
import ru.lavila.menudesigner.presenters.TreePresenter;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ItemsSwitchView extends JTabbedPane implements ItemsView
{
    private List<ItemsView> views;

    public ItemsSwitchView(ItemsList itemsList)
    {
        super();

        views = new ArrayList<ItemsView>();
        TableView tableView = new TableView(new TablePresenter(itemsList));
        views.add(tableView);
        addTab("List", new JScrollPane(tableView, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        for (Hierarchy hierarchy : itemsList.getHierarchies())
        {
            if (hierarchy.isTaxomony())
            {
                TreeView treeView = new TreeView(new TreePresenter(hierarchy), new TreeController(hierarchy));
                views.add(treeView);
                addTab(hierarchy.getRoot().getName(), treeView);
            }
        }
    }

    public java.util.List<Element> getSelectedElements()
    {
        return views.get(getSelectedIndex()).getSelectedElements(); 
    }
}
