package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.math.*;

import java.util.ArrayList;
import java.util.List;

public class MenuModelsPresenter implements MenuModelListener
{
    private final ItemsListCalculator calculator;
    private final List<MenuModel> models;
    private final List<CalculationsListener> listeners;

    public MenuModelsPresenter(ItemsListCalculator calculator)
    {
        this.calculator = calculator;
        this.listeners = new ArrayList<CalculationsListener>();
        this.models = new ArrayList<MenuModel>();
        models.add(new ReadUntilMenuModel(0.5, 0.1, 0.5, 0.5));
        models.add(new ReadUntilWithErrorMenuModel(1, 0, 1, 0.5, 0.05));
        calculator.addModelListener(this);
    }

    public String[] getModelNames()
    {
        String[] names = new String[models.size()];
        for (int index = 0; index < models.size(); index++)
        {
            names[index] = models.get(index).getName();
        }
        return names;
    }

    public MenuModel getModel(int index)
    {
        return models.get(index);
    }

    public int getDefaultIndex()
    {
        return 1;
    }

    public String getOptimalSearchTime()
    {
        return String.format("%.2f", calculator.getOptimalSearchTime());
    }

    public String getOptimalProportion()
    {
        double[] proportion = calculator.getOptimalProportion();
        String result = "";
        for (double v : proportion)
        {
            result += "; " + String.format("%.2f", v);
        }
        return result.substring(2);
    }

    public void addCalculationListener(CalculationsListener listener)
    {
        if (!listeners.contains(listener))
        {
            listeners.add(listener);
        }
    }

    public void removeCalculationListener(CalculationsListener listener)
    {
        listeners.remove(listener);
    }

    public void menuModelChanged()
    {
        for (CalculationsListener listener : listeners)
        {
            listener.valuesChanged();
        }
    }

    public String getOptimalBranchingFactor()
    {
        return Integer.toString(calculator.getOptimalProportion().length);
    }
}
