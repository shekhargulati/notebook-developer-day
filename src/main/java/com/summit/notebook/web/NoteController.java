package com.summit.notebook.web;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.summit.notebook.domain.Note;
import com.summit.notebook.domain.Notebook;
import com.summit.notebook.service.NotebookService;

@Controller
public class NoteController {

    @Autowired
    NotebookService notebookService;

    @Autowired
    private Twitter twitter;

    @RequestMapping(value = "/notebooks/{notebookId}/notes", produces = "text/html", method = RequestMethod.PUT)
    public String addNote(@PathVariable("notebookId") BigInteger notebookId,
            @Valid Note note, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("itemId", notebookId);
            populateEditForm(uiModel, note);
            return "notes/create";
        }
        uiModel.asMap().clear();
        Notebook notebook = notebookService.findNotebook(notebookId);
        note.setId(UUID.randomUUID().toString());
        notebook.getNotes().add(note);
        notebookService.updateNotebook(notebook);
        return "redirect:/notebooks/"
                + notebookId
                + "/notes/"
                + encodeUrlPathSegment(note.getId().toString(),
                        httpServletRequest);
    }

    @RequestMapping(value = "/notebooks/{notebookId}/notes", params = "form", produces = "text/html")
    public String createForm(@PathVariable("notebookId") BigInteger notebookId,
            Model uiModel) {
        populateEditForm(uiModel, new Note());
        uiModel.addAttribute("itemId", notebookId);
        return "notes/create";
    }

    @RequestMapping(value = "/notebooks/{notebookId}/notes/{noteId}", produces = "text/html")
    public String show(@PathVariable("notebookId") BigInteger notebookId,
            @PathVariable("noteId") String noteId, Model uiModel) {

        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("note",
                notebookService.findNote(notebookId, noteId));
        uiModel.addAttribute("itemId", noteId);
        uiModel.addAttribute("notebook", notebookService.findNotebook(notebookId));
        return "notes/show";
    }

    @RequestMapping(value = "/twitter/tweet", produces = "text/html")
    public String tweet(@RequestParam("tweetUrl") String tweetUrl) {
        twitter.timelineOperations().updateStatus("Created a new note " + tweetUrl);
        return "index";
    }

    @RequestMapping(value = "/notebooks/{notebookId}/notes", produces = "text/html")
    public String list(@PathVariable("notebookId") BigInteger notebookId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int start = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            int end = start + sizeNo;
            uiModel.addAttribute("notes",
                    notebookService.findNotes(notebookId, start, end));
            float nrOfPages = (float) notebookService.notesCount(notebookId)
                    / sizeNo;
            uiModel.addAttribute(
                    "maxPages",
                    (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
                            : nrOfPages));
        } else {
            uiModel.addAttribute("notes",
                    notebookService.findAllNotes(notebookId));
        }
        return "notes/list";
    }

    @RequestMapping(value = "/notebooks/{notebookId}/notes/update", method = RequestMethod.PUT, produces = "text/html")
    public String update(@PathVariable("notebookId") BigInteger notebookId,
            @Valid Note note, BindingResult bindingResult, Model uiModel,
            HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, note);
            return "notes/update";
        }
        uiModel.asMap().clear();
        notebookService.updateNote(notebookId, note);
        uiModel.addAttribute("notebookId", notebookId);
        return "redirect:/notebooks/"
                + encodeUrlPathSegment(notebookId.toString(),
                        httpServletRequest)
                + "/notes/"
                + encodeUrlPathSegment(note.getId().toString(),
                        httpServletRequest);
    }

    @RequestMapping(value = "/notebooks/{notebookId}/notes/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("notebookId") BigInteger notebookId, @PathVariable("id") String noteId, Model uiModel) {
        populateEditForm(uiModel, notebookService.findNote(notebookId, noteId));
        uiModel.addAttribute("notebookId", notebookId);
        return "notes/update";
    }

    @RequestMapping(value = "/notebooks/{notebookId}/notes/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("notebookId") BigInteger notebookId, @PathVariable("id") String noteId,
            @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        uiModel.asMap().clear();
        notebookService.removeNoteFromNotebook(notebookId, noteId);
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/notebooks/" + notebookId + "/notes";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute(
                "note_created_date_format",
                DateTimeFormat.patternForStyle("M-",
                        LocaleContextHolder.getLocale()));
    }

    void populateEditForm(Model uiModel, Note note) {
        uiModel.addAttribute("note", note);
        addDateTimeFormatPatterns(uiModel);
    }

    String encodeUrlPathSegment(String pathSegment,
            HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }

}
