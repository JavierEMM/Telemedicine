<!DOCTYPE html>
<%@ page import="pe.edu.pucp.iweb.teledrugs.Beans.BProducto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.Beans.BFarmacia" %>
<jsp:useBean type="java.lang.Integer"  scope="request"  id="pag" />
<jsp:useBean type="java.lang.Integer"  scope="request"  id="index" />
<% ArrayList<BProducto> listaProductos = session.getAttribute("listaProductos") != null ? (ArrayList) session.getAttribute("listaProductos") : new ArrayList<>();%>
<%BFarmacia farmacia = session.getAttribute("farmacia") != null ? (BFarmacia) session.getAttribute("farmacia") : new BFarmacia(); %>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>Lista Productos</title>
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
            <h1 class="display-4 fw-bolder">Lista de productos</h1>
            <p class="lead fw-normal text-white-50 mb-0">Farmacia <%=farmacia.getNombre() %></p>
        </div>
    </div>
    <form method="post" action="<%=request.getContextPath()%>/FarmaciaPrincipal?action=Buscar">
        <div class = "box">
            <input  type="text" name="search" placeholder="Buscar producto" class="src" autocomplete = "off">
        </div>
    </form>
    <div style="display:flex; align-items:center; justify-content:center; margin-top: 50px; margin-bottom: 15px;">
        <a class="btn btn-success" href="<%=request.getContextPath()%>/FarmaciaPrincipal?action=crear" style="width:250px;">Agregar Producto</a>

        <a class="btn btn-success" href="<%=request.getContextPath()%>/FarmaciaPrincipal?action=listarPedidos" style="width:250px;">Ver Historial de Pedidos</a>
    </div>
</header>
</div>
<div class="container padding-bottom-3x mb-1", style=" margin-top: 25px;">
    <div class="container bootstrap snippets bootdey" style=" margin-top: 25px;">
        <% if (session.getAttribute("msg") != null){ %>
        <div id="mensaje" class="alert alert-danger">
            <%=session.getAttribute("msg")%>
        </div>
        <script type="text/javascript">
            window.setTimeout("closeHelpDiv();", 5000);
            function closeHelpDiv(){
                document.getElementById("mensaje").style.display=" none";
            }
        </script>
        <% session.removeAttribute("msg");
        }%>
    <div class="table-responsive shopping-cart">
        <table class="table">
            <thead>
            <tr>
                <th class="text-center">Imagen del producto</th>
                <th class="text-center">Nombre del producto</th>
                <th class="text-center">Descripcion</th>
                <th class="text-center">Stock/Cantidad</th>
                <th class="text-center">Precio Unitario</th>
                <th class="text-center">Editar</th>
                <th class="text-center">Eliminar</th>
            </tr>
            </thead>
            <tbody>
            <%
                int i = 0;
                for (BProducto listaproducto : listaProductos) {
            %>
            <tr>

                <td class="text-center text-lg text-medium"><img style="width: 50px; height: 50px;" src="ImagenesServlet?id=<%=listaproducto.getIdProducto()%>"></td>
                <td class="text-center text-lg text-medium"><%=listaproducto.getNombre()%></td>
                <td class="text-center text-lg text-medium"><%=listaproducto.getDescripcion()%></td>
                <td class="text-center text-lg text-medium"><%=listaproducto.getStock()%></td>
                <td class="text-center text-lg text-medium"><%=listaproducto.getPrecio()%></td>


                <td class="text-center text-lg text-medium">
                    <form id="<%=i%>" method="post" action="<%=request.getContextPath()%>/FarmaciaPrincipal?action=editarProducto">
                        <input name="idProducto" value="<%=listaproducto.getIdProducto()%>" type="hidden"/>
                        <button class="btn btn-success" type="submit">
                            Editar
                        </button>
                    </form>
                </td>
                <td class="text-center">
                    <form id="<%=i%>" method="post" action="<%=request.getContextPath()%>/FarmaciaPrincipal?action=eliminar">
                        <input name="idProducto" value="<%=listaproducto.getIdProducto()%>" type="hidden"/>
                        <button onclick="return confirm('Esta seguro de eliminar este Producto?')"  class="btn btn-outline-danger flex-shrink-0" type="submit">
                            <i class="bi bi-trash"></i>
                        </button>
                    </form>
                </td>
            </tr>
            <%
                    i=i+1;
                }
            %>

            </tbody>
        </table>
        <div class="column" style="margin-top:20px;">
            <nav aria-label="Page navigation example">
                <ul class="pagination justify-content-center">
                    <%if (pag==1){%>
                    <li class="page-item disabled">
                        <a class="page-link" href="<%=request.getContextPath()%>/FarmaciaPrincipal&offset=<%=pag-1%>">Previous</a>
                    </li>
                    <%}else
                    {%>
                    <li class="page-item">
                        <a class="page-link" href="<%=request.getContextPath()%>/FarmaciaPrincipal?offset=<%=pag-1%>">Previous</a>
                    </li>
                    <%}%>
                    <%for (int j=1; j<=index;j++){%>
                    <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/FarmaciaPrincipal?offset=<%=j%>"> <%=j%>  </a></li>
                    <%}
                        if(pag==index || index==1){%>
                    <li class="page-item disabled">
                        <a class="page-link" href="<%=request.getContextPath()%>/FarmaciaPrincipal?offset=<%=pag+1%>">Next</a>
                    </li>
                    <%}else {%>
                    <li class="page-item">
                        <a class="page-link" href="<%=request.getContextPath()%>/FarmaciaPrincipal?offset=<%=pag+1%>">Next</a>
                            <%}%>
                </ul>
            </nav>
        </div>
    </div>
    </div>
</div>
<footer class="py-5 bg-dark">
    <div class="container"><p class="m-0 text-center text-white">Copyright &copy; TeleDrugs 2021</p></div>
</footer>

<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
<!-- Core theme JS-->
<script src="js/scripts.js"></script>
</body>
</html>
