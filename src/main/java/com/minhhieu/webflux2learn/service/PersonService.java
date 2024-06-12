package com.minhhieu.webflux2learn.service;

import com.minhhieu.webflux2learn.annotation.Permission;
import com.minhhieu.webflux2learn.model.filter.PersonFilter;
import com.minhhieu.webflux2learn.model.request.CreatePersonRequest;
import com.minhhieu.webflux2learn.model.request.UpdatePersonRequest;
import com.minhhieu.webflux2learn.model.response.PersonResponse;
import com.minhhieu.webflux2learn.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface PersonService {
    Mono<Page<PersonResponse>> getPersons(PersonFilter filter, Pageable pageable);

    // The annotation can't work here, cause project follows by functional style, the annotation will not trigger
    @Permission(subject = Constant.CREATE_PERSON, action = "#request.status", object = "#request.name")
    Mono<Void> create(CreatePersonRequest request);

    Mono<PersonResponse> update(UpdatePersonRequest request);

    Mono<Void> delete(Long id);
}
