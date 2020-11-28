package com.markettrender.apimanager.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MockMvc;

import com.markettrender.apimanager.model.entity.Api;
import com.markettrender.apimanager.service.ApiService;

@WebMvcTest(controllers = ApiRestController.class)
public class ApiControllerTest {

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
	void fetchAllApis() throws Exception {
		
		Mockito.when(apiService.findAll()).thenReturn(apiList);
		
		this.mockMvc.perform(get("/apis"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()", is(apiList.size())));
	}
	
	@Test
	void fetchApiByName() throws Exception {
		
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
}
