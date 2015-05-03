package xyz.mijack.blog.csdn.model;

import com.mustafaferhan.debuglog.DebugLog;

import org.jsoup.select.Elements;

import xyz.mijack.blog.csdn.R;

/**
 * Created by MiJack on 2015/5/1.
 */
public enum BlogType {
    Original {
        @Override
        public String toString() {
            return "Original";
        }

        @Override
        public int getImageId() {
            return R.drawable.original;
        }

        @Override
        public String getName() {
            return "原文";
        }
    }, Translated {
        @Override
        public String toString() {
            return "Translated";
        }

        @Override
        public int getImageId() {
            return R.drawable.translated;
        }

        @Override
        public String getName() {
            return "翻译";
        }
    }, Repost {
        @Override
        public String toString() {
            return "Repost";
        }

        @Override
        public int getImageId() {
            return R.drawable.repost;
        }

        @Override
        public String getName() {
            return "转载";
        }
    };

    public abstract int getImageId();

    public abstract String getName();

    public static BlogType getBlogType(Elements element) {
        String string = element.toString();
        for (BlogType blogType : BlogType.values()) {
            if (string.contains(blogType.toString())) {
                DebugLog.d(blogType.getName());
                return blogType;
            }
        }
        throw new RuntimeException("not found the type about " + string);
    }
}