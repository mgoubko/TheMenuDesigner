package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.AliasController;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.stub.SourceHierarchy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainContentPane extends JPanel
{
    private final AliasController controller;
    private final HierarchyView sourceView;
    private final HierarchyView targetView;

    public MainContentPane()
    {
        super(new BorderLayout());
        Hierarchy sourceHierarchy = new SourceHierarchy();
        Hierarchy targetHierarchy = new Hierarchy();
        controller = new AliasController(sourceHierarchy, targetHierarchy);
        sourceView = new HierarchyView(sourceHierarchy, false, new SourceToolbarConfigurator());
        targetView = new HierarchyView(targetHierarchy, true, new TargetToolbarConfigurator());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sourceView, targetView);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
        add(new JLabel(" "), BorderLayout.SOUTH);
    }

    private class SourceToolbarConfigurator implements ToolbarConfigurator
    {
        public void fillToolbar(ToolbarConfig config)
        {
            config.addSwitchViewButton();

            JButton use = new JButton(">>");
            use.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent e)
                        {
                            controller.aliasElements(targetView.getSelectedCategory(), sourceView.getSelectedElements());
                        }
                    });
            config.addButton(use);
        }
    }

    private class TargetToolbarConfigurator implements ToolbarConfigurator
    {
        public void fillToolbar(ToolbarConfig config)
        {
        }
    }
}
