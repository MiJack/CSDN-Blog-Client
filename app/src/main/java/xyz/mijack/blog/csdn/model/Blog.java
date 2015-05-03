package xyz.mijack.blog.csdn.model;


import org.litepal.crud.DataSupport;

/**
 * Created by MiJack on 2015/4/17.<p/>
 * 共{@link Blog#id}、{@link Blog#authorId}、
 * {@link Blog#blogUrl}、{@link Blog#category}、{@link Blog#title}、{@link Blog#content}、{@link Blog#farovite}等7个属性
 */
public class Blog extends DataSupport {
    private int id;
    private int authorId;
    private String blogUrl;
    private String category;
    private String title;
    private String content;
    private long time;
    private boolean farovite;

    public Blog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public boolean isFarovite() {
        return farovite;
    }

    public void setFarovite(boolean farovite) {
        this.farovite = farovite;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", blogUrl='" + blogUrl + '\'' +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }
}