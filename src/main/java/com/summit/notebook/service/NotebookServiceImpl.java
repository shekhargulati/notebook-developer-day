package com.summit.notebook.service;

import com.summit.notebook.domain.Notebook;
import com.summit.notebook.repository.NotebookRepository;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class NotebookServiceImpl implements NotebookService {

	@Autowired
    NotebookRepository notebookRepository;

	public long countAllNotebooks() {
        return notebookRepository.count();
    }

	public void deleteNotebook(Notebook notebook) {
        notebookRepository.delete(notebook);
    }

	public Notebook findNotebook(BigInteger id) {
        return notebookRepository.findOne(id);
    }

	public List<Notebook> findAllNotebooks() {
        return notebookRepository.findAll();
    }

	public List<Notebook> findNotebookEntries(int firstResult, int maxResults) {
        return notebookRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveNotebook(Notebook notebook) {
        notebookRepository.save(notebook);
    }

	public Notebook updateNotebook(Notebook notebook) {
        return notebookRepository.save(notebook);
    }
}
