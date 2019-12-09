package cz.ismar.projects.IEdit.ui;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.prefs.Preferences;

public class BoundsSaver extends ComponentAdapter
{

    public BoundsSaver(Component component1, Preferences preferences1)
    {
        component = component1;
        preferences = preferences1;
    }

    public void initiateComponentBounds()
    {
        initiateComponentBounds(-1, -1, -1, -1);
    }

    public void initiateComponentBounds(int i, int j, int k, int l)
    {
        Rectangle rectangle = component.getBounds();
        if(i == -1)
            i = rectangle.x;
        if(j == -1)
            j = rectangle.y;
        if(k == -1)
            k = rectangle.width;
        if(l == -1)
            l = rectangle.height;
        component.setBounds(preferences.getInt("window_x", i), preferences.getInt("window_y", j), preferences.getInt("window_w", k), preferences.getInt("window_h", l));
        component.addComponentListener(this);
    }

    public void componentResized(ComponentEvent componentevent)
    {
        preferences.putInt("window_w", component.getWidth());
        preferences.putInt("window_h", component.getHeight());
    }

    public void componentMoved(ComponentEvent componentevent)
    {
        Rectangle rectangle = component.getBounds();
        preferences.putInt("window_x", rectangle.x);
        preferences.putInt("window_y", rectangle.y);
    }

    private Component component;
    private Preferences preferences;
}
