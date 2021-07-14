package com.example.gradleAdminApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.example.gradleAdminApi.ifs.CUDInterface;

@Component
public abstract class BaseService<Req, Res, Entity> implements CUDInterface<Req, Res>{

	@Autowired(required = false)
	protected JpaRepository<Entity, Long> baseRepository;
}
