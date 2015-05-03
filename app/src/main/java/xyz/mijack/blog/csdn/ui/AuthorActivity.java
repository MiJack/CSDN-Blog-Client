package xyz.mijack.blog.csdn.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.mustafaferhan.debuglog.DebugLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import xyz.mijack.blog.csdn.R;
import xyz.mijack.blog.csdn.adapter.AuthorBlogDetailAdapter;
import xyz.mijack.blog.csdn.cache.ImageLoader;
import xyz.mijack.blog.csdn.callback.RecyclerItemClickListener;
import xyz.mijack.blog.csdn.constant.Constant;
import xyz.mijack.blog.csdn.constant.Key;
import xyz.mijack.blog.csdn.html.HTMLHandler;
import xyz.mijack.blog.csdn.html.HtmlTagList;
import xyz.mijack.blog.csdn.model.SimpleBlog;

/**
 * 浏览个人Blog的主页，启动时需要传递authorUrl、iconUrl两个参数
 */
public class AuthorActivity extends BaseActivity implements ObservableScrollViewCallbacks {
    private static final int LOAD_BLOG_FORM_AUTHOR_COMPLETE = 1;

    private ImageView authorIcon;
    private int parallaxImageHeight;
    private ObservableRecyclerView recyclerView;
    private View recyclerViewBackground;
    private LinearLayoutManager layoutManager;
    private String authorUrl;
    private String iconUrl;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_BLOG_FORM_AUTHOR_COMPLETE:
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    isLoad = false;
                    authorAdapter.addBlogs((List<SimpleBlog>) msg.obj);
                    break;
            }
        }
    };
    ProgressBar progressBar;

    /* 与网络请求相关的变量*/
    private boolean isLoad = false;
    private boolean hasNextPage = true;
    private int page = 0;
    private AuthorBlogDetailAdapter authorAdapter;
    private int rippleDuration;
    private int zoomDuration;

    private void loadBlog() {
        //正在加载或者没有下一页,则取消
         if (isLoad) {
            return;
        }
        if (!hasNextPage) {
            return;
        }
        isLoad = true;
        new Thread() {
            @Override
            public void run() {
                page++;
                String url = getBlogListURL(page);
                DebugLog.d("url:" + url);
                try {
                    Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
                    List<SimpleBlog> blogs = HTMLHandler.getBlogListFromAuthor(document);
                    Element papelist = document.getElementById(HtmlTagList.id_papelist);
                    hasNextPage = papelist.text().contains(Constant.NEXT_PAGE);
                    DebugLog.d(papelist.text() + "  :  " + hasNextPage);
                    Message message = Message.obtain();
                    message.what = LOAD_BLOG_FORM_AUTHOR_COMPLETE;
                    message.obj = blogs;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取和作者相关的信息
        iconUrl = getIntent().getStringExtra(Key.author_icon);
        authorUrl = getIntent().getStringExtra(Key.author_url);
        rippleDuration = getResources().getInteger(R.integer.rippleDuration);
        zoomDuration = getResources().getInteger(R.integer.zoomDuration);
        parallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
        setContentView(R.layout.activity_author);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getContentView().getLayoutParams();
        lp.topMargin = 0;
        getContentView().setLayoutParams(lp);
        recyclerViewBackground = findViewById(R.id.list_background);
        authorIcon = (ImageView) findViewById(R.id.author_icon);
        recyclerView = (ObservableRecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView.setScrollViewCallbacks(this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        authorAdapter = new AuthorBlogDetailAdapter();
        recyclerView.setAdapter(authorAdapter);
//        //将RecyclerView的背景recyclerViewBackground下移parallaxImageHeight;
//        recyclerViewBackground.setTranslationY(Math.max(0, parallaxImageHeight));
        //加载作者头像
        ImageLoader loader = new ImageLoader(this, Constant.CACHE_NAME);
        loader.loadImage(authorIcon, iconUrl);
        loadBlog();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, MotionEvent e) {
                if (position == 0) {
                    return;
                }
                final AuthorBlogDetailAdapter.AuthorHolder h = (AuthorBlogDetailAdapter.AuthorHolder) holder;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BlogActivity.startActivity(AuthorActivity.this, h.data.getTitle(), h.data.getBlogUrl());
                    }
                }, rippleDuration + zoomDuration);

            }
        }));
    }

    /**
     * 输入  author#authorUrl=> http://blog.csdn.net/{author}
     *
     * @param page
     * @return http://blog.csdn.net/{author}/article/list/{page}
     */
    private String getBlogListURL(int page) {

        return authorUrl + "/article/list/" + page;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = 1 - (float) Math.max(0, parallaxImageHeight - scrollY) / parallaxImageHeight;
        getToolbar().setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        authorIcon.setTranslationY(-scrollY / 2);
        // Translate list   background
        recyclerViewBackground.setTranslationY(Math.max(0, -scrollY + parallaxImageHeight));
        //请求网络判断
        if (layoutManager.findLastVisibleItemPosition() + 10 >= authorAdapter.getItemCount()) {
            loadBlog();
        }
        if (layoutManager.findLastCompletelyVisibleItemPosition() + 1 == authorAdapter.getItemCount()) {
            if (isLoad) {
                //todo
//                Toast.makeText(this, R.string.loading, Toast.LENGTH_SHORT).show();
            } else {
                loadBlog();
            }
        }
    }


    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    public static void startActivity(Context context, String authorUrl, String iconUrl) {
        Intent intent = new Intent(context, AuthorActivity.class);
        intent.putExtra(Key.author_url, authorUrl);
        intent.putExtra(Key.author_icon, iconUrl);
        context.startActivity(intent);
    }
}