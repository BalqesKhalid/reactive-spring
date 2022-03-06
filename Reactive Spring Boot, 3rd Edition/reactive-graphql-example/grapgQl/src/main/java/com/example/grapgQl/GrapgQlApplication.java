package com.example.grapgQl;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
@SpringBootApplication
public class GrapgQlApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrapgQlApplication.class, args);
	}

}

record Order(Integer id, Integer customerId) {

}

record Customer(Integer id, String name) {

}

@Component
class CrmRuntimeWiringConfigurer implements RuntimeWiringConfigurer {

	private final CrmClient crm;

	CrmRuntimeWiringConfigurer(CrmClient crm) {
		this.crm = crm;
	}

	@Override
	public void configure(RuntimeWiring.Builder builder) {
		builder.type("Query",builder1 -> builder1.dataFetcher("cutomers", new DataFetcher() {
			@Override
			public Object get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
				return crm.getCustomers();
			}
		}) );
	}
}

@Component
class CrmClient {

	private final Map<Customer , Collection<Order>> db = new ConcurrentHashMap<>();

	private final AtomicInteger id = new AtomicInteger();

	CrmClient(){

		Flux.fromIterable(Arrays.asList("Yuxin","Josh","Madhura"
		,"Olga","Violetta","Dr. Syer","Stephane","JUrgen"))
				.flatMap(this::addCustomer)
				.subscribe(customer ->{
					var list = this.db
							.get(customer);
					for (var orderId = 1 ; orderId <=(Math.random() *100); orderId++){
						list.add(new Order(orderId,customer.id()));
					}
				});
	}

	Flux<Customer> getCustomers(){

		return Flux.fromIterable(this.db.keySet());
	}
	Flux<Customer> getCustomerByName(String name){

		return getCustomers().filter(c->c.name().equalsIgnoreCase(name));
	}

	Flux<Customer> getCustomerById(Integer id){

		return getCustomers().filter(c->c.id() == id);
	}

	Mono<Customer> addCustomer(String name){
		var key = new Customer(id(),name);
		this.db.put(key,new CopyOnWriteArrayList<>());
		return Mono.just(key);
	}

	Flux<Order> getOrdersFor(Integer customerId){
		return getCustomerById(customerId)
				.map(this.db::get)
				.flatMap(Flux::fromIterable);
	}
	Flux<CustomerEvent> getCutomerEvents(Integer customerId){

		return getCustomerById(customerId)
				.flatMap(customer -> Flux.fromStream(
						Stream.generate(()->{
							var event= Math.random()>.5?CustomerEventType.UPDATED :
									CustomerEventType.CREATED;
							return new CustomerEvent(customer,event);
						})
				))
				.take(10)
				.delayElements(Duration.ofSeconds(1));

	}
	private int id(){
		return this.id.incrementAndGet();
	}
}

record CustomerEvent(Customer customer, CustomerEventType customerEvent){

}

enum CustomerEventType{
	CREATED, UPDATED
}

@Controller
class CrmGraphqlController{

	private final CrmClient crm;


	CrmGraphqlController(CrmClient crm) {
		this.crm = crm;
	}
//specialization of schema mapping
//	@QueryMapping(value = "customers")
	//@SchemaMapping(typeName = "Query",field ="customer")
	//or
	@QueryMapping
	Flux<Customer> customers(){
		return this.crm.getCustomers();
	}
	@QueryMapping
	Flux<Customer> getCustomerByName(@Argument String name){
		return this.crm.getCustomerByName(name);
	}

	@SchemaMapping
	Flux<Order> orders(Customer customer){
		return this.crm.getOrdersFor(customer.id());
	}

}
