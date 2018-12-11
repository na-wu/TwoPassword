package model;

import java.awt.event.ActionEvent;

public class ModelEvent extends ActionEvent {
    public ModelEvent(Object obj, int id, String message)
    {
        super(obj, id, message);
    }

}
