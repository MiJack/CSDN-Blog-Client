package xyz.mijack.blog.csdn.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ImageView;

import com.mustafaferhan.debuglog.DebugLog;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import xyz.mijack.blog.csdn.R;
import xyz.mijack.blog.csdn.fragment.BlogPagerFragment;
import xyz.mijack.blog.csdn.model.CategoryList;


public class MainActivity extends MaterialNavigationDrawer {
    BlogPagerFragment pagerFragment;
    MaterialSectionListener intentListener = new MaterialSectionListener() {
        @Override
        public void onClick(MaterialSection materialSection) {
            Intent intent = materialSection.getTargetIntent();
            startActivity(intent);
            closeDrawer();
        }
    };

    private MaterialSectionListener categoryListener = new MaterialSectionListener() {
        @Override
        public void onClick(MaterialSection section) {
            closeDrawer();
            MaterialSection currentSection = MainActivity.this.getCurrentSection();
            if (currentSection == section) {
                //如果点击的是当前分类，不做处理
                return;
            }
            MainActivity.this.setSection(section);
            pagerFragment.changeCategory
                    (CategoryList.getCategoryByString
                            (MainActivity.this, section.getTitle()));
            getToolbar().setTitle(section.getTitle());
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        closeDrawer();
        getToolbar().setTitleTextColor(getResources().getColor(R.color.colorAccent));
        getToolbar().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ImageView imageView = (ImageView) findViewById(it.neokree.materialnavigationdrawer.R.id.statusBar);
        if (imageView != null && Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            imageView.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        pagerFragment = new BlogPagerFragment();
        MaterialAccount account =
                new MaterialAccount(this.getResources(),
                        getString(R.string.author), getString(R.string.gmail_address),
                        R.drawable.profile, R.drawable.background);
        addAccount(account);
        MaterialSection accountSection = newSection(getString(R.string.github), this);
        addAccountSection(accountSection);
        addSubheader(getString(R.string.blog_category));
        MaterialSection section = new MaterialSection(this, 0, true, 0).useRealColor();
        section.setTarget(pagerFragment);
        section.setTitle(getString(R.string.category_mobile));
        section.setSectionColor(getResources().getColor(R.color.select_color));
        section.setOnClickListener(categoryListener);
        addSection(section);
        addSection(buildSection(R.string.category_web, categoryListener));
        addSection(buildSection(R.string.category_enterprise, categoryListener));
        addSection(buildSection(R.string.category_code, categoryListener));
        addSection(buildSection(R.string.category_www, categoryListener));
        addSection(buildSection(R.string.category_database, categoryListener));
        addSection(buildSection(R.string.category_system, categoryListener));
        addSection(buildSection(R.string.category_cloud, categoryListener));
        addSection(buildSection(R.string.category_software, categoryListener));
        addSection(buildSection(R.string.category_other, categoryListener));
        // create bottom section
        addSubheader(getString(R.string.application));
        MaterialSection aboutSection = newSection(getString(R.string.app_about), new Intent(this, AboutActivity.class));
        aboutSection.setOnClickListener(intentListener);
        addSection(aboutSection);

    }


    public MaterialSection buildSection(int string, MaterialSectionListener listener) {
        final MaterialSection section = new MaterialSection(this, 0, true, 2);
        section.setTitle(getString(string));
        section.setSectionColor(getResources().getColor(R.color.select_color));
        if (listener != null) {
            section.setTarget(listener);
        }
        return section;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_BACK) {
            if (isDrawerOpen()) {
                closeDrawer();
            }
            DebugLog.d("onKeyDown");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}