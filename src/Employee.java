import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.*;

public class Employee {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtContact;
    private JButton saveButton;
    private JTable table1;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField txtid;
    private JScrollPane table_1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Connection con;
    PreparedStatement pst;



    public void connect()
    {

            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/employeescrud", "root", "your password");
                System.out.println("Successfully");

            }
            catch (ClassNotFoundException ex){

                ex.printStackTrace();
            }

            catch(SQLException ex)
            {
                ex.printStackTrace();
            }

        }






    public Employee() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empname,salary,contact;

                empname = txtName.getText();
                salary = txtSalary.getText();
                contact = txtContact.getText();


                try {
                    pst = con.prepareStatement("insert into employee(empname,salary,contact)values(?,?,?)");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3,contact);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Successfully added");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtContact.setText("");
                    txtName.requestFocus();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String empid = txtid.getText();

                    pst = con.prepareStatement("select empname,salary,contact from employee where id = ?");
                    pst.setString(1, empid);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()==true)
                    {
                        String empname = rs.getString(1);
                        String emsalary = rs.getString(2);
                        String emcontact = rs.getString(3);

                        txtName.setText(empname);
                        txtSalary.setText(emsalary);
                        txtContact.setText(emcontact);

                    }
                    else
                    {
                        txtName.setText("");
                        txtSalary.setText("");
                        txtContact.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid Employee Number");

                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
            });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empid,empname,salary,contact;
                empname = txtName.getText();
                salary = txtSalary.getText();
                contact = txtContact.getText();
                empid = txtid.getText();

                try {
                    pst = con.prepareStatement("update employee set empname = ?,salary = ?,contact = ? where id = ?");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, contact);
                    pst.setString(4, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Update complited!");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtContact.setText("");
                    txtName.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
            });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empid;
                empid = txtid.getText();

                try {
                    pst = con.prepareStatement("delete from employee  where id = ?");

                    pst.setString(1, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleted!");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtContact.setText("");
                    txtName.requestFocus();
                }

                catch (SQLException e1)
                {

                    e1.printStackTrace();
                }
            }
        });
    }

    private void table_load() {
        try
        {
            pst = con.prepareStatement("select * from employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {

    }
}
