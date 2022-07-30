package com.devsuperior.dscatolog.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatolog.dto.ProductDTO;
import com.devsuperior.dscatolog.entities.Category;
import com.devsuperior.dscatolog.entities.Product;
import com.devsuperior.dscatolog.repositories.CategoryRepository;
import com.devsuperior.dscatolog.repositories.ProductRepository;
import com.devsuperior.dscatolog.services.exceptions.DataBaseException;
import com.devsuperior.dscatolog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatolog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;

	private Long existingId;

	private Long notExistingId;

	private Long dependentId;

	private PageImpl<Product> page;

	private Product product;

	private ProductDTO dto;
	
	private Category category;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		notExistingId = 1000L;
		dependentId = 4L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		dto = Factory.createProductDTO();
		category = Factory.createCategory();

		doNothing().when(repository).deleteById(existingId);

		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(notExistingId);

		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

		Mockito.when(repository.findById(this.existingId)).thenReturn(Optional.of(product));

		Mockito.when(repository.findById(this.notExistingId)).thenReturn(Optional.empty());

		Mockito.when(repository.getOne(this.existingId)).thenReturn(product);

		Mockito.when(repository.getOne(this.notExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getOne(this.existingId)).thenReturn(category);

		Mockito.when(categoryRepository.getOne(this.notExistingId)).thenThrow(EntityNotFoundException.class);

	}

	@Test
	public void delete_ShouldDoNothing_WhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});

		verify(repository, times(1)).deleteById(existingId);
	}

	@Test
	public void delete_ShouldThrowResourceNotFoundException_WhenNotExistingid() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(notExistingId);
		});

		verify(repository, times(1)).deleteById(notExistingId);
	}

	@Test
	public void delete_ShouldThrowDataBaseException_WhenDependentId() {

		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		});

		verify(repository, times(1)).deleteById(dependentId);

	}

	@Test
	public void findAllPage_ShouldReturnPageOfProductDTO() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findAllPage(pageable);

		Assertions.assertNotNull(result);
		verify(repository).findAll(pageable);
	}

	@Test
	public void findById_ShoudlReturnProductDTO_WhenIdExists() {
		ProductDTO dto = service.findById(this.existingId);

		Assertions.assertTrue(dto.getId() == this.existingId);
		Assertions.assertNotNull(dto);

		Mockito.verify(repository, times(1)).findById(existingId);
	}

	@Test
	public void findById_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(this.notExistingId);
		});

		verify(repository, times(1)).findById(this.notExistingId);
	}

	@Test
	public void update_ShouldReturnProductDTO_WhenIdExists() {
		
		ProductDTO dt1 = Factory.createProductDTO();

		 dt1 = service.update(this.existingId, dt1);

		Assertions.assertNotNull(dt1);

		verify(repository).getOne(existingId);
	}

	@Test
	public void update_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(this.notExistingId, dto);
		});
		
		verify(repository).getOne(this.notExistingId);
	}

}
