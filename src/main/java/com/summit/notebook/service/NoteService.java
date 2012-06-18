package com.summit.notebook.service;

import com.summit.notebook.domain.Note;
import java.math.BigInteger;
import java.util.List;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { com.summit.notebook.domain.Note.class })
public interface NoteService {

	public abstract long countAllNotes();


	public abstract void deleteNote(Note note);


	public abstract Note findNote(BigInteger id);


	public abstract List<Note> findAllNotes();


	public abstract List<Note> findNoteEntries(int firstResult, int maxResults);


	public abstract void saveNote(Note note);


	public abstract Note updateNote(Note note);

}
