/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cafe;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SKONE COMPUTERS
 */
public class menu extends javax.swing.JFrame {
 Connection con=null;
    Statement st=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    private String Select;
    static String id;
    private ArrayList<MenuItem> items = new ArrayList<>();
    private boolean updatingSuggestions = false;

    /**
     * Creates new form menu
     */
    public menu() {
        initComponents();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe","root","12345678");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
         showDataInToTable();
         loadMenuItems();
        //setupSearchField();
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                   
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow != -1) {
                       
                        String vname = jTable1.getValueAt(selectedRow, 1).toString();
                       
                        deleteRoom(vname);
                        
                    }
                }
            }
        });
    }
    
   private void loadMenuItems() {
        try {
            pst = con.prepareStatement("select * from product");
            rs = pst.executeQuery();
            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("price"),
                        rs.getBytes("image")
                );
                items.add(item);
            }
        } catch (SQLException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*  private void setupSearchField() {
    searchField.getDocument().addDocumentListener(new DocumentListener() {
    @Override
    public void insertUpdate(DocumentEvent e) {
    updateSuggestions();
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
    updateSuggestions();
    }
    @Override
    public void changedUpdate(DocumentEvent e) {
    updateSuggestions();
    }
    });
    suggestionsBox.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
    if (!updatingSuggestions && suggestionsBox.getSelectedItem() != null) {
    String selectedItemName = suggestionsBox.getSelectedItem().toString();
    searchField.setText(selectedItemName);
    // Find the corresponding MenuItem
    for (MenuItem item : items) {
    if (item.name.equals(selectedItemName)) {
    showItemInTable(item);
    break;
    }
    }
    }
    }
    });
    }
    private void updateSuggestions() {
    SwingUtilities.invokeLater(new Runnable() {
    public void run() {
    updatingSuggestions = true;
    String input = searchField.getText().toLowerCase();
    suggestionsBox.removeAllItems();
    if (!input.isEmpty()) {
    Collections.sort(items, new Comparator<MenuItem>() {
    @Override
    public int compare(MenuItem o1, MenuItem o2) {
    return o1.name.compareToIgnoreCase(o2.name);
    }
    });
    int index = binarySearch(items, input);
    if (index != -1) {
    if (items.get(index).name.toLowerCase().startsWith(input)) {
    suggestionsBox.addItem(items.get(index).name);
    }
    int left = index - 1;
    while (left >= 0 && items.get(left).name.toLowerCase().startsWith(input)) {
    suggestionsBox.addItem(items.get(left).name);
    left--;
    }
    int right = index + 1;
    while (right < items.size() && items.get(right).name.toLowerCase().startsWith(input)) {
    suggestionsBox.addItem(items.get(right).name);
    right++;
    }
    }
    if (suggestionsBox.getItemCount() > 0) {
    suggestionsBox.showPopup();
    } else {
    suggestionsBox.hidePopup();
    }
    } else {
    suggestionsBox.hidePopup();
    }
    updatingSuggestions = false;
    }
    });
    }
     */

   
