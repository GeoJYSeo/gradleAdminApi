package com.example.gradleAdminApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.example.gradleAdminApi.ifs.CUDInterface;
import com.example.gradleAdminApi.model.network.Header;
import com.example.gradleAdminApi.service.BaseService;

import javax.validation.Valid;

@Component
public abstract class CUDController<Req, Res, Entity> implements CUDInterface<Req, Res> {
	
	@Autowired(required = false)
	protected BaseService<Req, Res, Entity> baseService;

	@Override
	@PostMapping("")
	public Header<Res> create(@RequestBody @Valid Header<Req> request, Authentication authentication) throws Exception {
		return baseService.create(request, authentication);
	}
	
	@Override
	@PutMapping("")
	public Header<Res> update(@RequestBody Header<Req> request, Authentication authentication) throws Exception {
		return baseService.update(request, authentication);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@DeleteMapping("/{id}")
	public Header delete(@PathVariable Long id, @RequestParam(name = "user-id") Long userId, Authentication authentication) throws Exception {
		return baseService.delete(id, userId, authentication);
	}
}
