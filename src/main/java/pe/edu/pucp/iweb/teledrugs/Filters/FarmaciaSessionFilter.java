package pe.edu.pucp.iweb.teledrugs.Filters;

import pe.edu.pucp.iweb.teledrugs.Beans.BAdministrador;
import pe.edu.pucp.iweb.teledrugs.Beans.BFarmacia;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "FarmaciaSessionFilter", urlPatterns = {"/FarmaciaPrincipal"})
public class FarmaciaSessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        BFarmacia farmacia = (BFarmacia) session.getAttribute("farmacia");

        if(farmacia!= null && farmacia.getRuc()!=""){
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setDateHeader("Expires", 0);
            chain.doFilter(request, response);
        }else{
            response.sendRedirect(request.getContextPath());
        }


    }
}
