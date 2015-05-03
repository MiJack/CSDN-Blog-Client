package xyz.mijack.blog.csdn.model;

/**
 * 一个Library对象对应一个本项目用到的开源库
 */
public class Library {
    public static final String ATTR_NAME = "name";
    public static final String ATTR_CONTENT = "content";
    public static final String ATTR_GRADLE = "gradle";
    public static final String Tag="Library";
    String name;
    String gradle;
    String content;

    public Library() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGradle() {
        return gradle;
    }

    public void setGradle(String gradle) {
        this.gradle = gradle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}