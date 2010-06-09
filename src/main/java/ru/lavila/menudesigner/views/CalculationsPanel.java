package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.math.MenuModelListener;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.presenters.CalculationsListener;

import javax.swing.*;
import java.awt.*;

public class CalculationsPanel extends JPanel implements CalculationsListener, MenuModelListener
{
    private final HierarchyCalculator calculator;
    private final JLabel currentSearchTime;
    private final JLabel optimalSearchTime;
    private final JLabel searchTimeExcess;
    private final JPanel panelCalculations;
    private Category activeCategory;

    public CalculationsPanel(HierarchyCalculator calculator)
    {
        super(new BorderLayout());

        this.calculator = calculator;
        calculator.addModelListener(this);

        setPreferredSize(new Dimension(500, 120));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel hierarchyCalculations = new JPanel(new GridLayout(3, 2, 20, 5));
        hierarchyCalculations.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Menu Search Time"), BorderFactory.createEmptyBorder(5, 10, 5, 0)));
        hierarchyCalculations.add(new JLabel("Current"));
        currentSearchTime = new JLabel();
        hierarchyCalculations.add(currentSearchTime);
        hierarchyCalculations.add(new JLabel("Optimal"));
        optimalSearchTime = new JLabel();
        hierarchyCalculations.add(optimalSearchTime);
        hierarchyCalculations.add(new JLabel("Excess"));
        searchTimeExcess = new JLabel();
        hierarchyCalculations.add(searchTimeExcess);
        add(hierarchyCalculations, BorderLayout.WEST);

        panelCalculations = new JPanel(new GridLayout(3, 1, 20, 5));
        panelCalculations.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Active Category"), BorderFactory.createEmptyBorder(5, 20, 5, 0)));
        panelCalculations.add(new JLabel());
        panelCalculations.add(new JLabel());
        panelCalculations.add(new JLabel());

        add(panelCalculations, BorderLayout.CENTER);

        valuesChanged();
    }

    public void valuesChanged()
    {
        recalculateParameters();
    }

    public void menuModelChanged()
    {
        recalculateParameters();
    }

    private void recalculateParameters()
    {
        double optimal = calculator.getOptimalSearchTime();
        optimalSearchTime.setText(String.format("%.2fs", optimal));
        double current = calculator.getHierarchySearchTime();
        if (Double.isNaN(current))
        {
            currentSearchTime.setText("Unknown");
            searchTimeExcess.setText("Unknown");
        }
        else
        {
            currentSearchTime.setText(String.format("%.2fs", current));
            searchTimeExcess.setText(String.format("%d%%", Math.round(100 * (current - optimal) / optimal)));
        }

        String[] texts = new String[3];
        if (activeCategory != null)
        {
            texts[0] = activeCategory.getName();
            texts[1] = "Time loss";
            double timeLoss = calculator.getCategoryTimeLoss(activeCategory);
            texts[2] = Double.isNaN(timeLoss) ? "Unknown" : String.format("%.4fs", timeLoss);
            if (!Double.isNaN(current) && !Double.isNaN(timeLoss))
            {
                texts[2] += String.format(" (%d%%)", Math.round(100 * timeLoss / (current - optimal)));
            }
        }
        for (int index = 0; index < 3; index++)
        {
            ((JLabel) panelCalculations.getComponent(index)).setText(texts[index]);
        }
    }

    public void showFor(Category category)
    {
        if (activeCategory != category)
        {
            activeCategory = category;
            recalculateParameters();
        }
    }
}
