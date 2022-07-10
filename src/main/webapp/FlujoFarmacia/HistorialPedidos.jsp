<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.DTO.DTOPedidoHistorial" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.Daos.FarmaciaDao" %>
<%@ page import="pe.edu.pucp.iweb.teledrugs.Beans.BProducto" %>
<jsp:useBean type="pe.edu.pucp.iweb.teledrugs.Beans.BFarmacia" scope="session" id="farmacia"/>

<%Integer pag = (Integer) request.getAttribute("pag");%>
<%Integer index = (Integer) request.getAttribute("index");%>
<% ArrayList<DTOPedidoHistorial>listaHistorialPedidos = (ArrayList<DTOPedidoHistorial>) session.getAttribute("listaHistorialPedidos"); %>
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
			<h1 class="display-4 fw-bolder">Historial de pedidos</h1>
			<p class="lead fw-normal text-white-50 mb-0">Farmacia <%=farmacia.getNombre()%></p>
		</div>
	</div>
	<form method="post" action="<%=request.getContextPath()%>/FarmaciaPrincipal?action=BuscarPedido">
		<div class = "box">
			<input  type="text" name="search" placeholder="Buscar pedido" class="src" autocomplete = "off">
		</div>
	</form>
	<div style="display:flex; align-items:center; justify-content:center; margin-top: 50px; margin-bottom: 15px;">
		<a class="btn btn-success" href="<%=request.getContextPath()%>/FarmaciaPrincipal" style="width:250px;">Regresar a lista de Productos</a>
	</div>
</header>
<div class="container padding-bottom-3x mb-1", style=" margin-top: 25px;">
	<div class="table-responsive shopping-cart">
		<table class="table">
			<thead>
			<tr>
				<th class="text-center">Codigo de pedido</th>
				<th class="text-center">Datos del cliente</th>
				<th class="text-center">Estado de pedido</th>
				<th class="text-center">Fecha de recojo</th>
				<th class="text-center">Receta y Detalles de Pedido</th>
				<th class="text-center">Confirmar entrega</th>
				<th class="text-center">Cancelar entrega</th>
			</tr>
			</thead>
			<tbody>
			<%
				int i = 1;
				for (DTOPedidoHistorial pedidoHistorial : listaHistorialPedidos) {
			%>
			<tr>
				<td class="text-center text-lg text-medium"><%=pedidoHistorial.getCodigoAleatorio()%></td>
				<td class="text-center text-lg text-medium">
					<ul class="list-group">
						<li class="list-group-item"><%=pedidoHistorial.getNombreCompleto()%></li>
						<li class="list-group-item"><%=pedidoHistorial.getDni()%></li>
					</ul>
				</td>
				<td class="text-center text-lg text-medium"><%=pedidoHistorial.getEstado()%></td>
				<td class="text-center text-lg text-medium"><%=pedidoHistorial.getFechaRecojo()%></td>
				<td class="text-center text-lg text-medium">
					<form id="<%=i%>" method="post" action="<%=request.getContextPath()%>/FarmaciaPrincipal?action=mostrarDetalles">
						<input name="codigoAleatorio" value="<%=pedidoHistorial.getCodigoAleatorio()%>" type="hidden"/>
						<input name="numeroOrden" value="<%=pedidoHistorial.getNumeroOrden()%>" type="hidden"/>
						<button class="btn btn-success" type="submit">
							Detalles y Recetas
						</button>
					</form>
				</td>
				<%if(pedidoHistorial.getEstado().equalsIgnoreCase("Pendiente")){%>
				<td class="text-center">
					<form id="<%=i%>" method="post" action="<%=request.getContextPath()%>/FarmaciaPrincipal?action=confirmarEntrega">
						<input name="numeroOrden" value="<%=pedidoHistorial.getNumeroOrden()%>" type="hidden"/>
						<button style="width:120px; margin:5px;" onclick="return confirm('¿Esta seguro de confirmar la Entrega?')"  class="btn btn-secondary" type="submit">
							Confirmar
						</button>
					</form>
				</td>
				<%
					FarmaciaDao farmaciaDao = new FarmaciaDao();
					String horas = farmaciaDao.restarFechas(pedidoHistorial.getFechaRecojo());%>
				<td class="text-center">
					<%if(Double.parseDouble(horas) <= -24.0){%>
					<form id="<%=i%>" method="post" action="<%=request.getContextPath()%>/FarmaciaPrincipal?action=borrarPedido">
						<input name="numeroOrden" value="<%=pedidoHistorial.getNumeroOrden()%>" type="hidden"/>
						<button onclick="return confirm('¿Esta seguro de cancelar el Pedido?')"  class="btn btn-outline-danger flex-shrink-0" type="submit">
							<i class="bi bi-trash"></i>
						</button>
					</form>
					<%}%>
				</td>
				<%}else{%>
				<td class="text-center">
				</td>
				<td class="text-center">
				</td>
				<%}%>
			</tr>
			<%i=i+1;
				}
			%>
			</tbody>
		</table>
		<div class="column" style="margin-top:20px;">
			<nav aria-label="Page navigation example">
				<ul class="pagination justify-content-center">
					<%if (pag==1){%>
					<li class="page-item disabled">
						<a class="page-link" href="<%=request.getContextPath()%>/FarmaciaPrincipal?action=listarPedidos&offset=<%=pag-1%>">Previous</a>
					</li>
					<%}else
					{%>
					<li class="page-item">
						<a class="page-link" href="<%=request.getContextPath()%>/FarmaciaPrincipal?action=listarPedidos&offset=<%=pag-1%>">Previous</a>
					</li>
					<%}%>
					<%for (int j=1; j<=index;j++){%>
					<li class="page-item"><a class="page-link" href="<%=request.getContextPath()%>/FarmaciaPrincipal?action=listarPedidos&offset=<%=j%>"><%=j%></a></li>
					<%}
						if(pag==index || index==1){%>
					<li class="page-item disabled">
						<a class="page-link" href="<%=request.getContextPath()%>/FarmaciaPrincipal?action=listarPedidos&offset=<%=pag+1%>">Next</a>
					</li>
					<%}else {%>
					<li class="page-item">
						<a class="page-link" href="<%=request.getContextPath()%>/FarmaciaPrincipal?action=listarPedidos&offset=<%=pag+1%>">Next</a>
							<%}%>
				</ul>
			</nav>

		</div>

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
