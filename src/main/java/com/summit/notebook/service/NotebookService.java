package com.summit.notebook.service;

import com.summit.notebook.domain.Notebook;
import java.math.BigInteger;
import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.summit.notebook.domain.Notebook.class })
public interface NotebookService {

	public abstract long countAllNotebooks();


	public abstract void deleteNotebook(Notebook notebook);


	public abstract Notebook findNotebook(BigInteger id);


	public abstract List<Notebook> findAllNotebooks();


	public abstract List<Notebook> findNotebookEntries(int firstResult, int maxResults);


	public abstract void saveNotebook(Notebook notebook);


	public abstract Notebook updateNotebook(Notebook notebook);

}
