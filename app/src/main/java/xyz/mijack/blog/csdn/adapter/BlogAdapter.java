package xyz.mijack.blog.csdn.adapter;


import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.mustafaferhan.debuglog.DebugLog;

import org.litepal.crud.DataSupport;

import java.util.Date;

import github.recyclerview.cursoradapter.RecyclerViewCursorAdapter;
import xyz.mijack.blog.csdn.R;
import xyz.mijack.blog.csdn.cache.ImageLoader;
import xyz.mijack.blog.csdn.constant.Constant;
import xyz.mijack.blog.csdn.db.DBUtil;
import xyz.mijack.blog.csdn.html.HTMLHandler;
import xyz.mijack.blog.csdn.model.Author;
import xyz.mijack.blog.csdn.model.Blog;
import xyz.mijack.blog.csdn.model.Category;
import xyz.mijack.blog.csdn.model.Order;

/**
 * Created by MiJack on 2015/4/17.
 */
public class BlogAdapter extends RecyclerViewCursorAdapter<BlogAdapter.BlogHolder> {

    public static final String[] columns = new String[]{
            "blog.id",//0
            "blog.title",//1
            "blog.category",//2
            "blog.content",//3
            "blog.blogurl",//4
            "author.id",//5
            "author.iconurl",//6
            "author.authorurl",//7
            "blog.farovite",//8
            "blog.time"//9
    };
    private final Activity activity;
    private final int rippleDuring;
    private final int zoomDuration;


    ImageLoader loader;

    /**
     * Recommended constructor.
     *
     * @param activity The context
     * @param c        The cursor from which to get the data.
     */
    public BlogAdapter(Activity activity, Cursor c) {
        super(activity, c, FLAG_REGISTER_CONTENT_OBSERVER);
        this.activity = activity;
        loader = new ImageLoader(activity, Constant.CACHE_NAME/*缓存目录*/);
        rippleDuring = activity.getResources().getInteger(R.integer.rippleDuration);
        zoomDuration = activity.getResources().getInteger(R.integer.zoomDuration);
    }

    /**
     * 将cursor的内容清空
     */
    public void reset() {
        Cursor c = DataSupport.findBySQL(" select * from blog where 1=0");
        changeCursor(c);
    }


    public void changeCursor(Category category, Order order) {
        StringBuilder builder = new StringBuilder();
        /*添加Select语句*/
        builder.append("SELECT ");
        //拼成相应的列
        String[] columns = BlogAdapter.columns;
        for (int i = 0; i < columns.length; i++) {
            builder.append(columns[i]);
            if (i + 1 < columns.length) {
                builder.append(", ");
            }
        }
        /*添加From语句*/
        builder.append(" FROM blog,author" + (order == Order.index ? ",hotblog " : " "));
        /*添加Where语句*/
        //blog表和user级联
        builder.append(" where blog.authorid=author.id ");
        //添加类型筛选
        builder.append(" and blog.category=\"" +
                category.getName() + "\"");
        /*添加Order语句,按热度排名还需要添加与hotblog表的关联条件*/
        if (order == Order.index) {
            long request = DBUtil.getCurrentRequest(category);
            builder.append(" AND hotblog.blogid=blog.id AND hotblog.request=" +
                    request + "  ORDER BY hotblog.sequence ");
        } else {
            builder.append("  ORDER BY blog.time DESC");
        }
        String sql = builder.toString();
        DebugLog.d("sql:" + sql);
        Cursor c = DataSupport.findBySQL(sql);
        changeCursor(c);
    }

    @Override
    public BlogHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.list_item_blog, parent, false);
        BlogHolder holder = new BlogHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BlogHolder holder, final Cursor cursor) {
        //读取数据
        holder.blog.setId(cursor.getInt(0));
        holder.blog.setTitle(cursor.getString(1));
        holder.blog.setCategory(cursor.getString(2));
        holder.blog.setContent(cursor.getString(3));
        holder.blog.setBlogUrl(cursor.getString(4));
        holder.author.setId(cursor.getInt(5));
        holder.author.setIconUrl(cursor.getString(6));
        holder.author.setAuthorUrl(cursor.getString(7));
        holder.blog.setFarovite(cursor.getInt(8) == 1);
        holder.blog.setTime(cursor.getLong(9));
        //UI呈现
        holder.content.setText(holder.blog.getContent());
        holder.time.setText(HTMLHandler.dateFormat.format(new Date(holder.blog.getTime())));
        holder.title.setText(holder.blog.getTitle());
        holder.icon.setImageResource(R.drawable.loading);
        loader.loadImage(holder.icon, holder.author.getIconUrl());
    }

    @Override
    protected void onContentChanged() {

    }


    public static class BlogHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView time;
        public TextView content;
        public Author author;
        public Blog blog;
        public RippleView rippleView;
        public RelativeLayout blogContainer;

        public BlogHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.author_icon);
            title = (TextView) itemView.findViewById(R.id.blog_title);
            time = (TextView) itemView.findViewById(R.id.blog_time);
            content = (TextView) itemView.findViewById(R.id.blog_content);
            rippleView = (RippleView) itemView.findViewById(R.id.ripple_view);
            blogContainer = (RelativeLayout) itemView.findViewById(R.id.blog_container);
            author = new Author();
            blog = new Blog();
        }
    }
}
