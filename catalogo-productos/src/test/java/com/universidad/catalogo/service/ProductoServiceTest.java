package com.universidad.catalogo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.universidad.catalogo.model.Producto;
import com.universidad.catalogo.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

	@Mock
	ProductoRepository repo;

	@InjectMocks
	ProductoService service;

	@Test
	void crear_conDatosValidos_guardaYRetorna() {
		Producto producto = new Producto();
		producto.setNombre("Teclado mecánico");
		producto.setPrecio(120.0);
		producto.setCategoria("ELECTRONICA");
		when(repo.save(any())).thenReturn(producto);

		assertThat(service.crear(producto).getNombre()).isEqualTo("Teclado mecánico");
		verify(repo).save(producto);
	}

	@Test
	void crear_conPrecioNegativo_lanzaIllegalArgumentException() {
		Producto producto = new Producto();
		producto.setNombre("Mouse");
		producto.setPrecio(-10.0);

		assertThrows(IllegalArgumentException.class, () -> service.crear(producto));
		verify(repo, never()).save(any());
	}

	@Test
	void buscarPorId_noExiste_lanzaEntityNotFoundException() {
		when(repo.findById(99L)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(99L));
	}

	@Test
	void actualizarPrecio_conValorValido_actualizaYGuarda() {
		Producto producto = new Producto();
		producto.setId(1L);
		producto.setPrecio(100.0);
		when(repo.findById(1L)).thenReturn(Optional.of(producto));
		when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		assertThat(service.actualizarPrecio(1L, 150.0).getPrecio()).isEqualTo(150.0);
	}
}
