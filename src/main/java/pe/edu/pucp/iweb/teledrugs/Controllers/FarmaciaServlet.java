package pe.edu.pucp.iweb.teledrugs.Controllers;

import pe.edu.pucp.iweb.teledrugs.Beans.BFarmacia;
import pe.edu.pucp.iweb.teledrugs.Beans.BPedido;
import pe.edu.pucp.iweb.teledrugs.Beans.BProducto;
import pe.edu.pucp.iweb.teledrugs.DTO.*;
import pe.edu.pucp.iweb.teledrugs.Daos.FarmaciaDao;
import pe.edu.pucp.iweb.teledrugs.Daos.ProductoDao;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "FarmaciaServlet", value = "/FarmaciaPrincipal")
@MultipartConfig
public class FarmaciaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "listarProductos" : request.getParameter("action");
        ProductoDao productoDao =new ProductoDao();
        FarmaciaDao farmaciaDao = new FarmaciaDao();
        HttpSession session = request.getSession();
        RequestDispatcher view;
        System.out.println(action);
        BFarmacia bFarmacia = session.getAttribute("farmacia") == null ? new BFarmacia() : (BFarmacia) session.getAttribute("farmacia");
        String offset = request.getParameter("offset") != null ? request.getParameter("offset") : "1";
        switch (action){
            case "listarProductos":
                String rucFarmacia = bFarmacia.getRuc();
                ArrayList<BProducto> listaProductos_offset = productoDao.mostrarListaProductos_offset(offset,rucFarmacia);
                ArrayList<BProducto> listaProductos = productoDao.ProductosPorFarmacia(rucFarmacia);
                session.setAttribute("listaProductos",listaProductos_offset);
                request.setAttribute("pag",Integer.parseInt(offset));

                int index;
                if(listaProductos.size() % 4 == 0){
                    index=listaProductos.size()/4;
                }else{
                    index=1+(listaProductos.size()/4);
                }
                request.setAttribute("index",index);
                view = request.getRequestDispatcher("/FlujoFarmacia/ListaProductos.jsp");
                view.forward(request,response);
                break;


            case "listarPedidos":

                //response.setContentType("text/html");
                int index1=0;
                String rucFarmacia1 = bFarmacia.getRuc(); //"27207510101";
                ArrayList<DTOPedidoHistorial> listaHistorialPedidos = farmaciaDao.mostrarHistorialPedidos(rucFarmacia1);
                ArrayList<DTOPedidoHistorial> listaHistorialPedidos2 = farmaciaDao.mostrarHistorialPedidos_offset(rucFarmacia1,offset);
                session.setAttribute("listaHistorialPedidos",listaHistorialPedidos2);
                if(listaHistorialPedidos.size() % 4 == 0){
                    index1 = listaHistorialPedidos.size()/4;
                }else{
                    index1=1+(listaHistorialPedidos.size()/4);
                }
                request.setAttribute("index",index1);
                request.setAttribute("pag",Integer.parseInt(offset));
                //request.setAttribute("ruc",ruc);
                view = request.getRequestDispatcher("/FlujoFarmacia/HistorialPedidos.jsp");
                view.forward(request,response);
                break;

            case "Buscar":
                String texto = (String) session.getAttribute("texto");
                rucFarmacia = bFarmacia.getRuc();
                ArrayList<DTOBuscarProductoCliente> productos = farmaciaDao.buscarProducto_offset(rucFarmacia,texto,offset);
                ArrayList<DTOBuscarProductoCliente> productos1 = farmaciaDao.buscarProducto(rucFarmacia,texto);
                if(productos1.size() % 4 == 0){
                    index = productos1.size()/4;
                }else{
                    index = 1 + (productos1.size()/4);
                }
                request.setAttribute("pag",Integer.parseInt(offset));
                request.setAttribute("index", index);
                request.setAttribute("productos", productos);

                RequestDispatcher view10 = request.getRequestDispatcher("/FlujoFarmacia/Buscador.jsp");
                view10.forward(request,response);
                break;
            case "BuscarPedido":
                int index2=0;
                String texto1 = (String) session.getAttribute("texto");
                rucFarmacia = bFarmacia.getRuc();
                ArrayList<DTOPedidoHistorial> pedidos = farmaciaDao.mostrarHistorialPedidosBuscador_offset(rucFarmacia,texto1,offset);
                ArrayList<DTOPedidoHistorial> pedidos1 = farmaciaDao.mostrarHistorialPedidosBuscador(rucFarmacia,texto1);
                if(pedidos1.size() % 4 == 0){
                    index2 = pedidos1.size()/4;
                }else{
                    index2 = 1 + (pedidos1.size()/4);
                }
                for(DTOPedidoHistorial pedidoHistorial : pedidos){
                    System.out.println(pedidoHistorial.getCodigoAleatorio());
                }
                request.setAttribute("pag",Integer.parseInt(offset));
                request.setAttribute("index", index2);
                session.setAttribute("listaHistorialPedidos", pedidos);

                RequestDispatcher view12 = request.getRequestDispatcher("/FlujoFarmacia/BuscadorHistorial.jsp");
                view12.forward(request,response);
                break;
            case "crear":
                view=request.getRequestDispatcher("/FlujoFarmacia/AgregarProducto.jsp");
                view.forward(request,response);
                break;
            case "update":
                view=request.getRequestDispatcher("/FlujoFarmacia/EditarProducto.jsp");
                view.forward(request,response);
                break;

            case "editarProducto":
                String id = (String) session.getAttribute("idProducto");
                BProducto producto= productoDao.obtenerProducto(id);
                if(producto!=null){
                    session.setAttribute("producto",producto);
                    view = request.getRequestDispatcher("/FlujoFarmacia/EditarProducto.jsp");
                    view.forward(request, response);
                }else {
                    response.sendRedirect(request.getContextPath()+"/FarmaciaPrincipal");
                }
                break;
            case "mostrarDetalles":
                view = request.getRequestDispatcher("/FlujoFarmacia/Detalles.jsp");
                view.forward(request,response);
                break;
            case "logout":
                session.invalidate();
                response.sendRedirect(request.getContextPath());
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action") != null ? request.getParameter("action") : "crear";
        ProductoDao productoDao = new ProductoDao();
        FarmaciaDao farmaciaDao = new FarmaciaDao();
        BFarmacia bFarmacia = session.getAttribute("farmacia") == null ? new BFarmacia() : (BFarmacia) session.getAttribute("farmacia");
        RequestDispatcher view;
        switch (action) {
            case "crear":
                Part foto = request.getPart("file");
                InputStream inputStream = foto.getInputStream();
                String nombre = request.getParameter("nombre") != null ? request.getParameter("nombre") : "";
                String precioStr = request.getParameter("precio") != null ? request.getParameter("precio") : "";
                String stockStr = request.getParameter("stock") != null ? request.getParameter("stock") : "";
                String descripcion = request.getParameter("descripcion") != null ? request.getParameter("descripcion") : "";
                String requiere = request.getParameter("requiere") != null ? request.getParameter("requiere") : "";

                if(!nombre.equalsIgnoreCase("")){
                    if(!descripcion.equalsIgnoreCase("")){
                        if(!stockStr.equalsIgnoreCase("")){
                            if (!precioStr.equalsIgnoreCase("")){
                                if(foto.getInputStream().available() != 0){
                                    if(foto.getContentType().startsWith("image/")){
                                        double precio = Double.parseDouble(precioStr);
                                        int stock = Integer.parseInt(stockStr);
                                        boolean validar=false;
                                        if (requiere.equalsIgnoreCase("1")){
                                            validar=true;
                                        }
                                        if(stock >= 0){
                                            if(precio >= 0){
                                                session.setAttribute("part",foto);
                                                productoDao.inserta_producto(nombre, precio, stock, descripcion, validar, bFarmacia.getRuc(),inputStream);
                                                response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal");
                                            }else{
                                                session.setAttribute("msg","Al agregar un producto el precio no puede ser nulo o negativo");
                                                response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=crear");
                                            }
                                        }else{
                                            session.setAttribute("msg","Al agregar un producto el stock no puede ser nulo o negativo");
                                            response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=crear");
                                        }
                                    }else{
                                        session.setAttribute("msg","El tipo de archivo subido no corresponde a una imagen");
                                        response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=crear");
                                    }

                                }else{
                                    session.setAttribute("msg","La foto es necesaria");
                                    response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=crear");
                                }
                            }else{
                                session.setAttribute("msg","Al agregar un producto no puede dejar el precio vacio");
                                response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=crear");
                            }
                        }else{
                            session.setAttribute("msg","Al agregar un producto el stock no puede ser vacio");
                            response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=crear");
                        }
                    }else{
                        session.setAttribute("msg","Al agregar un producto la descripcion no puede ser vacia");
                        response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=crear");
                    }
                }else{
                    session.setAttribute("msg", "Al agregar un producto el nombre no puede estar vacio");
                    response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=crear");
                }
                break;
            case "update":
                Part foto1 = request.getPart("file");
                InputStream inputStream1 = foto1.getInputStream();
                BProducto producto= (BProducto) session.getAttribute("producto");
                System.out.println(inputStream1.available());
                if(inputStream1.available() != 0){
                    String precioStr2 = request.getParameter("precio") != null ? request.getParameter("precio") : "";
                    String stockStr2 = request.getParameter("stock") != null ? request.getParameter("stock") : "";
                    String descripcion2 = request.getParameter("descripcion") != null ? request.getParameter("descripcion") : "";
                    String requiere2 = request.getParameter("requiere") != null ? request.getParameter("requiere") : "";
                    System.out.println("Precio: " + precioStr2);
                    System.out.println("Stock: " + stockStr2);
                    System.out.println("Descripcion: "+descripcion2);
                    System.out.println("requiere: "+requiere2);
                    if(!descripcion2.equalsIgnoreCase("")){
                        if(!stockStr2.equalsIgnoreCase("")){
                            if (!precioStr2.equalsIgnoreCase("")){
                                if(foto1.getContentType().startsWith("image/")){
                                    double precio2 = Double.parseDouble(precioStr2);
                                    int stock2 = Integer.parseInt(stockStr2);
                                    boolean validar2=false;
                                    if (requiere2.equalsIgnoreCase("1")){
                                        validar2=true;
                                    }
                                    if(stock2 >= 0){
                                        if(precio2 > 0){
                                            System.out.println("ENTRA AQUI");
                                            productoDao.editarProducto(precio2, stock2, descripcion2, validar2, String.valueOf(producto.getIdProducto()),inputStream1);
                                            response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal");
                                        }else{
                                            session.setAttribute("msg","Al editar un producto el precio no puede ser nulo o negativo");
                                            response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                                        }
                                    }else{
                                        session.setAttribute("msg","Al editar un producto el stock no puede ser nulo o negativo");
                                        response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                                    }
                                }else{
                                    session.setAttribute("msg","El tipo de archivo subido no corresponde a una imagen");
                                    response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                                }
                            }else{
                                session.setAttribute("msg","Al editar un producto no puede dejar el precio vacio");
                                response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                            }
                        }else{
                            session.setAttribute("msg","Al editar un producto el stock no puede ser vacio");
                            response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                        }
                    }else{
                        session.setAttribute("msg","Al editar un producto la descripcion no puede ser vacia");
                        response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                    }

                }else{

                    String precioStr2 = request.getParameter("precio") != null ? request.getParameter("precio") : "";
                    String stockStr2 = request.getParameter("stock") != null ? request.getParameter("stock") : "";
                    String descripcion2 = request.getParameter("descripcion") != null ? request.getParameter("descripcion") : "";
                    String requiere2 = request.getParameter("requiere") != null ? request.getParameter("requiere") : "";
                    System.out.println("Precio: " + precioStr2);
                    System.out.println("Stock: " + stockStr2);
                    System.out.println("Descripcion: "+descripcion2);
                    System.out.println("requiere: "+requiere2);

                    if(!descripcion2.equalsIgnoreCase("")){
                        if(!stockStr2.equalsIgnoreCase("")){
                            if (!precioStr2.equalsIgnoreCase("")){
                                double precio2 = Double.parseDouble(precioStr2);
                                int stock2 = Integer.parseInt(stockStr2);
                                boolean validar2=false;
                                if (requiere2.equalsIgnoreCase("1")){
                                    validar2=true;
                                }
                                if(stock2 >= 0){
                                    if(precio2 > 0){
                                        System.out.println("ENTRA POR AQUI");
                                        System.out.println(bFarmacia.getRuc());
                                        productoDao.editarProducto(precio2, stock2, descripcion2, validar2, String.valueOf(producto.getIdProducto()),producto.getFoto());
                                        response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal");
                                    }else{
                                        session.setAttribute("msg","Al editar un producto el precio no puede ser nulo o negativo");
                                        response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                                    }
                                }else{
                                    session.setAttribute("msg","Al editar un producto el stock no puede ser nulo o negativo");
                                    response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                                }
                            }else{
                                session.setAttribute("msg","Al editar un producto no puede dejar el precio vacio");
                                response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                            }
                        }else{
                            session.setAttribute("msg","Al editar un producto el stock no puede ser vacio");
                            response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                        }
                    }else{
                        session.setAttribute("msg","Al editar un producto la descripcion no puede ser vacia");
                        response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=update");
                    }
                }

                break;
            case "Buscar":
                String texto = request.getParameter("search");
                if (texto.equalsIgnoreCase("")) {
                    System.out.println("vuelve");
                    response.sendRedirect(request.getContextPath()+"/FarmaciaPrincipal");
                } else {
                    session.setAttribute("texto", texto);
                    System.out.println("llega aqui para get");
                    response.sendRedirect(request.getContextPath()+"/FarmaciaPrincipal?action=Buscar");
                }
                break;
            case "BuscarPedido":
                texto = request.getParameter("search");
                if (texto.equalsIgnoreCase("")) {
                    System.out.println("vuelve");
                    response.sendRedirect(request.getContextPath()+"/FarmaciaPrincipal?action=listarPedidos");
                } else {
                    session.setAttribute("texto", texto);
                    System.out.println("llega aqui para get");
                    response.sendRedirect(request.getContextPath()+"/FarmaciaPrincipal?action=BuscarPedido");
                }
                break;

            case "editarProducto":
                String id=request.getParameter("idProducto");
                session.setAttribute("idProducto",id);
                response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal?action=editarProducto");
                break;
            case "eliminar":
                String id1 = request.getParameter("idProducto");
                try {
                    productoDao.eliminarProducto(Integer.parseInt(id1), bFarmacia.getRuc());
                    session.setAttribute("msg","El producto no se encuentra en ningun pedido, se elimina de la base de datos");
                    response.sendRedirect(request.getContextPath() + "/FarmaciaPrincipal");
                } catch (SQLException e) {
                    ArrayList<DTOProductoBorrar> dtoProductoBorrars = productoDao.productoEnPedido(id1);
                    boolean bandera = true;
                    for(DTOProductoBorrar dtoProductoBorrar : dtoProductoBorrars){
                        System.out.println(dtoProductoBorrar.getEstadoPedido());
                        if(dtoProductoBorrar.getEstadoPedido().equalsIgnoreCase("pendiente")){
                            bandera = false;
                        }
                    }
                    if(bandera){
                        productoDao.setStocktoZero(id1);
                        session.setAttribute("msg","El producto se encuentra en historial de pedidos, pero no tiene pedidos pendientes, el stock ahora es 0");
                        response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal");
                    }else{
                        productoDao.setStocktoZero(id1);
                        session.setAttribute("msg","El producto se encuentra en historial de pedidos y aún tiene pedidos pendientes, el stock restante ahora es 0");
                        response.sendRedirect(request.getContextPath() +"/FarmaciaPrincipal");
                    }
                }
                break;

            case "confirmarEntrega":
                String numeroOrden = request.getParameter("numeroOrden");
                farmaciaDao.confirmarEntrega(Integer.parseInt(numeroOrden));
                response.sendRedirect(request.getContextPath()+"/FarmaciaPrincipal?action=listarPedidos");

                break;

            case "borrarPedido":
                ArrayList<DTOinservible> dtOinservibles = farmaciaDao.listaStockSumar(request.getParameter("numeroOrden"));
                farmaciaDao.sumarStock(dtOinservibles);
                farmaciaDao.cancelarPedido(Integer.parseInt(request.getParameter("numeroOrden")));
                response.sendRedirect(request.getContextPath()+"/FarmaciaPrincipal?action=listarPedidos");
                break;
            case "mostrarDetalles":
                String numeroOrden1 = request.getParameter("numeroOrden");
                String codigoAleatorio = request.getParameter("codigoAleatorio");
                ArrayList<DTODetallesPedido> bProductos = farmaciaDao.listaSinRepetir(Integer.parseInt(numeroOrden1));
                session.setAttribute("numeroOrdenimage",numeroOrden1);
                session.setAttribute("codigoAleatorio",codigoAleatorio);
                session.setAttribute("detalles",bProductos);
                response.sendRedirect(request.getContextPath()+"/FarmaciaPrincipal?action=mostrarDetalles");
            break;
        }
    }
}
