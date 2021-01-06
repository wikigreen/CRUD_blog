package com.vladimir.crudblog.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Post {
    private Long id;
    private String content;
    private Date created;
    private Date undated;

    public Post(){}

    public Post(Long id, String content, Date created, Date undated) {
        this.id = id;
        this.content = content;
        this.created = created;
        this.undated = undated;
    }

    public Post(Long id, String content){
        this.id = id;
        this.content = content;
        this.created = new Date();
        this.undated = new Date(created.getTime());
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUndated() {
        return undated;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(Date created) {
        this.created = created;
    }



    public void setUpdated(Date undated) {
        this.undated = undated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (id != null ? !id.equals(post.id) : post.id != null) return false;
        if (content != null ? !content.equals(post.content) : post.content != null) return false;
        if (!created.equals(post.created)) return false;
        return undated != null ? undated.equals(post.undated) : post.undated == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + created.hashCode();
        result = 31 * result + (undated != null ? undated.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.ENGLISH);
        String createdTime = format.format(getCreated());
        String updatedTime = getCreated().equals(getUndated())  ?
                "Not updated yet" :
                format.format(getUndated());
        return  "Id: " + id + "\n" +
                "Created: " + createdTime +"\n"
                + "Updated: "+ updatedTime  +"\n"
                + "Content:" + "\n" + content ;

    }
}
