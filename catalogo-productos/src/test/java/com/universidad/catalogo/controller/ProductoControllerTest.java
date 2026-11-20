package com.universidad.catalogo.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.universidad.catalogo.model.Producto;
import com.universidad.catalogo.service.ProductoService;

import jakarta.persistence.EntityNotFoundException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockitoBean
	ProductoService service;

	@Test
	void get_productoExiste_retorna200() throws Exception {
		Producto producto = new Producto();
		producto.setId(1L);
		producto.setNombre("Laptop");
		producto.setPrecio(1500.0);
		when(service.buscarPorId(1L)).thenReturn(producto);

		mockMvc.perform(get("/api/productos/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Laptop"));
	}

	@Test
	void get_noExiste_retorna404() throws Exception {
		when(service.buscarPorId(99L)).thenThrow(new EntityNotFoundException("no encontrado"));

		mockMvc.perform(get("/api/productos/99"))
				.andExpect(status().isNotFound());
	}

	@Test
	void post_conDatosValidos_retorna201() throws Exception {
		Producto entrada = new Producto();
		entrada.setNombre("Mouse");
		entrada.setPrecio(50.0);
		Producto guardado = new Producto();
		guardado.setId(1L);
		guardado.setNombre("Mouse");
		guardado.setPrecio(50.0);
		when(service.crear(any())).thenReturn(guardado);

		mockMvc.perform(post("/api/productos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(entrada)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void post_conPrecioInvalido_retorna400() throws Exception {
		Producto entrada = new Producto();
		entrada.setNombre("Auricular");
		entrada.setPrecio(-5.0);
		when(service.crear(any()))
				.thenThrow(new IllegalArgumentException("El precio debe ser mayor a cero"));

		mockMvc.perform(post("/api/productos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(entrada)))
				.andExpect(status().isBadRequest());
	}
}
