package com.devsuperior.dscatolog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatolog.dto.CategoryDTO;
import com.devsuperior.dscatolog.dto.ProductDTO;
import com.devsuperior.dscatolog.entities.Category;
import com.devsuperior.dscatolog.entities.Product;
import com.devsuperior.dscatolog.repositories.CategoryRepository;
import com.devsuperior.dscatolog.repositories.ProductRepository;
import com.devsuperior.dscatolog.services.exceptions.DataBaseException;
import com.devsuperior.dscatolog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository catRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPage(PageRequest request) {
		Page<Product> list = repository.findAll(request);
		return list.map(cat -> new ProductDTO(cat, cat.getCategories()));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> product = repository.findById(id);
		Product prod = product.orElseThrow(() -> new ResourceNotFoundException("Id not found"));
		return new ProductDTO(prod, prod.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product prod = new Product();
		copyDtoToEntity(dto, prod);
		prod = repository.save(prod);
		return new ProductDTO(prod);
	}

	@Transactional(readOnly = true)
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product prod = repository.getOne(id);
			copyDtoToEntity(dto, prod);
			repository.save(prod);
			return new ProductDTO(prod, prod.getCategories());
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setDescription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		for (CategoryDTO catDto : dto.getCategories()) {
			Category cat = catRepository.getOne(catDto.getId());
			entity.getCategories().add(cat);

		}

	}
}
