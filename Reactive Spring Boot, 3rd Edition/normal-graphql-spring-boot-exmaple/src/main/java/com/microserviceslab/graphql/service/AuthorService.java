package com.microserviceslab.graphql.service;

import org.springframework.stereotype.Service;

import com.microserviceslab.graphql.model.AuthorEntity;
import com.microserviceslab.graphql.model.BookEntity;

import graphql.schema.AsyncDataFetcher;

@Service
public class AuthorService {
	public AsyncDataFetcher<AuthorEntity> getAuthor(){
		return AsyncDataFetcher.async(env -> {
			BookEntity bookEntity = env.getSource();
			return bookEntity.getAuthor();
		});
	}
}
