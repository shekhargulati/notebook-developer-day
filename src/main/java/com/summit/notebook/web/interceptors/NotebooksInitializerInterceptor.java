package com.summit.notebook.web.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.summit.notebook.domain.Notebook;
import com.summit.notebook.security.SecurityUtils;
import com.summit.notebook.service.NotebookService;

@Component
public class NotebooksInitializerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private NotebookService notebookService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = SecurityUtils.getCurrentLoggedInUsername();
        if(username == null){
            return true;
        }
        List<Notebook> notebooks = notebookService.findAllNotebookForUser(username);
        request.setAttribute("notebooks", notebooks);
        return true;
    }
}
