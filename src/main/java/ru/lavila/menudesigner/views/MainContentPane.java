package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.presenters.TreePresenter;
import ru.lavila.menudesigner.stub.Stub;

import javax.swing.*;
import java.awt.*;

public class MainContentPane extends JPanel
{
    public MainContentPane()
    {
        super(new BorderLayout());
        ItemsList itemsList = Stub.getSourceData();
        Hierarchy targetHierarchy = itemsList.newHierarchy("Menu", false);

        ItemsSwitchView sourceView = new ItemsSwitchView(itemsList);

        TreeView targetView = new TreeView(new TreePresenter(targetHierarchy, new HierarchyCalculator(itemsList, targetHierarchy)), new TreeController(targetHierarchy));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Target Menu", targetView);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sourceView, tabbedPane);
        for (Component component : splitPane.getComponents())
        {
            component.setMinimumSize(new Dimension(200, 100));
            component.setPreferredSize(new Dimension(400, 500));
        }
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
        
        add(new JLabel(" "), BorderLayout.SOUTH);
    }
}
