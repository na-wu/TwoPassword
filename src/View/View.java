package View;

import controller.Controller;
import model.Model;

public interface View {
    Controller getController();
    void setController(Controller controller);
    Model getModel();
    void setModel(Model model);
}
