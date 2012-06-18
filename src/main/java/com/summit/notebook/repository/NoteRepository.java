package com.summit.notebook.repository;

import com.summit.notebook.domain.Note;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooMongoRepository(domainType = Note.class)
public interface NoteRepository extends PagingAndSortingRepository<Note, BigInteger> {

    List<com.summit.notebook.domain.Note> findAll();
}
