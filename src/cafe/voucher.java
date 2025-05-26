/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cafe;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SKONE COMPUTERS
 */
public class voucher extends javax.swing.JFrame {
Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM voucher";
    private static final String INSERT_QUERY = "INSERT INTO voucher (name, discount) VALUES (?, ?)";
 private static final String DB_URL = "jdbc:mysql://localhost:3306/cafe";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345678";
    /**
     * Creates new form voucher
     */
    public voucher() {
        initComponents();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe","root","12345678");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(order.class.getName()).log(Level.SEVERE, null, ex);
        }
        ShowRecordInTable();
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                   
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow != -1) {
                       
                        String vname = jTable1.getValueAt(selectedRow, 0).toString();
                       
                        deleteRoom(vname);
                    }
                }
            }
        });
    }
    static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return conn;
    }
    public void addVoucherIfPossible() {
        String name = txtname.getText();
        int discount = parseInt(txtdiscount.getText());
        try (Connection conn = connect();
             PreparedStatement pst = conn.prepareStatement(COUNT_QUERY);
             ResultSet rs = pst.executeQuery()) {

            rs.next();
            int count = rs.getInt(1);

            if (count < 5) {
                try (PreparedStatement insertStmt = conn.prepareStatement(INSERT_QUERY)) {
                    insertStmt.setString(1, name);
                    insertStmt.setInt(2, discount);
                    insertStmt.executeUpdate();
                   JOptionPane.showMessageDialog(this, "Voucher added successfully.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Cannot add voucher. The voucher table already contains 5 entries.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void ShowRecordInTable() {
        int c=0;
        try {
            pst=con.prepareStatement("select * from voucher");
            rs=pst.executeQuery();
            ResultSetMetaData rsmd= rs.getMetaData();
            c=rsmd.getColumnCount();
            DefaultTableModel DModel= (DefaultTableModel) jTable1.getModel();
            DModel.setRowCount(0);
            while(rs.next()) {
                Vector column = new Vector();
                for(int i=1; i<=c; i++){
                    column.add(rs.getString("name"));
                    column.add(rs.getString("discount"));
                    }
                DModel.addRow(column);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(voucher.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void deleteRoom(String name) {
      int yes = JOptionPane.showConfirmDialog(this,"Are you sure?", "delete room", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
if(yes==JOptionPane.YES_OPTION){
 
      try {
            
            pst = con.prepareStatement("DELETE FROM voucher WHERE name = ?");
            pst.setString(1, name);
            pst.executeUpdate();
            // Show a confirmation message
            JOptionPane.showMessageDialog(this, "voucher deleted successfully");
            // Update the table
            ShowRecordInTable();
        } catch (SQLException ex) {
            Logger.getLogger(voucher.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        jLabel2 = new javax.swing.JLabel();
        txtname = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtdiscount = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Voucher");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 45, -1, -1));

        txtname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnameActionPerformed(evt);
            }
        });
        jPanel1.add(txtname, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 140, 30));

        jButton1.setText("add voucher");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "vouchers", "discount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, 440, 250));

        txtdiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdiscountActionPerformed(evt);
            }
        });
        jPanel1.add(txtdiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 140, 30));

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 20, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("double click on voucher to delete");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 210, 290, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1366x768-burlywood-solid-color-background.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, 0, 660, 532));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    addVoucherIfPossible();
    ShowRecordInTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnameActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    
        
    
    }//GEN-LAST:event_jTable1MouseClicked

    private void txtdiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdiscountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdiscountActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        new HomeFrame().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

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
            java.util.logging.Logger.getLogger(voucher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(voucher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(voucher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(voucher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new voucher().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtdiscount;
    private javax.swing.JTextField txtname;
    // End of variables declaration//GEN-END:variables
}
