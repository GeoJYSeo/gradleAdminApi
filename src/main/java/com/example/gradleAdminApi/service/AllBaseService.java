package com.example.gradleAdminApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.example.gradleAdminApi.ifs.ICRUDInterface;

@Component
public abstract class AllBaseService<Req, Res, Entity> implements ICRUDInterface<Req, Res>{

	@Autowired(required = false)
	protected JpaRepository<Entity, Long> allBaseRepository;
}
