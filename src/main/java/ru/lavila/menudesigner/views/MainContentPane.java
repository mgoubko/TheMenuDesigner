package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.io.ItemsListLoader;
import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.models.impl.ItemsListImpl;
import ru.lavila.menudesigner.presenters.TreePresenter;
import ru.lavila.menudesigner.views.toolbars.LoadToolBar;

import javax.swing.*;
import java.awt.*;

public class MainContentPane extends JPanel
{
    private final JSplitPane splitPane;
    private final LoadToolBar toolBar;

    public MainContentPane()
    {
        super(new BorderLayout());
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
        add(new JLabel(" "), BorderLayout.SOUTH);
        toolBar = new LoadToolBar(this);

        //todo: Remove autoload of data
        ItemsList itemsList = new ItemsListLoader().loadItemsList(getClass().getResourceAsStream("/Mobile.xls"));
        if (itemsList == null) itemsList = new ItemsListImpl();
        setupView(itemsList);
    }

    public void setupView(ItemsList itemsList)
    {
        Hierarchy targetHierarchy = itemsList.newHierarchy("Menu", false);

        ItemsSwitchView sourceView = new ItemsSwitchView(itemsList);
        sourceView.addToolBar(toolBar);

        TreeView targetView = new TreeView(new TreePresenter(targetHierarchy), new TreeController(targetHierarchy), new HierarchyCalculator(itemsList, targetHierarchy));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Target Menu", targetView);

        splitPane.setLeftComponent(sourceView);
        splitPane.setRightComponent(tabbedPane);

        for (Component component : splitPane.getComponents())
        {
            component.setMinimumSize(new Dimension(200, 100));
            component.setPreferredSize(new Dimension(400, 500));
        }
    }
}
