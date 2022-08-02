package com.devsuperior.dscatolog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatolog.entities.Product;
import com.devsuperior.dscatolog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	private Long existingId;

	private Long notExistingId;

	private Long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		notExistingId = 50L;
		countTotalProducts = 25L;
	}

	@Test
	public void deleteShouldDeleteProductWhenIdExists() {

		repository.deleteById(existingId);

		Optional<Product> p = repository.findById(existingId);

		Assertions.assertTrue(!p.isPresent());
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {

		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(notExistingId);
		});
	}

	@Test
	public void saveShouldPersistProductWithAutoincrementWhenIdIsNull() {
		Product p = Factory.createProduct();
		p.setId(null);

		repository.save(p);

		Assertions.assertTrue(p.getId() != null);
		Assertions.assertEquals(p.getId(), this.countTotalProducts + 1);

	}

	@Test
	public void findByIdShouldReturnOptionalProductNotNullWhenIdExists() {

		Optional<Product> obj = repository.findById(existingId);

		Assertions.assertNotNull(obj);
	}

	@Test
	public void findyByIdShouldReturnNullOptionalProductWhenIdDoesNotExist() {
		
		Optional<Product> obj = repository.findById(notExistingId);
		
		Assertions.assertTrue(obj.isEmpty());
	}
}
