package xyz.mijack.blog.csdn.html;


import com.mustafaferhan.debuglog.DebugLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import xyz.mijack.blog.csdn.constant.Constant;
import xyz.mijack.blog.csdn.db.DBUtil;
import xyz.mijack.blog.csdn.model.Author;
import xyz.mijack.blog.csdn.model.Blog;
import xyz.mijack.blog.csdn.model.BlogType;
import xyz.mijack.blog.csdn.model.Category;
import xyz.mijack.blog.csdn.model.SimpleBlog;

/**
 * Created by MiJack on 2015/4/17.
 */
public class HTMLHandler {
    /**
     * 将从网页获取到的文本转化成相应Blog对象，其间包含着{@link xyz.mijack.blog.csdn.model.Author}对象的持久化<br/>
     *
     * @return 对应的Blog列表对象
     * @see org.jsoup.nodes.Document
     */
    public static List<Blog> getBlogList(Document document, Category category) {
        Elements blogsHtml = document.getElementsByClass(HtmlTagList.class_blog_item);
        List<Blog> list = new ArrayList<>();
        for (Element element : blogsHtml) {
            Blog blog = getBlogFromHtml(element, category);
            list.add(blog);
        }
        return list;
    }

    public static Blog getBlogFromHtml(Element element, Category category) {
        Blog blog = new Blog();
        //设置Blog的类型
        blog.setCategory(category.getName());
        // 获取并设置Blog的标题、内容以及相应的URL
        Elements atags = element.getElementsByTag(HtmlTagList.tag_a);
        blog.setTitle(atags.get(0).text());
        blog.setContent(element.getElementsByTag(HtmlTagList.tag_dd).first().text());
        blog.setBlogUrl(atags.get(0).attr(HtmlTagList.attr_href));
        //设置Blog的时间
        try {
            // 无法获取Blog的时间
            Document document = Jsoup.parse(new URL(blog.getBlogUrl()), 10000);
            String time = document.getElementsByClass(HtmlTagList.class_link_postdate).first().text();
            blog.setTime(dateFormat.parse(time).getTime());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //设置Aothor的id
        Author author = new Author();
        Element dtElement = element.getElementsByTag(HtmlTagList.tag_dt).first();
        author.setIconUrl(dtElement.getElementsByTag(HtmlTagList.tag_img).first().attr(HtmlTagList.attr_src));
        Element userName = element.getElementsByClass(HtmlTagList.class_user_name).first();
        author.setAuthorUrl(userName.attr(HtmlTagList.attr_href));
        //author对象存入表中，获取对应的id
        DBUtil.saveAuthor(author);
        blog.setAuthorId(author.getId());
        DebugLog.i(blog.toString() + "\n\t" + author.toString());
        return blog;
    }

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static List<SimpleBlog> getBlogListFromAuthor(Document document) {
        Element blogsHtml = document.getElementById(HtmlTagList.id_article_list);
        List<SimpleBlog> list = new ArrayList<>();
        for (Element element : blogsHtml.getElementsByClass(HtmlTagList.class_list_item)) {
            SimpleBlog blog = getBlogFromAuthor(element);
            list.add(blog);
        }
        return list;
    }

    private static SimpleBlog getBlogFromAuthor(Element element) {
        //一个博客对应的div的class属性为list_item
        SimpleBlog simpleBlog = new SimpleBlog();
        //类型对应的class属性为ico,同时也可能带有ico_type_Repost、ico_type_Original、ico_type_Translated中的一个
        simpleBlog.setBlogTpye(BlogType.getBlogType(element.getElementsByClass(HtmlTagList.class_ico)));
        //title 为第一个a标签
        Element titleElement = element.getElementsByTag(HtmlTagList.tag_a).first();
        String blogUrl=titleElement.attr(HtmlTagList.attr_href);
        if (!blogUrl.startsWith(Constant.WEB_ADDRESS)){
            blogUrl= Constant.WEB_ADDRESS+blogUrl;
        }
        simpleBlog.setBlogUrl(blogUrl);
        DebugLog.d("simpleBlog:" + simpleBlog.getBlogUrl());
        //时间
        try {
            simpleBlog.setDate(dateFormat.parse(element.getElementsByClass(HtmlTagList.class_link_postdate).first().text()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //url
        simpleBlog.setTitle(titleElement.text());
        DebugLog.d(simpleBlog.toString());
        return simpleBlog;
    }
}
