package xyz.mijack.blog.csdn.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.mustafaferhan.debuglog.DebugLog;

import xyz.mijack.blog.csdn.R;
import xyz.mijack.blog.csdn.adapter.NavigationAdapter;
import xyz.mijack.blog.csdn.model.Category;
import xyz.mijack.blog.csdn.util.Util;

public class BlogPagerFragment extends Fragment {


    private NavigationAdapter mPagerAdapter;
    private ViewPager pager;

    public BlogPagerFragment() {
    }


    public NavigationAdapter getmPagerAdapter() {
        return mPagerAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blog_pager, container, false);
        pager = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new NavigationAdapter(this.getActivity());
        pager.setAdapter(mPagerAdapter);
        pager.setOffscreenPageLimit(mPagerAdapter.getCount());
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(pager);
        return rootView;
    }

    public void changeCategory(Category category) {
        DebugLog.d("changeCategory");
        mPagerAdapter.changeCategory(category);
        Util.showSnackBar(getActivity(), getString(category.getCategory()), 1000);
    }


}
