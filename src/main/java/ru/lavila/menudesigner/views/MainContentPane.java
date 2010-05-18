package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.DesignerController;
import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.presenters.TreePresenter;
import ru.lavila.menudesigner.stub.Stub;
import ru.lavila.menudesigner.views.toolbars.DesignerToolbar;
import ru.lavila.menudesigner.views.toolbars.TreeToolBar;

import javax.swing.*;
import java.awt.*;

public class MainContentPane extends JPanel
{
    public MainContentPane()
    {
        super(new BorderLayout());
        ItemsList itemsList = Stub.getSourceData();
        Hierarchy targetHierarchy = itemsList.newHierarchy("Menu", false);

        DesignerController controller = new DesignerController(itemsList, targetHierarchy);

        ItemsSwitchView sourceView = new ItemsSwitchView(itemsList);
        sourceView.setMinimumSize(new Dimension(200, 100));
        sourceView.setPreferredSize(new Dimension(400, 500));

        TreeView targetView = new TreeView(new TreePresenter(targetHierarchy), new TreeController(targetHierarchy));
        targetView.setMinimumSize(new Dimension(200, 100));
        targetView.setPreferredSize(new Dimension(400, 500));

        sourceView.addToolBar(new DesignerToolbar(sourceView, targetView, controller));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Target Menu", targetView);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sourceView, tabbedPane);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
        
        add(new JLabel(" "), BorderLayout.SOUTH);
    }
}
