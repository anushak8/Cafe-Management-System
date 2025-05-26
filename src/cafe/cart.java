/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cafe;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigInteger;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.util.UUID;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 *
 * @author SKONE COMPUTERS
 */
public class cart extends javax.swing.JFrame {

     Connection con = null;
     PreparedStatement pst = null;
     ResultSet rs = null;
     String id;
     String nm;
     String price;
    static String cartId;
     ArrayList<cart.MenuItem> items = new ArrayList<>();
     private boolean updatingSuggestions = false;
    static double totalBill= 0;
    
    
    public cart() {
       
        initComponents();
        connectDatabase();
        showDataInToTable();
        setupQuantityKeyListener();
         loadMenuItems();
        setupSearchField();
                
    }
 private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe", "root", "12345678");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

public void dataInTable() {
        try {
            pst = con.prepareStatement("select * from cart where cid =?");
            pst.setString(1, cartId);
            rs = pst.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
            tableModel.setRowCount(0);
            while(rs.next()){
                Vector v=new Vector();
                for(int i=1;i<=columnCount;i++){
                    v.add(rs.getString("pName"));
                    v.add(rs.getString("price"));
                    v.add(rs.getString("qty"));
                    v.add(rs.getString("total"));
                 
                    
                }
                tableModel.addRow(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
private void loadMenuItems() {
        try {
            pst = con.prepareStatement("select * from product");
            rs = pst.executeQuery();
            while (rs.next()) {
                cart.MenuItem item = new cart.MenuItem(
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

   private void setupSearchField() {
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

    suggestionBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!updatingSuggestions && suggestionBox.getSelectedItem() != null) {
                String selectedItemName = suggestionBox.getSelectedItem().toString();
                searchField.setText(selectedItemName);

                // Find the corresponding MenuItem
                for (cart.MenuItem item : items) {
                    if (item.name.equals(selectedItemName)) {
                        showItemInTable(item);
                        break;
                    }
                }
            }
        }
    });
}
private int binarySearch(ArrayList<cart.MenuItem> items, String searchText) {
        int left = 0;
        int right = items.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            cart.MenuItem midItem = items.get(mid);
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
 private void updateSuggestions() {
    
}

private void showItemInTable(cart.MenuItem item) {
    DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
    df.setRowCount(0); // Clear existing rows
    Vector<Object> v = new Vector<>();
    v.add(item.id);
    v.add(item.name);
    v.add(item.price);
    v.add(item.image);
    df.addRow(v);
}
private String generateUniqueCartId() {
    String generateUUIDNo = String.format("%010d",new BigInteger(UUID.randomUUID().toString().replace("-",""),16));
        String unique_no = generateUUIDNo.substring( generateUUIDNo.length() - 4);
    return unique_no;
}

    private void setupQuantityKeyListener() {
        txtquantity.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtquantityKeyTyped(evt);
            }
        });
    }

    private void txtquantityKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            evt.consume();
            JOptionPane.showMessageDialog(this, "Please enter only numeric values for Quantity.");
        }
    }

    public void showDataInToTable() {
        try {
            pst = con.prepareStatement("select * from product");
            rs = pst.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
            jTable1.getColumnModel().getColumn(3).setCellRenderer(new ImageRenderer());
            tableModel.setRowCount(0);
            while (rs.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    if (i == 4) {
                        rowData.add(rs.getBytes(i));
                    } else {
                        rowData.add(rs.getString(i));
                    }
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            Logger.getLogger(menu.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtname = new javax.swing.JTextField();
        txtquantity = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtprice = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtcart = new javax.swing.JTextField();
        txtpid = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        searchField = new javax.swing.JTextField();
        suggestionBox = new javax.swing.JComboBox<>();
        searchButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(100);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 110, 410, 490));

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Name:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        jLabel2.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Quantity:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, -1, -1));

