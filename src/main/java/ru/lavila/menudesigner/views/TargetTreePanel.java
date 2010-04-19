package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.presenters.ItemsTreePresenter;
import ru.lavila.menudesigner.stub.TargetHierarchy;

import javax.swing.*;
import java.awt.*;

public class TargetTreePanel extends JPanel
{
    public TargetTreePanel()
    {
        super(new BorderLayout());
        buildGUI();
    }

    private void buildGUI()
    {
        JScrollPane tree = new JScrollPane(new ItemsTree(new ItemsTreePresenter(new TargetHierarchy())), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.add(new JButton("New category"));
        add(toolbar, BorderLayout.NORTH);
        add(tree, BorderLayout.CENTER);
        setMinimumSize(new Dimension(200, 100));
        setPreferredSize(new Dimension(400, 500));
    }
}
