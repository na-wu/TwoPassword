package model;

import java.util.ArrayList;
import java.util.Iterator;

public class AbstractModel implements Model {

    private ArrayList listeners = new ArrayList(5);

    @Override
    public void notifyChanged(ModelEvent event) {
        ArrayList list = (ArrayList)listeners.clone();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ModelListener ml = (ModelListener)it.next();
            ml.modelChanged(event);
        }
    }
    public void addModelListener(ModelListener l)
    {
        listeners.add(l);
    }
}
