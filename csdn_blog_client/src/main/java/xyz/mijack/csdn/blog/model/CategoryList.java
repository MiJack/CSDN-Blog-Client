package xyz.mijack.csdn.blog.model;


import android.content.Context;

import xyz.mijack.csdn.blog.R;
import xyz.mijack.csdn.blog.ui.MainActivity;

/**
 * Created by MiJack on 2015/4/16.
 */
public class CategoryList {

    private static final Category[] categories = new Category[]{
            //  new Category(R.drawable.all, R.string.all,"all"),
            new Category(R.drawable.category_mobile, R.string.category_mobile, "mobile"),
            new Category(R.drawable.category_web, R.string.category_web, "web"),
            new Category(R.drawable.category_enterprise, R.string.category_enterprise, "enterprise"),
            new Category(R.drawable.category_code, R.string.category_code, "code"),
            new Category(R.drawable.category_www, R.string.category_www, "www"),
            new Category(R.drawable.category_database, R.string.category_database, "database"),
            new Category(R.drawable.category_system, R.string.category_system, "system"),
            new Category(R.drawable.category_cloud, R.string.category_cloud, "cloud"),
            new Category(R.drawable.category_software, R.string.category_software, "software"),
            new Category(R.drawable.category_other, R.string.category_other, "other"),
    };

    public static int getCount() {
        return categories.length;
    }

    public static Category get(int position) {
        return categories[position];
    }

    public static Category getCategoryByString(Context context, String title) {
        for (Category category : categories) {
            if (context.getString(category.getCategory()).equals(title)) {
                return category;
            }
        }
        return null;
    }
}
