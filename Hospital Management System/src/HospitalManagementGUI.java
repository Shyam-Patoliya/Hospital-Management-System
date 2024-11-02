import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class HospitalManagementGUI extends JFrame implements ActionListener {
    private JButton viewDoctorsButton;
    private JButton viewPatientsButton;
    private JButton addDoctorButton;
    private JButton addPatientButton;
    private JButton deleteDetailsButton;
    private JButton searchButton;
    private JButton updateDetailsButton;
    private JButton exitButton;
    private JTextArea displayArea;
    private Connection conn;

    public HospitalManagementGUI() {
        setTitle("Hospital Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel buttonPanel = new JPanel();
        viewDoctorsButton = new JButton("View Doctors");
        viewPatientsButton = new JButton("View Patients");
        addDoctorButton = new JButton("Add Doctor");
        addPatientButton = new JButton("Add Patient");
        deleteDetailsButton = new JButton("Delete Details");
        searchButton = new JButton("Search");
        updateDetailsButton = new JButton("Update Details");
        exitButton = new JButton("Exit");

        viewDoctorsButton.addActionListener(this);
        viewPatientsButton.addActionListener(this);
        addDoctorButton.addActionListener(this);
        addPatientButton.addActionListener(this);
        deleteDetailsButton.addActionListener(this);
        searchButton.addActionListener(this);
        updateDetailsButton.addActionListener(this);
        exitButton.addActionListener(this);

        buttonPanel.add(viewDoctorsButton);
        buttonPanel.add(viewPatientsButton);
        buttonPanel.add(addDoctorButton);
        buttonPanel.add(addPatientButton);
        buttonPanel.add(deleteDetailsButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateDetailsButton);
        buttonPanel.add(exitButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        Container contentPane = getContentPane();
        contentPane.add(buttonPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Call createTables method from Hospital_Management class
        try {
            Hospital_Management.createTables();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error creating tables: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewDoctorsButton) {
            try {
                viewDoctors();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == viewPatientsButton) {
            try {
                viewPatients();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == addDoctorButton) {
            try {
                addDoctor();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == addPatientButton) {
            try {
                addPatient();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == deleteDetailsButton) {
            deleteDetails();
        } else if (e.getSource() == searchButton) {
            try {
                search();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }else if (e.getSource() == updateDetailsButton) {
            String[] options = {"Doctor", "Patient"};
            int choice = JOptionPane.showOptionDialog(null, "Whose details do you want to update?", "Update Details",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            try {
                if (choice == 0) {
                    updateDoctor();
                } else if (choice == 1) {
                    updatePatient();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == exitButton) {
            dispose();
        }
    }

    private void viewDoctors() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM doctors");

        StringBuilder stringBuilder = new StringBuilder();
        while (rs.next()) {
            stringBuilder.append("Doctor ID: ").append(rs.getInt("d_id")).append("\n");
            stringBuilder.append("Doctor Name: ").append(rs.getString("d_name")).append("\n");
            stringBuilder.append("Doctor Age: ").append(rs.getInt("d_age")).append("\n");
            stringBuilder.append("Doctor Department: ").append(rs.getString("d_department")).append("\n");
            stringBuilder.append("Doctor Phone Number: ").append(rs.getLong("d_phono")).append("\n\n");
        }
        displayArea.setText(stringBuilder.toString());

        rs.close();
        stmt.close();
        conn.close();
    }

    private void updateDoctor() throws SQLException {
        int d_id = Integer.parseInt(JOptionPane.showInputDialog("Enter doctor ID to update:"));

        String[] doctorOptions = {"Name", "Department", "Phone Number"};
        int doctorChoice = JOptionPane.showOptionDialog(null, "Which detail do you want to update?", "Update Doctor Details",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, doctorOptions, doctorOptions[0]);

        if (doctorChoice == 0) {
            String newName = JOptionPane.showInputDialog("Enter new name:");
            // Update name
            conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE doctors SET d_name=? WHERE d_id=?");
            pstmt.setString(1, newName);
            pstmt.setInt(2, d_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Doctor name updated successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Doctor ID not found", "Error", JOptionPane.ERROR_MESSAGE);
            }

            pstmt.close();
            conn.close();
        } else if (doctorChoice == 1) {
            String newDepartment = JOptionPane.showInputDialog("Enter new department:");
            // Update department
            conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE doctors SET d_department=? WHERE d_id=?");
            pstmt.setString(1, newDepartment);
            pstmt.setInt(2, d_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Doctor department updated successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Doctor ID not found", "Error", JOptionPane.ERROR_MESSAGE);
            }

            pstmt.close();
            conn.close();
        } else if (doctorChoice == 2) {
            long newPhone = Long.parseLong(JOptionPane.showInputDialog("Enter new phone number:"));
            // Update phone number
            conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE doctors SET d_phono=? WHERE d_id=?");
            pstmt.setLong(1, newPhone);
            pstmt.setInt(2, d_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Doctor phone number updated successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Doctor ID not found", "Error", JOptionPane.ERROR_MESSAGE);
            }

            pstmt.close();
            conn.close();
        }
    }

    private void updatePatient() throws SQLException {
        int p_id = Integer.parseInt(JOptionPane.showInputDialog("Enter patient ID to update:"));

        String[] patientOptions = {"Name", "Problems", "Phone Number"};
        int patientChoice = JOptionPane.showOptionDialog(null, "Which detail do you want to update?", "Update Patient Details",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, patientOptions, patientOptions[0]);

        if (patientChoice == 0) {
            String newName = JOptionPane.showInputDialog("Enter new name:");
            // Update name
            conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE patients SET p_name=? WHERE p_id=?");
            pstmt.setString(1, newName);
            pstmt.setInt(2, p_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Patient name updated successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Patient ID not found", "Error", JOptionPane.ERROR_MESSAGE);
            }

            pstmt.close();
            conn.close();
        } else if (patientChoice == 1) {
            String newProblems = JOptionPane.showInputDialog("Enter new problems:");
            // Update problems
            conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE patients SET p_problems=? WHERE p_id=?");
            pstmt.setString(1, newProblems);
            pstmt.setInt(2, p_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Patient problems updated successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Patient ID not found", "Error", JOptionPane.ERROR_MESSAGE);
            }

            pstmt.close();
            conn.close();
        } else if (patientChoice == 2) {
            long newPhone = Long.parseLong(JOptionPane.showInputDialog("Enter new phone number:"));
            // Update phone number
            conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE patients SET p_phono=? WHERE p_id=?");
            pstmt.setLong(1, newPhone);
            pstmt.setInt(2, p_id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Patient phone number updated successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Patient ID not found", "Error", JOptionPane.ERROR_MESSAGE);
            }

            pstmt.close();
            conn.close();
        }
    }

    private void viewPatients() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM patients");

        StringBuilder stringBuilder = new StringBuilder();
        while (rs.next()) {
            stringBuilder.append("Patient ID: ").append(rs.getInt("p_id")).append("\n");
            stringBuilder.append("Patient Name: ").append(rs.getString("p_name")).append("\n");
            stringBuilder.append("Patient Age: ").append(rs.getInt("p_age")).append("\n");
            stringBuilder.append("Patient Problems: ").append(rs.getString("p_problems")).append("\n");
            stringBuilder.append("Patient Phone Number: ").append(rs.getLong("p_phono")).append("\n");
            stringBuilder.append("Admit Date: ").append(rs.getString("p_admitdate")).append("\n");
            stringBuilder.append("Doctor ID: ").append(rs.getInt("d_id")).append("\n\n");
        }
        displayArea.setText(stringBuilder.toString());

        rs.close();
        stmt.close();
        conn.close();
    }

    private void addDoctor() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
        Statement stmt = conn.createStatement();

        int d_id = Integer.parseInt(JOptionPane.showInputDialog("Enter doctor ID:"));
        String d_name = JOptionPane.showInputDialog("Enter doctor name:");
        int d_age = Integer.parseInt(JOptionPane.showInputDialog("Enter doctor age:"));
        String d_department = JOptionPane.showInputDialog("Enter doctor department:");
        long d_phono = Long.parseLong(JOptionPane.showInputDialog("Enter doctor phone number:"));

        String sql = String.format("INSERT INTO doctors (d_id, d_name, d_age, d_department, d_phono) VALUES (%d, '%s', %d, '%s', %d)", d_id, d_name, d_age, d_department, d_phono);
        stmt.executeUpdate(sql);

        JOptionPane.showMessageDialog(null, "Doctor added successfully");

        stmt.close();
        conn.close();
    }

    private void addPatient() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
        Statement stmt = conn.createStatement();

        int p_id = Integer.parseInt(JOptionPane.showInputDialog("Enter patient ID:"));
        String p_name = JOptionPane.showInputDialog("Enter patient name:");
        int p_age = Integer.parseInt(JOptionPane.showInputDialog("Enter patient age:"));
        String p_problems = JOptionPane.showInputDialog("Enter patient problems:");
        long p_phono = Long.parseLong(JOptionPane.showInputDialog("Enter patient phone number:"));
        String p_admitdate = JOptionPane.showInputDialog("Enter admit date (YYYY-MM-DD):");
        int d_id = Integer.parseInt(JOptionPane.showInputDialog("Enter doctor ID:"));

        String sql = String.format("INSERT INTO patients (p_id, p_name, p_age, p_problems, p_phono, p_admitdate, d_id) VALUES (%d, '%s', %d, '%s', %d, '%s', %d)", p_id, p_name, p_age, p_problems, p_phono, p_admitdate, d_id);
        stmt.executeUpdate(sql);

        JOptionPane.showMessageDialog(null, "Patient added successfully");

        stmt.close();
        conn.close();
    }

    private void deleteDetails() {
        String[] options = {"Doctor", "Patient"};
        int choice = JOptionPane.showOptionDialog(null, "What details do you want to delete?", "Delete Details",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            try {
                deleteDoctorDetails();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (choice == 1) {
            try {
                deletePatientDetails();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deletePatientDetails() throws SQLException {
        int p_id = Integer.parseInt(JOptionPane.showInputDialog("Enter patient ID to delete:"));

        conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
        Statement stmt = conn.createStatement();

        String sql = "DELETE FROM patients WHERE p_id=" + p_id;
        int rowsAffected = stmt.executeUpdate(sql);

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Patient details deleted successfully");
        } else {
            JOptionPane.showMessageDialog(null, "Patient ID not found", "Error", JOptionPane.ERROR_MESSAGE);
        }

        stmt.close();
        conn.close();
    }

    private void deleteDoctorDetails() throws SQLException {
        int d_id = Integer.parseInt(JOptionPane.showInputDialog("Enter doctor ID to delete:"));

        conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
        Statement stmt = conn.createStatement();

        // First, delete associated patients
        String deletePatientsSql = "DELETE FROM patients WHERE d_id=" + d_id;
        int patientsDeleted = stmt.executeUpdate(deletePatientsSql);

        // Then, delete the doctor
        String deleteDoctorSql = "DELETE FROM doctors WHERE d_id=" + d_id;
        int doctorsDeleted = stmt.executeUpdate(deleteDoctorSql);

        if (doctorsDeleted > 0) {
            JOptionPane.showMessageDialog(null, "Doctor details deleted successfully");
        } else {
            JOptionPane.showMessageDialog(null, "Doctor ID not found", "Error", JOptionPane.ERROR_MESSAGE);
        }

        stmt.close();
        conn.close();
    }

    private void search() throws SQLException {
        String searchText = JOptionPane.showInputDialog("Enter doctor/patient ID or name to search:");
        if (searchText != null && !searchText.isEmpty()) {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/Hospital", "root", "463922");
            Statement stmt = conn.createStatement();

            // Search for doctors
            ResultSet doctorResult = stmt.executeQuery("SELECT * FROM doctors WHERE d_id = '" + searchText + "' OR d_name LIKE '%" + searchText + "%'");
            StringBuilder doctorResultBuilder = new StringBuilder();
            while (doctorResult.next()) {
                doctorResultBuilder.append("Doctor ID: ").append(doctorResult.getInt("d_id")).append("\n");
                doctorResultBuilder.append("Doctor Name: ").append(doctorResult.getString("d_name")).append("\n");
                doctorResultBuilder.append("Doctor Age: ").append(doctorResult.getInt("d_age")).append("\n");
                doctorResultBuilder.append("Doctor Department: ").append(doctorResult.getString("d_department")).append("\n");
                doctorResultBuilder.append("Doctor Phone Number: ").append(doctorResult.getLong("d_phono")).append("\n\n");
            }

            // Search for patients
            ResultSet patientResult = stmt.executeQuery("SELECT * FROM patients WHERE p_id = '" + searchText + "' OR p_name LIKE '%" + searchText + "%'");
            StringBuilder patientResultBuilder = new StringBuilder();
            while (patientResult.next()) {
                patientResultBuilder.append("Patient ID: ").append(patientResult.getInt("p_id")).append("\n");
                patientResultBuilder.append("Patient Name: ").append(patientResult.getString("p_name")).append("\n");
                patientResultBuilder.append("Patient Age: ").append(patientResult.getInt("p_age")).append("\n");
                patientResultBuilder.append("Patient Problems: ").append(patientResult.getString("p_problems")).append("\n");
                patientResultBuilder.append("Patient Phone Number: ").append(patientResult.getLong("p_phono")).append("\n");
                patientResultBuilder.append("Admit Date: ").append(patientResult.getString("p_admitdate")).append("\n");
                patientResultBuilder.append("Doctor ID: ").append(patientResult.getInt("d_id")).append("\n\n");
            }

            // Display search results
            StringBuilder searchResultBuilder = new StringBuilder();
            searchResultBuilder.append("Doctor Search Results:\n").append(doctorResultBuilder).append("\n");
            searchResultBuilder.append("Patient Search Results:\n").append(patientResultBuilder);

            displayArea.setText(searchResultBuilder.toString());

            stmt.close();
            conn.close();
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid search query", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Call createTables method from Hospital_Management class
        try {
            Hospital_Management.createTables();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error creating tables: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the application if tables cannot be created
        }

        // Create and display the GUI
        HospitalManagementGUI gui = new HospitalManagementGUI();
        gui.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });

    }
}