private void showItemInTable(MenuItem item) {
    DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
    df.setRowCount(0); 
    Vector<Object> v = new Vector<>();
    v.add(item.id);
    v.add(item.name);
    v.add(item.price);
    v.add(item.image);
    df.addRow(v);
}


 public void showDataInToTable(){
     
        try {
            pst=con.prepareStatement("select * from product");
            rs=pst.executeQuery();
            ResultSetMetaData ob=rs.getMetaData();
            int n=ob.getColumnCount();
            DefaultTableModel df=(DefaultTableModel) jTable1.getModel();
            jTable1.getColumnModel().getColumn(3).setCellRenderer(new menu.ImageRenderer());
            df.setRowCount(0);
            while(rs.next()){
                Vector v=new Vector();
                for(int i=1;i<=n;i++){
                    v.add(rs.getString("id"));               
                    v.add(rs.getString("name"));
                    v.add(rs.getString("price"));
                    v.add(rs.getBytes("image"));
                    
                    
                }
                df.addRow(v);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
 }

    private void deleteRoom(String name) {
      int yes = JOptionPane.showConfirmDialog(this,"Are you sure?", "delete room", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
if(yes==JOptionPane.YES_OPTION){
 
      try {
            
            pst = con.prepareStatement("DELETE FROM product WHERE name = ?");
            pst.setString(1, name);
            pst.executeUpdate();
            // Show a confirmation message
            JOptionPane.showMessageDialog(this, "item deleted successfully");
            // Update the table
            showDataInToTable();
        } catch (SQLException ex) {
            Logger.getLogger(voucher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  }

    private int binarySearch(ArrayList<MenuItem> items, String searchText) {
        int left = 0;
        int right = items.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            MenuItem midItem = items.get(mid);
            int comparison = midItem.name.compareToIgnoreCase(searchText);

            if (comparison == 0) {
                return mid;
            }
            if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        searchField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        suggestionBox = new javax.swing.JComboBox<>();
        searchButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));
        jPanel1.setForeground(new java.awt.Color(0, 51, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1310, 20, -1, -1));

        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFieldKeyReleased(evt);
            }
        });
        jPanel1.add(searchField, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 140, 30));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id", "Name", "Price", "image"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Byte.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setAutoscrolls(false);
        jTable1.setMaximumSize(new java.awt.Dimension(2147483647, 100));
        jTable1.setPreferredSize(new java.awt.Dimension(800, 600));
        jTable1.setRowHeight(100);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 776, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("MENU");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, -1, -1));

        suggestionBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suggestionBoxActionPerformed(evt);
            }
        });
        jPanel1.add(suggestionBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 80, 140, -1));

        searchButton.setText("clear");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        jPanel1.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/3b6d3733-7fc5-4cf1-97e0-3e51224415f7.gif"))); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 170, 420, 250));

        jLabel4.setText("double click to delete product");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 100, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1366x768-burlywood-solid-color-background.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
     showDataInToTable();
     searchField.setText("");
     
     
     
    }//GEN-LAST:event_searchButtonActionPerformed

    private void searchDatabase() {
    String searchText = searchField.getText().trim();

    if (!searchText.isEmpty()) {
        String query = "SELECT name FROM product WHERE name LIKE ? OR price LIKE ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe", "root", "12345678");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameters for the query
            stmt.setString(1, searchText + "%");
            stmt.setString(2, searchText + "%");

            ResultSet rs = stmt.executeQuery();

            // Temporarily disable updates to avoid flickering
            suggestionBox.setPopupVisible(false);
            suggestionBox.removeAllItems();

            // Add matching results to the suggestion box
            boolean hasResults = false;
            while (rs.next()) {
                suggestionBox.addItem(rs.getString("name"));
                hasResults = true;
            }

            // If no results, add a default message
            if (!hasResults) {
                suggestionBox.addItem("No suggestions available");
            }

            // Display the dropdown suggestions
            suggestionBox.setPopupVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error while searching: " + ex.getMessage());
        }
    } else {
        // Hide the suggestion box if the input is empty
        suggestionBox.setPopupVisible(false);
    }
}



    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        new HomeFrame().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldActionPerformed

    private void suggestionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suggestionBoxActionPerformed
        // TODO add your handling code here:
       
    suggestionBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!updatingSuggestions && suggestionBox.getSelectedItem() != null) {
                String selectedItemName = suggestionBox.getSelectedItem().toString();
                searchField.setText(selectedItemName);

                // Find the corresponding MenuItem
                for (MenuItem item : items) {
                    if (item.name.equals(selectedItemName)) {
                        showItemInTable(item);
                        break;
                    }
                }
            }
        }
    });
    }//GEN-LAST:event_suggestionBoxActionPerformed
    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
        // TODO add your handling code here:

    String searchText = searchField.getText().trim();
    suggestionBox.removeAllItems();

    if (!searchText.isEmpty()) {
        try {
            
            boolean isNumeric = searchText.matches("\\d+");

            String query;
            if (isNumeric) {
                
                query = "SELECT name FROM product WHERE price LIKE ?";
            } else {
                
                query = "SELECT name FROM product WHERE name LIKE ?";
            }

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, searchText + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                suggestionBox.addItem(rs.getString("name"));
            }

            if (suggestionBox.getItemCount() == 0) {
                suggestionBox.addItem("No suggestions available");
            }

            suggestionBox.setPopupVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error while searching: " + ex.getMessage());
        }
    } else {
        suggestionBox.setPopupVisible(false);
    }                          
    }//GEN-LAST:event_searchFieldKeyReleased
     
    private class ImageRenderer extends DefaultTableCellRenderer{

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel jL = new JLabel();
            byte[] bytes = (byte[]) value;
            ImageIcon imageicon = new ImageIcon(new ImageIcon(bytes).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            jL.setIcon(imageicon);
            return jL;
                    }
        
        
                }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JComboBox<String> suggestionBox;
    // End of variables declaration//GEN-END:variables
 private class MenuItem {
        String id;
        String name;
        String price;
        byte[] image;

        MenuItem(String id, String name, String price, byte[] image) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.image = image;
        }
    }
}
  
