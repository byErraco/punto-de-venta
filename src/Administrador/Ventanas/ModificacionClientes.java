/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrador.Ventanas;

import PuntoVenta.Inicio.MenuPrincipal;
import PuntoVenta.Modelos.ModeloCliente;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 *
 * @author Inverdata
 */
public class ModificacionClientes extends javax.swing.JInternalFrame {

    private Admin admin;
    private MenuPrincipal menuPrincipal;
    //private ObjetoBaseDatos obd;
    private Object ObjetoBaseDatos;
    private Object txtNombre;
    Object panel;

    /**
     * Creates new form RegistroEmpleados
     *
     * @param admin
     */
    public ModificacionClientes(Admin admin) {
        this.admin = admin;
        initComponents();
        txtDocumento.getText();
        txtNombres.getText();
        txtApellido.getText();
        txtTelefono.getText();
        txtCorreo.getText();
        //crearHotKeys();
    }

    /**
     * Creates new form RegistroEmpleados
     *
     * @param admin Ventana de admin.
     * @param identificador Indicador del combobox. J,V,E,P.
     * @param documento Cedula, RIF o número de pasaporte de la persona
     */
    public ModificacionClientes(Admin admin, char identificador, String documento) {
        this.admin = admin;
        initComponents();
        //crearHotKeys();
        //cmbTipoDocumento.setSelectedItem(identificador);
        //txtDocumento.setText(documento);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContenedor = new javax.swing.JPanel();
        btnModificarEmpleados = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblDocumento = new javax.swing.JLabel();
        cmbTipoDocumento = new javax.swing.JComboBox();
        txtDocumento = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        lblApellido = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        lblCorreo = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaDireccion = new javax.swing.JTextArea();
        lblDireccion = new javax.swing.JLabel();

        setClosable(true);

        pnlContenedor.setBackground(new java.awt.Color(32, 182, 155));

        btnModificarEmpleados.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnModificarEmpleados.setText("<html><center>Modificar<br>F2</center></html>");
        btnModificarEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarEmpleadosActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(32, 182, 155));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Obligatorios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 18), java.awt.Color.white)); // NOI18N

        lblDocumento.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblDocumento.setForeground(new java.awt.Color(255, 255, 255));
        lblDocumento.setText("CI / RIF:");

        cmbTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "V", "J", "E", "P" }));

        txtDocumento.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDocumentoActionPerformed(evt);
            }
        });
        txtDocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDocumentoKeyTyped(evt);
            }
        });

        lblNombre.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setText("Nombre:");

        txtNombres.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombresActionPerformed(evt);
            }
        });
        txtNombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombresKeyTyped(evt);
            }
        });

        txtApellido.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });

        lblApellido.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblApellido.setForeground(new java.awt.Color(255, 255, 255));
        lblApellido.setText("Apellido:");

        lblTelefono.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblTelefono.setForeground(new java.awt.Color(255, 255, 255));
        lblTelefono.setText("Telefono:");

        txtTelefono.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
            }
        });

        lblCorreo.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblCorreo.setForeground(new java.awt.Color(255, 255, 255));
        lblCorreo.setText("Correo:");

        txtCorreo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoActionPerformed(evt);
            }
        });
        txtCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreoKeyTyped(evt);
            }
        });

        txaDireccion.setColumns(20);
        txaDireccion.setRows(5);
        jScrollPane1.setViewportView(txaDireccion);

        lblDireccion.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblDireccion.setForeground(new java.awt.Color(255, 255, 255));
        lblDireccion.setText("Direccion:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDocumento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTelefono)
                            .addComponent(lblApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCorreo)
                            .addComponent(lblNombre)
                            .addComponent(lblDireccion))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(txtCorreo)
                    .addComponent(txtTelefono)
                    .addComponent(txtApellido)
                    .addComponent(txtNombres)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDocumento))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNombre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblApellido))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTelefono))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(lblCorreo)))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDireccion))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlContenedorLayout = new javax.swing.GroupLayout(pnlContenedor);
        pnlContenedor.setLayout(pnlContenedorLayout);
        pnlContenedorLayout.setHorizontalGroup(
            pnlContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContenedorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlContenedorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnModificarEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(120, 120, 120))
        );
        pnlContenedorLayout.setVerticalGroup(
            pnlContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContenedorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnModificarEmpleados, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlContenedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlContenedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnModificarEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarEmpleadosActionPerformed

        modificarCliente();
        admin.actualizarTabla();
    }//GEN-LAST:event_btnModificarEmpleadosActionPerformed

    private void txtDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDocumentoActionPerformed

    private void txtDocumentoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDocumentoKeyTyped
        /*

        char c=evt.getKeyChar();

        if(Character.isDigit(c) || evt.getKeyCode()==KeyEvent.VK_ENTER);
        else
        {
            evt.consume();
            JOptionPane.showMessageDialog(this, "Error: Solo se aceptan numeros.");
        }

         */
    }//GEN-LAST:event_txtDocumentoKeyTyped

    private void txtNombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombresActionPerformed

    private void txtNombresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombresKeyTyped
        /*char c=evt.getKeyChar();

        if(Character.isDigit(c))
        {
            evt.consume();
            JOptionPane.showMessageDialog(this, "Error: Solo se aceptan letras.");
        }*/
    }//GEN-LAST:event_txtNombresKeyTyped

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
        char c = evt.getKeyChar();

        if (Character.isDigit(c)) {
            evt.consume();

        }
    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped
        boolean valido = true;
        if ((!Character.isDigit(evt.getKeyChar()) && (evt.getKeyChar() != KeyEvent.VK_MINUS))) {
            evt.consume();
        } else if ((Character.isDigit(evt.getKeyChar())) && txtTelefono.getText().length() == 4) {
            evt.consume();
        } else if (txtTelefono.getText().length() != 4 && (evt.getKeyChar() == KeyEvent.VK_MINUS)) {
            evt.consume();
        } else if (txtTelefono.getText().length() > 11) {
            evt.consume();
        } else {
            valido = false;
        }
        if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
            valido = false;
        }
        //Alerta.setVisible(valido);
    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoActionPerformed
        /*boolean esValido = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence email = null;
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches())
        {
            esValido = true;
        }
         */
    }//GEN-LAST:event_txtCorreoActionPerformed

    private void txtCorreoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoKeyTyped
        /*
        char aux=evt.getKeyChar();
        String str=null;
        int e=0;
        try{
            e=txtDocumento.getText().length();
        }catch(Exception E){

        }

        System.out.println(e);
         */
        //if(!Character.isDigit(aux) || e==8) evt.consume();

    }//GEN-LAST:event_txtCorreoKeyTyped

    private void cerrarVentana() {
        this.dispose();
    }

    private void modificarCliente() {
        String cedula, nombre, apellido, telefono, correo, direccion;
        char nacionalidad;
        int idCliente;

        boolean modificado;

        nacionalidad = cmbTipoDocumento.getSelectedItem().toString().charAt(0);
        cedula = txtDocumento.getText();
        nombre = txtNombres.getText();
        apellido = txtApellido.getText();
        telefono = txtTelefono.getText();
        correo = txtCorreo.getText();
        direccion = txaDireccion.getText();

//        txtDocumento.setEnabled(false);
//        cmbCargoId.setEnabled(false);
        if (nombre.isEmpty()) {
            Utilidades.Sonidos.beep();
            txtNombres.requestFocus();
            return;
        }
        if (apellido.isEmpty() && !cmbTipoDocumento.getSelectedItem().toString().equalsIgnoreCase("J")) {
            Utilidades.Sonidos.beep();
            txtApellido.requestFocus();
            return;
        }
        if (telefono.isEmpty()) {
            Utilidades.Sonidos.beep();
            txtTelefono.requestFocus();
            return;
        }
        if (correo.isEmpty()) {
            Utilidades.Sonidos.beep();
            txtCorreo.requestFocus();
            return;
        }
        if (direccion.isEmpty()) {
            Utilidades.Sonidos.beep();
            txaDireccion.requestFocus();
            return;
        }

        idCliente = admin.menuPrincipal.getOBD().modificarCliente(nombre, apellido, direccion, telefono, correo, cedula);

        if (idCliente > 0) {
            HashMap<String, String> mapCliente = admin.menuPrincipal.getOBD().getMapCliente(idCliente);

            ModeloCliente cliente = new ModeloCliente(mapCliente);

            correo = txtCorreo.getText();
            if (!correo.isEmpty()) {
                cliente.setCorreo(correo);
            }
            telefono = txtTelefono.getText();
            if (!telefono.isEmpty()) {
                cliente.setTelefono(telefono);
            }

            this.cerrarVentana();

        } else {
            Utilidades.Sonidos.beep();
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnModificarEmpleados;
    private javax.swing.JComboBox cmbTipoDocumento;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblApellido;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblDocumento;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JPanel pnlContenedor;
    private javax.swing.JTextArea txaDireccion;
    public static javax.swing.JTextField txtApellido;
    public static javax.swing.JTextField txtCorreo;
    public static javax.swing.JTextField txtDocumento;
    public static javax.swing.JTextField txtNombres;
    public static javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
