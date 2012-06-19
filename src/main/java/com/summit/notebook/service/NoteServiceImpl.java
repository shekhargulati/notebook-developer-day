package com.summit.notebook.service;

import com.summit.notebook.domain.Note;
import com.summit.notebook.repository.NoteRepository;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    @Autowired
    NoteRepository noteRepository;

    public long countAllNotes() {
        return noteRepository.count();
    }

    public void deleteNote(Note note) {
        noteRepository.delete(note);
    }

    public Note findNote(BigInteger id) {
        return noteRepository.findOne(id);
    }

    public List<Note> findAllNotes() {
        return noteRepository.findAll();
    }

    public List<Note> findNoteEntries(int firstResult, int maxResults) {
        return noteRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

    public void saveNote(Note note) {
        noteRepository.save(note);
    }

    public Note updateNote(Note note) {
        return noteRepository.save(note);
    }
}
