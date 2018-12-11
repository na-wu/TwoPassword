package util;

import util.Password;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;

public class PasswordDB {

    Connection sqlConnection = null;

    public PasswordDB(String filename) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            sqlConnection = DriverManager.getConnection("jdbc:sqlite:" + filename + ".db");
            this.createTable();
        } catch (SQLException | ClassNotFoundException e) {
            throw e;
        }
    }

    private void createTable() throws SQLException {
        Statement stmt = null;
        String table = "CREATE TABLE IF NOT EXISTS passwords (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME VARCHAR(50) NOT NULL UNIQUE, PASSWORD VARCHAR(120) NOT NULL)";
        try {
            stmt = sqlConnection.createStatement();
            stmt.executeUpdate(table);
            stmt.close();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void addPassword(Password password) throws SQLException {
        PreparedStatement insert = null;
        String query = "INSERT INTO passwords (NAME, PASSWORD) VALUES (?, ?)";
        try {
            insert = this.sqlConnection.prepareStatement(query);
            insert.setString(1, password.getPasswordName());
            insert.setString(2, password.getPasswordString());
            insert.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (insert != null) {
                insert.close();
            }
        }

    }

    public Password getPassword(String nameOfPass) throws SQLException, EmptyOrSpaceException{
        ResultSet results;
        PreparedStatement getPassword;
        String query = "SELECT * from passwords where name = ?";
        getPassword = this.sqlConnection.prepareStatement(query);
        getPassword.setString(1, nameOfPass);
        results = getPassword.executeQuery();
        String name = results.getString("NAME");
        String password = results.getString("PASSWORD");
        if (getPassword != null) {
            getPassword.closeOnCompletion();
        }
        return new Password(name, password);
    }

    public ArrayList<Password> getPasswords() throws SQLException, EmptyOrSpaceException{
        ResultSet results;
        PreparedStatement getPasswords;
        ArrayList<Password> passwords = new ArrayList<>();
        String query = "SELECT * FROM passwords";
        getPasswords = this.sqlConnection.prepareStatement(query);
        results = getPasswords.executeQuery();

        while (results.next()) {
            String name = results.getString("NAME");
            String password = results.getString("PASSWORD");
            Password row = new Password(name, password);
            passwords.add(row);
        }
        if (getPasswords != null)
            getPasswords.closeOnCompletion();
        return passwords;
    }

    public void storePasswords(ArrayList<Password> passwords) throws SQLException {
        PreparedStatement storePasswords;
        String query = "INSERT INTO passwords (NAME,PASSWORD) VALUES(?, ?)";
        storePasswords = this.sqlConnection.prepareStatement(query);
        for (Password p : passwords) {
            storePasswords.setString(1, p.getPasswordName());
            storePasswords.setString(2, p.getPasswordString());
            storePasswords.executeUpdate();
        }
        if (storePasswords != null)
            storePasswords.close();
    }

    public void removePassword(String name) throws SQLException {
        PreparedStatement removePassword;
        String query = "DELETE FROM passwords where name= ?";
        removePassword = this.sqlConnection.prepareStatement(query);
        removePassword.setString(1, name);
        removePassword.executeUpdate();
        if (removePassword != null)
            removePassword.close();
    }

    public void renamePassword(String oldName, String newName) throws SQLException {
        PreparedStatement renamePassword;
        String query = "UPDATE passwords set name = ? where name = ?";
        renamePassword = this.sqlConnection.prepareStatement(query);
        renamePassword.setString(1, newName);
        renamePassword.setString(2, oldName);
        renamePassword.executeUpdate();
        if (renamePassword != null)
            renamePassword.close();
    }

    public void clearPasswords() throws SQLException {
        PreparedStatement clear = this.sqlConnection.prepareStatement("DELETE FROM passwords");
        clear.executeUpdate();
        if (clear != null)
            clear.close();
    }

    public void updatePassword(Password newP) throws SQLException {
        PreparedStatement update;
        String query = "UPDATE passwords set password = ? where name = ?";
        update = this.sqlConnection.prepareStatement(query);
        update.setString(1, newP.getPasswordString());
        update.setString(2, newP.getPasswordString());
        update.executeUpdate();
    }

    public void closeDatabase() throws SQLException {
        this.sqlConnection.close();
    }

}
