package pe.edu.pucp.iweb.teledrugs.Beans;

import java.io.InputStream;

public class BProducto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private boolean requiereReceta;
    private InputStream foto;
    private int stock;
    private double precio;
    private String farmaciaRUC;

    public BProducto() {
    }

    public BProducto(int idProducto, String nombre, String descripcion, InputStream foto, int stock, double precio, String farmaciaRUC) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.stock = stock;
        this.precio = precio;
        this.farmaciaRUC = farmaciaRUC;
    }



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

}
