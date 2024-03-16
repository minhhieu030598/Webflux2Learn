package com.minhhieu.webflux2learn.repository;

import com.minhhieu.webflux2learn.model.Person;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PersonRepository extends ReactiveCrudRepository<Person, Long>, SpecificationExecute<Person>,
        InsertUpdateRepository<Person> {
    @Query("SELECT nextval('person_id_seq')")
    Mono<Long> nextId();

    @Query("select nextval('person_id_seq') from generate_series(1, :amount)")
    Flux<Long> nextIds(int amount);

}
