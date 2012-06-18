package com.summit.notebook.web;

import com.summit.notebook.domain.Note;
import com.summit.notebook.domain.Notebook;
import com.summit.notebook.service.NoteService;
import com.summit.notebook.service.NotebookService;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	@Autowired
    NoteService noteService;

	@Autowired
    NotebookService notebookService;

	public Converter<Note, String> getNoteToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.summit.notebook.domain.Note, java.lang.String>() {
            public String convert(Note note) {
                return new StringBuilder().append(note.getTitle()).append(" ").append(note.getText()).append(" ").append(note.getCreated()).toString();
            }
        };
    }

	public Converter<BigInteger, Note> getIdToNoteConverter() {
        return new org.springframework.core.convert.converter.Converter<java.math.BigInteger, com.summit.notebook.domain.Note>() {
            public com.summit.notebook.domain.Note convert(java.math.BigInteger id) {
                return noteService.findNote(id);
            }
        };
    }

	public Converter<String, Note> getStringToNoteConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.summit.notebook.domain.Note>() {
            public com.summit.notebook.domain.Note convert(String id) {
                return getObject().convert(getObject().convert(id, BigInteger.class), Note.class);
            }
        };
    }

	public Converter<Notebook, String> getNotebookToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.summit.notebook.domain.Notebook, java.lang.String>() {
            public String convert(Notebook notebook) {
                return new StringBuilder().append(notebook.getName()).append(" ").append(notebook.getDescription()).append(" ").append(notebook.getCreated()).append(" ").append(notebook.getAuthor()).toString();
            }
        };
    }

	public Converter<BigInteger, Notebook> getIdToNotebookConverter() {
        return new org.springframework.core.convert.converter.Converter<java.math.BigInteger, com.summit.notebook.domain.Notebook>() {
            public com.summit.notebook.domain.Notebook convert(java.math.BigInteger id) {
                return notebookService.findNotebook(id);
            }
        };
    }

	public Converter<String, Notebook> getStringToNotebookConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.summit.notebook.domain.Notebook>() {
            public com.summit.notebook.domain.Notebook convert(String id) {
                return getObject().convert(getObject().convert(id, BigInteger.class), Notebook.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getNoteToStringConverter());
        registry.addConverter(getIdToNoteConverter());
        registry.addConverter(getStringToNoteConverter());
        registry.addConverter(getNotebookToStringConverter());
        registry.addConverter(getIdToNotebookConverter());
        registry.addConverter(getStringToNotebookConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
