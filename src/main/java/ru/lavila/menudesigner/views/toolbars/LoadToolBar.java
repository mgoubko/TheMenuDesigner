package ru.lavila.menudesigner.views.toolbars;

import ru.lavila.menudesigner.io.ItemsListLoader;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.views.MainContentPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadToolBar extends JToolBar
{
    private final MainContentPane contentPane;
    private final JFileChooser fileChooser;
    private final ItemsListLoader loader;

    public LoadToolBar(MainContentPane contentPane)
    {
        super("Load Data");
        this.contentPane = contentPane;
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        loader = new ItemsListLoader();
        setupButtons();
    }

    private void setupButtons()
    {
        JButton open = new JButton("Open");
        open.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int result = fileChooser.showOpenDialog(contentPane);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    ItemsList itemsList = loader.loadItemsList(fileChooser.getSelectedFile().getPath());
                    if (itemsList != null)
                    {
                        contentPane.setupView(itemsList);
                    }
                }
            }
        });
        add(open);
    }

}
