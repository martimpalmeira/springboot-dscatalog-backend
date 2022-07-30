package com.devsuperior.dscatolog.tests;

import java.time.Instant;

import com.devsuperior.dscatolog.dto.ProductDTO;
import com.devsuperior.dscatolog.entities.Category;
import com.devsuperior.dscatolog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product p = new Product(1L, "The Lord of the Rings", "Good phone", 900.0,
				"https://www.collinsdictionary.com/images/full/mobilephone_103792316.jpg",
				Instant.parse("2020-07-13T20:50:07Z"));
		p.getCategories().add(createCategory());
		return p;
	}

	public static ProductDTO createProductDTO() {
		Product p = createProduct();
		ProductDTO dto = new ProductDTO(p, p.getCategories());
		return dto;
	}
	
	public static Category createCategory() {
		return new Category(1L, "Eletronics");
	}
}
