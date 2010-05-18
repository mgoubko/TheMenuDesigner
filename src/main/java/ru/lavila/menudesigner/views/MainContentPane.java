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
        TreeView targetView = new TreeView(new TreePresenter(targetHierarchy), new TreeController(targetHierarchy));

        DesignerToolbar sourceToolbar = new DesignerToolbar(sourceView, targetView, controller);
        JPanel sourcePanel = buildContentPanel(sourceView, sourceToolbar);

        TreeToolBar targetToolbar = new TreeToolBar(targetView);
        JPanel targetPanel = buildContentPanel(targetView, targetToolbar);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sourcePanel, targetPanel);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
        add(new JLabel(" "), BorderLayout.SOUTH);
    }

    private JPanel buildContentPanel(JComponent view, JToolBar toolbar)
    {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setMinimumSize(new Dimension(200, 100));
        panel.setPreferredSize(new Dimension(400, 500));

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(view, BorderLayout.CENTER);

        return panel;
    }
}
