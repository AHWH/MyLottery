/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import sg.reddotdev.sharkfin.R;


public class GridLayoutDividerDecoration extends RecyclerView.ItemDecoration {
    private Drawable hDivider;
    private Drawable vDivider;

    public GridLayoutDividerDecoration(Context context) {
        hDivider = ContextCompat.getDrawable(context, R.drawable.horizontal_divider);
        vDivider = ContextCompat.getDrawable(context, R.drawable.vertical_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVerticalDivider(c, parent);
        drawHorizontalDivider(c, parent);
    }

    private void drawVerticalDivider(Canvas c, RecyclerView parent) {
        if (parent.getChildCount() == 0) return;

        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) child.getLayoutParams();

            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin;
            int top = child.getBottom() + params.bottomMargin ;
            int bottom = top + hDivider.getIntrinsicHeight();
            hDivider.setBounds(left, top, right, bottom);
            hDivider.draw(c);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) child.getLayoutParams();

            int left = child.getRight() + params.rightMargin;
            int right = left + vDivider.getIntrinsicWidth();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            vDivider.setBounds(left, top, right, bottom);
            vDivider.draw(c);
        }
    }
}
