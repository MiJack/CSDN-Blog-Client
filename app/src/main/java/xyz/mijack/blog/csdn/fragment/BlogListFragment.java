package xyz.mijack.blog.csdn.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.mustafaferhan.debuglog.DebugLog;
import com.wangjie.androidbucket.support.recyclerview.layoutmanager.ABaseLinearLayoutManager;
import com.wangjie.androidbucket.support.recyclerview.listener.OnRecyclerViewScrollListener;
import com.wangjie.androidbucket.support.recyclerview.listener.OnRecyclerViewScrollLocationListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import xyz.mijack.blog.csdn.R;
import xyz.mijack.blog.csdn.adapter.BlogAdapter;
import xyz.mijack.blog.csdn.callback.RecyclerItemClickListener;
import xyz.mijack.blog.csdn.constant.Key;
import xyz.mijack.blog.csdn.db.DBUtil;
import xyz.mijack.blog.csdn.html.HTMLHandler;
import xyz.mijack.blog.csdn.model.Blog;
import xyz.mijack.blog.csdn.model.Category;
import xyz.mijack.blog.csdn.model.CategoryList;
import xyz.mijack.blog.csdn.model.Order;
import xyz.mijack.blog.csdn.ui.AuthorActivity;
import xyz.mijack.blog.csdn.ui.BlogActivity;
import xyz.mijack.blog.csdn.util.Util;


/**
 * 主要用于呈现各个BLogList页面的信息
 * Created by MiJack on 2015/4/16.
 */
public class BlogListFragment extends Fragment implements OnRecyclerViewScrollListener, OnRecyclerViewScrollLocationListener, SwipeRefreshLayout.OnRefreshListener {

    private Order order;
    private Category category;
    private int page;
    private int rippleDuration;
    private int zoomDuration;
    /**
     * 当前列表项为空时，加载进度条
     */
    private ProgressBar progressBar;
    private ObservableRecyclerView recyclerView;
    private SmoothProgressBar bottomProgressBar;
    private SmoothProgressBar topProgressBar;
    private ABaseLinearLayoutManager layoutManager;
    private SwipeRefreshLayout refreshLayout;
    private BlogAdapter adapter;

     /*
     * 定义一系列和{@link android.os.Message#what}相关的常量
     */

