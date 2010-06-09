package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.MenuDesigner;
import ru.lavila.menudesigner.controllers.MenuModelsController;
import ru.lavila.menudesigner.models.menumodels.MenuModelListener;
import ru.lavila.menudesigner.models.menumodels.MenuModelsLibraryListener;
import ru.lavila.menudesigner.presenters.MenuModelsPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuModelsPanel extends JPanel implements MenuModelListener, MenuModelsLibraryListener
{
    private final MenuModelsPresenter presenter;
    private final MenuModelsController controller;
    private final JComboBox menuModels;
    private final JLabel timeLabel;
    private final JLabel proportionLabel;
    private final JLabel branchingLabel;
    private final JFileChooser fileChooser;

    public MenuModelsPanel(MenuModelsPresenter presenter, MenuModelsController controller)
    {
        super(new GridLayout(4, 1));

        this.presenter = presenter;
        this.controller = controller;
        presenter.addMenuModelListener(this);
        presenter.addMenuModelsLibraryListener(this);

        setPreferredSize(new Dimension(500, 120));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        timeLabel = new JLabel();
        branchingLabel = new JLabel();
        proportionLabel = new JLabel();
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        JPanel menuTypePanel = new JPanel(new BorderLayout(10, 0));
        menuTypePanel.add(new JLabel("Menu type"), BorderLayout.WEST);

        //todo: refactor presenter to implement combo box model
        menuModels = new JComboBox(presenter.getModelNames());
        menuModels.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (menuModels.getSelectedIndex() == -1) return;
                MenuModelsPanel.this.controller.setMenuModel(MenuModelsPanel.this.presenter.getModel(menuModels.getSelectedIndex()));
            }
        });
        menuModels.setSelectedIndex(this.presenter.getCurrentModelIndex());
        menuTypePanel.add(menuModels, BorderLayout.CENTER);
        JButton loadMenuModels = new JButton(MenuDesigner.getIcon("folder"));
        loadMenuModels.setToolTipText("Load menu models...");
        loadMenuModels.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int result = fileChooser.showOpenDialog(MenuModelsPanel.this);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    MenuModelsPanel.this.controller.loadMenuModels(fileChooser.getSelectedFile().getPath());
                }
            }
        });
        menuTypePanel.add(loadMenuModels, BorderLayout.EAST);
        add(menuTypePanel);

        add(timeLabel);
        add(branchingLabel);
        add(proportionLabel);
    }

    public void menuModelChanged()
    {
        timeLabel.setText("Optimal menu search time: " + presenter.getOptimalSearchTime());
        branchingLabel.setText("Menu panels should have " + presenter.getOptimalBranchingFactor() + " choices with relative popularities");
        proportionLabel.setText(presenter.getOptimalProportion());
    }

    public void libraryChanged()
    {
        menuModels.removeAllItems();
        for (String name : presenter.getModelNames())
        {
            menuModels.addItem(name);
        }
        menuModels.setSelectedIndex(presenter.getCurrentModelIndex());
    }
}
