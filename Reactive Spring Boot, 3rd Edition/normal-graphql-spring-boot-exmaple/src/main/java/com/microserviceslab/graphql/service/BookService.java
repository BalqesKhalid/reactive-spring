package com.microserviceslab.graphql.service;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microserviceslab.graphql.model.BookEntity;
import com.microserviceslab.graphql.repository.BookRepository;

import graphql.schema.AsyncDataFetcher;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private ObjectMapper objectMapper;

	public AsyncDataFetcher<BookEntity> getBook() {
		return AsyncDataFetcher.async(env -> bookRepository.findById(env.getArgument("id")).map(Function.identity()).orElse(null));
	}

	public AsyncDataFetcher<BookEntity> createBook() {
		return AsyncDataFetcher.async(env -> bookRepository.save(objectMapper.reader().readValue(objectMapper.writeValueAsString(env.getArgument("book")), BookEntity.class)));
	}
}
