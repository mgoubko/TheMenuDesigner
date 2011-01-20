package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TargetTreeController;
import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.io.ItemsListLoader;
import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.models.impl.ItemsListImpl;
import ru.lavila.menudesigner.models.menumodels.MenuModelsLibrary;
import ru.lavila.menudesigner.presenters.TreePopupMenuPresenter;
import ru.lavila.menudesigner.presenters.TreePresenter;
import ru.lavila.menudesigner.views.toolbars.LoadToolBar;
import ru.lavila.menudesigner.views.toolbars.TargetToolBar;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class MainContentPane extends JPanel
{
    private final JSplitPane splitPane;

    public MainContentPane()
    {
        super(new BorderLayout());
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
        add(new JLabel(" "), BorderLayout.SOUTH);

        InputStream stream = getClass().getResourceAsStream("/Sample.xls");
        ItemsList itemsList = stream == null ? new ItemsListImpl() : new ItemsListLoader().loadItemsList(stream);
        setupView(itemsList);
    }

    public void setupView(ItemsList itemsList)
    {
        Hierarchy targetHierarchy = itemsList.newHierarchy("Menu", false);
        MenuModelsLibrary menuModelsLibrary = new MenuModelsLibrary();

        ItemsListCalculator calculator = new ItemsListCalculator(itemsList, menuModelsLibrary.getMenuModels().get(0));

        ItemsSwitchView sourceView = new ItemsSwitchView(itemsList, calculator, menuModelsLibrary);
        sourceView.addToolBar(new LoadToolBar(this));

        HierarchyCalculator targetHierarchyCalculator = new HierarchyCalculator(calculator, targetHierarchy);
        TreeView targetView = new TreeView(new TreePresenter(targetHierarchy), new TreeController(targetHierarchy), targetHierarchyCalculator);
        targetView.addToolBar(new TargetToolBar(this, new TargetTreeController(targetHierarchy, itemsList, targetHierarchyCalculator, calculator)));
        targetView.setPopupMenu(new TreePopupMenu(new TreePopupMenuPresenter(itemsList), new TargetTreeController(targetHierarchy, itemsList, targetHierarchyCalculator, calculator), targetView));

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
