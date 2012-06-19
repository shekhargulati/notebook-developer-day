package com.summit.notebook.jpa;

import java.util.List;

import com.summit.notebook.domain.Author;

public interface IAuthorRepository {

    public abstract long countAuthors();

    public abstract List<Author> findAllAuthors();

    public abstract Author findAuthor(Long id);

    public abstract List<Author> findAuthorEntries(int firstResult,
            int maxResults);

    public abstract Author findAuthorByUsernameAndPassword(String email,
            String password);

    public abstract void persist(Author author);

    public abstract void remove(Author author);

    public abstract void flush();

    public abstract void clear();

    public abstract Author merge(Author author);

    public abstract Author findAuthorByUsername(String username);

}