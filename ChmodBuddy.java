import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChmodBuddy extends JFrame {

    private JCheckBox ownerRead, ownerWrite, ownerExecute;
    private JCheckBox groupRead, groupWrite, groupExecute;
    private JCheckBox otherRead, otherWrite, otherExecute;
    private JTextField chmodCommandField;
    private JTextField lsField;
    private JTextArea outputArea;

    public ChmodBuddy() {
        // Set the application name for macOS
        System.setProperty("apple.awt.application.name", "Chmod Buddy");

        // Set the window title
        setTitle("Chmod Buddy - Linux File Permissions Helper");

        // Set the initial window size
        setSize(700, 500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Permissions Panel
        JPanel permissionsPanel = new JPanel(new GridLayout(3, 1));
        permissionsPanel.setBorder(BorderFactory.createTitledBorder("Choose Permissions"));

        // Owner Permissions
        JPanel ownerPanel = new JPanel(new GridLayout(1, 4));
        ownerPanel.setBorder(BorderFactory.createTitledBorder("Owner Permissions"));
        ownerRead = new JCheckBox("Read");
        ownerWrite = new JCheckBox("Write");
        ownerExecute = new JCheckBox("Execute");

        ownerPanel.add(new JLabel("Owner:"));
        ownerPanel.add(ownerRead);
        ownerPanel.add(ownerWrite);
        ownerPanel.add(ownerExecute);

        // Group Permissions
        JPanel groupPanel = new JPanel(new GridLayout(1, 4));
        groupPanel.setBorder(BorderFactory.createTitledBorder("Group Permissions"));
        groupRead = new JCheckBox("Read");
        groupWrite = new JCheckBox("Write");
        groupExecute = new JCheckBox("Execute");

        groupPanel.add(new JLabel("Group:"));
        groupPanel.add(groupRead);
        groupPanel.add(groupWrite);
        groupPanel.add(groupExecute);

        // Other Permissions
        JPanel otherPanel = new JPanel(new GridLayout(1, 4));
        otherPanel.setBorder(BorderFactory.createTitledBorder("Other Permissions"));
        otherRead = new JCheckBox("Read");
        otherWrite = new JCheckBox("Write");
        otherExecute = new JCheckBox("Execute");

        otherPanel.add(new JLabel("Other:"));
        otherPanel.add(otherRead);
        otherPanel.add(otherWrite);
        otherPanel.add(otherExecute);

        // Add each panel to the permissionsPanel
        permissionsPanel.add(ownerPanel);
        permissionsPanel.add(groupPanel);
        permissionsPanel.add(otherPanel);

        add(permissionsPanel, BorderLayout.NORTH);

        // Command Panel
        JPanel commandPanel = new JPanel(new BorderLayout());
        commandPanel.setBorder(BorderFactory.createTitledBorder("Generated Command"));

        chmodCommandField = new JTextField();
        chmodCommandField.setEditable(false);
        chmodCommandField.setPreferredSize(new Dimension(0, 30)); // Set height to 30 pixels

        JButton generateChmodButton = new JButton("Generate chmod Command");
        generateChmodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateChmodCommand(); // Generate the chmod command
                generateLsOutput(); // Update lsField
                updateLsBreakdown(); // Update the detailed breakdown
            }
        });

        commandPanel.add(chmodCommandField, BorderLayout.CENTER);
        commandPanel.add(generateChmodButton, BorderLayout.EAST);

        add(commandPanel, BorderLayout.CENTER);

        // ls -l Input Panel
        JPanel lsPanel = new JPanel(new BorderLayout());
        lsPanel.setBorder(BorderFactory.createTitledBorder("Paste ls -l Output"));

        lsField = new JTextField();
        outputArea = new JTextArea(10, 30); // Set larger size to show more lines and wider output
        outputArea.setEditable(false);

        // Add ActionListener to detect "Enter" key in lsField
        lsField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseLsOutput(); // Trigger parsing when Enter is pressed
            }
        });

        JButton parseLsButton = new JButton("Parse ls -l Output");
        parseLsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseLsOutput();
            }
        });

        // Add the Clear button
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields(); // Clear everything when clicked
            }
        });

        // Panel for Parse and Clear buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(parseLsButton);
        buttonPanel.add(clearButton); // Add the clear button next to Parse

        lsPanel.add(lsField, BorderLayout.NORTH);
        lsPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        lsPanel.add(buttonPanel, BorderLayout.SOUTH); // Add buttonPanel to lsPanel

        add(lsPanel, BorderLayout.SOUTH);

        // Request focus elsewhere (e.g., on the main window) to avoid focusing the first checkbox
        this.addWindowFocusListener(new java.awt.event.WindowAdapter() {
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                getRootPane().requestFocus();
            }
        });
    }

    private void generateChmodCommand() {
        int owner = (ownerRead.isSelected() ? 4 : 0) + (ownerWrite.isSelected() ? 2 : 0) + (ownerExecute.isSelected() ? 1 : 0);
        int group = (groupRead.isSelected() ? 4 : 0) + (groupWrite.isSelected() ? 2 : 0) + (groupExecute.isSelected() ? 1 : 0);
        int other = (otherRead.isSelected() ? 4 : 0) + (otherWrite.isSelected() ? 2 : 0) + (otherExecute.isSelected() ? 1 : 0);

        // If filename is not available, default to <filename>
        String filename = "<filename>";
        if (!lsField.getText().isEmpty()) {
            String[] parts = lsField.getText().trim().split("\\s+");
            if (parts.length >= 9) {
                filename = parts[8]; // Use the actual filename from the pasted ls -l output
            }
        }

        String chmodCommand = "chmod " + owner + group + other + " " + filename;
        chmodCommandField.setText(chmodCommand);
    }

    private void parseLsOutput() {
        String lsOutput = lsField.getText().trim();
        String[] parts = lsOutput.split("\\s+");

        if (parts.length < 9) {
            outputArea.setText("Invalid input. Please paste a full line from 'ls -l'.");
            return;
        }

        String fileType = parts[0].charAt(0) == 'd' ? "Directory" : "File";
        String ownerPermissions = parsePermissions(parts[0].substring(1, 4));
        String groupPermissions = parsePermissions(parts[0].substring(4, 7));
        String otherPermissions = parsePermissions(parts[0].substring(7, 10));
        String fileName = parts[8];  // Get the file/dir name

        StringBuilder output = new StringBuilder();
        output.append("File Type: ").append(fileType).append("\n");
        output.append("File Name: ").append(fileName).append("\n");  // Show the file name
        output.append("Owner Permissions: ").append(ownerPermissions).append("\n");
        output.append("Group Permissions: ").append(groupPermissions).append("\n");
        output.append("Other Permissions: ").append(otherPermissions).append("\n");
        output.append("Number of Links: ").append(parts[1]).append("\n");
        output.append("Owner: ").append(parts[2]).append("\n");
        output.append("Group: ").append(parts[3]).append("\n");
        output.append("File Size: ").append(parts[4]).append(" bytes\n");
        output.append("Last Modified: ").append(parts[5]).append(" ").append(parts[6]).append(" ").append(parts[7]);

        outputArea.setText(output.toString());

        // Update checkboxes based on ls -l output
        updateCheckboxes(parts[0].substring(1, 10));

        // Generate chmod command using the filename
        generateChmodCommand();
    }

    private void updateCheckboxes(String permissions) {
        // Owner Permissions
        ownerRead.setSelected(permissions.charAt(0) == 'r');
        ownerWrite.setSelected(permissions.charAt(1) == 'w');
        ownerExecute.setSelected(permissions.charAt(2) == 'x');

        // Group Permissions
        groupRead.setSelected(permissions.charAt(3) == 'r');
        groupWrite.setSelected(permissions.charAt(4) == 'w');
        groupExecute.setSelected(permissions.charAt(5) == 'x');

        // Other Permissions
        otherRead.setSelected(permissions.charAt(6) == 'r');
        otherWrite.setSelected(permissions.charAt(7) == 'w');
        otherExecute.setSelected(permissions.charAt(8) == 'x');
    }

    private String parsePermissions(String permissions) {
        StringBuilder result = new StringBuilder();
        result.append(permissions.charAt(0) == 'r' ? "Read " : "");
        result.append(permissions.charAt(1) == 'w' ? "Write " : "");
        result.append(permissions.charAt(2) == 'x' ? "Execute" : "");
        return result.length() > 0 ? result.toString() : "No Permissions";
    }

    private void generateLsOutput() {
        StringBuilder lsOutput = new StringBuilder();

        // Owner Permissions
        lsOutput.append(ownerRead.isSelected() ? "r" : "-");
        lsOutput.append(ownerWrite.isSelected() ? "w" : "-");
        lsOutput.append(ownerExecute.isSelected() ? "x" : "-");

        // Group Permissions
        lsOutput.append(groupRead.isSelected() ? "r" : "-");
        lsOutput.append(groupWrite.isSelected() ? "w" : "-");
        lsOutput.append(groupExecute.isSelected() ? "x" : "-");

        // Other Permissions
        lsOutput.append(otherRead.isSelected() ? "r" : "-");
        lsOutput.append(otherWrite.isSelected() ? "w" : "-");
        lsOutput.append(otherExecute.isSelected() ? "x" : "-");

        // Update lsField with generated ls -l permissions
        lsField.setText("-" + lsOutput.toString() + " 1 owner group 0 Jan 1 00:00 <filename>");
    }

    private void updateLsBreakdown() {
        String[] parts = lsField.getText().split("\\s+");
        if (parts.length < 9) return;

        String fileType = parts[0].charAt(0) == 'd' ? "Directory" : "File";
        String ownerPermissions = parsePermissions(parts[0].substring(1, 4));
        String groupPermissions = parsePermissions(parts[0].substring(4, 7));
        String otherPermissions = parsePermissions(parts[0].substring(7, 10));
        String fileName = parts[8];  // Get the file/dir name

        StringBuilder output = new StringBuilder();
        output.append("File Type: ").append(fileType).append("\n");
        output.append("File Name: ").append(fileName).append("\n");  // Show the file name
        output.append("Owner Permissions: ").append(ownerPermissions).append("\n");
        output.append("Group Permissions: ").append(groupPermissions).append("\n");
        output.append("Other Permissions: ").append(otherPermissions).append("\n");

        outputArea.setText(output.toString());
    }

    private void clearFields() {
        // Clear all checkboxes
        ownerRead.setSelected(false);
        ownerWrite.setSelected(false);
        ownerExecute.setSelected(false);
        groupRead.setSelected(false);
        groupWrite.setSelected(false);
        groupExecute.setSelected(false);
        otherRead.setSelected(false);
        otherWrite.setSelected(false);
        otherExecute.setSelected(false);

        // Clear all text fields and output area
        lsField.setText("");
        chmodCommandField.setText("");
        outputArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChmodBuddy frame = new ChmodBuddy();
            frame.setVisible(true);
        });
    }
}

