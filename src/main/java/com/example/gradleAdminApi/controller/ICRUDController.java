package com.example.gradleAdminApi.controller;

import java.util.List;

import com.example.gradleAdminApi.service.AllBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.example.gradleAdminApi.ifs.ICRUDInterface;
import com.example.gradleAdminApi.model.network.Header;

import javax.validation.Valid;

@Component
public abstract class ICRUDController<Req, Res, Entity> implements ICRUDInterface<Req, Res> {
	
	@Autowired(required = false)
	protected AllBaseService<Req, Res, Entity> allBaseService;
	
	@Override
	@GetMapping("")
	public Header<List<Res>> index(
			@RequestParam(name="user-id") Long id, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size=8) Pageable pageable, Authentication authentication) throws Exception {
		return allBaseService.index(id, pageable, authentication);
	}

	@Override
	@PostMapping("")
	public Header<Res> create(@RequestBody @Valid Header<Req> request, Authentication authentication) throws Exception {
		return allBaseService.create(request, authentication);
	}
	
	@Override
	@GetMapping("/{id}")
	public Header<Res> read(@PathVariable Long id, @RequestParam(name="user-id") Long userId, Authentication authentication) throws Exception {
		return allBaseService.read(id, userId, authentication);
	}	
	
	@Override
	@PutMapping("")
	public Header<Res> update(@RequestBody Header<Req> request, Authentication authentication) throws Exception {
		return allBaseService.update(request, authentication);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	@DeleteMapping("/{id}")
	public Header delete(@PathVariable Long id, Authentication authentication) throws Exception {
		return allBaseService.delete(id, authentication);
	}
}
