/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PuntoVenta.BaseDatos;

import ClasesExtendidas.Numeros.XBigDecimal;
import PuntoVenta.Modelos.ModeloCliente;
import PuntoVenta.Modelos.ModeloEmpleado;
import PuntoVenta.reporte1;
import Utilidades.ArticuloDescontar;
import Utilidades.Cripto;
import Utilidades.ValorPagos;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para el manejo de la base de datos del stpv.
 */
public class ObjetoBaseDatos {

    private DecimalFormat redondeo = new DecimalFormat("0.00");
    final String url;
    PostgreSQL postgreSQL;
    HashMap<String, String> mapTabla = new HashMap<String, String>();
    HashMap<String, String> mapSchema = new HashMap<String, String>();

    public ObjetoBaseDatos(String url, String login, String password) {
        this.url = url;
        postgreSQL = new PostgreSQL(url, login, password);
        crearMaps();
    }

    /**
     * Mapas para no tener que cambiar los nombres de los schemas y tablas si se
     * cambian en la base de datos.
     */
    private void crearMaps() {
        mapSchema.put("stpv", "stpv");
        mapTabla.put("caja", "caja");
        mapTabla.put("cargo", "cargo");
        mapTabla.put("cliente", "cliente");
        mapTabla.put("cliente__telefono", "cliente__telefono");
        mapTabla.put("corte_caja", "corte_caja");
        mapTabla.put("desglose_caja", "desglose_caja");
        mapTabla.put("desglose_caja_cierre", "desglose_caja_cierre");
        mapTabla.put("empleado", "empleado");
        mapTabla.put("estado_caja", "estado_caja");
        mapTabla.put("estado_venta", "estado_venta");
        mapTabla.put("moneda", "moneda");
        mapTabla.put("pago", "pago");
        mapTabla.put("telefono", "telefono");
        mapTabla.put("tipo_moneda", "tipo_moneda");
        mapTabla.put("usuario", "usuario_sistema");
        mapTabla.put("venta", "venta");
        mapTabla.put("venta__pago", "venta__pago");
        mapTabla.put("venta__producto", "venta__producto");

        mapSchema.put("inventario", "inventario");
        mapTabla.put("producto", "producto");

    }

    /**
     * Inserta un cliente en la tabla stpv.cliente.
     *
     * @param nombre
     * @param apellido
     * @param nacionalidad
     * @param cedula
     * @param direccion
     * @return id del cliente resultado del INSERT o -1 en caso de fallar.
     */
    public int crearClienteAdmin(String nombre, String apellido, char nacionalidad, String cedula, String direccion, String telefono, String correo) {
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append("INSERT INTO ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("cliente"))
                .append("(nombre, apellido, direccion, nacionalidad, cedula, correo, telefono) VALUES (")
                .append("'").append(nombre).append("', ")
                .append("'").append(apellido).append("', ")
                .append("'").append(direccion).append("', ")
                .append("'").append(nacionalidad).append("', ")
                .append("'").append(cedula).append("', ")
                .append("'").append(correo).append("', ")
                .append("'").append(telefono).append("');");

        int resultado = ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());

        return resultado;
    }

