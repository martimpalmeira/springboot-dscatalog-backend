package com.devsuperior.dscatolog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatolog.dto.ProductDTO;
import com.devsuperior.dscatolog.repositories.ProductRepository;
import com.devsuperior.dscatolog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository repository;

	private Long existingId;

	private Long notExistingId;

	private Long countTotalProducts;
	
	private Pageable pageable;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		notExistingId = 100L;
		countTotalProducts = 25L;
		

	}

	@Test
	public void delete_ShouldDeleteFromDataBase_WhenIdExists() {
		productService.delete(existingId);

		Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}

	@Test
	public void delete_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.delete(notExistingId);
		});
	}
	
	@Test
	public void findAllPage_ShouldReturnPage0With10ProductDTO() {
		pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> pageDto = productService.findAllPage(pageable);
		
		Assertions.assertFalse(pageDto.isEmpty());
		Assertions.assertEquals(0, pageDto.getNumber());
		Assertions.assertEquals(10, pageDto.getSize());
		Assertions.assertEquals(this.countTotalProducts, pageDto.getTotalElements());
	
	}
	
	@Test
	public void finaAllPage_ShouldReturnEmptyPage_WhenPageDoesNotExists() {
		pageable = PageRequest.of(50, 10);
		
		Page<ProductDTO> pageDto = productService.findAllPage(pageable);
		
		Assertions.assertTrue(pageDto.isEmpty());
	}
	
	@Test
	public void findAllPage_ShouldReturnPage0With10ProductsSortedByName() {
		pageable = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<ProductDTO> pageDto = productService.findAllPage(pageable);
		
		Assertions.assertEquals("Macbook Pro", pageDto.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", pageDto.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", pageDto.getContent().get(2).getName());
	}
	


}
