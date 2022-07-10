package pe.edu.pucp.iweb.teledrugs.Controllers;

import pe.edu.pucp.iweb.teledrugs.Daos.ClienteDao;
import pe.edu.pucp.iweb.teledrugs.Daos.ProductoDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ImagenesServlet", value = "/ImagenesServlet")
public class ImagenesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "mostrarimagen" : request.getParameter("action");
        ProductoDao productoDao = new ProductoDao();
        switch (action){
            case "mostrarimagen":
                String id = request.getParameter("id");
                productoDao.listarImg(id,response);
                break;
            case "mostrarReceta":
                String id2 = request.getParameter("id");
                productoDao.listarImgProductoReceta(id2,response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "mostrarimagen" : request.getParameter("action");
        System.out.println(action);
        ProductoDao productoDao = new ProductoDao();
        switch (action){
            case "mostrarReceta":
                String id2 = request.getParameter("id");
                productoDao.listarImgProductoReceta(id2,response);
                break;
        }
    }
}
