package com.minhhieu.webflux2learn.service.impl;

import com.minhhieu.webflux2learn.mapper.PersonMapper;
import com.minhhieu.webflux2learn.model.Person;
import com.minhhieu.webflux2learn.model.filter.PersonFilter;
import com.minhhieu.webflux2learn.model.request.CreatePersonRequest;
import com.minhhieu.webflux2learn.model.response.PersonResponse;
import com.minhhieu.webflux2learn.repository.PersonRepository;
import com.minhhieu.webflux2learn.service.PersonService;
import com.minhhieu.webflux2learn.util.Filters;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;


@Service
@Log4j2
public class PersonServiceImpl implements PersonService {
    private final PersonMapper personMapper;
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public Mono<Page<PersonResponse>> getPersons(PersonFilter filter, Pageable pageable) {
        var criteria = Filters.toCriteria(filter);
        return personRepository.findAll(criteria, pageable, Person.class)
                .map(personMapper::map)
                .collectList()
                .flatMap(persons -> {
                    if (persons.size() < pageable.getPageSize()) {
                        var total = persons.size() + (pageable.getPageSize() * pageable.getPageNumber());
                        return Mono.just(new PageImpl<>(persons, pageable, total));
                    } else {
                        return personRepository.count(criteria)
                                .flatMap(total -> Mono.just(new PageImpl<>(persons, pageable, total)));
                    }
                });
    }

    @Override
    public Mono<Void> create(CreatePersonRequest request) {
        return personRepository.nextId()
                .map(id -> personMapper.map(id, request, OffsetDateTime.now()))
                .flatMap(personRepository::insert)
                .then();
    }


}
