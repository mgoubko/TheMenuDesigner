package ru.lavila.menudesigner.models.menumodels;

import ru.lavila.menudesigner.math.menumodels.ReadUntilMenuModel;
import ru.lavila.menudesigner.math.menumodels.ReadUntilWithErrorMenuModel;
import ru.lavila.menudesigner.math.menumodels.SemanticAwareMenuModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuModelsLibrary
{
    private final List<MenuModel> menuModels;
    private final List<MenuModelsLibraryListener> listeners;

    public MenuModelsLibrary()
    {
        menuModels = new ArrayList<MenuModel>();
        listeners = new ArrayList<MenuModelsLibraryListener>();

        //todo: remove hardcoded examples
        addMenuModel(new ReadUntilMenuModel(0.5, 0.1, 0.5, 0.5));
        addMenuModel(new ReadUntilWithErrorMenuModel(1, 0, 4, 1, 0.05));
        addMenuModel(new SemanticAwareMenuModel(1, 0, 1, 0.05));
    }

    public List<MenuModel> getMenuModels()
    {
        return Collections.unmodifiableList(menuModels);
    }

    public void addMenuModel(MenuModel menuModel)
    {
        menuModels.add(menuModel);
        fireMenuModelsLibraryChanged();
    }

    public void setMenuModels(List<MenuModel> menuModels)
    {
        this.menuModels.clear();
        this.menuModels.addAll(menuModels);
        fireMenuModelsLibraryChanged();
    }

    public void addMenuModelsLibraryListener(MenuModelsLibraryListener listener)
    {
        if (!listeners.contains(listener))
        {
            listeners.add(listener);
        }
    }

    public void removeMenuModelsLibraryListener(MenuModelsLibraryListener listener)
    {
        listeners.remove(listener);
    }

    private void fireMenuModelsLibraryChanged()
    {
        for (MenuModelsLibraryListener listener : listeners)
        {
            listener.libraryChanged();
        }
    }
}
