// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.summit.notebook.service;

import com.summit.notebook.domain.Note;
import com.summit.notebook.service.NoteService;
import java.math.BigInteger;
import java.util.List;

privileged aspect NoteService_Roo_Service {
    
    public abstract long NoteService.countAllNotes();    
    public abstract void NoteService.deleteNote(Note note);    
    public abstract Note NoteService.findNote(BigInteger id);    
    public abstract List<Note> NoteService.findAllNotes();    
    public abstract List<Note> NoteService.findNoteEntries(int firstResult, int maxResults);    
    public abstract void NoteService.saveNote(Note note);    
    public abstract Note NoteService.updateNote(Note note);    
}