package com.summit.notebook.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.summit.notebook.dao.ProfileJpaDao;
import com.summit.notebook.domain.Profile;

@RequestMapping("/authors")
@Controller
public class AuthorController {

    @Autowired
    private ProfileJpaDao profileJpaDao;

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Profile author, BindingResult bindingResult,
            Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, author);
            return "authors/create";
        }
        uiModel.asMap().clear();
        profileJpaDao.persist(author);
        return "redirect:/authors/"
                + encodeUrlPathSegment(author.getId().toString(),
                        httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Profile());
        return "authors/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("author", profileJpaDao.findProfile(id));
        uiModel.addAttribute("itemId", id);
        return "authors/show";
    }

    @RequestMapping(value = "/profile/{username}", produces = "text/html")
    public String showUser(@PathVariable("username") String username, Model uiModel) {
        Profile author = profileJpaDao.findProfileByUsername(username);
        uiModel.addAttribute("author", author);
        uiModel.addAttribute("itemId", author.getId());
        return "authors/show";
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
            uiModel.addAttribute("authors",
                    profileJpaDao.findProfileEntries(firstResult, sizeNo));
            float nrOfPages = (float) profileJpaDao.countProfiles() / sizeNo;
            uiModel.addAttribute(
                    "maxPages",
                    (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
                            : nrOfPages));
        } else {
            uiModel.addAttribute("authors", profileJpaDao.findAllProfiles());
        }
        return "authors/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Profile author, BindingResult bindingResult,
            Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, author);
            return "authors/update";
        }
        uiModel.asMap().clear();
        profileJpaDao.merge(author);
        return "redirect:/authors/"
                + encodeUrlPathSegment(author.getId().toString(),
                        httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, profileJpaDao.findProfile(id));
        return "authors/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            Model uiModel) {
        Profile author = profileJpaDao.findProfile(id);
        profileJpaDao.remove(author);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/authors";
    }

    void populateEditForm(Model uiModel, Profile author) {
        uiModel.addAttribute("author", author);
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
