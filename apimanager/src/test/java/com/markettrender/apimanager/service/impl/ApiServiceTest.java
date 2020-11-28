package com.markettrender.apimanager.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.markettrender.apimanager.model.entity.Api;
import com.markettrender.apimanager.model.repository.ApiRepository;
import com.markettrender.apimanager.service.ApiService;

@SpringBootTest
public class ApiServiceTest {

	@Autowired
	private ApiService apiService;

	@MockBean
	private ApiRepository apiRepo;

	@Test
	public void testCreateApi() {

		Api mockedApi = new Api();
		mockedApi.setName("mockedApi");

		when(apiRepo.save(Mockito.any(Api.class))).thenReturn(mockedApi);

		Api savedApi = apiService.save(mockedApi);

		assertThat(savedApi.getName()).isEqualTo("mockedApi");
	}
}
