<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.Beans.BFarmacia" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.Beans.BProducto" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.DTO.DTOCarritoCliente" %>
<!DOCTYPE html>
<%Integer pagina = (Integer) request.getAttribute("pagina");%>
<%Integer index = (Integer) request.getAttribute("index");%>
<%ArrayList<DTOCarritoCliente> listaCarrito = session.getAttribute("carrito") != null ? (ArrayList) session.getAttribute("carrito") : new ArrayList<>();%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%ArrayList<BFarmacia> listafarmacias = (ArrayList) session.getAttribute("listafarmacias");%>
<%BFarmacia bFarmacia2 = session.getAttribute("farmacia") == null ? null : (BFarmacia) session.getAttribute("farmacia");%>
<%ArrayList<BProducto> listaProducto = (ArrayList) session.getAttribute("listaProducto");%>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>Homepage</title>
        <!-- Favicon-->
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        <!-- Bootstrap icons-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="FlujoUsuario/css/styles.css" rel="stylesheet" />
    </head>
    <body>
    <%if(bFarmacia2.getRuc() == null){%>
        <div id="modal" class="modal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div style="text-align: center; margin-top: 20px;"><h4 class="form-title">ELEGIR FARMACIA</h4></div>
                    <div class="modal-body">
                        <form method="post" action="<%=request.getContextPath()%>/Usuario?opcion=mostrarFarmacia" class= "register-form">
                            <div style="margin-bottom: 20px; display: flex; justify-content: center;" class="form-group">
                                <select class="form-control form-select-sm" name="ruc">
                                    <%for (BFarmacia bFarmacia : listafarmacias){%>
                                    <option value="<%=bFarmacia.getRuc()%>"><%=bFarmacia.getNombre()%> - <%=bFarmacia.getDistrito()%> - <%=bFarmacia.getDireccion()%></option>
                                    <%}%>
                                </select>
                            </div>
                            <div class="form-group form-button">
                                <div style="margin-top:5px; text-align: center;"><button type="submit" class="btn btn-success">Continuar</button></div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <%}else{%>
        <!-- Navigation-->
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container px-4 px-lg-5">
                <a class="navbar-brand" href="#!">Teledrugs</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
                        <li class="nav-item"><a class="nav-link" aria-current="page" href="<%=request.getContextPath()%>/Usuario">Pagina principal</a></li>
                        <li class="nav-item"><a class="nav-link" href="<%=request.getContextPath()%>/Usuario?opcion=historialPedidos">Estado de pedido</a></li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">Farmacias</a>
                            <ul class="dropdown-menu" style="height: 200px;width: 300px;" aria-labelledby="navbarDropdown">
                                <div style="text-align: center; margin-top: 20px;"><h4 class="form-title">ELEGIR FARMACIA</h4></div>
                                <div class="modal-body">
                                    <form method="post" action="<%=request.getContextPath()%>/Usuario?opcion=mostrarFarmacia" class= "register-form">
                                        <div style="margin-bottom: 20px; display: flex; justify-content: center;" class="form-group">
                                            <select class="form-control form-select-sm" name="ruc">
                                                <%for (BFarmacia bFarmacia : listafarmacias){%>
                                                <option value="<%=bFarmacia.getRuc()%>"><%=bFarmacia.getNombre()%> - <%=bFarmacia.getDistrito()%> - <%=bFarmacia.getDireccion()%></option>
                                                <%}%>
                                            </select>
                                        </div>
                                        <div class="form-group form-button">
                                            <div style="margin-top:5px; text-align: center;"><button type="submit" class="btn btn-success">Continuar</button></div>
                                        </div>
                                    </form>
                                </div>
                            </ul>
                        </li>
					</ul>
						<div class="dropdown">
						  <a class="btn btn-outline-dark dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
							 <i class='bi bi-person-circle' style='font-size:15px'></i>
							 Usuario
						  </a>

						  <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
							<li><a href="<%= request.getContextPath()%>/Usuario?opcion=mostrarPerfil" class="dropdown-item" >Ver perfil</a></li>
							<li><a href="<%= request.getContextPath()%>/Usuario?opcion=logout" class="dropdown-item" >Cerrar sesión</a></li>
						  </ul>
						</div>

                    <form method="post" action="<%= request.getContextPath()%>/Usuario?opcion=carrito">
                        <form class="d-flex">
                            <button class="btn btn-outline-dark" type="submit">
                                <i class="bi-cart-fill me-1"></i>
                                Carrito
                                <span class="badge bg-dark text-white ms-1 rounded-pill"><%=listaCarrito.size()%></span>
                            </button>
                        </form>
                    </form>
                </div>
            </div>
        </nav>

        <!-- Header-->
        <header class="bg-dark py-5">
            <div class="container px-4 px-lg-5 my-5">
                <div class="text-center text-white">
                    <h1 class="display-4 fw-bolder">Farmacia <%=bFarmacia2.getNombre()%></h1>
                    <p class="lead fw-normal text-white-50 mb-0">El mejor lugar para comprar</p>
                </div>
            </div>
            <form method="post" action="<%=request.getContextPath()%>/Usuario?opcion=Buscar">
                <div class = "box">
                    <input  type="text" name="search" placeholder="Buscar producto" class="src" autocomplete = "off">
                </div>
            </form>
        </header>
        <!-- Section-->

        <section class="py-5">
            <div class="container px-4 px-lg-5 mt-5" style="margin-top: 0px">
                <div class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
                    <%for(BProducto bProducto : listaProducto){%>
                    <div class="col mb-5">
                        <div class="card h-100" style="display: flex; align-content: center; align-items: center;">
                            <!-- Sale badge-->
                            <div class="badge bg-dark text-white position-absolute" style="top: 0.5rem; right: 0.5rem">En venta</div>
                            <!-- Product image-->
                            <img style="height: 206px; width: 206px;" class="card-img-top" src="ImagenesServlet?id=<%=bProducto.getIdProducto()%>" alt="..." />
                            <!-- Product details-->
                            <div class="card-body p-4">
                                <div class="text-center">
                                    <!-- Product name-->
                                    <h5 class="fw-bolder"><%=bProducto.getNombre()%></h5>
                                    <!-- Product price-->
                                    S/ <%=bProducto.getPrecio()%>
                                </div>
                            </div>
                            <!-- Product actions-->
                            <form class="mb-5" method="post" action="<%=request.getContextPath()%>/Usuario?opcion=mostrarProducto">
                                <input name="idProducto" value="<%=bProducto.getIdProducto()%>" type="hidden"/>
                                <button  class="btn btn-outline-dark flex-shrink-0" type="submit">
                                    Ver detalle
                                </button>
                            </form>
                        </div>
                    </div>
                    <%}%>
                </div>
            </div>
            <nav aria-label="Page navigation example">
                <ul class="pagination justify-content-center">
                    <%if(pagina==1){%>
                    <li class="page-item disabled">
                        <a class="page-link" href="<%=request.getContextPath()%>/Usuario&offset=<%=pagina-1%>">Previous</a>
                    </li>
                    <%}else{%>
                    <li class="page-item">
                        <a class="page-link" href="<%=request.getContextPath()%>/Usuario?offset=<%=pagina-1%>">Previous</a>
                    </li>
                     <%}%>
                    <%for(int i=1;i<= index;i++){%>
                    <li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/Usuario?offset=<%=i%>"><%=i%></a></li>
                    <%}%>
                    <%if(pagina == index){%>
                        <li class="page-item disabled">
                            <a class="page-link" href="<%=request.getContextPath()%>/Usuario?offset=<%=pagina+1%>">Next</a>
                        </li>
                    <%}else{%>
                        <li class="page-item">
                            <a class="page-link" href="<%=request.getContextPath()%>/Usuario?offset=<%=pagina+1%>">Next</a>
                        </li>
                    <%}%>
                    </li>
                </ul>
            </nav>
        </section>
        <!-- Footer-->
        <footer class="py-5 bg-dark">
            <div class="container"><p class="m-0 text-center text-white">Copyright &copy; Your Website 2021</p></div>
        </footer>
    <%};%>
        <!-- Bootstrap core JS-->
        <script>
            var modalObject = document.getElementById("modal");
            modalObject.style.display = "block";
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Core theme JS-->
        <script src="FlujoUsuario/js/scripts.js"></script>
    </body>
</html>
