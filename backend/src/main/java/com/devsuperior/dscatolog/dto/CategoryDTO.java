package com.devsuperior.dscatolog.dto;

import java.io.Serializable;
import java.util.Optional;

import com.devsuperior.dscatolog.entities.Category;

public class CategoryDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	public CategoryDTO() {
		
	}

	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CategoryDTO(Category cat) {
		this.id = cat.getId();
		this.name = cat.getName();
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	
	
	
}