    /**
     * 加载首页成功
     */
    private static final int LOAD_HOME_PAGE = 1;
    /**
     * 加载更多失败
     */
    private static final int LOAD_MORE_PAGE = 2;
    /**
     * 加载页面失败
     */
    private static final int LOAD_PAGE_WITH_SOME_EXCEPCTION = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissProgressBar(bottomProgressBar);
            dismissProgressBar(topProgressBar);
            switch (msg.what) {
                case LOAD_HOME_PAGE:
                    dismissProgressBar(topProgressBar);
                    progressBar.setVisibility(View.GONE);
                    adapter.changeCursor(category, order);
                    recyclerView.setAdapter(adapter);
                    break;
                case LOAD_MORE_PAGE:
                    progressBar.setVisibility(View.GONE);
                    dismissProgressBar(bottomProgressBar);
                    adapter.changeCursor(category, order);
                    recyclerView.setAdapter(adapter);
                    break;
                case LOAD_PAGE_WITH_SOME_EXCEPCTION:
                    Util.showSnackBar(getActivity(), "加载异常", 1000);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取有关的参数并初始化其他参数
        category = CategoryList.get(0);
        rippleDuration = getResources().getInteger(R.integer.rippleDuration);
        zoomDuration = getResources().getInteger(R.integer.zoomDuration);
        //加载布局
        View rootView = inflater.inflate(R.layout.fragment_blog_list, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        recyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.recycler_view);
        topProgressBar = (SmoothProgressBar) rootView.findViewById(R.id.progressbar_top);
        bottomProgressBar = (SmoothProgressBar) rootView.findViewById(R.id.progressbar_bottom);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        //设置下拉刷新的事件
        refreshLayout.setOnRefreshListener(this);

        //设置RecyclerView的LayoutManager
        layoutManager = new ABaseLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //监听Recycler的滚动，用于控制SwipeRefreshLayout的刷新，
        // 对应的方法有void onScrollStateChanged(RecyclerView,int)、void onScrolled(RecyclerView,int, int);
        layoutManager.getRecyclerViewScrollManager().addScrollListener(recyclerView, this);
        //监听recyclerView是否到达顶部或底部，
        // 对应的方法有void onTopWhenScrollIdle(RecyclerView)、void onBottomWhenScrollIdle(RecyclerView);
        layoutManager.setOnRecyclerViewScrollLocationListener(recyclerView, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        //设置RecyclerView的Adapter
        adapter = new BlogAdapter(getActivity(), null);
        adapter.changeCursor(category, order);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.SimpleOnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, final MotionEvent e) {
                final BlogAdapter.BlogHolder bh = (BlogAdapter.BlogHolder) holder;
                DebugLog.d("X:" + e.getX() + "Y:" + e.getY());
                DebugLog.d("Raw X:" + e.getRawX() + " Y:" + e.getRawY());
                int[] locations = new int[2];
                bh.icon.getLocationOnScreen(locations);
                DebugLog.d(locations[0] + "  " + locations[1]);
                DebugLog.d("size w:" + bh.icon.getWidth() + " h:" + bh.icon.getHeight());
                //判断有没有点击到icon
                if (    //水平判断
                        e.getRawX() >= locations[0] && e.getRawX() <= locations[0] + bh.icon.getWidth()
                                //竖直判断
                                && e.getRawY() >= locations[1] && e.getRawY() <= locations[1] + bh.icon.getHeight()
                        ) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AuthorActivity.startActivity(getActivity(), bh.author.getAuthorUrl(), bh.author.getIconUrl());
                        }
                    }, rippleDuration + zoomDuration);
                    return;
                }
                //判断有没有触碰到blogContainer
                bh.blogContainer.getLocationOnScreen(locations);
                DebugLog.d(locations[0] + "  " + locations[1]);
                DebugLog.d("size w:" + bh.blogContainer.getWidth() + " h:" + bh.blogContainer.getHeight());
                //判断有没有点击到icon
                if (    //水平判断
                        e.getRawX() >= locations[0] && e.getRawX() <= locations[0] + bh.blogContainer.getWidth()
                                //竖直判断
                                && e.getRawY() >= locations[1] && e.getRawY() <= locations[1] + bh.blogContainer.getHeight()
                        ) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            BlogActivity.startActivity(getActivity(), bh.blog.getTitle(), bh.blog.getBlogUrl());
                        }
                    }, rippleDuration + zoomDuration);
                }
            }
        }));
        resetFragment();
        return rootView;
    }

    /**
     * 展示顶部ProgressBar，并发起网络请求
     *
     * @param showTopProgressbar
     */
    private void loadHomePage(boolean showTopProgressbar) {
        //隐藏底部加载，显示顶部加载
        dismissProgressBar(bottomProgressBar);
        if (showTopProgressbar) {
            showProgressBar(topProgressBar);
        }
        page = 1;
        refreshLayout.setRefreshing(true);
        refreshLayout.setRefreshing(false);
        //url、category、order和isLoadHome
        new GetBlogThread(getURL(), category, order, true).start();
    }

    private void loadMorePage() {
        //情况判断，当底部或者顶部正在加载的时候不必加载
        if (!isProgressFinish(bottomProgressBar)
                || !isProgressFinish(topProgressBar)
                || progressBar.getVisibility() == View.VISIBLE) {
            return;
        }
        //显示加载进度条
        showProgressBar(bottomProgressBar);
        page++;
        new GetBlogThread(getURL(), category, order, false).start();
    }

    /**
     * 根据当前的page、category、order等属性获取相应的url网址
     *
     * @return 请求的网页的URL地址
     */

    private String getURL() {
        String url = new StringBuffer().append("http://blog.csdn.net/").append(category.getName())
                .append("/").append(order.toUrl()).append("?&page=").append(page).toString();
        DebugLog.d(url);
        return url;
    }

    private void resetFragment() {
        order = Order.valueOf(getArguments().getString(Key.Order, Order.index.toString()));
        adapter.changeCursor(category, order);
        //隐藏SmoothProgressBar
        dismissProgressBar(topProgressBar);
        dismissProgressBar(bottomProgressBar);
        page = 1;
        if (adapter.getItemCount() == 0) {
            progressBar.setVisibility(View.VISIBLE);
            loadHomePage(false);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 显示并启动progressBar
     *
     * @param progressBar
     */
    public void showProgressBar(SmoothProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.progressiveStart();
    }

    /**
     * 停止并隐藏progressBar
     *
     * @param progressBar
     */
    public void dismissProgressBar(SmoothProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
        progressBar.progressiveStop();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int i) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int i, int i1) {
        refreshLayout.setEnabled(
                (layoutManager.findFirstCompletelyVisibleItemPosition() == 0)
                        && (isProgressFinish(topProgressBar))
                        && (isProgressFinish(bottomProgressBar))
        );
    }

    private boolean isProgressFinish(SmoothProgressBar progressBar) {
        SmoothProgressDrawable drawable = (SmoothProgressDrawable) progressBar.getIndeterminateDrawable();
        return drawable.isFinishing();
    }


    @Override
    public void onTopWhenScrollIdle(RecyclerView recyclerView) {
    }

    @Override
    public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
        loadMorePage();
    }

    public void changeCategory(Category targetCategory) {
        this.category = targetCategory;
        DebugLog.d("order:" + order.name() + " category:" + category.getName());
        resetFragment();
    }

    /**
     * 刷新当前的blog列表
     */
    @Override
    public void onRefresh() {
        loadHomePage(true);
    }

    /**
     * 启动Thread时需要url、category、order和isLoadHome等四个参数
     */
    public class GetBlogThread extends Thread {
        private String url;
        private Category category;
        private Order order;
        private boolean isLoadHome;

        public GetBlogThread(String url, Category category, Order order, boolean isLoadHome) {
            this.url = url;
            //防止浅拷贝
            this.category = new Category(category.getCategory(), category.getName());
            this.order = order;
            this.isLoadHome = isLoadHome;
        }


        @Override
        public void run() {
            Message m = Message.obtain();
            m.obj = category;
            try {
                Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
                List<Blog> blogs = HTMLHandler.getBlogList(document, category);
                DBUtil.saveBlogList(blogs, category, order == Order.index, isLoadHome);
                m.what = isLoadHome ? LOAD_HOME_PAGE : LOAD_MORE_PAGE;
            } catch (Exception e) {
                e.printStackTrace();
                m.what = LOAD_PAGE_WITH_SOME_EXCEPCTION;
            }
            handler.sendMessage(m);
        }
    }
}
