package com.summit.notebook.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.summit.notebook.domain.Notebook;

@Repository
public interface NotebookRepository extends PagingAndSortingRepository<Notebook, BigInteger> {

    List<com.summit.notebook.domain.Notebook> findAll();
}
