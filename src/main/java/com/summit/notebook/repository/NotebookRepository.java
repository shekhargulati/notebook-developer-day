package com.summit.notebook.repository;

import com.summit.notebook.domain.Notebook;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooMongoRepository(domainType = Notebook.class)
public interface NotebookRepository extends PagingAndSortingRepository<Notebook, BigInteger> {

    List<com.summit.notebook.domain.Notebook> findAll();
}
