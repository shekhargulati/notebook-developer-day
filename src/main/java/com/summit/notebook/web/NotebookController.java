package com.summit.notebook.web;

import com.summit.notebook.domain.Notebook;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/notebooks")
@Controller
@RooWebScaffold(path = "notebooks", formBackingObject = Notebook.class)
@RooWebJson(jsonObject = Notebook.class)
public class NotebookController {
}
