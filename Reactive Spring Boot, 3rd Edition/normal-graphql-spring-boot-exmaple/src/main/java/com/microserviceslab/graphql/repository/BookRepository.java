package com.microserviceslab.graphql.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microserviceslab.graphql.model.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Integer>{

}
