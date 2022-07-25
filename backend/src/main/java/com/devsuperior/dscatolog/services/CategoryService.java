package com.devsuperior.dscatolog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatolog.dto.CategoryDTO;
import com.devsuperior.dscatolog.entities.Category;
import com.devsuperior.dscatolog.repositories.CategoryRepository;
import com.devsuperior.dscatolog.services.exceptions.DataBaseException;
import com.devsuperior.dscatolog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		return list.stream().map(cat -> new CategoryDTO(cat)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findCategoryById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category cat = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CategoryDTO(cat);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO cat) {
		Category entity = new Category();
		entity.setName(cat.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category cat = repository.getOne(id);
			cat.setName(dto.getName());
			cat = repository.save(cat);
			return new CategoryDTO(cat);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
	}

	public Page<CategoryDTO> findAllPageable(PageRequest pageRequest) {
		Page<Category> list = repository.findAll(pageRequest);
		return list.map(cat -> new CategoryDTO(cat));
	}

}
