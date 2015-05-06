package xyz.mijack.blog.csdn.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import xyz.mijack.blog.csdn.R;

/**
 * Created by MiJack on 2015/5/2.
 */
public class BaseActivity extends AppCompatActivity {
    Toolbar toolbar;
    FrameLayout content;
    private int statusBarHeight = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(this, R.layout.activity_base, null);
        super.setContentView(rootView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = (FrameLayout) findViewById(R.id.root_content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //通过是设置padding来显示状态栏
        rootView.setPadding(0, getStatusHeight(), 0, 0);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    /**
     * 获取状态栏的高度
     *
     * @return 状态栏的高度
     */
    public int getStatusHeight() {
        if (statusBarHeight > 0) return statusBarHeight;
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        } else {
            statusBarHeight = (int) Math.ceil(25 * getResources().getDisplayMetrics().density);
        }
        return statusBarHeight;
    }

    public View getContentView() {
        return content;
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, content, false);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        content.addView(view);
    }

    public Toolbar getToolbar() {
        return toolbar;
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
}
