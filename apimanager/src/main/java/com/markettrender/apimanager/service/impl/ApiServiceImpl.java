package com.markettrender.apimanager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.markettrender.apimanager.model.entity.Api;
import com.markettrender.apimanager.model.repository.ApiRepository;
import com.markettrender.apimanager.service.ApiService;

@Service
public class ApiServiceImpl implements ApiService {

	@Autowired
	private ApiRepository apiRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Api> findAll() {
		return (List<Api>) apiRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Api findById(Long id) {
		return apiRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Api findByName(String name) {
		return apiRepository.findByNameLikeIgnoreCase(name);
	}

	@Override
	@Transactional
	public Api save(Api api) {
		return apiRepository.save(api);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		apiRepository.deleteById(id);
	}

}
