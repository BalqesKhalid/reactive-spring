package com.microserviceslab.graphql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microserviceslab.graphql.model.AuthorEntity;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {
	
}
