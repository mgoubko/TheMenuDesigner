package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.ItemsController;
import ru.lavila.menudesigner.controllers.MenuModelsController;
import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.models.menumodels.MenuModelsLibrary;
import ru.lavila.menudesigner.presenters.MenuModelsPresenter;
import ru.lavila.menudesigner.presenters.TablePresenter;
import ru.lavila.menudesigner.presenters.TreePresenter;
import ru.lavila.menudesigner.views.toolbars.ItemsToolBar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ItemsSwitchView extends JTabbedPane implements ItemsView
{
    private final ItemsController controller;
    private final List<JToolBar> toolBars;

    public ItemsSwitchView(ItemsList itemsList, ItemsListCalculator calculator)
    {
        super();

        controller = new ItemsController(itemsList);

        MenuModelsLibrary menuModelsLibrary = new MenuModelsLibrary();
        TableView tableView = new TableView(
                new TablePresenter(itemsList),
                new MenuModelsPanel(new MenuModelsPresenter(calculator, menuModelsLibrary), new MenuModelsController(calculator, menuModelsLibrary))
        );
        addTab("List", tableView);
        for (Hierarchy hierarchy : itemsList.getHierarchies())
        {
            if (hierarchy.isTaxomony())
            {
                TreeView treeView = new TreeView(new TreePresenter(hierarchy), new TreeController(hierarchy), new HierarchyCalculator(calculator, hierarchy));
                addTab(hierarchy.getRoot().getName(), treeView);
            }
        }

        toolBars = new ArrayList<JToolBar>();
        addToolBar(new ItemsToolBar(controller));

        addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                for (JToolBar toolBar : toolBars)
                {
                    getSelectedView().addToolBar(toolBar);
                }
            }
        });
    }

    public void addToolBar(JToolBar toolBar)
    {
        toolBars.add(toolBar);
        getSelectedView().addToolBar(toolBar);
    }

    private ItemsView getSelectedView()
    {
        return (ItemsView) getSelectedComponent();
    }

    public java.util.List<Element> getSelectedElements()
    {
        return getSelectedView().getSelectedElements(); 
    }
}
