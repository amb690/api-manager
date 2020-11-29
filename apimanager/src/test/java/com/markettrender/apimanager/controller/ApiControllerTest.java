package com.markettrender.apimanager.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markettrender.apimanager.model.entity.Api;
import com.markettrender.apimanager.service.ApiService;

@WebMvcTest(controllers = ApiRestController.class)
class ApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ApiService apiService;

	private List<Api> apiList;

	@BeforeEach
	void setUp() {
		this.apiList = new ArrayList<>();

		Api twitterApi = new Api();
		twitterApi.setName("twitter");
		this.apiList.add(twitterApi);
	}

	@Test
	void fetchAllApisTest() throws Exception {
		
		Mockito.when(apiService.findAll()).thenReturn(apiList);
		
		this.mockMvc.perform(get("/apis"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()", is(apiList.size())));
	}
	
	@Test
	void fetchApiByNameTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.when(apiService.findByName(Mockito.anyString())).thenReturn(stockNewsApi);
		
		this.mockMvc.perform(get("/apis/{name}", apiName))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(stockNewsApi.getName())));
	}
	
	@Test
	void fetchApiByNameThrowsDatabaseExceptionTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.when(apiService.findByName(Mockito.anyString())).thenThrow(new DataAccessException("something") {
			private static final long serialVersionUID = 1L;});
		
		this.mockMvc.perform(get("/apis/{name}", apiName))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	void fetchApiByNameNotFoundTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.when(apiService.findByName(Mockito.anyString())).thenReturn(null);
		
		this.mockMvc.perform(get("/apis/{name}", apiName))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void createApiTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.when(apiService.save(Mockito.any(Api.class))).thenReturn(stockNewsApi);
		
		ObjectMapper mapper = new ObjectMapper();
		String apiBody = mapper.writeValueAsString(stockNewsApi);
		
		this.mockMvc.perform(post("/apis")
			.contentType(MediaType.APPLICATION_JSON)
			.content(apiBody))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.api.name", is(stockNewsApi.getName())));
	}
	
	@Test
	void createApiResultHasErrorsTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.when(apiService.save(Mockito.any(Api.class))).thenReturn(stockNewsApi);
		
		ObjectMapper mapper = new ObjectMapper();
		String apiBody = mapper.writeValueAsString(stockNewsApi);
		
		this.mockMvc.perform(post("/apis")
			.contentType(MediaType.APPLICATION_JSON)
			.content(apiBody))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void createApiThrowsDatabaseExceptionTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		ObjectMapper mapper = new ObjectMapper();
		String apiBody = mapper.writeValueAsString(stockNewsApi);
		
		Mockito.when(apiService.save(Mockito.any(Api.class))).thenThrow(new DataAccessException("something") {
			private static final long serialVersionUID = 1L;
		});
		
		this.mockMvc.perform(post("/apis")
				.contentType(MediaType.APPLICATION_JSON)
				.content(apiBody))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	void updateApiTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.when(apiService.findById(Mockito.anyLong())).thenReturn(stockNewsApi);
		Mockito.when(apiService.save(Mockito.any(Api.class))).thenReturn(stockNewsApi);
		
		ObjectMapper mapper = new ObjectMapper();
		String apiBody = mapper.writeValueAsString(stockNewsApi);
		
		this.mockMvc.perform(put("/apis/{id}", apiId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(apiBody))
			.andExpect(status().isCreated());
	}
	
	@Test
	void updateApiNotFoundTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.when(apiService.save(Mockito.any(Api.class))).thenReturn(stockNewsApi);
		
		ObjectMapper mapper = new ObjectMapper();
		String apiBody = mapper.writeValueAsString(stockNewsApi);
		
		this.mockMvc.perform(put("/apis/{id}", apiId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(apiBody))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void updateApiResultHasErrorsTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.when(apiService.save(Mockito.any(Api.class))).thenReturn(stockNewsApi);
		
		ObjectMapper mapper = new ObjectMapper();
		String apiBody = mapper.writeValueAsString(stockNewsApi);
		
		this.mockMvc.perform(put("/apis/{id}", apiId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(apiBody))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateApiThrowsDatabaseExceptionTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		ObjectMapper mapper = new ObjectMapper();
		String apiBody = mapper.writeValueAsString(stockNewsApi);
		
		Mockito.when(apiService.findById(Mockito.anyLong())).thenReturn(stockNewsApi);
		Mockito.when(apiService.save(Mockito.any(Api.class))).thenThrow(new DataAccessException("something") {
			private static final long serialVersionUID = 1L;
		});
		
		this.mockMvc.perform(put("/apis/{id}", apiId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(apiBody))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	void updateNumberOfPetitionsApiTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.when(apiService.findById(Mockito.anyLong())).thenReturn(stockNewsApi);
		Mockito.when(apiService.save(Mockito.any(Api.class))).thenReturn(stockNewsApi);
		
		ObjectMapper mapper = new ObjectMapper();
		String apiBody = mapper.writeValueAsString(stockNewsApi);
		
		this.mockMvc.perform(put("/apis/petitions/{id}", apiId)
			.param("numberOfPetitions", "50000")
			.contentType(MediaType.APPLICATION_JSON)
			.content(apiBody))
			.andExpect(status().isCreated());
	}
	
	@Test
	void updateNumberOfPetitionsNullApiTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.when(apiService.findById(Mockito.anyLong())).thenReturn(null);
		Mockito.when(apiService.save(Mockito.any(Api.class))).thenReturn(stockNewsApi);
		
		ObjectMapper mapper = new ObjectMapper();
		String apiBody = mapper.writeValueAsString(stockNewsApi);
		
		this.mockMvc.perform(put("/apis/petitions/{id}", apiId)
			.param("numberOfPetitions", "50000")
			.contentType(MediaType.APPLICATION_JSON)
			.content(apiBody))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void updateApiNumberOfPetitionsThrowsDatabaseExceptionTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		ObjectMapper mapper = new ObjectMapper();
		String apiBody = mapper.writeValueAsString(stockNewsApi);
		
		Mockito.when(apiService.findById(Mockito.anyLong())).thenReturn(stockNewsApi);
		Mockito.when(apiService.save(Mockito.any(Api.class))).thenThrow(new DataAccessException("something") {
			private static final long serialVersionUID = 1L;
		});
		
		this.mockMvc.perform(put("/apis/petitions/{id}", apiId)
				.param("numberOfPetitions", "50000")
				.contentType(MediaType.APPLICATION_JSON)
				.content(apiBody))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	void deleteApiTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.doNothing().when(apiService).delete(Mockito.anyLong());
		
		this.mockMvc.perform(delete("/apis/{id}", apiId))
			.andExpect(status().isOk());
	}
	
	@Test
	void deleteApiThrowsDatabaseExceptionTest() throws Exception {
		
		final Long apiId = 5L;
		final String apiName = "stockNews";
		final Api stockNewsApi = new Api();
		stockNewsApi.setId(apiId);
		stockNewsApi.setName(apiName);
		
		Mockito.doThrow(new DataAccessException("something") {
			private static final long serialVersionUID = 1L;
		}).when(apiService).delete(Mockito.anyLong());
		
		this.mockMvc.perform(delete("/apis/{id}", apiId))
			.andExpect(status().isInternalServerError());
	}
	
}
