package ru.lavila.menudesigner;

import ru.lavila.menudesigner.views.MainContentPane;

import javax.swing.*;

public class MenuDesigner
{
    private static void createAndShowGUI()
    {
        JFrame frame = new JFrame("Menu Designer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MainContentPane());
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    public static Icon getIcon(String name)
    {
        return new ImageIcon(MenuDesigner.class.getResource("icons/" + name + ".gif"));
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }
}
