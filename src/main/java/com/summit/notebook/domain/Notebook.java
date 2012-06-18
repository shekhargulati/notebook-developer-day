package com.summit.notebook.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoEntity;
import org.springframework.roo.addon.tostring.RooToString;

@Persistent
@RooJavaBean
@RooToString
@RooMongoEntity
@RooJson
public class Notebook {

    @NotNull
    private String name;

    @NotNull
    @Size(max = 4000)
    private String description;

    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date created = new Date();

    @NotNull
    private String author;

    @NotNull
    private String[] tags;

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static Notebook fromJsonToNotebook(String json) {
        return new JSONDeserializer<Notebook>().use(null, Notebook.class).deserialize(json);
    }

	public static String toJsonArray(Collection<Notebook> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<Notebook> fromJsonArrayToNotebooks(String json) {
        return new JSONDeserializer<List<Notebook>>().use(null, ArrayList.class).use("values", Notebook.class).deserialize(json);
    }

	@Id
    private BigInteger id;

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
}
