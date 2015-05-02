package xyz.mijack.csdn.blog.model;

/**
 * Created by MiJack on 2015/4/16.
 */
public class Category  {
    private int icon;
    private int category;
    private String name;

    public Category(int icon, int category, String name) {
        this.icon = icon;
        this.category = category;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return !(name != null ? !name.equals(category.name) : category.name != null);
    }

}
