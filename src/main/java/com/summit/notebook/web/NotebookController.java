package com.summit.notebook.web;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.summit.notebook.domain.Notebook;
import com.summit.notebook.security.SecurityUtils;
import com.summit.notebook.service.NotebookService;

@RequestMapping("/notebooks")
@Controller
public class NotebookController {

    @Autowired
    NotebookService notebookService;

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Notebook notebook, BindingResult bindingResult,
            Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, notebook);
            return "notebooks/create";
        }
        uiModel.asMap().clear();
        notebook.setAuthor(SecurityUtils.getCurrentLoggedInUsername());
        notebookService.saveNotebook(notebook);
        return "redirect:/notebooks/"
                + encodeUrlPathSegment(notebook.getId().toString(),
                        httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Notebook());
        return "notebooks/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") BigInteger id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("notebook", notebookService.findNotebook(id));
        uiModel.addAttribute("itemId", id);
        return "notebooks/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1)
                    * sizeNo;
            uiModel.addAttribute("notebooks",
                    notebookService.findNotebookEntries(firstResult, sizeNo));
            float nrOfPages = (float) notebookService.countAllNotebooks()
                    / sizeNo;
            uiModel.addAttribute(
                    "maxPages",
                    (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
                            : nrOfPages));
        } else {
            uiModel.addAttribute("notebooks",
                    notebookService.findAllNotebooks());
        }
        addDateTimeFormatPatterns(uiModel);
        return "notebooks/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Notebook notebook, BindingResult bindingResult,
            Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, notebook);
            return "notebooks/update";
        }
        uiModel.asMap().clear();
        notebook.setAuthor(SecurityUtils.getCurrentLoggedInUsername());
        notebookService.updateNotebook(notebook);
        return "redirect:/notebooks/"
                + encodeUrlPathSegment(notebook.getId().toString(),
                        httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") BigInteger id, Model uiModel) {
        populateEditForm(uiModel, notebookService.findNotebook(id));
        return "notebooks/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") BigInteger id,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            Model uiModel) {
        Notebook notebook = notebookService.findNotebook(id);
        notebookService.deleteNotebook(notebook);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/notebooks";
    }

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") BigInteger id) {
        Notebook notebook = notebookService.findNotebook(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (notebook == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(notebook.toJson(), headers,
                HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Notebook> result = notebookService.findAllNotebooks();
        return new ResponseEntity<String>(Notebook.toJsonArray(result),
                headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Notebook notebook = Notebook.fromJsonToNotebook(json);
        notebook.setAuthor(SecurityUtils.getCurrentLoggedInUsername());
        notebookService.saveNotebook(notebook);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Notebook notebook : Notebook.fromJsonArrayToNotebooks(json)) {
            notebook.setAuthor(SecurityUtils.getCurrentLoggedInUsername());
            notebookService.saveNotebook(notebook);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Notebook notebook = Notebook.fromJsonToNotebook(json);
        notebook.setAuthor(SecurityUtils.getCurrentLoggedInUsername());
        if (notebookService.updateNotebook(notebook) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Notebook notebook : Notebook.fromJsonArrayToNotebooks(json)) {
            notebook.setAuthor(SecurityUtils.getCurrentLoggedInUsername());
            if (notebookService.updateNotebook(notebook) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(
            @PathVariable("id") BigInteger id) {
        Notebook notebook = notebookService.findNotebook(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (notebook == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        notebookService.deleteNotebook(notebook);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute(
                "notebook_created_date_format",
                DateTimeFormat.patternForStyle("M-",
                        LocaleContextHolder.getLocale()));
    }

    void populateEditForm(Model uiModel, Notebook notebook) {
        uiModel.addAttribute("notebook", notebook);
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
