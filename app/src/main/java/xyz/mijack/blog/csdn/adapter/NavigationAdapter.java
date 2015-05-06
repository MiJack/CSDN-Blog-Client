package xyz.mijack.blog.csdn.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

import xyz.mijack.blog.csdn.R;
import xyz.mijack.blog.csdn.constant.Key;
import xyz.mijack.blog.csdn.fragment.BlogListFragment;
import xyz.mijack.blog.csdn.model.Category;
import xyz.mijack.blog.csdn.model.Order;

/**
 * Created by MiJack on 2015/5/2.
 */
public class NavigationAdapter extends CacheFragmentStatePagerAdapter {


    private Context context;
    private BlogListFragment[] fragments;

    public NavigationAdapter(FragmentActivity activity) {
        super(activity.getSupportFragmentManager());
        this.context = activity;
        fragments = new BlogListFragment[2];
        fragments[0] = createItem(0);
        fragments[1] = createItem(1);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? context.getString(R.string.order_by_hot) : context.getString(R.string.order_by_time);
    }

    @Override
    protected BlogListFragment createItem(int position) {
        BlogListFragment fragment = fragments[position];
        if (fragment == null) {
            fragment = new BlogListFragment();
            Bundle arguments = new Bundle();
            arguments.putString(Key.Order, position == 0 ? Order.index.name() : Order.newest.name());
            fragment.setArguments(arguments);
            fragments[position] = fragment;
        }
        return fragment;
    }

    public void changeCategory(Category category) {
        for (BlogListFragment blogListFragment : fragments) {
            blogListFragment.changeCategory(category);
        }
    }
}