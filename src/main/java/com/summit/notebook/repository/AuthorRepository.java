package com.summit.notebook.repository;

import org.springframework.roo.addon.layers.repository.jpa.RooJpaRepository;

import com.summit.notebook.domain.Author;

@RooJpaRepository(domainType = Author.class)
public interface AuthorRepository {
}
