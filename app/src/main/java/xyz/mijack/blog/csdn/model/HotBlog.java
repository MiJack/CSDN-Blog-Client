package xyz.mijack.blog.csdn.model;

import org.litepal.crud.DataSupport;

/**
 * Created by MiJack on 2015/4/17.
 */
public class HotBlog extends DataSupport{
    private int id;
    private int blogId;
    private String category;
    private int request;
    private int sequence;

    public HotBlog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public int getSequence() {
        return sequence;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
