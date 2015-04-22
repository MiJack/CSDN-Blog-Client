package xyz.mijack.csdn.blog.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import xyz.mijack.csdn.blog.R;
import xyz.mijack.csdn.blog.callback.OnCategoryChangeListener;
import xyz.mijack.csdn.blog.callback.OnOderChangeListener;
import xyz.mijack.csdn.blog.model.Category;
import xyz.mijack.csdn.blog.model.Order;

/**
 * Created by MiJack on 2015/4/21.
 */
public class TextFragment extends Fragment implements OnOderChangeListener, OnCategoryChangeListener {
    TextView orderText, categoryText;
    Order order;
    Category category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_text, container, false);
        orderText = (TextView) root.findViewById(R.id.order);
        categoryText = (TextView) root.findViewById(R.id.category);
        return root;
    }

    @Override
    public boolean changeCategory(Category c) {
        if (c == category) {
            return false;
        }
        category = c;
        categoryText.setText(category.getName());
        return true;
    }

    @Override
    public void changeOrder(Order o) {
        order = o;
        orderText.setText(order.toString());
    }


}
