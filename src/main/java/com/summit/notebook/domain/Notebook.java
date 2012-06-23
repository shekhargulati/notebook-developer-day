package com.summit.notebook.domain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Persistent
public class Notebook {

    @Id
    private BigInteger id;

    @NotNull
    @Indexed
    private String name;

    @NotNull
    @Size(max = 4000)
    private String description;

    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date created = new Date();

    private String author;

    @NotNull
    private String[] tags;

    private List<Note> notes = new ArrayList<Note>();

    public BigInteger getId() {
        return this.id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String[] getTags() {
        return this.tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public static Notebook fromJsonToNotebook(String json) {
        return new JSONDeserializer<Notebook>().use(null, Notebook.class)
                .deserialize(json);
    }

    public static String toJsonArray(Collection<Notebook> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static Collection<Notebook> fromJsonArrayToNotebooks(String json) {
        return new JSONDeserializer<List<Notebook>>()
                .use(null, ArrayList.class).use("values", Notebook.class)
                .deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
