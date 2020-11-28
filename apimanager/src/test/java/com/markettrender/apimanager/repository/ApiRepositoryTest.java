package com.markettrender.apimanager.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;

import com.markettrender.apimanager.model.entity.Api;
import com.markettrender.apimanager.model.repository.ApiRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
@ComponentScan(basePackages = { "com.markettrender.apimanager" })
@EntityScan(basePackages = { "com.markettrender.apimanager.model" })
@EnableJpaRepositories(basePackages = { "com.markettrender.apimanager" })
public class ApiRepositoryTest {

	@Autowired
	private ApiRepository apiRepo;

	@Test
	@Rollback(false)
	@Order(1)
	public void testCreateApi() {

		Api mockedApi = new Api();
		mockedApi.setName("mockedApi");

		Api savedApi = apiRepo.save(mockedApi);

		assertThat(savedApi.getName()).isEqualTo("mockedApi");
	}

	@Test
	@Order(2)
	public void testFindProductByName() {
		Api api = apiRepo.findByName("mockedApi");
		assertThat(api.getName()).isEqualTo("mockedApi");
	}

	@Test
	@Rollback(false)
	@Order(3)
	public void testDeleteProduct() {
		Api product = apiRepo.findByName("mockedApi");

		apiRepo.deleteById(product.getId());

		Api deletedProduct = apiRepo.findByName("mockedApi");

		assertThat(deletedProduct).isNull();

	}

}
