package ru.lavila.menudesigner.models;

import java.util.List;

public interface Category extends Choice
{
    public List<Choice> getChoices();
    public void add(Choice... choices);
}
