package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.stub.SourceHierarchy;
import ru.lavila.menudesigner.stub.TargetHierarchy;

import javax.swing.*;
import java.awt.*;

public class MainContentPane extends JPanel
{
    public MainContentPane()
    {
        super(new BorderLayout());
        buildGUI();
    }

    private void buildGUI() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new HierarchyView(new SourceHierarchy(), false),
                new HierarchyView(new TargetHierarchy(), true));
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
        add(new JLabel(" "), BorderLayout.SOUTH);
    }
}
