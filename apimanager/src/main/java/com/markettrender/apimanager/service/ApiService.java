package com.markettrender.apimanager.service;

import java.util.List;

import com.markettrender.apimanager.model.entity.Api;

public interface ApiService {

	public List<Api> findAll();

	public Api findById(Long id);

	public Api findByName(String name);

	public Api save(Api api);

	public void delete(Long id);

}
