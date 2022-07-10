<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.DTO.DTOPedidoHistorial" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.Daos.FarmaciaDao" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.Beans.BProducto" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.DTO.DTODetallesPedido" %>
<jsp:useBean type="pe.edu.pucp.iweb.teledrugs.Beans.BFarmacia" scope="session" id="farmacia"/>
<%String codigoAleatorio = (String) session.getAttribute("codigoAleatorio");%>
<% ArrayList<DTODetallesPedido> listaProductos = (ArrayList) session.getAttribute("detalles"); %>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>Historial Pedidos</title>
    <!-- Favicon-->
    <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Core theme CSS (includes Bootstrap)-->
    <link href="FlujoFarmacia/css/styles.css" rel="stylesheet" />
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container px-4 px-lg-5">
        <a class="navbar-brand" href="#!">Teledrugs</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">

            </ul>
            <div class="dropdown">
                <a class="btn btn-outline-dark dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class='bi bi-person-circle' style='font-size:15px'></i>
                    Usuario
                </a>

                <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                    <li><a class="dropdown-item" href="<%=request.getContextPath()%>/FarmaciaPrincipal?action=logout">Cerrar sesión</a></li>
                </ul>
            </div>
        </div>
    </div>
</nav>
<header class="bg-dark py-5">
    <div class="container px-4 px-lg-5 my-5">
        <div class="text-center text-white">
            <h1 class="display-4 fw-bolder">Detalles de compra</h1>
            <p class="lead fw-normal text-white-50 mb-0">Farmacia <%=farmacia.getNombre()%></p>
            <p class="lead fw-normal text-white-50 mb-0">Pedido #<%=codigoAleatorio%></p>
        </div>
    </div>
    <div style="display:flex; align-items:center; justify-content:center; margin-top: 50px; margin-bottom: 15px;">
        <a class="btn btn-success" href="<%=request.getContextPath()%>/FarmaciaPrincipal?action=listarPedidos" style="width:250px;">Regresar a lista de Pedidos</a>
    </div>
</header>
<div class="container padding-bottom-3x mb-1", style=" margin-top: 25px;">
    <div class="table-responsive shopping-cart">
        <table class="table">
            <thead>
            <tr>
                <th class="text-center">Nombre de Producto</th>
                <th class="text-center">Cantidad</th>
                <th class="text-center">Requiere receta</th>
                <th class="text-center">Receta</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <%
                int i = 1;
                for (DTODetallesPedido bProducto : listaProductos) {
            %>
            <tr>
                <td class="text-center text-lg text-medium"><%=bProducto.getNombre()%></td>
                <td class="text-center text-lg text-medium"><%=bProducto.getStock()%></td>
                <%if(bProducto.isRequiereReceta()){%>
                <td class="text-center text-lg text-medium">Si</td>
                <td class="text-center text-lg text-medium"><img style="width: 100px; height: 100px;" src="ImagenesServlet?action=mostrarReceta&id=<%=bProducto.getIdProducto_haspedido()%>"></td>
                <td>
                    <form id="<%=i%>" method="post" action="<%=request.getContextPath()%>/ImagenesServlet?action=mostrarReceta">
                        <input name="id" value="<%=bProducto.getIdProducto_haspedido()%>" type="hidden"/>
                        <button style="width:120px; margin:5px;" class="btn btn-secondary" type="submit">
                            Ver Receta
                        </button>
                    </form>
                </td>
                <%}else{%>
                <td class="text-center text-lg text-medium">No</td>
                <td class="text-center text-lg text-medium">No tiene receta</td>
                <%}%>
            </tr>
            <%i=i+1;
            }
            %>
            </tbody>
        </table>
    </div>
</div>

<footer class="py-5 bg-dark">
    <div class="container"><p class="m-0 text-center text-white">Copyright &copy; TeleDrugs 2021</p></div>
</footer>

<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Core theme JS-->
<script src="js/scripts.js"></script>
</body>
</html>
