package xyz.mijack.csdn.blog.model;

/**
 * Created by MiJack on 2015/4/17.
 */
public enum Order {
    index {
        public String toUrl() {
            return "index.html";
        }

        @Override
        public String toString() {
            return "最热";
        }
    }, newest {
        public String toUrl() {
            return "newest.html";
        }

        @Override
        public String toString() {
            return "最新";
        }
    };

    public abstract String toUrl();

}
