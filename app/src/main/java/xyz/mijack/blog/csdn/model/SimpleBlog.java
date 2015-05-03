package xyz.mijack.blog.csdn.model;

/**
 * Created by MiJack on 2015/5/1.
 */
public class SimpleBlog {
    private BlogType blogTpye;
    private String title;
    private long date;
    private String blogUrl;

    public SimpleBlog() {
    }

    public BlogType getBlogTpye() {
        return blogTpye;
    }

    public void setBlogTpye(BlogType blogTpye) {
        this.blogTpye = blogTpye;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    @Override
    public String toString() {
        return "SimpleBlog{" +
                "blogTpye=" + blogTpye.toString() +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", url=" + blogUrl +

                '}';
    }
}
