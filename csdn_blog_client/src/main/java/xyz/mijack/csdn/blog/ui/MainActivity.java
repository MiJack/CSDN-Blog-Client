package xyz.mijack.csdn.blog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.mustafaferhan.debuglog.DebugLog;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import xyz.mijack.csdn.blog.R;
import xyz.mijack.csdn.blog.fragment.TextFragment;
import xyz.mijack.csdn.blog.model.CategoryList;
import xyz.mijack.csdn.blog.model.Order;


public class MainActivity extends MaterialNavigationDrawer implements RadioGroup.OnCheckedChangeListener {
    TextFragment fragment;
    RadioGroup radioGroup;
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
            MainActivity.this.onClick(section);
            //后续处理
            DebugLog.i(section.getTitle());
            if (fragment.changeCategory(CategoryList.getCategoryByString(MainActivity.this, section.getTitle()))) {
                getToolbar().setTitle(section.getTitle());
                radioGroup.check(R.id.hotButton);
            }
        }
    };

    @Override
    public void init(Bundle savedInstanceState) {
        radioGroup = (RadioGroup) LayoutInflater.from(this).inflate(R.layout.layout_radio_group, null);
        Toolbar.LayoutParams lp =
                new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        radioGroup.setLayoutParams(lp);
        getToolbar().addView(radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        fragment = new TextFragment();
        // add account
        MaterialAccount account =
                new MaterialAccount(this.getResources(),
                        getString(R.string.author), getString(R.string.gmail_address),
                        R.drawable.profile, R.drawable.background);
        addAccount(account);
        MaterialSection accountSection = newSection(getString(R.string.github), this);
        addAccountSection(accountSection);

        addSubheader(getString(R.string.blog_category));

        MaterialSection section = newSection(getString(R.string.category_mobile), fragment);
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
        MaterialSection setSection = newSection(getString(R.string.app_settings), new Intent(this, SettingActivity.class));
        setSection.setOnClickListener(intentListener);
        addSection(setSection);
        MaterialSection aboutSection = newSection(getString(R.string.app_about), new Intent(this, AboutActivity.class));
        aboutSection.setOnClickListener(intentListener);
        addSection(aboutSection);


    }


    public MaterialSection buildSection(int string, MaterialSectionListener listener) {
        final MaterialSection section = new MaterialSection(this, 0, true, 2);
        section.setTitle(getString(string));
        section.setSectionColor(getResources().getColor(R.color.select_color));
        if (listener != null) {
            section.setOnClickListener(listener);
        }
        return section;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Order o = checkedId == R.id.hotButton ? Order.index : Order.newest;
        fragment.changeOrder(o);
    }
}