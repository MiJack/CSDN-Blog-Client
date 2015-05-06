package xyz.mijack.blog.csdn.model;


import android.content.Context;

import xyz.mijack.blog.csdn.R;


/**
 * Created by MiJack on 2015/4/16.
 */
public class CategoryList {

    private static final Category[] categories = new Category[]{
             new Category(R.string.category_mobile, "mobile"),
            new Category(R.string.category_web, "web"),
            new Category( R.string.category_enterprise, "enterprise"),
            new Category( R.string.category_code, "code"),
            new Category( R.string.category_www, "www"),
            new Category( R.string.category_database, "database"),
            new Category(R.string.category_system, "system"),
            new Category( R.string.category_cloud, "cloud"),
            new Category( R.string.category_software, "software"),
            new Category(R.string.category_other, "other"),
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
