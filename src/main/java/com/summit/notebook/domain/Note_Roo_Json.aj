// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.summit.notebook.domain;

import com.summit.notebook.domain.Note;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Note_Roo_Json {
    
    public String Note.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static Note Note.fromJsonToNote(String json) {
        return new JSONDeserializer<Note>().use(null, Note.class).deserialize(json);
    }
    
    public static String Note.toJsonArray(Collection<Note> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<Note> Note.fromJsonArrayToNotes(String json) {
        return new JSONDeserializer<List<Note>>().use(null, ArrayList.class).use("values", Note.class).deserialize(json);
    }
    
}