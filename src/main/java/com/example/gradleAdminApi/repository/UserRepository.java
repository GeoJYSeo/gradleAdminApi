package com.example.gradleAdminApi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gradleAdminApi.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUserEmail(String userEmail);

	List<User> findByUserEmailContaining(String keyword);

	List<User> findByUserNameContaining(String keyword);

	List<User> findByUserSurnameContaining(String keyword);
}
