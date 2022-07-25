package com.devsuperior.dscatolog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.devsuperior.dscatolog.entities.Category;
import com.devsuperior.dscatolog.entities.Product;

public class ProductDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String description;

	private Double price;

	private String imgUrl;
	
	private Instant date;
	
	private List<CategoryDTO> categories = new ArrayList<>();

	public ProductDTO() {

	}

	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	
	public ProductDTO(Product p) {
		this.id = p.getId();
		this.name = p.getName();
		this.description = p.getDescription();
		this.price = p.getPrice();
		this.imgUrl = p.getImgUrl();
		this.date = p.getDate();
	}
	
	public ProductDTO(Product p, Set<Category> categories) {
		this(p);
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	
	
	
	
	
}