    /**
     * Modifica un cliente en la tabla stpv.cliente.
     *
     * @param nombre
     * @param apellido
     * @param cedula
     * @param telefono
     * @param correo
     * @param direccion
     * @return id del cliente resultado del INSERT o -1 en caso de fallar.
     */
    public int modificarCliente(String nombre, String apellido, String direccion, String telefono, String correo, String cedula) {
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;
        sqlQuery.append("UPDATE ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("cliente"))
                .append(" SET nombre='").append(nombre)
                .append("', apellido='").append(apellido)
                .append("', direccion='").append(direccion)
                .append("', correo='").append(correo)
                .append("', telefono='").append(telefono)
                .append("' WHERE cedula='")
                .append(cedula)
                .append("';");
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Elimina un cliente, dado un idCliente y la cedula del cliente. La cedula
     * es utilizada para sacar el id del cliente ya que en la tabla de empleados
     * no se guarda su id, sino la cedula.
     *
     *
     * @param cedula Cedula del empleado que se desee eliminar.
     * @return
     */
    public int eliminarCliente(String cedula) {
        ResultSet result;
        int id = -1;

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("DELETE FROM ")
                .append(mapSchema.get("stpv"))
                .append(".").append(mapTabla.get("cliente"))
                .append(" WHERE cedula='")
                .append(cedula)
                .append("';");

        try {
            postgreSQL.conectar();
            result = postgreSQL.ejecutarSelect(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }

        return id;
    }

    /**
     * Inserta un empleado en la tabla stpv.empleado.
     *
     * @param nombre
     * @param apellido
     * @param nacionalidad
     * @param cedula
     * @param telefono
     * @param correo
     * @return id del empleado resultado del INSERT o -1 en caso de fallar.
     */
    public int crearEmpleado(String nombre, String apellido, char nacionalidad, String cedula, String telefono, String correo, int cargo_id, String password, String departamento) {
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append("INSERT INTO ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("empleado"))
                .append("(nombre, apellido, nacionalidad, cedula, telefono, correo, cargo_id, password,departamento) VALUES (")
                .append("'").append(nombre).append("', ")
                .append("'").append(apellido).append("', ")
                .append("'").append(nacionalidad).append("', ")
                .append("'").append(cedula).append("', ")
                .append("'").append(telefono).append("', ")
                .append("'").append(correo).append("', ")
                .append("'").append(cargo_id).append("', ")
                .append("'").append(password).append("', ")
                .append("'").append(password).append("');");

        int resultado = ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        return resultado;
    }

    /**
     * Consulta los datos de la empresa en la tabla stpv.empresa.
     *
     * @return Empresa resultado de la consulta o null en caso de fallar.
     */
    public Empresa datosEmpresas() {
        ResultSet rs;
        Empresa emp = new Empresa();

        String query = "SELECT * FROM stpv.empresa";
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(query);
            while (rs.next()) {
                emp.setNombre(rs.getString("nombre"));
                emp.setRif(rs.getString("rif"));
                emp.setTelefono(rs.getString("telefono"));
                emp.setDireccion(rs.getString("direccion"));
                emp.setMoneda(rs.getString("moneda_utilizada"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        if (emp != null) {
            return emp;
        }
        return null;
    }

    /**
     * Consulta los datos del Empleado en la tabla stpv.empleado.
     *
     * @param idEmpleado
     * @return Empleado resultado de la consulta o null en caso de fallar.
     */
    public Empleado datosEmpleado(int idEmpleado) {
        ResultSet rs;
        Empleado emple = new Empleado();

        String query = "SELECT * FROM stpv.empleado where id=" + idEmpleado;
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(query);
            while (rs.next()) {
                emple.setId(rs.getString("id"));
                emple.setNombre(rs.getString("nombre"));
                emple.setApellido(rs.getString("apellido"));
                emple.setNacionalidad(rs.getString("nacionalidad"));
                emple.setCedula(rs.getString("cedula"));
                emple.setCorreo(rs.getString("correo"));
                emple.setTelefono(rs.getString("telefono"));
                emple.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        if (emple != null) {
            return emple;
        }
        return null;
    }

    /**
     * Inserta los datos de la empresa en la tabla stpv.empresa.
     *
     * @param nombre
     * @param rif
     * @param telefono
     * @param moneda
     * @param direccion
     */
    public void ingresoEmp(String nombre, String rif, String telefono, String direccion, String moneda) {
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        String sql = "SELECT * FROM stpv.empresa";
        try {
            postgreSQL.conectar();
            rs = postgreSQL.getSentencia().executeQuery(sql);
            while (rs.next()) {
                sql = "UPDATE stpv.empresa SET nombre='" + nombre + "',rif='" + rif + "',direccion='" + direccion + "',moneda_utilizada='" + moneda + "'";
                try {
                    postgreSQL.conectar();
                    postgreSQL.getSentencia().executeQuery(sql);
                } catch (Exception ex) {
                } finally {
                    postgreSQL.desconectar();
                }
                return;
            }
            sqlQuery.append("INSERT INTO ")
                    .append(mapSchema.get("stpv")).append(".")
                    .append("empresa")
                    .append(" (nombre, rif, telefono, direccion, moneda_utilizada)")
                    .append(" VALUES (")
                    .append("'").append(nombre).append("', ")
                    .append("'").append(rif).append("', ")
                    .append("'").append(telefono).append("', ")
                    .append("'").append(direccion).append("', ")
                    .append("'").append(moneda).append("')");
            try {
                postgreSQL.conectar();
                postgreSQL.getSentencia().executeQuery(sqlQuery.toString());
            } catch (Exception ex) {
            } finally {
                postgreSQL.desconectar();
            }
        } catch (Exception ex) {
            Logger.getLogger(ObjetoBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Modifica los datos del empleado en la tabla stpv.empleado.
     *
     * @param nombre
     * @param apellido
     * @param cedula
     * @param telefono
     * @param correo
     * @param cargo_id
     * @param password
     * @param departamento
     * @return
     */
    public int modificarEmpleado(String nombre, String apellido, String cedula, String telefono, String correo, int cargo_id, String password, String departamento) {
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;
        sqlQuery.append("UPDATE ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("empleado"))
                .append(" SET nombre='").append(nombre)
                .append("', apellido='").append(apellido)
                .append("', correo='").append(correo)
                .append("', cargo_id='").append(cargo_id)
                .append("', telefono='").append(telefono)
                .append("', password='").append(password)
                .append("', departamento='").append(departamento)
                .append("' WHERE cedula='")
                .append(cedula)
                .append("';");
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Elimina un empleado, dado un idEmpleado y la cedula del empleado. La
     * cedula es utilizada para sacar el id del empleado ya que en la tabla de
     * empleados no se guarda su id, sino la cedula.
     *
     *
     * @param cedula Cedula del empleado que se desee eliminar.
     * @return
     */
    public int eliminarEmpleado(String cedula) {
        ResultSet result;
        int id = -1;

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("DELETE FROM ")
                .append(mapSchema.get("stpv"))
                .append(".").append(mapTabla.get("empleado"))
                .append(" WHERE cedula='")
                .append(cedula)
                .append("';");

        try {
            postgreSQL.conectar();
            result = postgreSQL.ejecutarSelect(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }

        return id;
    }

    /**
     * Verifica la existencia de un usuario en la base de datos. -Utiliza el
     * schema "stpv" del mapSchema y la tabla "usuario" del mapTabla.
     *
     * @param usuario usuario a validar.
     * @param password del usuario.
     * @return Id del usuario. Si no existe retorna -1
     */
    public int autenticarUsuario(String usuario, char[] password) {
        ResultSet result;
        int id = -1;

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT empleado_id AS id, password FROM ")
                .append(mapSchema.get("stpv"))
                .append(".").append(mapTabla.get("usuario"))
                .append(" WHERE email='")
                .append(usuario)
                .append("';");

        try {
            postgreSQL.conectar();
            result = postgreSQL.ejecutarSelect(sqlQuery.toString());
            String p = null;

            if (result.next()) {
                id = result.getInt("id");
                p = result.getString("password");
            }
            Cripto c = new Cripto(p);
            boolean autenticado = c.validate(new String(password));
            if (autenticado) {
                return id;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            id = -1;
        } finally {
            postgreSQL.desconectar();
        }

        return id;
    }

    /**
     * Verifica la existencia de un empleado en la base de datos. -Utiliza el
     * schema "stpv" del mapSchema y la tabla "empleado" del mapTabla. Este
     * método funciona con la encriptacion sha1 del password.
     *
     * @param cedula empleado a validar.
     * @param password del empleado.
     * @return Id del empleado. Si no existe retorna -1
     */
    public int autenticarEmpleado(String cedula, char[] password) {
        ResultSet result;
        int id = -1;

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT cedula AS id, password FROM ")
                .append(mapSchema.get("stpv"))
                .append(".").append(mapTabla.get("empleado"))
                .append(" WHERE cedula='")
                .append(cedula)
                .append("';");

        try {
            postgreSQL.conectar();
            result = postgreSQL.ejecutarSelect(sqlQuery.toString());
            String p = null;

            if (result.next()) {
                id = result.getInt("id");
                p = result.getString("password");
            }
            char[] pass = PuntoVenta.Ventanas.bloqueo2.pass.getPassword();

            String passString = new String(pass);

            if (passString.equals(p)) {

                PuntoVenta.Inicio.MenuPrincipal.btnCaja.setEnabled(true);
                PuntoVenta.Inicio.MenuPrincipal.btnVentas.setEnabled(true);
                PuntoVenta.Inicio.MenuPrincipal.btnAyuda.setEnabled(true);
                PuntoVenta.Inicio.MenuPrincipal.jButton5.setEnabled(true);
                PuntoVenta.Inicio.MenuPrincipal.btnAdmin.setEnabled(true);
                PuntoVenta.Ventanas.bloqueo2.jButton2.setEnabled(true);
                PuntoVenta.Ventanas.bloqueo2.jButton2.requestFocus();

            } else {
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            id = -1;
        } finally {
            postgreSQL.desconectar();
        }

        return id;
    }

    public int autenticarEmpleado2(String cedula, char[] password) {
        ResultSet result;
        int id = -1;

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT id, password FROM ")
                .append(mapSchema.get("stpv"))
                .append(".").append(mapTabla.get("empleado"))
                .append(" WHERE cedula='")
                .append(cedula)
                .append("';");

        try {
            postgreSQL.conectar();
            result = postgreSQL.ejecutarSelect(sqlQuery.toString());
            String p = null;

            if (result.next()) {

                id = result.getInt("id");
                p = result.getString("password");
//                System.out.println("password" + password);
            }
            char[] pass = PuntoVenta.Ventanas.LogIn.jpwContrasena.getPassword();

            String passString = new String(pass);
//            System.out.println("password2" + pass);
            if (passString.equals(p)) {
//                System.out.println("password3" + p);

            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            id = -1;
        } finally {
            postgreSQL.desconectar();
        }

        return id;
    }

    public int autsupervisor(String password) {
        ResultSet result;
        int id1 = 1;
        int id = -1;
//        System.out.println(password + "1111");
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT password FROM ")
                .append(mapSchema.get("stpv"))
                .append(".").append(mapTabla.get("empleado"))
                .append(" WHERE cargo_id='")
                .append(id1)
                .append("'AND password='")
                .append(password)
                .append("';");

        try {
            postgreSQL.conectar();
            result = postgreSQL.ejecutarSelect(sqlQuery.toString());
            String j = null;
            if (result.next()) {
                j = result.getString("password");
            }
//            System.out.println(j + " este es el bd");
            if (password.equals(j)) {
//                System.out.println("lol");
                id = 6;
            } else {
//                System.out.println(password + "no lo es");
                id = -1;
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            postgreSQL.desconectar();
        }

        return id;
    }

    public int actualizarEmpleado(ModeloEmpleado empleado) {
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;
        sqlQuery.append("UPDATE ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("empleado"))
                .append(" SET nombre='").append(empleado.getNombre())
                .append("', apellido='").append(empleado.getApellido())
                .append("', correo='").append(empleado.getCorreo())
                .append("' WHERE id=").append(empleado.getId())
                .append(";");
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    public int actualizarCliente(ModeloCliente cliente) {
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;
        sqlQuery.append("UPDATE ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("cliente"))
                .append(" SET nombre='").append(cliente.getNombre())
                .append("', apellido='").append(cliente.getApellido())
                .append("', correo='").append(cliente.getCorreo())
                .append("', twitter='").append(cliente.getTwitter())
                .append("', facebook='").append(cliente.getFacebook())
                .append("' WHERE id=").append(cliente.getId())
                .append(";");
        int[] idTelefonos = crearTelefonosCliente(cliente.getId(), cliente.getListaTelefonos());
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Actualiza la informacion de un empleado basandose en el ModeloEmpleado.
     *
     * @param empleado
     * @return
     */
    public int actualizarClienteAdmin(ModeloCliente cliente) {
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;
        sqlQuery.append("UPDATE ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("empleado"))
                .append(" SET nombre='").append(cliente.getNombre())
                .append("', apellido='").append(cliente.getApellido())
                .append("', direccion='").append(cliente.getDireccion())
                .append("', correo='").append(cliente.getCorreo())
                .append("', telefono='").append(cliente.getTelefono())
                .append("' WHERE id=").append(cliente.getId())
                .append(";");
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;

    }

    /**
     * Verifica en la base de datos si una caja específica está abierta o
     * cerrada.
     *
     * @param idCaja
     * @return Estado de la caja. Abierto o cerrado.
     */
    public boolean getEstadoCaja(int idCaja) {
        boolean estado = false;
        ResultSet rs;
        String fecha_apertura = "";
        String fecha_cierre = "";

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT fecha_apertura, fecha_cierre FROM ")
                .append(mapSchema.get("stpv"))
                .append(".").append(mapTabla.get("estado_caja"))
                .append(" WHERE caja_id=")
                .append(idCaja)
                .append(" ORDER BY id DESC LIMIT 1;");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                fecha_apertura = rs.getString("fecha_apertura");
                fecha_cierre = rs.getString("fecha_cierre");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        //Este if verifica que la caja tenga fecha de apertura y no tenga fecha de cierre.
        if (!fecha_apertura.isEmpty() && (fecha_cierre == null || fecha_cierre.isEmpty())) {
            estado = true;
        }
        return estado;
    }

    /**
     * Verifica en la base de datos si una caja específica está abierta o
     * cerrada.
     *
     * @param idCaja
     * @return id de la caja si está abierta o -1 si está cerrada.
     */
    public int getIdEstadoCaja(int idCaja) {
        int idEstadoCaja = -1;
        ResultSet rs = null;
        String fecha_cierre = "";

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT id ,fecha_apertura, fecha_cierre FROM ")
                .append(mapSchema.get("stpv"))
                .append(".").append(mapTabla.get("estado_caja"))
                .append(" WHERE caja_id=") 
                .append(idCaja)
                .append(" ORDER BY id DESC LIMIT 1;");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                idEstadoCaja = rs.getInt("id");
                fecha_cierre = rs.getString("fecha_cierre");
            }
        } catch (Exception e) {
            e.printStackTrace();
            idEstadoCaja = -1;
        } finally {
            postgreSQL.desconectar();
        }
        //Este if verifica que la caja no tenga fecha de cierre.
        if (fecha_cierre != null) {
            idEstadoCaja = -1;
        }
        return idEstadoCaja;
    }

    public ArrayList<HashMap<String, String>> getArrayListEmpleado() {
        ArrayList<HashMap<String, String>> resultado = new ArrayList<HashMap<String, String>>();
        StringBuilder sqlQuery = new StringBuilder();
        ResultSet rs;
        HashMap<String, String> map;
        String[] columnasEmpleado = {"e.cedula AS cedula", "e.nombre||' '||e.apellido AS nombre"};
        sqlQuery.append("SELECT ");
        sqlQuery = addColumnasAlQuery(columnasEmpleado, "", sqlQuery);
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);
        sqlQuery.append(" FROM ")
                .append("stpv.empleado AS e;");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            while (rs.next()) {
                map = new HashMap<String, String>();
                for (String columna : columnasEmpleado) {
                    map.put(columna, rs.getString(columna));
                }
                resultado.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultado = null;
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método que busca los registros de apertura y cierre de una caja
     * particular y devulve el id, la cedula del empleado que la abrió/cerró, y
     * los datetime de apertura y cierre.
     *
     * NOTA: Probablemente se deba filtrar la busqueda por usuario tambien.
     *
     * @param idCaja
     * @return Arraylist de HashMap
     */
    public ArrayList<HashMap<String, String>> getArrayListEstadoCaja(int idCaja) {
        ArrayList<HashMap<String, String>> resultado = new ArrayList<HashMap<String, String>>();
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        String[] columnas = {"id", "empleado", "apertura", "cierre"};

        sqlQuery.append("SELECT ec.id AS id, CAST(e.nacionalidad AS text)||'-'||e.cedula AS empleado, ")
                .append("to_char(ec.fecha_apertura, 'DD Mon YYYY HH12:MI:SS AM') AS apertura, to_char(ec.fecha_cierre, 'DD Mon YYYY HH12:MI:SS AM') AS cierre ")
                .append("FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("empleado")).append(" AS e ")
                .append("LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("estado_caja")).append(" AS ec ")
                .append("ON e.id=ec.empleado_id ")
                .append("WHERE ec.caja_id=")
                .append(idCaja)
                .append(" ORDER BY id DESC;");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            while (rs.next()) {
                HashMap<String, String> row = new HashMap<String, String>();
                for (String columna : columnas) {
                    row.put(columna, rs.getString(columna));
                }
                resultado.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    public ArrayList<HashMap<String, String>> getArrayListFactura() {
        ArrayList<HashMap<String, String>> resultado = new ArrayList<HashMap<String, String>>();
        ResultSet rs;
//        StringBuilder sqlQuery = new StringBuilder();
//        String[] columnas = {"codigo_factura", "cliente", "fecha_hora", "monto"};
        String sql1 = "SELECT v.codigo_factura,CONCAT(c.nombre,' ',c.apellido) AS nombre, v.fecha_hora,v.total FROM stpv.cliente c INNER JOIN stpv.venta v on v.cliente_id= c.id ORDER BY v.codigo_factura DESC";

        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sql1);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                HashMap<String, String> row = new HashMap<String, String>();
                for (int i = 1; i < meta.getColumnCount() + 1; i++) {
                    String codigo = "";
                    if ("codigo_factura".equals(meta.getColumnName(i))) {
                        codigo = rs.getString(i);
                        while (codigo.length() < 10) {
                            codigo = "0" + codigo;
                        }
                        row.put(meta.getColumnName(i), codigo);
                    } else {
                        row.put(meta.getColumnName(i), rs.getString(i));
                    }
                }

                resultado.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método que busca los desglose de moneda y devulve el id, el valor y la
     * descripcion.
     *
     *
     * @return Arraylist de HashMap
     */
    public ArrayList<HashMap<String, String>> getArrayListDesgloseMoneda() {
        ArrayList<HashMap<String, String>> resultado = new ArrayList<HashMap<String, String>>();
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        String[] columnas = {"id", "valor", "descripcion"};

        sqlQuery.append("SELECT m.id, m.valor, m.descripcion ")
                .append("FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("moneda")).append(" AS m ")
                .append("ORDER BY m.id ASC;");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            while (rs.next()) {
                HashMap<String, String> row = new HashMap<String, String>();
                for (String columna : columnas) {
                    row.put(columna, rs.getString(columna));
                }
                resultado.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Obtiene una lista de los productos que fueron asociados a una venta
     * particular
     *
     * @param idVenta
     * @return ArrayList<HashMap<String, String>>
     */
    public ArrayList<HashMap<String, String>> getArrayListProductosEnVenta(int idVenta) {
        if (idVenta <= 0) {
            return null;
        }
        ArrayList<HashMap<String, String>> resultado = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        String[] columnaProducto = {"codigo_barra", "descripcion", "pvp-(pvp*impuesto) AS pvp", "pvp*cantidad_producto AS total", "pvp*impuesto AS impuesto"};
        String[] columnaProductoVenta = {"cantidad_producto"};

        sqlQuery.append("SELECT ");
        sqlQuery = addColumnasAlQuery(columnaProducto, "p.", sqlQuery);
        sqlQuery = addColumnasAlQuery(columnaProductoVenta, "vp.", sqlQuery);
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("inventario")).append(".")
                .append(mapTabla.get("producto")).append(" AS p")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta__producto")).append(" AS vp")
                .append(" ON p.id=vp.producto_id")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta")).append(" AS v")
                .append(" ON vp.venta_id=v.id")
                .append(" WHERE v.id=").append(idVenta).append(";");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            while (rs.next()) {
                map = new HashMap<String, String>();
                for (String columna : columnaProducto) {
                    if (("total".equals(columna) || "impuesto".equals(columna) || "pvp".equals(columna))) {
                        map.put(columna, redondeo.format(Double.parseDouble(rs.getString(columna))).replace(",", "."));
                    } else {
                        map.put(columna, rs.getString(columna));
                    }
                }
                for (String columna : columnaProductoVenta) {
                    if (rs.getString(columna).endsWith(".00")) {
                        map.put(columna, rs.getString(columna).substring(0, rs.getString(columna).length() - 3));
                    } else {
                        map.put(columna, rs.getString(columna));
                    }
                }
                resultado.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultado = null;
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método que busca los productos existentes en la tabla
     * inventario.productos y los almacena en un ArrayList
     *
     * @return Arraylist de HashMap
     */
    public ArrayList<HashMap<String, String>> getArrayListProductos() {
        ArrayList<HashMap<String, String>> resultado = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        String[] columnaProducto = {"id", "codigo_barra", "descripcion", "stockminimo", "stockmaximo", "limitedeventaporpersona", "puntodepedido", "costoxunidad", "margendeganancia", "baseimponible", "fecha_actualizacion", "impuesto", "pvp"};

        sqlQuery.append("SELECT ");
        sqlQuery = addColumnasAlQuery(columnaProducto, "p.", sqlQuery);
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("inventario")).append(".")
                .append(mapTabla.get("producto")).append(" AS p;");

        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            while (rs.next()) {
                map = new HashMap<String, String>();
                for (String columna : columnaProducto) {
                    map.put(columna, rs.getString(columna));
                }
                resultado.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultado = null;
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    public ArrayList<HashMap<String, String>> getArrayListTipoMoneda() {
        ArrayList<HashMap<String, String>> resultado = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        String[] columnaTipoMoneda = {"id", "descripcion"};

        sqlQuery.append("SELECT ");
        for (String columna : columnaTipoMoneda) {
            sqlQuery.append(columna).append(",");
        }
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("tipo_moneda")).append(";");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            while (rs.next()) {
                map = new HashMap<String, String>();
                for (String columna : columnaTipoMoneda) {
                    map.put(columna, rs.getString(columna));
                }
                resultado.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultado = null;
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    public ArrayList<HashMap<String, String>> getArrayListClientes() {
        ArrayList<HashMap<String, String>> resultado = new ArrayList<HashMap<String, String>>();
        StringBuilder sqlQuery = new StringBuilder();
        ResultSet rs;
        HashMap<String, String> map;
        String[] columnasCliente = {"c.cedula AS cedula", "c.nombre||' '||c.apellido AS nombre"};
        sqlQuery.append("SELECT ");
        sqlQuery = addColumnasAlQuery(columnasCliente, "", sqlQuery);
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);
        sqlQuery.append(" FROM ")
                .append("stpv.cliente AS c;");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            while (rs.next()) {
                map = new HashMap<String, String>();
                for (String columna : columnasCliente) {
                    map.put(columna, rs.getString(columna));
                }
                resultado.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultado = null;
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método para obtener la lista de cortes que se le han ralizado a un
     * estado_caja.
     *
     * @param idEstadoCaja
     * @return
     */
    public ArrayList<HashMap<String, String>> getArrayListCortesCaja(int idEstadoCaja) {
        ArrayList<HashMap<String, String>> resultado = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        String[] columnasCorteCaja = {"id AS nombre", "monto_corte"};

        sqlQuery.append("SELECT ");
        sqlQuery = addColumnasAlQuery(columnasCorteCaja, "cc.", sqlQuery);
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("corte_caja")).append(" AS cc")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("estado_caja")).append(" AS ec")
                .append(" ON cc.estado_caja_id=ec.id ")
                .append(" WHERE ec.id=").append(idEstadoCaja)
                .append(";");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            while (rs.next()) {
                map = new HashMap<String, String>();
                for (String columna : columnasCorteCaja) {
                    map.put(columna, rs.getString(columna));
                }
                resultado.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultado = null;
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Utiliza el id de la caja para buscar en la base de datos y crear un
     * map<k, v> de la tabla caja
     *
     * @param id
     * @return
     */
    public HashMap<String, String> getMapCaja(int id) {
        StringBuilder sqlQuery = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        ResultSet rs = null;

        sqlQuery.append("SELECT id, descripcion FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("caja"))
                .append(" WHERE id=").append(id)
                .append(" LIMIT 1;");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            String descripcion;
            if (rs.next()) {
                descripcion = rs.getString("descripcion");
                map.put("id", String.valueOf(id));
                map.put("descripcion", descripcion);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        } finally {
            postgreSQL.desconectar();
        }
        return map;
    }

    /**
     * Utiliza el id de un cliente para buscar en la base de datos y crear un
     * map<k, v> de la tabla cliente
     *
     * @param identificador
     * @param cedula
     * @return
     */
    public HashMap<String, String> getMapCliente(char identificador, String cedula) {
        StringBuilder sqlQuery = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        ResultSet rs = null;
        String[] columnaCliente = new String[]{"id", "nombre", "apellido", "direccion", "nacionalidad", "cedula", "correo", "facebook", "twitter"};

        sqlQuery.append("SELECT ");
        for (String columna : columnaCliente) {
            sqlQuery.append("c.").append(columna).append(",");
        }
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("cliente")).append(" AS c ")
                .append(" WHERE c.nacionalidad='").append(identificador).append("'")
                .append(" AND c.cedula='").append(cedula).append("'")
                .append(" LIMIT 1;");

        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                for (String columna : columnaCliente) {
                    map.put(columna, rs.getString(columna));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        } finally {
            postgreSQL.desconectar();
        }
        return map;
    }

    /**
     * Utiliza el id de un cliente para buscar en la base de datos y crear un
     * map<k, v> de la tabla cliente
     *
     * @param idCliente
     * @return
     */
    public HashMap<String, String> getMapCliente(final int idCliente) {
        StringBuilder sqlQuery = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        ResultSet rs = null;
        String[] columnaCliente = new String[]{"id", "nombre", "apellido", "nacionalidad", "cedula", "correo", "facebook", "twitter"};

        sqlQuery.append("SELECT ");
        addColumnasAlQuery(columnaCliente, "c.", sqlQuery);
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("cliente")).append(" AS c ")
                .append(" WHERE c.id=").append(idCliente).append("")
                .append(" LIMIT 1;");

        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                for (String columna : columnaCliente) {
                    map.put(columna, rs.getString(columna));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        } finally {
            postgreSQL.desconectar();
        }
        return map;
    }

    /**
     * Utiliza el id de un empleado para buscar en la base de datos y crear un
     * map<k, v> de la tabla empleado
     *
     * @param id
     * @return
     */
    public HashMap<String, String> getMapEmpleado(int id) {
        StringBuilder sqlQuery = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        ResultSet rs = null;

        String[] columnaEmpleado = new String[]{"id", "nombre", "apellido", "nacionalidad", "cedula", "correo", "cargo_id", "password"};
        String[] columnaCargo = new String[]{"descripcion"};

        sqlQuery.append("SELECT ");
        for (String columna : columnaEmpleado) {
            sqlQuery.append("e.").append(columna).append(",");
        }
        for (String columna : columnaCargo) {
            sqlQuery.append("c.").append(columna).append(",");
        }
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("empleado")).append(" AS e")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("cargo")).append(" AS c")
                .append(" ON e.cargo_id=c.id")
                .append(" WHERE e.id=").append(id)
                .append(" LIMIT 1;");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                for (String columna : columnaEmpleado) {
                    map.put(columna, rs.getString(columna));
                }
                for (String columna : columnaCargo) {
                    map.put(columna, rs.getString(columna));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        } finally {
            postgreSQL.desconectar();
        }
        return map;
    }

    public HashMap<String, String> getMapUsuarioSistema(int idEmpleado) {
        StringBuilder sqlQuery = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        ResultSet rs = null;

        String[] columnaUsuarioSistema = new String[]{"usuario"};

        sqlQuery.append("SELECT ");
        sqlQuery = addColumnasAlQuery(columnaUsuarioSistema, "u.", sqlQuery);
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("empleado")).append(" AS e")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("usuario")).append(" AS u")
                .append(" ON e.id=u.empleado_id")
                .append(" WHERE e.id=").append(idEmpleado)
                .append(" LIMIT 1;");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                for (String columna : columnaUsuarioSistema) {
                    map.put(columna, rs.getString(columna));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        } finally {
            postgreSQL.desconectar();
        }
        return map;
    }

    /**
     * Utiliza el id o el codigo de barras de un prodcuto para buscar en el
     * schema inventario y crear un map<k, v> de la tabla producto
     *
     * @param codigoBarra
     * @return
     */
    public HashMap<String, String> getMapProducto(String codigoBarra) {
        StringBuilder sqlQuery = new StringBuilder();
        HashMap<String, String> map = new HashMap<String, String>();
        ResultSet rs;
        String[] columnaProducto = {"id", "codigo_barra", "descripcion", "stockminimo", "stockmaximo", "limitedeventaporpersona", "puntodepedido", "costoxunidad", "margendeganancia", "baseimponible", "fecha_actualizacion", "impuesto", "pvp"};

        sqlQuery.append("SELECT ");
        addColumnasAlQuery(columnaProducto, "p.", sqlQuery);
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("inventario")).append(".")
                .append(mapTabla.get("producto")).append(" AS p ")
                .append(" WHERE ");
        try {
//            int id = Integer.parseInt(codigoBarra);
            //sqlQuery.append("p.id=").append(id);
            sqlQuery.append("p.codigo_barra='").append(codigoBarra).append("'");
//        } catch (NumberFormatException e) {
//            sqlQuery.append("p.codigo_barra='").append(codigoBarra).append("'");
        } finally {
            sqlQuery.append(" LIMIT 1;");
        }
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                for (String columna : columnaProducto) {
                    map.put(columna, rs.getString(columna));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map = null;
        } finally {
            postgreSQL.desconectar();
        }
        return map;
    }

    /**
     * Método para obtener el total de venta de un EstadoCaja basado en la
     * sumatoria de todos los montos asociados a pagos de alguna venta
     * (facturas).
     *
     * @param idEstadoCaja
     * @return XBigDecimal con el total.
     */
    public XBigDecimal getTotalEstadoCajaPorPagoCliente(int idEstadoCaja) {
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        XBigDecimal resultado;

        sqlQuery.append("SELECT SUM(p.monto) as total");

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta")).append(" AS v")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("estado_venta")).append(" AS ev")
                .append(" ON v.estado_venta_id=ev.id")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta__pago")).append(" AS vp")
                .append(" ON v.id=vp.venta_id")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("pago")).append(" AS p")
                .append(" ON vp.pago_id=p.id")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("estado_caja")).append(" AS ec")
                .append(" ON v.estado_caja_id=ec.id")
                .append(" WHERE v.estado_caja_id=").append(idEstadoCaja)
                .append(" AND ev.descripcion='finalizada';");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                resultado = new XBigDecimal(rs.getString("total"));
            } else {
                resultado = new XBigDecimal(0);
            }
        } catch (Exception e) {
            resultado = new XBigDecimal(0);
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método para obtener el total de venta de un EstadoCaja basado en la
     * sumatoria del pvp de todos los productos vendidos. Si la caja no tiene
     * ventas, regresa 0.
     *
     * NOTA. Este query ha sido cambiado por getTotalEstadoCaja, ya que al
     * cambiar el pvp de algun producto no iba a cuadrar la caja en el momento
     * de cierre.
     *
     * @param idEstadoCaja
     * @return XBigDecimal con el valor del total de la venta.
     */
    public XBigDecimal getTotalEstadoCajaPorProductoVendido(int idEstadoCaja) {
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        XBigDecimal resultado;

        sqlQuery.append("SELECT SUM(p.pvp*vp.cantidad_producto) as total");

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta")).append(" AS v")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("estado_venta")).append(" AS ev")
                .append(" ON v.estado_venta_id=ev.id")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta__producto")).append(" AS vp")
                .append(" ON v.id=vp.venta_id")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("inventario")).append(".")
                .append(mapTabla.get("producto")).append(" AS p")
                .append(" ON p.id=vp.producto_id")
                .append(" WHERE v.estado_caja_id=").append(idEstadoCaja)
                .append(" AND ev.descripcion='finalizada';");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                resultado = new XBigDecimal(rs.getString("total"));
            } else {
                resultado = new XBigDecimal(0);
            }
        } catch (Exception e) {
            resultado = new XBigDecimal(0);
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método para obtener el total de venta de un EstadoCaja basado en el valor
     * de la columna total en la tabla venta. Si la caja no tiene ventas,
     * regresa 0.
     *
     * NOTA. Este query se ha creado en reemplazo de
     * getTotalEstadoCajaPorProductoVendido;
     *
     * @param idEstadoCaja
     * @return XBigDecimal con el valor del total de la venta.
     */
    public XBigDecimal getTotalEstadoCaja(int idEstadoCaja) {
        ResultSet rs;
        XBigDecimal resultado = new XBigDecimal(0);
        XBigDecimal acum = new XBigDecimal(0);
        String sql = "SELECT v.total AS total FROM stpv.venta AS v LEFT JOIN stpv.estado_venta AS ev ON v.estado_venta_id=ev.id WHERE v.estado_caja_id=" + idEstadoCaja + " AND v.corte_caja is null";

        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sql);

            while (rs.next()) {
                acum = new XBigDecimal(rs.getString("total"));
                resultado = new XBigDecimal(resultado.add(acum.negate()).toString());
//                System.out.println("Valor del XBIGDECIMAL:" + resultado);
            }
//                resultado = new XBigDecimal(0);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            postgreSQL.desconectar();
        }
        resultado = new XBigDecimal(resultado.toString().replaceAll("-", ""));
//        System.out.println("REsultado valor xbd" + resultado);
        return resultado;
    }

    public XBigDecimal getTotalEstadoCajaCierre(int idEstadoCaja) {
        ResultSet rs;
        XBigDecimal resultado = new XBigDecimal(0);
        XBigDecimal acum = new XBigDecimal(0);
        String sql = "SELECT v.total AS total FROM stpv.venta AS v LEFT JOIN stpv.estado_venta AS ev ON v.estado_venta_id=ev.id WHERE v.estado_caja_id=" + idEstadoCaja + " AND v.cierre_caja is null";

        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sql);

            while (rs.next()) {
                acum = new XBigDecimal(rs.getString("total"));
                resultado = new XBigDecimal(resultado.add(acum.negate()).toString());
//                System.out.println("Valor del XBIGDECIMAL:" + resultado);
            }
//                resultado = new XBigDecimal(0);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            postgreSQL.desconectar();
        }
        resultado = new XBigDecimal(resultado.toString().replaceAll("-", ""));
//        System.out.println("REsultado valor xbd" + resultado);
        return resultado;
    }

    public List<ValorPagos> getTotalPagoCierre(int idEstadoCaja) {
        ResultSet rs;
        List<ValorPagos> resultado = new ArrayList();
        double ef = 0;
        double tdd = 0;
        double tdc = 0;
        double ctk = 0;
        String sql = "SELECT p.tipopago,p.monto FROM stpv.pago p INNER JOIN stpv.venta v ON p.id_venta=v.id LEFT JOIN stpv.estado_venta AS ev ON v.estado_venta_id=ev.id WHERE v.estado_caja_id=" + idEstadoCaja + " AND v.cierre_caja is null";

        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sql);

            while (rs.next()) {
                if ("Efectivo".equals(rs.getString("tipopago"))) {
                    ef = ef + Double.parseDouble(rs.getString("monto"));
                }
                if ("Debito".equals(rs.getString("tipopago"))) {
                    tdd = tdd + Double.parseDouble(rs.getString("monto"));
                }
                if ("Credito".equals(rs.getString("tipopago"))) {
                    tdc = tdc + Double.parseDouble(rs.getString("monto"));
                }
                if ("Cestaticket".equals(rs.getString("tipopago"))) {
                    ctk = ctk + Double.parseDouble(rs.getString("monto"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            postgreSQL.desconectar();
        }
        resultado.add(new ValorPagos("Efectivo", ef));
        resultado.add(new ValorPagos("Debito", tdd));
        resultado.add(new ValorPagos("Credito", tdc));
        resultado.add(new ValorPagos("Cestaticket", ctk));
        return resultado;
    }

    public List<ValorPagos> montoscorte(List<Integer> lista) {
        ResultSet rs;
        List<ValorPagos> resultado = new ArrayList();
        List<ValorPagos> valores = new ArrayList();
        double ef = 0;
        double tdd = 0;
        double tdc = 0;
        double ctk = 0;
        for (Integer id : lista) {
            String sql = "SELECT p.tipopago,p.monto FROM stpv.venta v INNER JOIN stpv.pago p on p.id_venta =v.id WHERE v.id='" + id + "';";
//            System.out.println(sql);
            try {
                postgreSQL.conectar();
                rs = postgreSQL.ejecutarSelect(sql);
                while (rs.next()) {
//                    System.out.println(rs.getString("tipopago")+":"+rs.getDouble("monto"));
                    valores.add(new ValorPagos(rs.getString("tipopago"), rs.getDouble("monto")));
                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                postgreSQL.desconectar();
            }
        }
        for (ValorPagos valor : valores) {
            if ("Efectivo".equals(valor.getTipo())) {
                ef = ef + valor.getMontoD();
            }
            if ("Debito".equals(valor.getTipo())) {
                tdd = tdd + valor.getMontoD();
            }
            if ("Credito".equals(valor.getTipo())) {
                tdc = tdc + valor.getMontoD();
            }
            if ("Cestaticket".equals(valor.getTipo())) {
                ctk = ctk + valor.getMontoD();
            }
        }
        resultado.add(new ValorPagos("Efectivo", ef));
        resultado.add(new ValorPagos("Debito", tdd));
        resultado.add(new ValorPagos("Credito", tdc));
        resultado.add(new ValorPagos("Cestaticket", ctk));
        return resultado;
    }

    public ValorPagos getTotalCortesCierre(int idEstadoCaja) {
        ResultSet rs;
//        List<ValorPagos> resultado = new ArrayList();
        double monto = 0;
        String sql = "SELECT cc.monto_corte FROM stpv.corte_caja cc WHERE cc.estado_caja_id=" + idEstadoCaja + ";";

        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sql);
            while (rs.next()) {
                monto = monto + Double.parseDouble(rs.getString("monto_corte"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            postgreSQL.desconectar();
        }
        ValorPagos resultado = new ValorPagos("monto", monto);
        return resultado;
    }

    public List<Integer> getListIDVentas(int idEstadoCaja) {
        ResultSet rs;
        List<Integer> resultado = new ArrayList();
        String sql = "SELECT v.id FROM stpv.venta AS v LEFT JOIN stpv.estado_venta AS ev ON v.estado_venta_id=ev.id WHERE v.estado_caja_id=" + idEstadoCaja + " AND v.corte_caja is null";
//        System.out.println(sql);
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sql);
            while (rs.next()) {
//                System.out.println(rs.getInt("id"));
                resultado.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            postgreSQL.desconectar();
        }
//        System.out.println(resultado);
        return resultado;
    }

    /**
     * Obtiene el limite de cantidad maxima de un producto.
     *
     * @param idEstadoCaja
     * @return XBigDecimal con el limite maximo del producto.
     */
    public int getLimiteMaximoProducto(String codigoBarra) {
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;

        sqlQuery.append("SELECT limitedeventaporpersona FROM ")
                .append(mapSchema.get("inventario")).append(".")
                .append(mapTabla.get("producto"))
                .append(" WHERE codigo_barra = '").append(codigoBarra).append("'");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            while (rs.next()) {
                resultado = rs.getInt(1);
            }
        } catch (Exception e) {
            resultado = -1;
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método para obtener el total de dinero sacado por cortes de caja.
     *
     * @param idEstadoCaja
     * @return XBigDecimal con el total.
     */
    public XBigDecimal getTotalCorteCaja(int idEstadoCaja) {
        ResultSet rs;
        StringBuilder sqlQuery = new StringBuilder();
        XBigDecimal resultado;
        String sql = "selec ";
        sqlQuery.append("SELECT SUM(cc.monto_corte) AS total");

        sqlQuery.append(" FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("corte_caja")).append(" AS cc")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("estado_caja")).append(" AS ec")
                .append(" ON cc.estado_caja_id=ec.id")
                .append(" WHERE ec.id=").append(idEstadoCaja).append(";");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                resultado = new XBigDecimal(rs.getString("total"));
            } else {
                resultado = new XBigDecimal(0);
            }
        } catch (Exception e) {
            resultado = new XBigDecimal(0);
        } finally {
            postgreSQL.desconectar();
        }
//        System.out.println("Total corte bddd: " + resultado);
        return resultado;
    }

    /**
     * Inserta un cliente en la tabla stpv.cliente.
     *
     * @param nombre
     * @param apellido
     * @param nacionalidad
     * @param cedula
     * @param direccion
     * @return id del cliente resultado del INSERT o -1 en caso de fallar.
     */
    public int crearCliente(String nombre, String apellido, char nacionalidad, String cedula, String direccion) {
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append("INSERT INTO ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("cliente"))
                .append("(nombre, apellido, nacionalidad, cedula, direccion) VALUES (")
                .append("'").append(nombre).append("', ")
                .append("'").append(apellido).append("', ")
                .append("'").append(nacionalidad).append("', ")
                .append("'").append(cedula).append("', ")
                .append("'").append(direccion).append("');");

        int resultado = ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());

        return resultado;
    }

    public int[] crearTelefonosCliente(int idCliente, List<String> listaTelefonos) {
        StringBuilder sqlQuery = new StringBuilder();
        int[] resultado = new int[listaTelefonos.size()];

        sqlQuery.append("INSERT INTO ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("telefono"))
                .append("(numero) VALUES ");

        for (String numero : listaTelefonos) {
            sqlQuery.append("('").append(numero).append("'),");
        }
        sqlQuery.deleteCharAt(sqlQuery.length() - 1);

        int cantidadTelefonos = ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        for (int i = 0; i < resultado.length; i++) {
            resultado[i] = cantidadTelefonos + i;
            sqlQuery = new StringBuilder();
            sqlQuery.append("INSERT INTO ")
                    .append(mapSchema.get("stpv")).append(".")
                    .append(mapTabla.get("cliente__telefono"))
                    .append("(cliente_id,telefono_id) VALUES ")
                    .append("('").append(idCliente).append("',")
                    .append("'").append(resultado[i]).append("');");
            int idTabla = ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        }
        return resultado;
    }

    /**
     * Realiza un INSERT a la tabla venta-
     *
     * @param idCliente
     * @param idEstadoCaja
     * @return
     */
    public int crearVenta(int idCliente, int idEstadoCaja) {
        ResultSet rs;
        Date date = new java.util.Date();
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;

        sqlQuery.append("SELECT id FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta"))
                .append(" WHERE cliente_id=").append(idCliente)
                .append(" AND estado_venta_id in (").append(EstadoVenta.EnProceso)
                .append(",").append(EstadoVenta.Pausada).append(");");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                resultado = rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }

        sqlQuery = new StringBuilder();

        if (resultado != -1) {
            sqlQuery.append("UPDATE ")
                    .append(mapSchema.get("stpv")).append(".")
                    .append(mapTabla.get("venta"))
                    .append(" SET estado_caja_id=").append(idEstadoCaja)
                    .append(" WHERE id=").append(resultado)
                    .append(";");
        } else {
            sqlQuery.append("INSERT INTO ")
                    .append(mapSchema.get("stpv")).append(".")
                    .append(mapTabla.get("venta"))
                    .append("(cliente_id, estado_caja_id, estado_venta_id,fecha_hora) VALUES (")
                    .append(idCliente).append(", ")
                    .append(idEstadoCaja).append(", ")
                    .append(EstadoVenta.EnProceso).append(", ")
                    .append("'").append(new Timestamp(date.getTime()))
                    .append("');");
        }
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    public String numeroFactura(int idCliente, int idEstadoCaja) {
        ResultSet res;
        StringBuilder sqlQuery = new StringBuilder();
        String resultado = "";
        sqlQuery.append("SELECT * FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta"))
                .append(" WHERE cliente_id=").append(idCliente)
                .append(" AND (estado_venta_id = ").append(EstadoVenta.EnProceso)
                .append(" OR estado_venta_id = ").append(EstadoVenta.Pausada).append(");");
        try {
            //System.out.println(sqlQuery.toString());
            postgreSQL.conectar();
            res = postgreSQL.getSentencia().executeQuery(sqlQuery.toString());
            if (res.next()) {
                resultado = res.getString("codigo_factura");
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        while (resultado.length() < 10) {
            resultado = "0" + resultado;
        }
        return resultado;
    }

    /**
     * Realiza un UPDATE a la tabla producto-
     *
     * @param codigo
     * @param cantidad
     * @return
     */
    public int descontarCantidad(String codigo, double cantidad) throws SQLException {

        ResultSet rs = null;
        int resultado = -1;
        double can = Double.parseDouble((String) PuntoVenta.Ventanas.Venta.txtCantidad.getText());

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("UPDATE ")
                .append(mapSchema.get("inventario")).append(".")
                .append(mapTabla.get("producto"))
                .append(" SET cantidad = " + cantidad + " - ").append(can)
                .append(" WHERE codigo_barra = '").append(codigo).append("'")
                .append(";");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                resultado = rs.getInt("codigo_barra");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
//        System.out.println(can + "cantidad2");
        //PreparedStatement us1 = con.conexion().prepareStatement(sql1);
        //int res1 = us1.executeUpdate();
//        System.out.println(sqlQuery);
        return resultado;
    }

    /**
     * Realiza un UPDATE a la tabla producto-
     *
     * @param codigo
     * @param cantidad
     * @return
     */
    public int contarCantidad(String codigo, double cantidad) throws SQLException {

        ResultSet rs = null;
        int resultado = -1;
        double can = PuntoVenta.Ventanas.Venta.jtbVenta.getModel().getRowCount();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("UPDATE ")
                .append(mapSchema.get("inventario")).append(".")
                .append(mapTabla.get("producto"))
                .append(" SET cantidad =" + cantidad + " + ").append(can)
                .append(" WHERE codigo_barra = '").append(codigo).append("'")
                .append(";");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                resultado = rs.getInt("codigo_barra");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
//        System.out.println(can + "cantidad2");
        //PreparedStatement us1 = con.conexion().prepareStatement(sql1);
        //int res1 = us1.executeUpdate();
//        System.out.println(sqlQuery);
        return resultado;
    }

    /**
     * Método para crear un corte de caja dado un id y un monto.
     *
     * @param idEstadoCaja EstadoCaja a la que se aplicará el corte.
     * @param monto Monto total del corte
     * @return
     */
    public int crearCorteCaja(int idEstadoCaja, XBigDecimal monto, XBigDecimal excedente, XBigDecimal restante) {
        Date date = new java.util.Date();
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;

        sqlQuery.append("INSERT INTO ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("corte_caja"))
                .append("(estado_caja_id, monto_corte, excedente, restante, fecha_hora) VALUES (")
                .append(idEstadoCaja).append(", ")
                .append(monto.setScale(2, RoundingMode.UNNECESSARY).toString()).append(", ")
                .append(excedente.setScale(2, RoundingMode.UNNECESSARY).toString()).append(", ")
                .append(restante.setScale(2, RoundingMode.UNNECESSARY).toString()).append(", ")
                .append("'").append(new Timestamp(date.getTime()))
                .append("');");

        try {
            postgreSQL.conectar();
//            System.out.println(sqlQuery.toString());
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método para crear un desglose de un corte de caja dado un id y un monto.
     *
     * @param idCorteCaja
     * @param tipo
     * @param monto
     * @return
     */
    public int crearDesgloseCaja(int idCorteCaja, String tipo, XBigDecimal monto) {
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;

        sqlQuery.append("INSERT INTO ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("desglose_caja"))
                .append("(corte_caja_id, monto,tipopag) VALUES (")
                .append(idCorteCaja).append(", ")
                .append(monto).append(",' ")
                .append(tipo).append("');");
        try {
//            System.out.println(sqlQuery.toString());
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    public void ActualizarCorteEnVenta(int idEstadoCaja) {
        String sql = "UPDATE stpv.venta v SET corte_caja = true WHERE v.estado_caja_id=" + idEstadoCaja;
//        System.out.println(sql);
        try {
            postgreSQL.conectar();
            postgreSQL.ejecutarSelect(sql);
//            System.out.println(sql);
        } catch (Exception e) {
//            System.out.println("hola");
        } finally {
            postgreSQL.desconectar();
        }
    }

    public void ActualizarCierreEnVenta(int idEstadoCaja) {
        String sql = "UPDATE stpv.venta v SET cierre_caja = true WHERE v.estado_caja_id=" + idEstadoCaja;
//        System.out.println(sql);
        try {
            postgreSQL.conectar();
            postgreSQL.ejecutarSelect(sql);
//            System.out.println(sql);
        } catch (Exception e) {
//            System.out.println("hola");
        } finally {
            postgreSQL.desconectar();
        }
    }

    public String montoInicial(int idEstadoCaja) {
        ResultSet rs;
        String monto = "";
        String sql = "SELECT ec.monto_apertura FROM stpv.estado_caja ec WHERE ec.id=" + idEstadoCaja;
//        System.out.println(sql);
        try {
            postgreSQL.conectar();
            rs = postgreSQL.getSentencia().executeQuery(sql);
            while (rs.next()) {
                monto = rs.getString("monto_apertura");
            }
        } catch (Exception e) {
        } finally {
            postgreSQL.desconectar();
        }
        return monto;
    }

    /**
     * Método para crear un desglose de un cierre de caja dado un id y un monto.
     *
     * @param idCorteCaja
     * @param idMoneda
     * @param cantidadMoneda
     * @param monto
     * @return
     */
    public int crearDesgloseCierreCaja(int idCaja, int idMoneda, int cantidadMoneda, XBigDecimal monto) {
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;

        sqlQuery.append("INSERT INTO ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("desglose_caja_cierre"))
                .append("(cierre_caja_id, moneda_id, cantidad_moneda, monto) VALUES (")
                .append(idCaja).append(", ")
                .append(idMoneda).append(", ")
                .append(cantidadMoneda).append(", ")
                .append(monto).append(");");
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Registra un pago en la base de datos. ATENCION: Este pago no se asocia a
     * ninguna otra tabla, debe hacerse manualmente. Ver crearPagoVenta();
     *
     * @param monto
     * @param idTipoPago
     * @return El id del pago creado.
     */
    public int crearPago(XBigDecimal monto, int idTipoPago) {
        Date date = new java.util.Date();
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;

        sqlQuery.append("INSERT INTO ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("pago"))
                .append("(monto, tipo_moneda_id, fecha_hora) VALUES (")
                .append(monto.toString()).append(", ")
                .append(idTipoPago).append(", '")
                .append(new Timestamp(date.getTime())).append("');");
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método para asociar un pago a una venta.
     *
     * @param idPago
     * @param idVenta
     * @return
     */
    public int asociarPagoVenta(int idPago, int idVenta) {
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;

        sqlQuery.append("INSERT INTO ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta__pago"))
                .append("(pago_id, venta_id) VALUES (")
                .append(idPago).append(", ")
                .append(idVenta).append(");");
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método para crear un
     *
     * @param idVenta
     * @param idProducto
     * @param cantidadProducto
     * @deprecated
     * @return
     */
    public int incluirProductoEnVenta(int idVenta, int idProducto, int cantidadProducto) {
        StringBuilder sqlQuery = new StringBuilder();
        ResultSet rs;
        int cantidadAnterior = 0;
        int resultado = -1;

        sqlQuery.append("SELECT id, cantidad_producto AS cantidad FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta__producto"))
                .append(" WHERE venta_id=").append(idVenta)
                .append(" AND producto_id=").append(idProducto).append(";");
        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                resultado = rs.getInt("id");
                cantidadAnterior = rs.getInt("cantidad");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }

        sqlQuery = new StringBuilder();
        if (resultado != -1) {
            sqlQuery.append("UPDATE ")
                    .append(mapSchema.get("stpv")).append(".")
                    .append(mapTabla.get("venta__producto"))
                    .append(" SET cantidad_producto=").append(cantidadProducto + cantidadAnterior)
                    .append(" WHERE id=").append(resultado)
                    .append(";");
        } else {
            sqlQuery.append("INSERT INTO ")
                    .append(mapSchema.get("stpv")).append(".")
                    .append(mapTabla.get("venta__producto"))
                    .append("(producto_id, venta_id, cantidad_producto) VALUES (")
                    .append(idProducto).append(", ")
                    .append(idVenta).append(", ")
                    .append(cantidadProducto).append(");");
        }
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Método para incluir un producto en una venta. Si existe un producto del
     * mismo idProducto en la la venta, se hace un UPDATE de la tabla. En caso
     * contrario se hace un INSERT
     *
     * @param idVenta
     * @param codigoProducto
     * @param cantidadProducto
     * @return
     */
    public int incluirProductoEnVenta(int idVenta, String codigoProducto, double cantidadProducto) {
        StringBuilder sqlQuery = new StringBuilder();
        ResultSet rs;
        double cantidadAnterior = 0.00;
        int resultado = -1;
        int idProducto = -1;

        sqlQuery.append("SELECT vp.producto_id AS idProducto, vp.cantidad_producto AS cantidad FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta__producto")).append(" AS vp ")
                .append(" LEFT JOIN ")
                .append(mapSchema.get("inventario")).append(".")
                .append(mapTabla.get("producto")).append(" AS p ")
                .append(" ON vp.producto_id=p.id")
                .append(" WHERE venta_id=").append(idVenta)
                .append(" AND ");
        try {
//            int id = Integer.parseInt(codigoProducto);
            //sqlQuery.append("(p.id='").append(codigoProducto).append("'");
            sqlQuery.append("p.codigo_barra='").append(codigoProducto).append("'");
//        } catch (NumberFormatException e) {
//            sqlQuery.append("p.codigo_barra='").append(codigoProducto).append("'");
        } finally {
            sqlQuery.append(";");
        }

        try {
            postgreSQL.conectar();
            rs = postgreSQL.ejecutarSelect(sqlQuery.toString());
            if (rs.next()) {
                idProducto = rs.getInt("idProducto");
                cantidadAnterior = rs.getDouble("cantidad");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }

        sqlQuery = new StringBuilder();
        if (idProducto > 0) {
            sqlQuery.append("UPDATE ")
                    .append(mapSchema.get("stpv")).append(".")
                    .append(mapTabla.get("venta__producto"))
                    .append(" SET cantidad_producto=").append(cantidadProducto + cantidadAnterior)
                    .append(" WHERE venta_id=").append(idVenta)
                    .append(" AND producto_id=").append(idProducto)
                    .append(";");
        } else {
            StringBuilder queryID = new StringBuilder();
            queryID.append("SELECT id FROM ")
                    .append(mapSchema.get("inventario")).append(".")
                    .append(mapTabla.get("producto"))
                    .append(" WHERE codigo_barra='").append(codigoProducto).append("'");

            sqlQuery.append("INSERT INTO ")
                    .append(mapSchema.get("stpv")).append(".")
                    .append(mapTabla.get("venta__producto"))
                    .append("(producto_id, venta_id, cantidad_producto) VALUES ((")
                    .append(queryID).append("), ")
                    .append(idVenta).append(", ")
                    .append(cantidadProducto).append(");");
        }
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Elimina un producto incluido en una venta, dado un idVenta y el código de
     * barra del producto. El código de barra es utilizado para sacar el id del
     * producto ya que en la tabla de productos no se guarda su id, sino el
     * codigo de barra.
     *
     * @param idVenta Id de la venta que esté abierta.
     * @param codigoBarra Codigo del producto que se desee eliminar.
     * @return
     */
    public int eliminarProductoEnVenta(int idVenta, String codigoBarra) {
        StringBuilder sqlQuery = new StringBuilder();
        int resultado = -1;
        String idProducto = "(SELECT id FROM inventario.producto WHERE codigo_barra='" + codigoBarra + "' LIMIT 1)";
        sqlQuery.append("DELETE FROM ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta__producto"))
                .append(" WHERE venta_id=").append(idVenta)
                .append(" AND producto_id=").append(idProducto).append(";");
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Hace el INSERT o el UPDATE de la tabla estado_caja.
     *
     * @param idEstadoCaja Id de la tabla estado_caja, en caso de ser 0 se asume
     * que la tabla no existe y realiza un insert
     * @param idCaja Id de la caja
     * @param idEmpleado Id de tabla del empleado.
     * @param monto Si es un INSERT, se toma como monto inicial. Si es un
     * UPDATE, se toma como monto final
     * @return id del estado_caja resultado del INSERT o del UPDATE.
     */
    public int setEstadoCaja(int idEstadoCaja, int idCaja, int idEmpleado, String monto) {
        Date date = new java.util.Date();
        int resultado = -1;
        StringBuilder sqlQuery = new StringBuilder();
        if (idEstadoCaja <= 0) {
            sqlQuery.append("INSERT INTO ")
                    .append(mapSchema.get("stpv")).append(".")
                    .append(mapTabla.get("estado_caja"))
                    .append("(caja_id, empleado_id, fecha_apertura, monto_apertura) VALUES (")
                    .append(idCaja).append(", ")
                    .append(idEmpleado).append(", '")
                    .append(new Timestamp(date.getTime())).append("', ")
                    .append(monto).append(");");
        } else {
            sqlQuery.append("UPDATE ")
                    .append(mapSchema.get("stpv")).append(".")
                    .append(mapTabla.get("estado_caja"))
                    .append(" SET fecha_cierre='").append(new Timestamp(date.getTime())).append("' ,")
                    .append("monto_cierre=").append(monto)
                    .append(" WHERE id=").append(idEstadoCaja)
                    .append(";");
        }
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Hace el UPDATE de la columna excedente de la tabla ESTADO_CAJP
     *
     * @param idEstadoCaja Id de la tabla estado_caja, en caso de ser 0 se asume
     * que la tabla no existe y realiza un insert
     * @param monto Si es positivo se entiende como excendente, de lo contrario
     * es faltante.
     * @return id del estado_caja resultado del INSERT o del UPDATE.
     */
    public int setExcendenteFaltanteCaja(int idEstadoCaja, String monto) {
        Date date = new java.util.Date();
        int resultado = -1;
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append("UPDATE ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("estado_caja"))
                .append(" SET excedente=").append(monto)
                .append(" WHERE id=").append(idEstadoCaja)
                .append(";");

        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Cambia el estado de la venta.
     *
     * @param idVenta
     * @param estadoVenta
     * @return
     */
    public int setEstadoVenta(int idVenta, EstadoVenta estadoVenta) {
        int resultado = -1;
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append("UPDATE ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta"))
                .append(" SET estado_venta_id=").append(estadoVenta)
                .append(" WHERE id=").append(idVenta)
                .append(";");

        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    public void descontarcantidad(List<ArticuloDescontar> ad) {
        ResultSet result;
        int canti = 0;
        for (ArticuloDescontar articulo : ad) {
            String sql1 = "SELECT * FROM  inventario.producto WHERE codigo_barra = '" + articulo.getCodigo_barra() + "';";
            try {
                postgreSQL.conectar();
                result = postgreSQL.getSentencia().executeQuery(sql1);
                if (result.next()) {
                    canti = result.getInt("cantidad");
                }
                String sql2 = "UPDATE inventario.producto set cantidad=" + (canti - articulo.getCantidad()) + " WHERE codigo_barra='" + articulo.getCodigo_barra() + "';";
                postgreSQL.getSentencia().executeQuery(sql2);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ObjetoBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                postgreSQL.desconectar();
            }
        }
    }

//    public String GetNombreEmp(int id){
//        String sql = "select concat(nombre,' ',apellido) as nombre from stpv.empleado,stpv.estado_caja,stpv.venta where stpv.empleado.id = stpv.estado_caja.empleado_id and stpv.estado_caja.id = stpv.venta.id and stpv.venta.id = 84";
//        String nombre = "";
//        ResultSet result;
//        return nombre;
//        try{
//            postgreSQL.conectar();
//            result = postgreSQL.getSentencia().executeQuery(sql);
//            if(result.next)
//        }catch(ClassNotFoundException | SQLException ex){
//            Logger.getLogger(ObjetoBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public boolean consultastock(String codp, String cantcomp) {
        ResultSet result;
        int cantidad = 0;
        int stock = 0;
        String sql1 = "SELECT * FROM  inventario.producto WHERE codigo_barra = '" + codp + "';";

        try {
            postgreSQL.conectar();
            System.out.println(sql1);
            result = postgreSQL.getSentencia().executeQuery(sql1);

            if (result.next()) {
                stock = result.getInt("stockminimo");
                cantidad = result.getInt("cantidad");
            }
            if ((cantidad - stock) < Integer.parseInt(cantcomp)) {
                return false;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ObjetoBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public void actualizaiva(int id, String iva, XBigDecimal monto, double pagoconinva, double pagosiniva, String totalpag, String cambio, int idemp) {
        SimpleDateFormat sdf = new SimpleDateFormat("Y-MM-dd hh:mm a");
        String fecha = sdf.format(new Date());
        String sql = "UPDATE stpv.venta SET iva ='" + iva + "',total='" + monto + "',fecha_hora='" + fecha + "',total_no_exento='" + pagoconinva + "',total_exento='" + pagosiniva + "',totalpag='" + totalpag + "',cambio='" + cambio + "',empleado_id='" + idemp + "' WHERE id=" + id + ";";
        try {
            postgreSQL.conectar();
            postgreSQL.getSentencia().executeQuery(sql);
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }

    }

    public void guardarPagos(int idventa, List<ValorPagos> valor) {
        String sql = "";
        for (ValorPagos vp : valor) {
            sql = "INSERT into stpv.pago (id_venta,tipopago,monto,fecha_hora) VALUES(" + idventa + ",'" + vp.getTipo() + "','" + vp.getMonto() + "','" + vp.getFecha() + "');";
            System.out.println(sql);
            try {
                postgreSQL.conectar();
                postgreSQL.getSentencia().executeQuery(sql);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                postgreSQL.desconectar();
            }
        }
    }

    /**
     * Actualiza un registro de venta con su correspondiente IVA y Total.
     *
     * @param idVenta
     * @param estadoVenta
     * @return
     */
    public int setMontoVenta(int idVenta, String iva, String total) {
        int resultado = -1;
        StringBuilder sqlQuery = new StringBuilder();

        sqlQuery.append("UPDATE ")
                .append(mapSchema.get("stpv")).append(".")
                .append(mapTabla.get("venta"))
                .append(" SET iva=").append(iva)
                .append(", total=").append(total)
                .append("WHERE id=").append(idVenta)
                .append(";");
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(sqlQuery.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    /**
     * Muestra el nombre del empleado en la caja
     *
     * @param cedula cedula del empleado que se mostrara.
     * @param password Codigo del producto que se desee eliminar.
     * @return
     */
    public int setEmpleadoCaja(String cedula, char[] password) {
        ResultSet result;
        int id = -1;

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT cedula AS id, password FROM ")
                .append(mapSchema.get("stpv"))
                .append(".").append(mapTabla.get("empleado"))
                .append(" WHERE cedula='")
                .append(cedula)
                .append("';");

        try {
            postgreSQL.conectar();
            result = postgreSQL.ejecutarSelect(sqlQuery.toString());
            String p = null;

            if (result.next()) {
                id = result.getInt("id");
                p = result.getString("password");
            }
            char[] pass = PuntoVenta.Ventanas.bloqueo2.pass.getPassword();
            String passString = new String(pass);

        } catch (Exception e) {
            e.printStackTrace();
            id = -1;
        } finally {
            postgreSQL.desconectar();
        }

        return id;
    }

    /**
     * Enum de los estados de una venta.
     */
    public enum EstadoVenta {

        EnProceso(1),
        Finalizada(2),
        Pausada(3),
        Cancelada(4);

        int id;

        EstadoVenta(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.valueOf(this.id);
        }
    }

    /**
     * Enum de los tipos de pago.
     */
    public enum TipoMoneda {

        Efectivo(1),
        Debito(2),
        Credito(3),
        Cheque(4),
        CestaTicket(5);

        int id;

        TipoMoneda(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.valueOf(this.id);
        }

//        public int getIdMoneda(XBigDecimal value) {
//            return 1;
//        }
        //Provisional
        public int getIdMoneda() {
            return this.id;
        }
    }

    /**
     * Agrega un String[] a un StringBuilder, utiliza una coma ',' despues de
     * cada valor y añade el prefijo a cada uno. Ejemplo: <code> culumnas = {"nombre", "apellido"}; prefijo = "p."; query += "p.nombre, p.apellido,";
     * </code>
     *
     * @param columnas
     * @param prefijo
     * @param query
     * @return
     */
    private StringBuilder addColumnasAlQuery(String[] columnas, final String prefijo, StringBuilder query) {
        String as = "AS";
        for (int i = 0; i < columnas.length; i++) {
            String columna = columnas[i];
            query.append(prefijo).append(columna).append(',');
            if (columna.contains(as)) {
                String[] splitColumna = columna.split(as);
                columnas[i] = splitColumna[splitColumna.length - 1].trim();
            }
        }
        return query;
    }

    private int ejecutarManipulacionDeDatosSimpple(String query) {
        int resultado = -1;
        try {
            postgreSQL.conectar();
            resultado = postgreSQL.ejecutarManipulacionDeDatosSimpple(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return resultado;
    }

    public String direccionCliente(String cedula) {
        String direccion = "";
        try {
            postgreSQL.conectar();
            ResultSet result;
            String sql = "SELECT * FROM stpv.cliente WHERE cedula='" + cedula + "';";
            result = postgreSQL.ejecutarSelect(sql);
            if (result.next()) {
                direccion = result.getString("direccion");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postgreSQL.desconectar();
        }
        return direccion;
    }

    public List<reporte1> reimprimirfac(String codigo) {
        DecimalFormat redondeo = new DecimalFormat("0.00");
        List lista = new ArrayList();
        Empresa emp;
        ResultSet rs;
        emp = datosEmpresas();
        String sql = "SELECT DISTINCT CONCAT(c.nombre,' ',c.apellido) as nombre,c.cedula,c.direccion,"
                + "ven.codigo_factura,ven.total_exento,ven.total_no_exento,ven.iva,ven.total,ven.totalpag,ven.cambio,"
                + "pag.tipopago,pag.monto,vp.cantidad_producto, CONCAT (e.nombre,' ',e.apellido) as nombE,"
                + "p.descripcion,p.pvp,p.impuesto,(p.pvp*vp.cantidad_producto) as pt FROM stpv.venta ven "
                + "INNER JOIN stpv.venta__producto vp on ven.id=vp.venta_id "
                + "INNER JOIN inventario.producto p on p.id=vp.producto_id "
                + "INNER JOIN stpv.pago pag on pag.id_venta=ven.id "
                + "INNER JOIN stpv.cliente c on c.id=ven.cliente_id "
                + "INNER JOIN stpv.empleado e on e.id=ven.empleado_id "
                + "WHERE ven.codigo_factura='" + codigo + "';";

        try {
            postgreSQL.conectar();
            rs = postgreSQL.getSentencia().executeQuery(sql);
            while (rs.next()) {
                String pagado = rs.getString("pt");
                String codigofac = rs.getString("codigo_factura");
                String descrip = "";
                if (rs.getString("descripcion").length() > 13) {
                    for (int k = 0; k < rs.getString("descripcion").length(); k++) {
                        if (k % 13 == 0 && k != 0) {
                            descrip = descrip + rs.getString("descripcion").substring(0, k) + "<br/>" + rs.getString("descripcion").substring(k, rs.getString("descripcion").length());
                        }
                    }
                } else {
                    descrip = rs.getString("descripcion");
                }
                while (codigofac.length() < 10) {
                    codigofac = "0" + codigofac;
                }
                pagado = "" + redondeo.format(Double.parseDouble(pagado));
                if (rs.getString("impuesto").equals("0.00")) {
                    PuntoVenta.reporte1 rp = new PuntoVenta.reporte1(rs.getString("cantidad_producto"), descrip + " (E)", rs.getString("pvp"), pagado, rs.getString("total"), rs.getString("nombre"), rs.getString("cedula"), rs.getString("direccion"), codigofac, rs.getString("totalpag"), rs.getString("tipopago"), emp.getRif(), emp.getNombre(), emp.getDireccion(), emp.getTelefono(), emp.getMoneda(), rs.getString("total_exento"), rs.getString("total_no_exento"), rs.getString("iva"), rs.getString("cambio"), rs.getString("nombE"));
                    lista.add(rp);
                } else {
                    PuntoVenta.reporte1 rp = new PuntoVenta.reporte1(rs.getString("cantidad_producto"), descrip, rs.getString("pvp"), pagado, rs.getString("total"), rs.getString("nombre"), rs.getString("cedula"), rs.getString("direccion"), codigofac, rs.getString("totalpag"), rs.getString("tipopago"), emp.getRif(), emp.getNombre(), emp.getDireccion(), emp.getTelefono(), emp.getMoneda(), rs.getString("total_exento"), rs.getString("total_no_exento"), rs.getString("iva"), rs.getString("cambio"), rs.getString("nombE"));
                    lista.add(rp);
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ObjetoBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            postgreSQL.desconectar();
        }
        return lista;
    }

    public void crearCierre(String monto_fisico, String monto_sistema, String empleado, String fecha) {
        String sql = "INSERT INTO stpv.cierre_caja(monto_fisico,monto_sistema,empleado,fecha) "
                + "VALUES('" + monto_fisico + "','" + monto_sistema + "','" + empleado + "','" + fecha + "')";
        System.out.println(sql);
        try {
            postgreSQL.conectar();
            postgreSQL.getSentencia().execute(sql);
        } catch (Exception ex) {
            Logger.getLogger(ObjetoBaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            postgreSQL.desconectar();
        }

    }

}
