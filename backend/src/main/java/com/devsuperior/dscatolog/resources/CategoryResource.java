package com.devsuperior.dscatolog.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatolog.entities.Category;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@GetMapping
	public ResponseEntity<List<Category>> getCategories() {
		List<Category> categories = new ArrayList<>(
				Arrays.asList(new Category(1L, "Books"), new Category(2L, "Eletronics")));

		return ResponseEntity.ok().body(categories);
	}

}