        txtname.setEditable(false);
        jPanel1.add(txtname, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 150, 30));

        txtquantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtquantityActionPerformed(evt);
            }
        });
        jPanel1.add(txtquantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 200, 150, 30));

        jButton1.setText("Add to cart");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 340, 100, 30));

        jButton2.setText("Proceed to pay");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 390, 120, 30));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 140, -1, -1));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1310, 20, -1, -1));

        jLabel3.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Product price:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, -1, -1));

        txtprice.setEditable(false);
        txtprice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpriceActionPerformed(evt);
            }
        });
        jPanel1.add(txtprice, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 240, 150, 30));

        jLabel4.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Cart id:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));
        jPanel1.add(txtcart, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 120, 150, 30));
        jPanel1.add(txtpid, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 100, 30));

        jLabel7.setText("click on the product to select");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 80, 170, 30));

        jLabel6.setText("total : 0");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 60, 110, 30));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "product name", "price", "quantity", "total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, 410, 460));

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
        jPanel1.add(searchField, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 30, 140, 30));

        suggestionBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suggestionBoxActionPerformed(evt);
            }
        });
        jPanel1.add(suggestionBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 60, 140, -1));

        searchButton.setText("clear");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        jPanel1.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 60, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1366x768-burlywood-solid-color-background.jpg"))); // NOI18N
        jLabel5.setText("jLabel5");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1370, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
private class ImageRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel jL = new JLabel();
            byte[] bytes = (byte[]) value;
            ImageIcon imageicon = new ImageIcon(new ImageIcon(bytes).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            jL.setIcon(imageicon);
            return jL;
        }

    }
    private void txtquantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtquantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtquantityActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        cartId = txtcart.getText();
        String name = txtname.getText();
        String quantity = txtquantity.getText();
        String price = txtprice.getText();
        String pid =  txtpid.getText();
        double total = Double.parseDouble(price)*Integer.parseInt(quantity);
            try {
            pst = con.prepareStatement("insert into cart(cid, pName, qty, price, pid, total) values(?, ?, ?, ?, ?, ?)");
            pst.setString(1, cartId);
            pst.setString(2, name);
            pst.setString(3, quantity);
            pst.setString(4, price);
            pst.setString(5, pid);
            pst.setDouble(6, total);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this,"item added in cart");
            dataInTable();
           
          totalBill += total;
            jLabel6.setText("total: " + totalBill);
           }
        catch (SQLException ex) {
            Logger.getLogger(cart.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton1ActionPerformed
public void connect(){
        PreparedStatement pst;
        ResultSet rs;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe","root","12345678");
            pst=con.prepareStatement("select * from product where id=?");
            pst.setString(1,id);
            rs=pst.executeQuery();
            if(rs.next()){
                nm=rs.getString("name");
                price=rs.getString("price");
                
                
            }
        
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(cart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    new order().setVisible(true);
   
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    new HomeFrame().setVisible(true);
    dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int selectedIndex = jTable1.getSelectedRow();
        id = model.getValueAt(selectedIndex, 0).toString();
        nm = model.getValueAt(selectedIndex, 1).toString();
        price = model.getValueAt(selectedIndex, 2).toString();
        txtpid.setText(id);               
        txtname.setText(nm);
        txtprice.setText(price);
    }//GEN-LAST:event_jTable1MouseClicked

    private void txtpriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpriceActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_txtpriceActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
         txtcart.setText(generateUniqueCartId());
    }//GEN-LAST:event_formWindowOpened

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
        showDataInToTable();
        searchField.setText("");

    }//GEN-LAST:event_searchButtonActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldActionPerformed

    private void suggestionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suggestionBoxActionPerformed
        // TODO add your handling code here:
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
            java.util.logging.Logger.getLogger(cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new cart().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JComboBox<String> suggestionBox;
    private javax.swing.JTextField txtcart;
    private javax.swing.JTextField txtname;
    private javax.swing.JTextField txtpid;
    private javax.swing.JTextField txtprice;
    private javax.swing.JTextField txtquantity;
    // End of variables declaration//GEN-END:variables
public class MenuItem {
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
