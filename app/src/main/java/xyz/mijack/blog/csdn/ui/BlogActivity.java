package xyz.mijack.blog.csdn.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mustafaferhan.debuglog.DebugLog;

import org.xwalk.core.XWalkView;

import xyz.mijack.blog.csdn.R;
import xyz.mijack.blog.csdn.constant.Key;

public class BlogActivity extends BaseActivity {
    XWalkView xWalkView;
    private FloatingActionMenu fam;
    private FloatingActionButton fabOpenWithBrowser;
    private FloatingActionButton fabShare;

    private String title;
    private String blogUrl;
    private int mPreviousScrollY = 0;
    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fam.close(true);
            switch (v.getId()) {
                case R.id.fab_share:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "我发现在csdn上有一篇博客不错哦，链接是" + blogUrl);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "分享"));
                    break;
                case R.id.fab_open_with_browser:
                    showDialog();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        //fam设置
        fam = (FloatingActionMenu) findViewById(R.id.floating_action_menu);
        fam.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(this, R.anim.jump_from_down));
        fam.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(this, R.anim.jump_to_down));
        fabShare = (FloatingActionButton) fam.findViewById(R.id.fab_share);
        fabOpenWithBrowser = (FloatingActionButton) fam.findViewById(R.id.fab_open_with_browser);
        fabShare.setOnClickListener(fabClickListener);
        fabOpenWithBrowser.setOnClickListener(fabClickListener);
        xWalkView = (XWalkView) findViewById(R.id.web_view);
        Intent intent = getIntent();
        title = intent.getStringExtra(Key.blog_title);
        blogUrl = intent.getStringExtra(Key.blog_url);
        setTitle(title);
        DebugLog.d("title:" + title + " blogUrl:" + blogUrl);
        xWalkView.load(blogUrl, null);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fam.isOpened()) {
                fam.close(true);
            } else {
                this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppTheme_Dialog)
                .setTitle(R.string.open_with_browser)
                .setMessage(R.string.open_with_browser_content)
                .setNegativeButton(R.string.use_pc_url, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openWithBrowser(blogUrl);
                    }
                });
        builder.setPositiveButton(R.string.use_mobile_url, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openWithBrowser(getMobileUrl(blogUrl));
            }
        });
        builder.create().show();
    }

    public String getMobileUrl(String pcUrl) {
        pcUrl = pcUrl.replace("http://blog.csdn.net/", "http://m.blog.csdn.net/blog/");
        return pcUrl.replace("/article/details/", "/");
    }

    public void openWithBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(Intent.createChooser(intent, getString(R.string.choose_browser)));
    }


    /**
     * 防止内存泄漏
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (xWalkView != null) {
            xWalkView.pauseTimers();
            xWalkView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (xWalkView != null) {
            xWalkView.resumeTimers();
            xWalkView.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (xWalkView != null) {
            xWalkView.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (xWalkView != null) {
            xWalkView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (xWalkView != null) {
            xWalkView.onNewIntent(intent);
        }
    }

    public static void startActivity(Context context, String title, String blogUrl) {
        Intent intent = new Intent(context, BlogActivity.class);
        intent.putExtra(Key.blog_title, title);
        intent.putExtra(Key.blog_url, blogUrl);
        context.startActivity(intent);
    }

}
