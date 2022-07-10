package pe.edu.pucp.iweb.teledrugs.DTO;

public class DTOProductoBorrar {

    private String nombreProducto;
    private String estadoPedido;
    private String numeroOrden;
    public  DTOProductoBorrar(){}
    public  DTOProductoBorrar(String nombreProducto,String estadoPedido, String numeroOrden){
        this.nombreProducto = nombreProducto;
        this.estadoPedido = estadoPedido;
        this.numeroOrden = numeroOrden;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }
}


