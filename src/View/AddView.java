package View;

import controller.AddPasswordController;
import controller.Controller;
import model.Model;
import model.ModelEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddView extends AbstractView {
    public static final String GEN = "Generate";
    private JTextField passSize;
    private JTextField passName;

    public AddView(Model model, Controller controller) {
        super(model, controller);
        Handler handle = new Handler();
        passName = new JTextField();
        passSize = new JTextField("8");
        JButton addPass = new JButton(GEN);

        JLabel passNameLabel = new JLabel("Password Name");
        JLabel passSizeLabel = new JLabel("Password Size");

        addPass.addActionListener(handle);
        passName.addActionListener(handle);
        passSize.addActionListener(handle);

        JPanel thePanel = new JPanel();
        thePanel.setLayout(new GridLayout(5, 25));
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
            ((AddPasswordController)getController()).operation(e.getActionCommand(),
                    passName.getText(), Integer.parseInt(passSize.getText()), null);
        }
    }
}
