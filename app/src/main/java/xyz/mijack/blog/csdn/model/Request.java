package xyz.mijack.blog.csdn.model;

import org.litepal.crud.DataSupport;

/**
 * Created by MiJack on 2015/4/17.
 */
public class Request extends DataSupport {
    private int id;
    private String category;
    private int request;

    public Request() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    /**
     * 将Request++，并存入数据库
     */
    public void updateRequest() {
        request++;
        this.save();
    }
}
