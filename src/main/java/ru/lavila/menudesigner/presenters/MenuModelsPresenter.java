package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.math.*;
import ru.lavila.menudesigner.models.menumodels.MenuModel;
import ru.lavila.menudesigner.models.menumodels.MenuModelListener;
import ru.lavila.menudesigner.models.menumodels.MenuModelsLibrary;
import ru.lavila.menudesigner.models.menumodels.MenuModelsLibraryListener;

import java.util.ArrayList;
import java.util.List;

public class MenuModelsPresenter implements MenuModelListener, MenuModelsLibraryListener
{
    private static final int DEFAULT_INDEX = 1;

    private final ItemsListCalculator calculator;
    private final MenuModelsLibrary library;
    private final List<MenuModelListener> menuModelListeners;
    private final List<MenuModelsLibraryListener> menuModelsLibraryListeners;

    public MenuModelsPresenter(ItemsListCalculator calculator, MenuModelsLibrary library)
    {
        this.calculator = calculator;
        this.library = library;
        this.menuModelListeners = new ArrayList<MenuModelListener>();
        this.menuModelsLibraryListeners = new ArrayList<MenuModelsLibraryListener>();
        calculator.addModelListener(this);
        library.addMenuModelsLibraryListener(this);
    }

    public String[] getModelNames()
    {
        List<MenuModel> models = library.getMenuModels();
        String[] names = new String[models.size()];
        for (int index = 0; index < models.size(); index++)
        {
            names[index] = models.get(index).getName();
        }
        return names;
    }

    public MenuModel getModel(int index)
    {
        return library.getMenuModels().get(index);
    }

    public int getCurrentModelIndex()
    {
        int index = library.getMenuModels().indexOf(calculator.getMenuModel());
        return index == -1 ? DEFAULT_INDEX : index;
    }

    public String getOptimalSearchTime()
    {
        return String.format("%.2fs", calculator.getOptimalSearchTime());
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

    public void addMenuModelListener(MenuModelListener listener)
    {
        if (!menuModelListeners.contains(listener))
        {
            menuModelListeners.add(listener);
        }
    }

    public void addMenuModelsLibraryListener(MenuModelsLibraryListener listener)
    {
        if (!menuModelsLibraryListeners.contains(listener))
        {
            menuModelsLibraryListeners.add(listener);
        }
    }

    public void menuModelChanged()
    {
        for (MenuModelListener listener : menuModelListeners)
        {
            listener.menuModelChanged();
        }
    }

    public void libraryChanged()
    {
        for (MenuModelsLibraryListener listener : menuModelsLibraryListeners)
        {
            listener.libraryChanged();
        }
    }

    public String getOptimalBranchingFactor()
    {
        return Integer.toString(calculator.getOptimalProportion().length);
    }
}
