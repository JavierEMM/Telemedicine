package pe.edu.pucp.iweb.teledrugs.Daos;

import pe.edu.pucp.iweb.teledrugs.Beans.BFarmacia;
import pe.edu.pucp.iweb.teledrugs.Beans.BPedido;
import pe.edu.pucp.iweb.teledrugs.Beans.BProducto;
import pe.edu.pucp.iweb.teledrugs.DTO.*;

import java.io.InputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FarmaciaDao extends BaseDao {

    public ArrayList<DTOBuscarProductoCliente> buscarProducto(String ruc, String nombre) {
        ArrayList<DTOBuscarProductoCliente> productos = new ArrayList<>();

        nombre = nombre.toLowerCase();

        String sql = "SELECT p.nombre,p.descripcion,p.foto,p.precio,p.idProducto FROM producto p INNER JOIN farmacia f ON f.ruc = p.farmacia_ruc WHERE f.ruc = ? AND lower(p.nombre) LIKE ?;";


        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,ruc);
            pstmt.setString(2,"%" + nombre + "%");

            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    DTOBuscarProductoCliente producto = new DTOBuscarProductoCliente();
                    producto.setNombre(rs.getString(1));
                    producto.setDescripcion(rs.getString(2));
                    producto.setFoto(rs.getBinaryStream(3));
                    producto.setPrecio(rs.getDouble(4));
                    producto.setIdProducto(rs.getInt(5));
                    productos.add(producto);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return productos;
    }





    public ArrayList<DTOPedidoHistorial> mostrarHistorialPedidos(){

        ArrayList<DTOPedidoHistorial> listaHistorialPedidos= new ArrayList<>();

        String sql ="select CONCAT(c.nombre,\" \",c.apellidos),c.dni,p.estado,p.fechaRecojo,p.codigoaleatorio from cliente c inner join pedidos p on (c.dni = p.usuarioDni) order by p.fechaRecojo;";

        try(Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                String nombreCompleto =rs.getString(1);
                String dni = rs.getString(2);
                String estado = rs.getString(3);
                String fechaRecojo = rs.getString(4);
                listaHistorialPedidos.add(new DTOPedidoHistorial(nombreCompleto,dni,estado,fechaRecojo));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listaHistorialPedidos;
    }
    public ArrayList<DTOPedidoHistorial> mostrarHistorialPedidos(String ruc){

        ArrayList<DTOPedidoHistorial> listaHistorialPedidos= new ArrayList<>();

        String sql ="select p.numeroOrden ,CONCAT(c.nombre,' ',c.apellidos),c.dni,p.estado,p.fechaRecojo,p.codigoaleatorio from cliente c \n" +
                "inner join pedidos p on (c.dni = p.usuarioDni) \n" +
                "inner join producto_tiene_pedidos pp on (p.numeroOrden = pp.pedidos_numeroOrden)\n" +
                "inner join producto pr on (pr.idProducto = pp.producto_idProducto)\n" +
                "inner join farmacia f on (f.ruc = pr.farmacia_ruc) \n" +
                "where f.ruc = ?\n" +
                "GROUP BY numeroOrden " +
                "ORDER BY p.numeroOrden DESC;";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1, ruc);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                int numeroOrden =rs.getInt(1);
                String nombreCompleto =rs.getString(2);
                String dni = rs.getString(3);
                String estado = rs.getString(4);
                String fechaRecojo = rs.getString(5);
                String codigoaleatorio = rs.getString(6);
                listaHistorialPedidos.add(new DTOPedidoHistorial(nombreCompleto,dni,estado,fechaRecojo, numeroOrden,codigoaleatorio));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listaHistorialPedidos;
    }

    public ArrayList<DTOPedidoHistorial> mostrarHistorialPedidosBuscador(String ruc, String texto){

        ArrayList<DTOPedidoHistorial> listaHistorialPedidos= new ArrayList<>();

        String sql ="select p.numeroOrden ,CONCAT(c.nombre,' ',c.apellidos),c.dni,p.estado,p.fechaRecojo,p.codigoaleatorio from cliente c\n" +
                "                inner join pedidos p on (c.dni = p.usuarioDni)\n" +
                "                inner join producto_tiene_pedidos pp on (p.numeroOrden = pp.pedidos_numeroOrden)\n" +
                "                inner join producto pr on (pr.idProducto = pp.producto_idProducto)\n" +
                "                inner join farmacia f on (f.ruc = pr.farmacia_ruc)\n" +
                "                where f.ruc = ? and (c.dni LIKE ? or p.codigoaleatorio LIKE ?) \n" +
                "GROUP BY numeroOrden                " +
                "ORDER BY p.numeroOrden DESC;";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1, ruc);
            pstmt.setString(2, "%"+texto + "%");
            pstmt.setString(3, "%"+texto + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                int numeroOrden =rs.getInt(1);
                String nombreCompleto =rs.getString(2);
                String dni = rs.getString(3);
                String estado = rs.getString(4);
                String fechaRecojo = rs.getString(5);
                String codigoaleatorio = rs.getString(6);
                listaHistorialPedidos.add(new DTOPedidoHistorial(nombreCompleto,dni,estado,fechaRecojo, numeroOrden,codigoaleatorio));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listaHistorialPedidos;
    }
    public ArrayList<DTOPedidoHistorial> mostrarHistorialPedidosBuscador_offset(String ruc, String texto, String offset){

        ArrayList<DTOPedidoHistorial> listaHistorialPedidos= new ArrayList<>();

        String sql ="select p.numeroOrden ,CONCAT(c.nombre,' ',c.apellidos),c.dni,p.estado,p.fechaRecojo,p.codigoaleatorio from cliente c\n" +
                "                inner join pedidos p on (c.dni = p.usuarioDni)\n" +
                "                inner join producto_tiene_pedidos pp on (p.numeroOrden = pp.pedidos_numeroOrden)\n" +
                "                inner join producto pr on (pr.idProducto = pp.producto_idProducto)\n" +
                "                inner join farmacia f on (f.ruc = pr.farmacia_ruc)\n" +
                "                where f.ruc = ? and (c.dni LIKE ? or p.codigoaleatorio LIKE ?) " +
                "                   GROUP BY numeroOrden " +
                "                ORDER BY p.numeroOrden DESC LIMIT 4 OFFSET ?;";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1, ruc);
            pstmt.setString(2, "%"+texto + "%");
            pstmt.setString(3,  "%"+texto + "%");
            int offset_num = (Integer.parseInt(offset)-1)*4;
            pstmt.setInt(4,offset_num);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int numeroOrden = rs.getInt(1);
                String nombreCompleto = rs.getString(2);
                String dni = rs.getString(3);
                String estado = rs.getString(4);
                String fechaRecojo = rs.getString(5);
                String codigoaleatorio = rs.getString(6);
                listaHistorialPedidos.add(new DTOPedidoHistorial(nombreCompleto, dni, estado, fechaRecojo, numeroOrden, codigoaleatorio));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listaHistorialPedidos;
    }

    public ArrayList<DTOPedidoHistorial> mostrarHistorialPedidos_offset(String ruc,String offset){

        ArrayList<DTOPedidoHistorial> listaHistorialPedidos= new ArrayList<>();

        String sql ="select p.numeroOrden ,CONCAT(c.nombre,' ',c.apellidos),c.dni,p.estado,p.fechaRecojo,p.codigoaleatorio,pr.descripcion from cliente c\n" +
                "                inner join pedidos p on (c.dni = p.usuarioDni)\n" +
                "                inner join producto_tiene_pedidos pp on (p.numeroOrden = pp.pedidos_numeroOrden)\n" +
                "                inner join producto pr on (pr.idProducto = pp.producto_idProducto)\n" +
                "                inner join farmacia f on (f.ruc = pr.farmacia_ruc)\n" +
                "                where f.ruc = ? " +
                "                GROUP BY numeroOrden " +
                "                ORDER BY p.numeroOrden DESC " +
                "                LIMIT 4 offset ?";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            int offset_num = (Integer.parseInt(offset)-1)*4;
            pstmt.setInt(2,offset_num);
            pstmt.setString(1, ruc);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                int numeroOrden =rs.getInt(1);
                String nombreCompleto =rs.getString(2);
                String dni = rs.getString(3);
                String estado = rs.getString(4);
                String fechaRecojo = rs.getString(5);
                String codigoaleatorio = rs.getString(6);
                listaHistorialPedidos.add(new DTOPedidoHistorial(nombreCompleto,dni,estado,fechaRecojo, numeroOrden,codigoaleatorio));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listaHistorialPedidos;
    }

    public ArrayList<DTOBuscarProductoCliente> buscarProducto_offset(String ruc, String nombre,String offset) {
        ArrayList<DTOBuscarProductoCliente> productos = new ArrayList<>();
        System.out.println(ruc + " " + nombre + " " + offset);

        nombre = nombre.toLowerCase();

        String sql = "SELECT p.foto,p.nombre,p.descripcion,p.stock,p.precio,p.idProducto FROM producto p " +
                "INNER JOIN farmacia f ON f.ruc = p.farmacia_ruc WHERE f.ruc = ? " +
                "AND lower(p.nombre) LIKE ? " +
                "order by p.nombre limit 4 offset ?";


        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,ruc);
            pstmt.setString(2,"%" + nombre + "%");

            int offset_num = (Integer.parseInt(offset)-1)*4;
            pstmt.setInt(3,offset_num);
            System.out.println("pstmt");
            //System.out.println(pstmt.getConnection().);

            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    System.out.println("next");
                    DTOBuscarProductoCliente producto = new DTOBuscarProductoCliente();
                    producto.setFoto(rs.getBinaryStream(1));
                    producto.setNombre(rs.getString(2));
                    producto.setDescripcion(rs.getString(3));
                    producto.setStock(rs.getString(4));
                    producto.setPrecio(rs.getDouble(5));
                    producto.setIdProducto(rs.getInt(6));
                    productos.add(producto);

                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables.getSQLState());
        }
        return productos;
    }


    public void confirmarEntrega (int id){
        String sqlUpdate = "UPDATE pedidos SET estado = 'Entregado' WHERE numeroOrden = ?;";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<DTODetallesPedido> listaSinRepetir(int idPedido){
        ArrayList<DTODetallesPedido> dtoDetallesPedidos = new ArrayList<>();
        String sql = "SELECT p.nombre,pt.cantidad,p.requiereReceta,pt.recetas,p.idProducto,pt.idProducto_has_Pedidoscol FROM producto p INNER JOIN producto_tiene_pedidos pt ON p.idProducto = pt.producto_idProducto WHERE pt.pedidos_numeroOrden = ? ;";
        try(Connection conn = this.getConnection();
        PreparedStatement pstmt =  conn.prepareStatement(sql);){
            pstmt.setInt(1,idPedido);
            try(ResultSet rs = pstmt.executeQuery();){
                while (rs.next()){
                    DTODetallesPedido dtoDetallesPedido = new DTODetallesPedido();
                    dtoDetallesPedido.setNombre(rs.getString(1));
                    dtoDetallesPedido.setStock(rs.getInt(2));
                    dtoDetallesPedido.setRequiereReceta(rs.getBoolean(3));
                    dtoDetallesPedido.setFoto(rs.getBinaryStream(4));
                    dtoDetallesPedido.setIdProducto(rs.getInt(5));
                    dtoDetallesPedido.setIdProducto_haspedido(rs.getInt(6));
                    dtoDetallesPedidos.add(dtoDetallesPedido);
                }
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return dtoDetallesPedidos;
    }


    public void cancelarPedido(int numeroOrden) {
        String sql = "UPDATE pedidos SET estado = 'Cancelado' WHERE (numeroOrden = ?);";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, numeroOrden);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DTOinservible> listaStockSumar(String idPedido){
        ArrayList<DTOinservible> datos = new ArrayList<>();
        String sqlSelect="SELECT producto_idProducto,sum(cantidad), p.stock FROM producto_tiene_pedidos pt INNER JOIN producto p ON pt.producto_idProducto=p.idProducto WHERE pedidos_numeroOrden = ? GROUP BY pt.producto_idProducto;";
        String sql="UPDATE producto SET stock = ? WHERE (idProducto = ?)";
        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlSelect)){
            pstmt.setString(1,idPedido);
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    DTOinservible dtOinservible = new DTOinservible();
                    dtOinservible.setCodigo(rs.getString(1));
                    dtOinservible.setCantidad(rs.getInt(2));
                    dtOinservible.setStock(rs.getString(3));
                    datos.add(dtOinservible);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datos;
    }

    public void sumarStock(ArrayList<DTOinservible> dtOinservibles){
        String sql ="UPDATE producto SET stock = ? WHERE (idProducto = ?)";
        for (DTOinservible dtOinservible : dtOinservibles){
            try(Connection conn = this.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int nuevoStock=Integer.parseInt(dtOinservible.getStock()) + dtOinservible.getCantidad();
                pstmt.setString(1,String.valueOf(nuevoStock));
                pstmt.setString(2,dtOinservible.getCodigo());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public String restarFechas(String fecha) {
        String horas = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fecha_actual= dtf5.format(LocalDateTime.now());
        try {
            java.util.Date d1 = df.parse(fecha_actual);
            java.util.Date d2 = df.parse(fecha);
            long diff = d2.getTime() - d1.getTime();
            long day = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - day * 24);
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            horas=String.valueOf(day*24 + hour + (min/60.0));
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return horas;
    }

    public ArrayList<BProducto> listarProductos(String ruc){

        ArrayList<BProducto> bProductos = new ArrayList<>();
        String sql ="SELECT p.* FROM producto p \n" +
                "INNER JOIN farmacia f ON f.ruc=p.farmacia_ruc\n" +
                "WHERE f.ruc = ? \n" +
                "ORDER BY p.nombre";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,ruc);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                int idProducto = rs.getInt(1);
                String nombre = rs.getString(2);
                String descripcion = rs.getString(3);
                String requiereReceta = rs.getString(4);
                InputStream foto = rs.getBinaryStream(5);
                String stock = rs.getString(6);
                double precio = rs.getDouble(7);
                String ruc2 = rs.getString(8);
                bProductos.add(new BProducto(idProducto,nombre,descripcion,foto,Integer.parseInt(stock),precio,ruc2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bProductos;
    }
    public ArrayList<BProducto> listarProductosPaginacion(String ruc,String offset){

        ArrayList<BProducto> bProductos = new ArrayList<>();
        String sql ="SELECT p.* FROM producto p\n" +
                "                INNER JOIN farmacia f ON f.ruc=p.farmacia_ruc\n" +
                "                WHERE f.ruc = ?\n" +
                "                ORDER BY p.nombre\n" +
                "                LIMIT 8 OFFSET ?;";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,ruc);
            int offset_num = (Integer.parseInt(offset)-1)*8;
            pstmt.setInt(2,offset_num);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                int idProducto = rs.getInt(1);
                String nombre = rs.getString(2);
                String descripcion = rs.getString(3);
                String requiereReceta = rs.getString(4);
                InputStream foto = rs.getBinaryStream(5);
                String stock = rs.getString(6);
                double precio = rs.getDouble(7);
                String ruc2 = rs.getString(8);
                bProductos.add(new BProducto(idProducto,nombre,descripcion,foto,Integer.parseInt(stock),precio,ruc2));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bProductos;
    }

    public ArrayList<BFarmacia> mostrarListaFarmacias(){

        ArrayList<BFarmacia> listaFarmacias = new ArrayList<>();
        String sql ="SELECT f.ruc, f.nombre, l.correo, f.distrito,f.pedidosPendientes,bloqueado,f.direccion FROM farmacia f\n" +
                "INNER JOIN credenciales l ON l.correo = f.logueo_correo\n" +
                "INNER JOIN producto p ON p.farmacia_ruc=f.ruc\n" +
                "LEFT JOIN producto_tiene_pedidos pt ON p.idProducto = pt.producto_idProducto\n" +
                "GROUP BY f.ruc " +
                "ORDER BY f.distrito;";

        try(Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();){

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                String ruc = rs.getString(1);
                String nombre = rs.getString(2);
                String correo = rs.getString(3);
                String distrito =  rs.getString(4);
                String pedidosPendientes1 = rs.getString(5);
                String bloqueado= rs.getString(6);
                String direccion = rs.getString(7);
                BFarmacia f = new BFarmacia(ruc, nombre,correo, distrito,bloqueado,pedidosPendientes1,direccion);
                listaFarmacias.add(f);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listaFarmacias;
    }

    public ArrayList<BFarmacia> mostrarListaFarmacias(String hola){

        ArrayList<BFarmacia> listaFarmacias = new ArrayList<>();
        String sql ="SELECT f.ruc, f.nombre, l.correo, f.distrito,f.pedidosPendientes,bloqueado,f.direccion FROM farmacia f\n" +
                "INNER JOIN credenciales l ON l.correo = f.logueo_correo\n" +
                "INNER JOIN producto p ON p.farmacia_ruc=f.ruc\n" +
                "LEFT JOIN producto_tiene_pedidos pt ON p.idProducto = pt.producto_idProducto\n" +
                "GROUP BY f.ruc " +
                "order by case when f.distrito like ? then 0 else 1 end, f.distrito;";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,hola);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String ruc = rs.getString(1);
                String nombre = rs.getString(2);
                String correo = rs.getString(3);
                String distrito =  rs.getString(4);
                String pedidosPendientes1 = rs.getString(5);
                String bloqueado= rs.getString(6);
                String direccion = rs.getString(7);
                BFarmacia f = new BFarmacia(ruc, nombre,correo, distrito,bloqueado,pedidosPendientes1,direccion);
                listaFarmacias.add(f);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listaFarmacias;
    }

    public ArrayList<BFarmacia> mostrarListaFarmaciasTotal(){

        ArrayList<BFarmacia> listaFarmacias = new ArrayList<>();
        String sql ="SELECT f.ruc, f.nombre, f.logueo_correo, f.distrito,f.pedidosPendientes,f.bloqueado,f.direccion from farmacia f";
        try(Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();){

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                String ruc = rs.getString(1);
                String nombre = rs.getString(2);
                String correo = rs.getString(3);
                String distrito =  rs.getString(4);
                String pedidosPendientes1 = rs.getString(5);
                String bloqueado= rs.getString(6);
                String direccion = rs.getString(7);
                BFarmacia f = new BFarmacia(ruc, nombre,correo, distrito,bloqueado,pedidosPendientes1,direccion);
                listaFarmacias.add(f);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listaFarmacias;
    }


    public BFarmacia mostrarFarmacia(String ruc){

        BFarmacia bFarmacia = null;
        String sql ="SELECT f.ruc, f.nombre, l.correo, f.distrito,f.pedidosPendientes,bloqueado,f.direccion FROM farmacia f\n" +
                "INNER JOIN credenciales l ON l.correo = f.logueo_correo\n" +
                "INNER JOIN producto p ON p.farmacia_ruc=f.ruc\n" +
                "LEFT JOIN producto_tiene_pedidos pt ON p.idProducto = pt.producto_idProducto\n" +
                "WHERE f.ruc = ? " +
                "GROUP BY f.ruc ";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,ruc);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String ruc2 = rs.getString(1);
                String nombre = rs.getString(2);
                String correo = rs.getString(3);
                String distrito =  rs.getString(4);
                String pedidosPendientes1 = rs.getString(5);
                String bloqueado= rs.getString(6);
                String direccion = rs.getString(7);
                bFarmacia = new BFarmacia(ruc2, nombre,correo, distrito,bloqueado,pedidosPendientes1,direccion);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bFarmacia;
    }
    public BFarmacia mostrarFarmaciaCorreo(String correo){

        BFarmacia bFarmacia = null;
        String sql ="SELECT f.ruc, f.nombre, f.logueo_correo, f.distrito,f.pedidosPendientes,bloqueado,f.direccion FROM farmacia f WHERE f.logueo_correo = ?";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,correo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String ruc2 = rs.getString(1);
                String nombre = rs.getString(2);
                String distrito =  rs.getString(4);
                String pedidosPendientes1 = rs.getString(5);
                String bloqueado= rs.getString(6);
                String direccion = rs.getString(7);
                bFarmacia = new BFarmacia(ruc2, nombre,correo, distrito,bloqueado,pedidosPendientes1,direccion);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bFarmacia;
    }

    public void insertarFarmacia(BFarmacia farmacia, String contrasena){

        CredencialesDao credenciales = new CredencialesDao();
        credenciales.registrarFarmacia(farmacia.getCorreo(),contrasena);

        //SI RECIEN SE ESTA CREANDO NO PUEDE TENER PEDIDOS PENDIENTES
        String sentenciaSQL = "INSERT INTO farmacia(ruc,nombre,direccion,distrito,bloqueado,pedidosPendientes,logueo_correo,fotos)\n" +
                "VALUES(?,?,?,?,'Falso','No',?,?)";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sentenciaSQL)){


            pstmt.setString(1,farmacia.getRuc());
            pstmt.setString(2,farmacia.getNombre());
            pstmt.setString(3,farmacia.getDireccion());
            pstmt.setString(4, farmacia.getDistrito());
            pstmt.setString(5,farmacia.getCorreo());
            pstmt.setBlob(6,farmacia.getFotos());
            pstmt.executeUpdate();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public boolean existeFarmacia(String RUC){


        boolean existeFarmacia = false;

        String sentenciaSQL = "SELECT * FROM mydb.farmacia where ruc=?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sentenciaSQL)){
            pstmt.setString(1,RUC);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                existeFarmacia = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return existeFarmacia;
    }

    public boolean bloquearFarmacia(String rucStr) throws SQLException {
        String sqlUpdate = "UPDATE farmacia f SET f.bloqueado ='Verdadero' WHERE f.ruc = ?";
        String sqlBusqueda ="SELECT f.pedidosPendientes FROM farmacia f WHERE f.ruc = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBusqueda)){
            pstmt.setString(1,rucStr);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            String pendientes =  rs.getString(1);
            if(pendientes.equalsIgnoreCase("no")){
                try(PreparedStatement pstmt2 = conn.prepareStatement(sqlUpdate)){
                    pstmt2.setString(1,rucStr);
                    pstmt2.executeUpdate();
                    return true;
                }
            }else{
                System.out.println("No se puede bloquear porque tiene pedidos pendientes");
            }
        }
        return false;
    }

    public ArrayList<BFarmacia> mostrarListaFarmacias_offset(String offset){

        ArrayList<BFarmacia> listaFarmacias = new ArrayList<>();
        String sql ="SELECT f.ruc, f.nombre, l.correo, f.distrito,f.pedidosPendientes,f.bloqueado,f.direccion FROM farmacia f\n" +
                "INNER JOIN credenciales l ON l.correo = f.logueo_correo\n" +
                "GROUP BY f.ruc LIMIT 4 OFFSET ?;";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            int offset_num = (Integer.parseInt(offset)-1)*4;
            pstmt.setInt(1,offset_num);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String ruc = rs.getString(1);
                String nombre = rs.getString(2);
                String correo = rs.getString(3);
                String distrito =  rs.getString(4);
                String pedidosPendientes1 = rs.getString(5);
                String bloqueado= rs.getString(6);
                String direccion = rs.getString(7);
                BFarmacia f = new BFarmacia(ruc, nombre,correo, distrito,bloqueado,pedidosPendientes1,direccion);
                listaFarmacias.add(f);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listaFarmacias;
    }



    public ArrayList<BFarmacia> listaFarmaciasPorBusqueda(String opcion,Integer offset){

        ArrayList<BFarmacia> listaFarmacias = new ArrayList<>();

        String sentenciaSQL = "SELECT f.ruc, f.nombre, l.correo, f.distrito,f.pedidosPendientes,f.bloqueado,f.direccion FROM farmacia f\n"+
                "INNER JOIN credenciales l ON l.correo = f.logueo_correo\n"+
                "GROUP BY f.ruc\n"+
                "HAVING lower(f.nombre) LIKE ?\n"+
                "LIMIT 4 OFFSET ?;";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sentenciaSQL)){
            int offset_num = (offset-1)*4;
            pstmt.setInt(2,offset_num);
            pstmt.setString(1,"%" + opcion + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                String ruc = rs.getString(1);
                String nombre = rs.getString(2);
                String correo = rs.getString(3);
                String distrito =  rs.getString(4);
                String pedidosPendientes1 = rs.getString(5);
                String bloqueado= rs.getString(6);
                String direccion = rs.getString(7);
                BFarmacia f = new BFarmacia(ruc, nombre,correo, distrito,bloqueado,pedidosPendientes1,direccion);
                listaFarmacias.add(f);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return listaFarmacias;

    }

    public  boolean primerLoginFarmacia(String correo){
        boolean a = false;
        String sql ="Select primerLogin  from farmacia where logueo_correo= ?";
        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,correo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                a = rs.getBoolean(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return a;
    }

    public void actualizarLogin (String correo){
        String sql = "UPDATE farmacia SET primerLogin = 0 WHERE logueo_correo = ?;";
        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,correo);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    public boolean emailisValid(String email) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public boolean rucValid(String ruc) {
        String regex = "^[0-9]{11,11}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ruc);
        return matcher.find();
    }
    public boolean pedidosPendientes(String dni) {
        String regex = "^[0-1]{1,1}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dni);
        return matcher.find();
    }

    public boolean contrasenaisValid(String contrasenia) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contrasenia);
        return matcher.find();
    }







}
