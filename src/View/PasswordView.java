package View;


import controller.PasswordController;
import model.ModelEvent;
import model.PasswordModel;
import util.PasswordDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


public class PasswordView extends AbstractView {

    public static final String ADD = "Generate Password";
    public static final String COPY = "Copy to clipboard";
    public static final String ADDOWN = "Add Password";
    public static final String REMOVE = "Remove Password";
    public static final String RENAME = "Rename Password";
    public static final String EXIT = "Exit";
    private JComboBox passwords;
    private static PasswordController pController;
    private static PasswordDB db = null;

    public PasswordView(PasswordModel model, PasswordController controller) {
        super(model, controller);

        super.setTitle("Super Awesome Password Manager");
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Handler handle = new Handler();

        passwords = new JComboBox(model.getNames());
        passwords.addActionListener(handle);
        JButton addPass = new JButton(ADD);
        JButton copyPass = new JButton(COPY);
        JButton addOwnPass = new JButton(ADDOWN);
        JButton removePass = new JButton(REMOVE);
        JButton renamePass = new JButton(RENAME);
        JButton exitButton = new JButton(EXIT);
        addPass.addActionListener(handle);
        copyPass.addActionListener(handle);
        addOwnPass.addActionListener(handle);
        removePass.addActionListener(handle);
        renamePass.addActionListener(handle);
        exitButton.addActionListener(handle);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));
        mainPanel.add(passwords);
        mainPanel.add(addOwnPass);
        mainPanel.add(addPass);
        mainPanel.add(copyPass);
        mainPanel.add(removePass);
        mainPanel.add(renamePass);
        mainPanel.add(exitButton);
        this.getContentPane().add(mainPanel);
        pack();
        setSize(500,500);
    }

    public void modelChanged(ModelEvent e) {
        if(e.getActionCommand() == "add"){
            passwords.removeAllItems();
            for (Object p : (((PasswordModel) getModel()).getNames())) {
                passwords.addItem(p);
            }
        } else if(e.getActionCommand() == "remove") {
            passwords.removeItemAt(e.getID());
        }
        this.repaint();
        pack();
    }

    class Handler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((PasswordController) getController()).operation(e.getActionCommand(), passwords.getSelectedIndex());
        }
    }


    public static void main(String args[]) {
        try {
            db = new PasswordDB("test");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        pController = new PasswordController(db);
    }

}
