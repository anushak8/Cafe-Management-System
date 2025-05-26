/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cafe;

import static cafe.cart.cartId;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SKONE COMPUTERS
 */
public class order extends javax.swing.JFrame {

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String cartId = cart.cartId;
    static double totalCartValue = 0.0;
    /**
     * Creates new form order
     */
    public order() {
        initComponents();
         SimpleDateFormat obj= new SimpleDateFormat("yyyy/MM/dd");
        Date date= new Date();
        txtdate.setText(obj.format(date));
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cafe","root","12345678");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(order.class.getName()).log(Level.SEVERE, null, ex);
        }
         showDataInToTable();
         getTotalCartValue();
         
    
    txtcash.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtcashKeyTyped(evt);
        }
    });
         txtname.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
            txtnameKeyTyped(evt);
        }
    });
         
    }            

    private void txtnameKeyTyped(java.awt.event.KeyEvent evt) {
    char c = evt.getKeyChar();
    if ((Character.isDigit(c) || (c == KeyEvent.VK_DELETE))) {
        evt.consume();
        JOptionPane.showMessageDialog(this, "Please enter only alphabets for name.");
    }
}
    
 public void showDataInToTable() {
        try {
            pst = con.prepareStatement("select * from cart where cid =?");
            pst.setString(1, cartId);
            rs = pst.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
            tableModel.setRowCount(0);
            while(rs.next()){
                Vector v=new Vector();
                for(int i=1;i<=columnCount;i++){
                          v.add(rs.getString("cid"));
                v.add(rs.getString("pid"));
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
 public void ApplyVoucherActionPerformed() {                                             
    String voucherCode = txtvoucher.getText();
    if(txtvoucher.getText()!= null){
    try {
        pst = con.prepareStatement("SELECT discount FROM voucher WHERE name = ?");
        pst.setString(1, voucherCode);
        rs = pst.executeQuery();
        
        if (rs.next()) {
            double discount = rs.getDouble("discount");
            double total = totalCartValue;
            double discountedTotal = total - (total * discount / 100);
            txttotaldiscount.setText(Double.toString(discountedTotal));
            JOptionPane.showMessageDialog(this, "Voucher applied successfully! Discount: " + discount + "%");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid voucher code.");
        }
    } catch (SQLException ex) {
        Logger.getLogger(order.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    else{
       JOptionPane.showMessageDialog(this, "enter voucher code.");        
    }
}
 public void getTotalCartValue() {
    
    try {
        pst = con.prepareStatement("SELECT SUM(price * qty) as total FROM cart WHERE cid = ?");
        pst.setString(1, cartId);
        rs = pst.executeQuery();
        
        while (rs.next()) {
            totalCartValue = rs.getDouble("total");
        }
       String totalPrice = Double.toString(totalCartValue);
        txttotal.setText(totalPrice);
    } catch (SQLException ex) {
        Logger.getLogger(cart.class.getName()).log(Level.SEVERE, null, ex);
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
        txtdate = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        txttotal = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtname = new javax.swing.JTextField();
        txtcash = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtchange = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtvoucher = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txttotaldiscount = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));
        jPanel1.setForeground(new java.awt.Color(0, 51, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "cid", "pid", "product name", "price", "quantity", "total"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 140, 580, 340));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Date:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Total");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        txtdate.setEditable(false);
        jPanel1.add(txtdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, 150, 30));

        jButton1.setText("reciept");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 570, -1, -1));

        txttotal.setEditable(false);
        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });
        jPanel1.add(txttotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 340, 150, 30));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Customer Name:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 430, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Cash:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 480, -1, -1));

        txtname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnameActionPerformed(evt);
            }
        });
        jPanel1.add(txtname, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 420, 150, 30));

        txtcash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcashActionPerformed(evt);
            }
        });
        txtcash.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcashKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcashKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcashKeyTyped(evt);
            }
        });
        jPanel1.add(txtcash, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 470, 150, 30));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Change:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 520, -1, -1));

        txtchange.setEditable(false);
        jPanel1.add(txtchange, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 510, 150, 30));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/close.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1340, 20, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Payment and Billing");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, -1, -1));
        jPanel1.add(txtvoucher, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 150, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Voucher");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total after discount");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, -1, -1));

        txttotaldiscount.setEditable(false);
        txttotaldiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotaldiscountActionPerformed(evt);
            }
        });
        jPanel1.add(txttotaldiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 380, 150, 30));

        jButton2.setText("apply voucher");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 290, -1, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1366x768-burlywood-solid-color-background.jpg"))); // NOI18N
        jLabel9.setText("jLabel9");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(-30, -10, 1400, 770));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-30, 0, 1420, 770));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
       String  name = txtname.getText();
       String date = txtdate.getText();
       double total = Double.parseDouble(txttotal.getText());
       double totaldiscount = Double.parseDouble(txttotaldiscount.getText());
       if (txtname.getText().equals("")||txtcash.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "All the given fields should be filled");
            txtname.requestFocus();
        }else{
    try {
            pst = con.prepareStatement("insert into payment(cid, cName, pdate, total, discounttotal) values(?, ?, ?, ?, ?)");
            pst.setString(1, cartId);
            pst.setString(2, name);
            pst.setString(3, date);
            pst.setDouble(4, total);
            pst.setDouble(5, totaldiscount);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this,"bill generated");
           }
        catch (SQLException ex) {
            Logger.getLogger(cart.class.getName()).log(Level.SEVERE, null, ex);
        }
        new BillPrint().setVisible(true);
       }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtcashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcashActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcashActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:        
        new HomeFrame().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton7ActionPerformed
public void cash(){
    if(txtcash!=null){
     double totalAmount = Double.parseDouble(txttotaldiscount.getText());
        double cashGiven = Double.parseDouble(txtcash.getText());
         
            double change = cashGiven - totalAmount;
            txtchange.setText(Double.toString(change));
    }
}
    private void txtcashKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcashKeyTyped
        // TODO add your handling code here:
           char c = evt.getKeyChar();
   if (!(Character.isDigit(c))) {
        evt.consume();
        JOptionPane.showMessageDialog(this, "Please enter only numbers for price.");
    
}
    
    }//GEN-LAST:event_txtcashKeyTyped

    private void txtcashKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcashKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcashKeyPressed

    private void txtcashKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcashKeyReleased
        // TODO add your handling code here:
    cash();
    }//GEN-LAST:event_txtcashKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    ApplyVoucherActionPerformed();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnameActionPerformed

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalActionPerformed

    private void txttotaldiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotaldiscountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotaldiscountActionPerformed

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
            java.util.logging.Logger.getLogger(order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new order().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtcash;
    private javax.swing.JTextField txtchange;
    private javax.swing.JTextField txtdate;
    private javax.swing.JTextField txtname;
    private javax.swing.JTextField txttotal;
    private javax.swing.JTextField txttotaldiscount;
    private javax.swing.JTextField txtvoucher;
    // End of variables declaration//GEN-END:variables
}
