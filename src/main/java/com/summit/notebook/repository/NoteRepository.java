package com.summit.notebook.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.summit.notebook.domain.Note;

@Repository
public interface NoteRepository extends PagingAndSortingRepository<Note, BigInteger> {

    List<com.summit.notebook.domain.Note> findAll();
}
