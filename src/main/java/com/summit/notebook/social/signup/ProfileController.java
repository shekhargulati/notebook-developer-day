package com.summit.notebook.social.signup;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.summit.notebook.dao.ProfileJpaDao;
import com.summit.notebook.domain.Profile;
import com.summit.notebook.social.account.UsernameAlreadyInUseException;
import com.summit.notebook.social.message.Message;
import com.summit.notebook.social.message.MessageType;
import com.summit.notebook.social.signin.SignInUtils;

@Controller
public class ProfileController {

    private final ProfileJpaDao profileJpaDao;

    @Inject
    public ProfileController(ProfileJpaDao profileJpaDao) {
        this.profileJpaDao = profileJpaDao;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String registerForm(WebRequest request, Model uiModel) {
        Connection<?> connection = ProviderSignInUtils.getConnection(request);
        Profile profile = null;
        if (connection != null) {
            request.setAttribute("message", new Message(MessageType.INFO, "Your " + StringUtils.capitalize(connection.getKey().getProviderId())
                    + " account is not associated with a notebook account. If you're new, please sign up."), WebRequest.SCOPE_REQUEST);
            profile = Profile.fromTwitterProfile(connection.fetchUserProfile());
        } else {
            profile = new Profile();
        }
        populateEditForm(uiModel, profile);
        return "profiles/create";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String register(@Valid Profile profile, BindingResult formBinding, WebRequest request, Model uiModel) {
        if (formBinding.hasErrors()) {
            populateEditForm(uiModel, profile);
            return "profiles/create";
        }
        profile = createProfile(profile, formBinding);
        if (profile != null) {
            SignInUtils.signin(profile.getUsername());
            ProviderSignInUtils.handlePostSignUp(profile.getUsername(), request);
            return "redirect:/profiles/"
                    + encodeUrlPathSegment(profile.getId().toString(),
                            request);
        }
        return "profiles/create";
    }

    @RequestMapping(value = "/profiles/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("profile", profileJpaDao.findProfile(id));
        uiModel.addAttribute("itemId", id);
        return "profiles/show";
    }

    @RequestMapping(value = "/viewprofile/{username}", produces = "text/html")
    public String showProfile(@PathVariable("username") String username, Model uiModel) {
        Profile profile = profileJpaDao.findProfileByUsername(username);
        uiModel.addAttribute("profile", profile);
        uiModel.addAttribute("itemId", profile.getId());
        return "profiles/show";
    }

    @RequestMapping(value = "/profiles", produces = "text/html")
    public String list(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1)
                    * sizeNo;
            uiModel.addAttribute("profiles",
                    profileJpaDao.findProfileEntries(firstResult, sizeNo));
            float nrOfPages = (float) profileJpaDao.countProfiles() / sizeNo;
            uiModel.addAttribute(
                    "maxPages",
                    (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
                            : nrOfPages));
        } else {
            uiModel.addAttribute("profiles", profileJpaDao.findAllProfiles());
        }
        return "profiles/list";
    }

    private Profile createProfile(Profile profile, BindingResult formBinding) {
        try {
            profileJpaDao.persist(profile);
            return profile;
        } catch (UsernameAlreadyInUseException e) {
            formBinding.rejectValue("username", "user.duplicateUsername", "already in use");
            return null;
        }
    }

    void populateEditForm(Model uiModel, Profile profile) {
        uiModel.addAttribute("profile", profile);
    }

    String encodeUrlPathSegment(String pathSegment,
            WebRequest request) {
        String enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
