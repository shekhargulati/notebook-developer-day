package com.summit.notebook.repository;

import com.summit.notebook.domain.Notebook;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Notebook.class)
public interface NotebookRepository {

    List<com.summit.notebook.domain.Notebook> findAll();
}
