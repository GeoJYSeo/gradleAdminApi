package com.example.gradleAdminApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.example.gradleAdminApi.ifs.AdminICRUDInterface;

@Component
public abstract class AdminBaseService<Req, Res, Entity> implements AdminICRUDInterface<Req, Res>{

	@Autowired(required = false)
	protected JpaRepository<Entity, Long> adminBaseRepository;
}
