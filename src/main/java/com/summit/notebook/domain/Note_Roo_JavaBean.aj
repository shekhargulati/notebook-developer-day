// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.summit.notebook.domain;

import java.lang.String;
import java.util.Date;

privileged aspect Note_Roo_JavaBean {
    
    public String Note.getTitle() {
        return this.title;
    }
    
    public void Note.setTitle(String title) {
        this.title = title;
    }
    
    public String Note.getText() {
        return this.text;
    }
    
    public void Note.setText(String text) {
        this.text = text;
    }
    
    public Date Note.getCreated() {
        return this.created;
    }
    
    public void Note.setCreated(Date created) {
        this.created = created;
    }
    
    public String[] Note.getTags() {
        return this.tags;
    }
    
    public void Note.setTags(String[] tags) {
        this.tags = tags;
    }
    
}
