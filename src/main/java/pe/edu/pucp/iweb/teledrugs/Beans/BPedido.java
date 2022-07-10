package pe.edu.pucp.iweb.teledrugs.Beans;

public class BPedido {
    private int numeroOrden;
    private String fechaRecojo;
    private String estado;
    private String usuarioDni;

    public BPedido(int numeroOrden, String fechaRecojo, String estado, String usuarioDni) {
        this.numeroOrden = numeroOrden;
        this.fechaRecojo = fechaRecojo;
        this.estado = estado;
        this.usuarioDni = usuarioDni;
    }

    public BPedido() {
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(int numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getFechaRecojo() {
        return fechaRecojo;
    }

    public void setFechaRecojo(String fechaRecojo) {
        this.fechaRecojo = fechaRecojo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuarioDni() {
        return usuarioDni;
    }

    public void setUsuarioDni(String usuarioDni) {
        this.usuarioDni = usuarioDni;
    }

}
