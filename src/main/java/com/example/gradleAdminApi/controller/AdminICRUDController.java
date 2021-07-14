package com.example.gradleAdminApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.gradleAdminApi.ifs.AdminICRUDInterface;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.service.AdminBaseService;

import javax.validation.Valid;

@Component
public abstract class AdminICRUDController<Req, Res, Entity> implements AdminICRUDInterface<Req, Res> {
	
	@Autowired(required = false)
	protected AdminBaseService<Req, Res, Entity> baseService;
	
	@Override
	@GetMapping("")
	public Header<List<Res>> index(Pageable pageable, Authentication authentication) throws Exception {
		return baseService.index(pageable, authentication);
	}

	@Override
	@PostMapping("")
	public Header<Res> create(@RequestBody @Valid Header<Req> request, Authentication authentication) throws Exception {
		return baseService.create(request, authentication);
	}
	
	@Override
	@GetMapping("/{id}")
	public Header<Res> read(@PathVariable Long id, Authentication authentication) throws Exception {
		return baseService.read(id, authentication);
	}	
	
	@Override
	@PutMapping("")
	public Header<Res> update(@RequestBody Header<Req> request, Authentication authentication) throws Exception {
		return baseService.update(request, authentication);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@DeleteMapping("/{id}")
	public Header delete(@PathVariable Long id, Authentication authentication) throws Exception {
		return baseService.delete(id, authentication);
	}
}
