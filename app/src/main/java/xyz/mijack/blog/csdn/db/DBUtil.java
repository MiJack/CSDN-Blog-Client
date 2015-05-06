package xyz.mijack.blog.csdn.db;


import android.content.ContentValues;

import com.mustafaferhan.debuglog.DebugLog;

import org.litepal.crud.DataSupport;

import java.util.List;

import xyz.mijack.blog.csdn.model.Author;
import xyz.mijack.blog.csdn.model.Blog;
import xyz.mijack.blog.csdn.model.Category;
import xyz.mijack.blog.csdn.model.HotBlog;
import xyz.mijack.blog.csdn.model.Request;

/**
 * Created by MiJack on 2015/4/17.
 */
public class DBUtil {
    public static void saveBlogList(List<Blog> blogs, Category category, boolean isHotBlog, boolean isLoadHome) {
        DebugLog.i("是否是最热的博客：" + isHotBlog
                + "\n是否需要从网页首页加载：" + isLoadHome);
        for (int i = 0; i < blogs.size(); i++) {
            Blog blog = blogs.get(i);
            checkBlog(blog);
        }
        if (isHotBlog) {
            //将blog的id保存到HotBlog表中
            int request = 1;
            int sequence = 1;
//         获取请求的编号
            if (isLoadHome) {
                request = updateRequest(category);
            } else {
                request = getCurrentRequest(category);
                /*参数 request，category从HotBlog中记载*/
                sequence += DataSupport.where(" request='" + request + "' and category='" +
                        category.getName() + "'").max(HotBlog.class, "sequence", Integer.TYPE);
            }
            for (int j = 0; j < blogs.size(); j++, sequence++) {
                Blog blog = blogs.get(j);
                HotBlog hotBlog = new HotBlog();
                hotBlog.setCategory(category.getName());
                hotBlog.setBlogId(blog.getId());
                hotBlog.setRequest(request);
                hotBlog.setSequence(sequence);
                checkHotBlog(hotBlog);
            }
        }
    }

    public static int getCurrentRequest(Category category) {
        List<Request> requests = DataSupport.where("category='" + category.getName() + "'").find(Request.class);
        if (requests.size() == 0) {
            Request request = new Request();
            request.setRequest(1);
            request.setCategory(category.getName());
            request.save();
            return 1;
        }
        return requests.get(0).getRequest();
    }

    public static int updateRequest(Category category) {
        List<Request> requests = DataSupport.where("category='" + category.getName() + "'").find(Request.class);
        if (requests.size() == 0) {
            getCurrentRequest(category);
            requests = DataSupport.where("category='" + category.getName() + "'").find(Request.class);
        }
        Request request = requests.get(0);
        request.updateRequest();
        return request.getRequest();
    }

    public static void checkHotBlog(HotBlog hotBlog) {
        ContentValues values = new ContentValues();
        values.put("request", hotBlog.getRequest());
        values.put("sequence", hotBlog.getSequence());
        if (DataSupport.updateAll(HotBlog.class, values, "blogid='" + hotBlog.getBlogId() + "'") == 0) {
            hotBlog.save();
        }
    }

    public static void checkBlog(Blog blog) {
        List<Blog> blogs = DataSupport.where("blogurl='" + blog.getBlogUrl() + "'").find(Blog.class);
        if (blogs.size() == 0) {
            blog.save();
        } else {
            blog.setId(blogs.get(0).getId());
        }
    }

    public static void saveAuthor(Author author) {
        List<Author> authors = DataSupport.where("authorurl='" + author.getAuthorUrl() + "'").find(Author.class);
        if (authors.size() == 0) {
            author.save();
        } else {
            author.setId(authors.get(0).getId());
        }
    }
}