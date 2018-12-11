package View;

import controller.AddPasswordController;
import controller.Controller;
import model.Model;
import model.ModelEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddOView extends AbstractView {
    private JTextField passName;
    private JTextField passSize;
    public static final String ADD = "Add Password";


    public AddOView(Model model, Controller controller)
    {
        super(model, controller);
        Handler handle = new Handler();
        passName = new JTextField();
        JLabel passNameLabel = new JLabel("Password Name");
        JLabel passSizeLabel = new JLabel("Password");
        passSize = new JTextField();
        JButton addPass = new JButton(ADD);
        addPass.addActionListener(handle);
        passName.addActionListener(handle);
        passSize.addActionListener(handle);
        JPanel thePanel = new JPanel();
        thePanel.setLayout(new GridLayout(5, 25, 2, 1));
        thePanel.add(passNameLabel);
        thePanel.add(passName);
        thePanel.add(passSizeLabel);
        thePanel.add(passSize);
        thePanel.add(addPass);
        this.getContentPane().add(thePanel);
        pack();
    }

    public void modelChanged(ModelEvent e) {

    }

    class Handler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                ((AddPasswordController)getController()).operation(e.getActionCommand(), passName.getText(), Integer.parseInt(passSize.getText()), passSize.getText());
            } catch(NumberFormatException f) {
                ((AddPasswordController)getController()).operation(e.getActionCommand(),
                        passName.getText(), 1, //1 is a placeholder int
                        passSize.getText());
            }
        }

    }
}
