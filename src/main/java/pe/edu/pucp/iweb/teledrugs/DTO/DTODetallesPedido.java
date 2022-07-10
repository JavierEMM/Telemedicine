package pe.edu.pucp.iweb.teledrugs.DTO;

import java.io.InputStream;

public class DTODetallesPedido {
    private int idProducto_haspedido;
    private int idProducto;
    private String nombre;
    private String descripcion;
    private boolean requiereReceta;
    private InputStream foto;

    public DTODetallesPedido() {

    }

    private int stock;
    private double precio;

    public DTODetallesPedido(int idProducto_haspedido, int idProducto, String nombre, boolean requiereReceta, int stock) {
        this.idProducto_haspedido = idProducto_haspedido;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.requiereReceta = requiereReceta;
        this.stock = stock;
    }

    private String farmaciaRUC;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isRequiereReceta() {
        return requiereReceta;
    }

    public void setRequiereReceta(boolean requiereReceta) {
        this.requiereReceta = requiereReceta;
    }

    public InputStream getFoto() {
        return foto;
    }

    public void setFoto(InputStream foto) {
        this.foto = foto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFarmaciaRUC() {
        return farmaciaRUC;
    }

    public void setFarmaciaRUC(String farmaciaRUC) {
        this.farmaciaRUC = farmaciaRUC;
    }

    public int getIdProducto_haspedido() {
        return idProducto_haspedido;
    }

    public void setIdProducto_haspedido(int idProducto_haspedido) {
        this.idProducto_haspedido = idProducto_haspedido;
    }
}
