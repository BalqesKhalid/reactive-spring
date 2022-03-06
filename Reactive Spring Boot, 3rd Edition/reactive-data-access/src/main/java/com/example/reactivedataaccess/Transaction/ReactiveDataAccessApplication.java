package com.example.reactivedataaccess.Transaction;

import com.mongodb.internal.operation.TransactionOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactiveDataAccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveDataAccessApplication.class, args);
	}


@Bean
	TransactionalOperator transactionalOperator(ReactiveTransactionManager manager){
		return TransactionalOperator.create(manager);
}

}

@Service
@RequiredArgsConstructor
class ReservationService{
private final ReactiveRepository reactiveRepository;
private final TransactionalOperator transactionalOperator;

	Flux<Reservation> saveAll(String ... names){

		Flux<Reservation> reservations = Flux
				.just(names)
				.map(name -> new Reservation(null, name))
				.flatMap(this.reactiveRepository::save)
				.doOnNext(this::assertValid);


		return this.transactionalOperator.transactional(reservations);
	}

	private void assertValid(Reservation r){

		Assert.isTrue(r.getName() != null && r.getName().length()>0
		&& Character.isUpperCase(r.getName().charAt(0)),"the name must start with a capital letter");
	}
}

@Component
@RequiredArgsConstructor
@Log4j2
class SampleDataInitializer{

	private final ReactiveRepository reactiveRepository = null;

	private final ReservationService reservationService;

	@EventListener(ApplicationReadyEvent.class)
	public void ready(){

		Flux<Reservation> reservations = reservationService.saveAll(
				"Madhura", "Josh", "Olga", "Marcin", "Stephane", "Violette", "Dr. syer");

		this.reactiveRepository
				.deleteAll()
				.thenMany(reservations)
				.thenMany(this.reactiveRepository.findAll())
				.subscribe(log::info);
	}


}
interface ReactiveRepository extends ReactiveCrudRepository<Reservation, Integer>{
	Flux<Reservation> FindByName(String name);
}

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Reservation{

	@Id
	private Integer id;

	private String name;
}
