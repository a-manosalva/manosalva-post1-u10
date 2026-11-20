package com.universidad.catalogo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.universidad.catalogo.model.Producto;

@DataJpaTest
class ProductoRepositoryTest {

	@Autowired
	ProductoRepository repo;

	@Autowired
	TestEntityManager em;

	@BeforeEach
	void setUp() {
		em.persistAndFlush(crear("Laptop", 1500.0, "ELECTRONICA"));
		em.persistAndFlush(crear("Cuaderno", 5.0, "PAPELERIA"));
	}

	@Test
	void findByCategoria_electronica_retornaUnProducto() {
		assertThat(repo.findByCategoria("ELECTRONICA"))
				.hasSize(1)
				.extracting("nombre")
				.containsExactly("Laptop");
	}

	@Test
	void findByCategoria_sinCoincidencias_retornaListaVacia() {
		assertThat(repo.findByCategoria("JUGUETERIA")).isEmpty();
	}

	private Producto crear(String nombre, double precio, String categoria) {
		Producto producto = new Producto();
		producto.setNombre(nombre);
		producto.setPrecio(precio);
		producto.setCategoria(categoria);
		return producto;
	}
}
