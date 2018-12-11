package controller;

import model.Model;
import View.View;

public class AbstractController implements Controller {
    private View view;
    private Model model;

    public void setModel(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
