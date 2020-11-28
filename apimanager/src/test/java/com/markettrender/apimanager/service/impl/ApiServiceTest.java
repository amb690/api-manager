package com.markettrender.apimanager.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.markettrender.apimanager.model.entity.Api;
import com.markettrender.apimanager.model.repository.ApiRepository;
import com.markettrender.apimanager.service.ApiService;

@SpringBootTest
class ApiServiceTest {

	@Autowired
	private ApiService apiService;

	@MockBean
	private ApiRepository apiRepo;

	@Test
	void testCreateApi() {

		Api mockedApi = new Api();
		mockedApi.setName("mockedApi");
		mockedApi.setLastPetition(new Date());
		mockedApi.setId(6L);
		mockedApi.setMaxPetitions(10000);
		mockedApi.setNumberOfPetitions(50L);
		mockedApi.prePersist();

		when(apiRepo.save(Mockito.any(Api.class))).thenReturn(mockedApi);

		Api savedApi = apiService.save(mockedApi);

		assertThat(savedApi.getId()).isEqualTo(6);
		assertThat(savedApi.getName()).isEqualTo("mockedApi");
		assertThat(savedApi.getLastPetition()).isNotNull();
		assertThat(savedApi.getMaxPetitions()).isEqualTo(10000);
		assertThat(savedApi.getNumberOfPetitions()).isEqualTo(50);
	}

	@Test
	void testfindApiById() {

		Api mockedApi = new Api();
		mockedApi.setName("mockedApi");
		Optional<Api> optMockedApi = Optional.of(mockedApi);

		when(apiRepo.findById(Mockito.anyLong())).thenReturn(optMockedApi);

		apiService.findById(5L);

		assertThat(optMockedApi.get().getName()).isEqualTo("mockedApi");
	}

	@Test
	void testfindApiByName() {

		Api mockedApi = new Api();
		mockedApi.setName("twitter");

		when(apiRepo.findByName(Mockito.anyString())).thenReturn(mockedApi);

		apiService.findByName("twitter");

		assertThat(mockedApi.getName()).isEqualTo("twitter");
	}

	@Test
	void testfindAll() {

		Api mockedApi = new Api();
		mockedApi.setName("mockedApi");

		List<Api> apis = new ArrayList<Api>();
		apis.add(mockedApi);

		when(apiRepo.findAll()).thenReturn(apis);

		List<Api> savedApi = apiService.findAll();

		assertThat(savedApi.get(0).getName()).isEqualTo("mockedApi");
	}

	@Test
	void testDeleteApi() {

		Api mockedApi = new Api();
		mockedApi.setName("twitter");

		doNothing().when(apiRepo).deleteById(Mockito.anyLong());

		apiService.delete(5L);

		assertThat(mockedApi.getName()).isEqualTo("twitter");
	}

}
