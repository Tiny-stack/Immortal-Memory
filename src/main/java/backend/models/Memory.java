package backend.models;

// package org.main.backend.models;
// import java.sql.SQLException;

import org.worm.Model;
public class Memory extends Model{

    private Integer id;
    private String title;
    private String content;
    private String date;
    private String attachments;

    @Override
    public Integer getId() {
        return this.id;
    }
    @Override
    public String getTableName()
    {
        return "memory";
    }
    public Memory()
    {

    }
    public Memory(String title, String content,String date,String attachMent)
    {
        this.title = title;
        this.content = content;
        this.attachments = attachMent;
        this.date = date;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getAttachments() {
        return attachments;
    }
    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }
    

}
