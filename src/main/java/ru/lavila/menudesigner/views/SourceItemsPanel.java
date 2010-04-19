package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.presenters.ItemsTablePresenter;
import ru.lavila.menudesigner.stub.SourceHierarchy;

import javax.swing.*;
import java.awt.*;

public class SourceItemsPanel extends JPanel
{
    public SourceItemsPanel()
    {
        super(new BorderLayout());
        buildGUI();
    }

    private void buildGUI()
    {
        JScrollPane items = new JScrollPane(new ItemsTable(new ItemsTablePresenter(new SourceHierarchy())), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.add(new JButton("View as tree"));
        toolbar.add(new JButton("Show unused items"));
        add(toolbar, BorderLayout.NORTH);
        add(items, BorderLayout.CENTER);
        setMinimumSize(new Dimension(200, 100));
        setPreferredSize(new Dimension(400, 500));
    }
}
