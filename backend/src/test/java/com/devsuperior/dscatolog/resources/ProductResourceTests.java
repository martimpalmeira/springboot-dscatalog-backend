package com.devsuperior.dscatolog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatolog.dto.ProductDTO;
import com.devsuperior.dscatolog.services.ProductService;
import com.devsuperior.dscatolog.services.exceptions.DataBaseException;
import com.devsuperior.dscatolog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatolog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService service;

	@Autowired
	private ObjectMapper objectMapper;

	private PageImpl<ProductDTO> page;

	private ProductDTO productDTO;

	private Long existingId;

	private Long notExistingId;

	private Long dependentId;

	@BeforeEach
	void setUp() throws Exception {
		productDTO = Factory.createProductDTO();

		existingId = 1L;

		notExistingId = 1000L;

		dependentId = 3L;

		page = new PageImpl<ProductDTO>(List.of(productDTO));

		Mockito.when(service.findAllPage(any())).thenReturn(page);

		Mockito.when(service.findById(existingId)).thenReturn(productDTO);
		Mockito.when(service.findById(notExistingId)).thenThrow(ResourceNotFoundException.class);

		Mockito.when(service.update(eq(existingId), any())).thenReturn(productDTO);
		Mockito.when(service.update(eq(notExistingId), any())).thenThrow(ResourceNotFoundException.class);

		Mockito.doNothing().when(service).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(notExistingId);
		Mockito.doThrow(DataBaseException.class).when(service).delete(dependentId);

		Mockito.when(service.insert(any())).thenReturn(productDTO);
	}

	@Test
	public void findAllPage_ShouldReturnProductDTOPage() throws Exception {
		ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}

	@Test
	public void findById_ShouldReturnProductDTO_WhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		result.andExpect(jsonPath("$.price").exists());
	}

	@Test
	public void findById_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/product/{id}", notExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());

		result.andExpect(jsonPath("$.id").doesNotExist());
		result.andExpect(jsonPath("$.name").doesNotExist());
		result.andExpect(jsonPath("$.description").doesNotExist());
		result.andExpect(jsonPath("$.price").doesNotExist());
	}

	@Test
	public void update_ShouldReturnProduct_WhenIdExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc.perform(put("/products/{id}", existingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
	}

	@Test
	public void update_ShouldReturnNotFound_WhenIdDoesNotExists() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc.perform(put("/products/{id}", notExistingId).content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
		result.andExpect(jsonPath("$.id").doesNotExist());
	}

	@Test
	public void insert_ShouldReturnProductDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result = mockMvc.perform(post("/products").content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
	}

	@Test
	public void delete_ShouldDoNothing_WhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("products/{id}", existingId));

		result.andExpect(status().isNoContent());
		result.andExpect(jsonPath("$.id").doesNotExist());
	}

	@Test
	public void delete_ShouldReturnNotFound_WhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(delete("products/{id}", notExistingId));
		
		result.andExpect(status().isNotFound());
	}
}
