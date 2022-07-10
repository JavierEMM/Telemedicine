package pe.edu.pucp.iweb.teledrugs.Filters;

import pe.edu.pucp.iweb.teledrugs.Beans.BFarmacia;
import pe.edu.pucp.iweb.teledrugs.Beans.BProducto;
import pe.edu.pucp.iweb.teledrugs.DTO.DTOPedidoHistorial;
import pe.edu.pucp.iweb.teledrugs.Daos.FarmaciaDao;
import pe.edu.pucp.iweb.teledrugs.Daos.ProductoDao;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebFilter(filterName = "OffsetFarmaciaFilter" ,urlPatterns = {"/FarmaciaPrincipal"})
public class OffsetFarmaciaFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String action = request.getParameter("action") == null ? "listarProductos" : request.getParameter("action");
        BFarmacia bFarmacia = session.getAttribute("farmacia") == null ? new BFarmacia() : (BFarmacia) session.getAttribute("farmacia");
        String rucFarmacia = bFarmacia.getRuc();
        String offset = request.getParameter("offset") != null ? request.getParameter("offset") : "1";

        Boolean condicion = !action.equalsIgnoreCase("listarProductos") && !action.equalsIgnoreCase("listarPedidos")&& !action.equalsIgnoreCase("Buscar")&& !action.equalsIgnoreCase("BuscarPedido")
                && !action.equalsIgnoreCase("crear")&& !action.equalsIgnoreCase("update")&& !action.equalsIgnoreCase("editarProducto")&& !action.equalsIgnoreCase("mostrarDetalles")
                && !action.equalsIgnoreCase("logout");
        if(condicion){
            action ="error";
        }
        System.out.println("La accion del filtro es " + action);
        int index=1;
        if(action.equalsIgnoreCase("Buscar") || action.equalsIgnoreCase("listarProductos")) {
            ProductoDao productoDao = new ProductoDao();
            ArrayList<BProducto> listaProductos = productoDao.ProductosPorFarmacia(rucFarmacia);

            if (listaProductos.size() % 4 == 0) {
                index = listaProductos.size() / 4;
            } else {
                index = 1 + (listaProductos.size() / 4);
            }
            try {
                Integer offsetint = Integer.parseInt(offset);
                if (offsetint > index) {
                    response.sendRedirect(request.getContextPath() + "/FarmaciaPrincipal");
                } else {
                    chain.doFilter(request, response);
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/FarmaciaPrincipal");
            }
        }
                else if(action.equalsIgnoreCase("listarPedidos") || action.equalsIgnoreCase("BuscarPedido") ) {
            FarmaciaDao farmaciaDao = new FarmaciaDao();
            ArrayList<DTOPedidoHistorial> listaHistorialPedidos = farmaciaDao.mostrarHistorialPedidos(rucFarmacia);
            if (listaHistorialPedidos.size() % 4 == 0) {
                index = listaHistorialPedidos.size() / 4;
            } else {
                index = 1 + (listaHistorialPedidos.size() / 4);
            }

            try {
                Integer offsetint = Integer.parseInt(offset);
                if (offsetint > index) {
                    response.sendRedirect(request.getContextPath() + "/FarmaciaPrincipal?action=listarPedidos");
                } else {
                    chain.doFilter(request, response);
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/FarmaciaPrincipal?action=listarPedidos");
            }
            }

        else if (action.equalsIgnoreCase("error")){
            response.sendRedirect(request.getContextPath() + "/FarmaciaPrincipal");
        }else  {
                chain.doFilter(request, response);
        }

        }
    }

