package org.main.backend.records;

public class Memory {
    private String title;
    private String content;
    private int date;
    private long id;
    public Memory(String title,String content, Integer date,long id)
    {
        this.title = title;
        this.content = content;
        this.date = date;
        this.id  = id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getContent()
    {
        return this.content;
    }
    public int getDate()
    {
        return this.date;
    }
    public String toString()
    {
        String response = "\n{\n    'title': '"+this.title+",'\n    'content': "+this.content+",\n    'date': "+this.date+"\n}\n";
        return response;
    }
    public long getId()
    {
        return this.id;
    }

}
