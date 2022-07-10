package pe.edu.pucp.iweb.teledrugs.Daos;

import pe.edu.pucp.iweb.teledrugs.Beans.BProducto;
import pe.edu.pucp.iweb.teledrugs.DTO.DTOProductoBorrar;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductoDao extends BaseDao {
    public void inserta_producto(String nombre, double precio, int stock, String descripcion, boolean requiereReceta, String farmacia_ruc, InputStream foto) {
        String sqlInsert = "INSERT INTO mydb.producto (nombre,precio,stock,descripcion, requiereReceta,farmacia_ruc,foto)\n" +
                "VALUES(?,?,?,?,?,?,?)";
        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlInsert);){
            pstmt.setString(1, nombre);
            pstmt.setDouble(2, precio);
            pstmt.setInt(3, stock);
            pstmt.setString(4, descripcion);
            pstmt.setBoolean(5, requiereReceta);
            pstmt.setString(6, farmacia_ruc);
            pstmt.setBlob(7, foto);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void editarProducto(double precio, int stock, String descripcion, boolean requiereReceta, String idProducto, InputStream foto) {
        String sqlInsert = "UPDATE mydb.producto SET descripcion = ?, requiereReceta = ?, stock = ?, precio = ?,foto = ? WHERE (idProducto = ?);";
        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlInsert);){
            pstmt.setDouble(4, precio);
            pstmt.setInt(3, stock);
            pstmt.setString(1, descripcion);
            pstmt.setBoolean(2, requiereReceta);
            pstmt.setString(6,idProducto);
            pstmt.setBlob(5,foto);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void listarImg(String id, HttpServletResponse response){
        String sql="SELECT foto FROM producto WHERE idProducto = ?";
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            outputStream=response.getOutputStream();
            pstmt.setString(1,id);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    inputStream=rs.getBinaryStream(1);
                }
            }
            bufferedInputStream= new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            int i =0;
            while ((i=bufferedInputStream.read())!=-1){
                bufferedOutputStream.write(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void listarImgProductoReceta(String id, HttpServletResponse response){
        String sql="SELECT recetas FROM producto_tiene_pedidos WHERE idProducto_has_Pedidoscol = ?";
        InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            outputStream=response.getOutputStream();
            pstmt.setString(1,id);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    inputStream=rs.getBinaryStream(1);
                }
            }
            bufferedInputStream= new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            int i =0;
            while ((i=bufferedInputStream.read())!=-1){
                bufferedOutputStream.write(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public ArrayList<BProducto> mostrarListaProductos_offset(String offset,String rucFarmacia){
        ArrayList<BProducto> listaProductos = new ArrayList<>();
        System.out.println(rucFarmacia);
        String sql = "select producto.foto,producto.idproducto,producto.nombre,producto.descripcion,producto.stock,producto.precio,producto.farmacia_ruc from producto " +
                "join farmacia on producto.farmacia_ruc = farmacia.ruc " +
                "where producto.farmacia_ruc = ? " +
                "limit 4 offset ? ";

        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            int offset_num = (Integer.parseInt(offset)-1)*4;
            pstmt.setInt(2,offset_num);
            pstmt.setString(1,rucFarmacia);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                InputStream fotoProducto = rs.getBinaryStream(1);
                int idProducto = rs.getInt(2);
                String nombreProducto = rs.getString(3);
                String descProducto = rs.getString(4);
                int stockProducto = rs.getInt(5);
                String precioProductoStr = rs.getString(6);
                double precioProducto = Double.parseDouble(precioProductoStr);
                listaProductos.add(new BProducto(idProducto, nombreProducto, descProducto, fotoProducto, stockProducto, precioProducto, rucFarmacia));

            }

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return listaProductos;
    }




    // FUNCION ELIMINAR PRODUCTO
    public void eliminarProducto(int idProducto, String farmaciaRuc) throws SQLException {
        String sql= "DELETE FROM `mydb`.`producto` WHERE (`idProducto` = ?);";
        try(Connection conn = this.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,idProducto);
            pstmt.executeUpdate();
        }
    }

    public ArrayList<BProducto> ProductosPorFarmacia(String rucFarmacia){
        ArrayList<BProducto> listaProdxFarm = new ArrayList<>();
        String sql = "select producto.foto,producto.idproducto,producto.nombre,producto.descripcion,producto.stock,producto.precio,producto.farmacia_ruc from producto " +
                "join farmacia on producto.farmacia_ruc = farmacia.ruc " +
                "where farmacia_ruc = ?";
        try(Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,rucFarmacia);
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    InputStream fotoProducto = rs.getBinaryStream(1);
                    int idProducto = rs.getInt(2);
                    String nombreProducto = rs.getString(3);
                    String descProducto = rs.getString(4);
                    int stockProducto = rs.getInt(5);
                    String precioProductoStr = rs.getString(6);
                    double precioProducto = Double.parseDouble(precioProductoStr);
                    listaProdxFarm.add(new BProducto(idProducto, nombreProducto, descProducto, fotoProducto, stockProducto, precioProducto, rucFarmacia));
                }
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
            throwables.getSQLState();
        }
        return listaProdxFarm;
    }

    public BProducto obtenerProducto(String idProducto) {
        BProducto producto = null;

        String sql = "select * from producto where idProducto = ?";

        try (Connection connection = this.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setInt(1,Integer.parseInt(idProducto));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    producto = new BProducto();
                    producto.setIdProducto(Integer.parseInt(idProducto));
                    producto.setNombre(rs.getString(2));
                    producto.setDescripcion(rs.getString(3));
                    producto.setRequiereReceta(rs.getBoolean(4));
                    producto.setFoto(rs.getBinaryStream(5));
                    producto.setStock(rs.getInt(6));
                    producto.setPrecio(rs.getDouble(7));
                    producto.setFarmaciaRUC(rs.getString(8));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producto;
    }
    public void setStocktoZero(String idProducto){
        String sql="UPDATE mydb.producto SET stock = 0 WHERE (idProducto = ?);";
        try(Connection conn = this.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,idProducto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DTOProductoBorrar> productoEnPedido(String idProducto){
        ArrayList<DTOProductoBorrar> dtoProductoBorrars = new ArrayList<>();
        String sql="SELECT p.nombre,pe.numeroOrden, pe.estado FROM producto p INNER JOIN producto_tiene_pedidos pt ON pt.producto_idProducto = p.idProducto INNER JOIN pedidos pe ON pt.pedidos_numeroOrden = pe.numeroOrden WHERE p.idProducto = ?;";
        try(Connection conn = this.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,Integer.parseInt(idProducto));
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    DTOProductoBorrar dtoProductoBorrar = new DTOProductoBorrar();
                    dtoProductoBorrar.setNombreProducto(rs.getString(1));
                    dtoProductoBorrar.setEstadoPedido(rs.getString(3));
                    dtoProductoBorrar.setNumeroOrden(String.valueOf(rs.getInt(2)));
                    dtoProductoBorrars.add(dtoProductoBorrar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dtoProductoBorrars;
    }


}