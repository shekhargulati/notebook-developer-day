package com.summit.notebook.service;

import java.math.BigInteger;
import java.util.List;

import com.summit.notebook.domain.Note;
import com.summit.notebook.domain.Notebook;

public interface NotebookService {

    public abstract long countAllNotebooks();

    public abstract void deleteNotebook(Notebook notebook);

    public abstract Notebook findNotebook(BigInteger id);

    public abstract List<Notebook> findAllNotebooks();

    public abstract List<Notebook> findNotebookEntries(int firstResult, int maxResults);

    public abstract void saveNotebook(Notebook notebook);

    public abstract Notebook updateNotebook(Notebook notebook);

    public void pushNotesToNotebook(BigInteger notebookId, Note note);

    public List<Note> findAllNotes(BigInteger notebookId);

    public List<Note> findNotes(BigInteger notebookId, int start, int end);

    int notesCount(BigInteger notebookId);

    public Note findNote(BigInteger notebookId, String noteId);

    public void updateNote(BigInteger notebookId, Note note);

    void removeNoteFromNotebook(BigInteger notebookId, String noteId);

}
