package xyz.mijack.blog.csdn.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mustafaferhan.debuglog.DebugLog;

import java.util.ArrayList;
import java.util.List;

import xyz.mijack.blog.csdn.R;
import xyz.mijack.blog.csdn.model.SimpleBlog;
import xyz.mijack.blog.csdn.util.Util;

/**
 * Created by MiJack on 2015/5/1.
 */
public class AuthorBlogDetailAdapter extends RecyclerView.Adapter<AuthorBlogDetailAdapter.AuthorHolder> {

    private List<SimpleBlog> blogs;

    public AuthorBlogDetailAdapter() {
        blogs = new ArrayList<>();
    }


    @Override
    public AuthorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_author_blog, parent, false);
        return new AuthorHolder(view);
    }

    @Override
    public void onBindViewHolder(AuthorHolder holder, int position) {
        if (position == 0) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.blog_container.setVisibility(View.GONE);
            return;
        }
        holder.imageView.setVisibility(View.GONE);
        holder.blog_container.setVisibility(View.VISIBLE);
        SimpleBlog blog = blogs.get(position - 1);
//        holder.blog_title.setText(blog.getTitle());
        SpannableString ss = new SpannableString(" " + blog.getTitle());
        //获取Drawable资源
        Drawable d = holder.itemView.getResources().getDrawable(blog.getBlogTpye().getImageId());
        int imageSize = Util.dip2px(holder.itemView.getContext(), 16);
        d.setBounds(0, 0, imageSize, imageSize);
        //创建ImageSpan
        ImageSpan is = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        ss.setSpan(is, 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        holder.blog_title.setText(ss);
        holder.data = blog;
    }


    @Override
    public int getItemCount() {
        return  (blogs != null ? blogs.size() : 0) + 1;
    }

    public void addBlogs(List<SimpleBlog> simpleBlogs) {
        this.blogs.addAll(simpleBlogs);
        DebugLog.d("add list size:" + simpleBlogs.size() + " count:" + blogs.size());
        this.notifyDataSetChanged();
    }

    public static class AuthorHolder extends RecyclerView.ViewHolder {
        TextView blog_title;
        CardView blog_container;
        ImageView imageView;
        public SimpleBlog data;

        public AuthorHolder(View itemView) {
            super(itemView);
            blog_title = (TextView) itemView.findViewById(R.id.blog_title);
            blog_container = (CardView) itemView.findViewById(R.id.author_blog_container);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

    }
}