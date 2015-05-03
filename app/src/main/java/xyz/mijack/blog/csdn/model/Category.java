package xyz.mijack.blog.csdn.model;

/**
 * Created by MiJack on 2015/4/16.
 */
public class Category {
    private int category;
    private String name;

    public Category(int category, String name) {
        this.category = category;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
