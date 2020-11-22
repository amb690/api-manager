package com.markettrender.apimanager.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.markettrender.apimanager.model.entity.Api;

public interface ApiRepository extends CrudRepository<Api, Long> {

	@Query("select a from Api a where a.name like %?1%")
	public Api findByName(String name);

	public Api findByNameLikeIgnoreCase(String name);
}
