package View;

import controller.Controller;
import model.AbstractModel;
import model.Model;
import model.ModelListener;

import javax.swing.*;

abstract public class AbstractView extends JFrame implements View, ModelListener {
    private Model model;
    private Controller controller;
    public AbstractView(Model model, Controller controller)
    { setModel(model);
        setController(controller);
    }

    public void registerWithModel() {
        ((AbstractModel)model).addModelListener(this);
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
        registerWithModel();
    }
}
