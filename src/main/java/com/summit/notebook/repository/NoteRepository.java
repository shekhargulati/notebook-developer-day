package com.summit.notebook.repository;

import com.summit.notebook.domain.Note;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Note.class)
public interface NoteRepository {

    List<com.summit.notebook.domain.Note> findAll();
}
