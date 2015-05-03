package xyz.mijack.blog.csdn.model;


import org.litepal.crud.DataSupport;

/**
 * Created by MiJack on 2015/4/17.
 */
public class Author extends DataSupport {
    private int id;
    private String authorUrl;
    private String iconUrl;
    private boolean farovite;

    public Author() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }


    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isFarovite() {
        return farovite;
    }

    public void setFarovite(boolean farovite) {
        this.farovite = farovite;
    }

    @Override
    public String toString() {
        return "Author{id=" + id +
                ",authorUrl='" + authorUrl + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}
