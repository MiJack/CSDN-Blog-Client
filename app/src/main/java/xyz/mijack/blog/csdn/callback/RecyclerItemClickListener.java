package xyz.mijack.blog.csdn.callback;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by MiJack on 2015/5/3.
 */

/**
 * Created by MiJack on 2015/4/26.
 */
public class RecyclerItemClickListener extends GestureDetector.SimpleOnGestureListener implements RecyclerView.OnItemTouchListener {

    private RecyclerView recyclerView;
    private OnItemClickListener mListener;
    private GestureDetector mGestureListener;

    public RecyclerItemClickListener(final Context context, RecyclerView recyclerView, OnItemClickListener listener) {
        this.recyclerView = recyclerView;
        mListener = listener;
        mGestureListener = new GestureDetector(context, this);
    }

    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        mGestureListener.onTouchEvent(e);
        return false;
    }

    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("Gesture", "onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Gesture", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("Gesture", "onSingleTapUp");
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null && mListener != null) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
            int position = recyclerView.getChildPosition(view);
            mListener.onItemClick(view, holder, position, e);
        }
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("Gesture", "onLongPress");
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null && mListener != null) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
            int position = recyclerView.getChildPosition(view);
            mListener.onItemLongClick(view, holder, position, e);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("Gesture", "onFling");
        View view = recyclerView.findChildViewUnder(e1.getX(), e1.getY());
        if (view != null && mListener != null) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
            int position = recyclerView.getChildPosition(view);
            mListener.onItemFling(view, holder, position, e1, e2, velocityX, velocityY);
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("Gesture", "onScroll");
        return false;
    }

    public static interface OnItemClickListener<T extends RecyclerView.ViewHolder> {
        void onItemClick(View view, T holder, int position, MotionEvent e);

        void onItemLongClick(View view, T holder, int position, MotionEvent e);

        void onItemFling(View view, T holder, int position, MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
    }

    public static class SimpleOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position, MotionEvent e) {

        }

        @Override
        public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position, MotionEvent e) {

        }

        @Override
        public void onItemFling(View view, RecyclerView.ViewHolder holder, int position, MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        }
    }
}