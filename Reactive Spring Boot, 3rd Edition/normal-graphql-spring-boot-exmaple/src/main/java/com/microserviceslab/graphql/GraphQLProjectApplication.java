package com.microserviceslab.graphql;

import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import com.microserviceslab.graphql.service.AuthorService;
import com.microserviceslab.graphql.service.BookService;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;

@SpringBootApplication
public class GraphQLProjectApplication {
	
	@Value("classpath:schema.graphqls")
	private Resource schemaGraphQL;
	
	@Autowired
	private BookService bookService;
	@Autowired
	private AuthorService authorService;
	
	public static void main(String[] args) {
		SpringApplication.run(GraphQLProjectApplication.class, args);
	}
	
	@Bean
	public GraphQL graphQL() {
		try {
			TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(new InputStreamReader(schemaGraphQL.getInputStream()));
			
			RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
			.type(TypeRuntimeWiring.newTypeWiring("Query").dataFetcher("getBook", bookService.getBook()))
			.type(TypeRuntimeWiring.newTypeWiring("Book").dataFetcher("author", authorService.getAuthor()))
			.type(TypeRuntimeWiring.newTypeWiring("Mutation").dataFetcher("createBook", bookService.createBook()))
			.build();
			
			GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
			return GraphQL.newGraphQL(schema).build();
			
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		} 
	}
}
