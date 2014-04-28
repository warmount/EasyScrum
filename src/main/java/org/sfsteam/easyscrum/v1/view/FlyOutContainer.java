package org.sfsteam.easyscrum.v1.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by warmount on 12.06.13.
 */
public class FlyOutContainer extends LinearLayout {

    // References to groups contained in this view.
    private View menu;
    private View content;
    private View addNew;

    // Constants
    protected static final int menuMargin = 150;

    public enum MenuState {
        CLOSED, OPEN,
    };

    // Position information attributes
    protected int currentContentOffset = 0;
    protected MenuState menuCurrentState = MenuState.CLOSED;

    public FlyOutContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlyOutContainer(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.menu = this.getChildAt(0);
        this.content = this.getChildAt(1);
        this.addNew = this.getChildAt(2);

        this.menu.setVisibility(View.GONE);
        this.addNew.setVisibility(View.GONE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        if (changed)
            this.calculateChildDimensions();

        this.menu.layout(left, top, right - menuMargin, bottom);

        this.content.layout(left + this.currentContentOffset, top, right
                + this.currentContentOffset, bottom);

        this.addNew.layout(left+menuMargin,top,right,bottom);

    }

    public void toggleMenuOpen() {

        this.menu.setVisibility(View.VISIBLE);
        this.currentContentOffset = this.getMenuWidth();
        this.content.offsetLeftAndRight(currentContentOffset);
        this.menuCurrentState = MenuState.OPEN;
        this.invalidate();
    }

    public void toggleMenuClose() {

        this.content.offsetLeftAndRight(-currentContentOffset);
        this.currentContentOffset = 0;
        this.menuCurrentState = MenuState.CLOSED;
        this.menu.setVisibility(View.GONE);
        this.addNew.setVisibility(View.GONE);

        this.invalidate();
    }

    public void toggleNewOpen(){

        this.currentContentOffset=-this.content.getLayoutParams().width+menuMargin;

        this.menu.setVisibility(View.GONE);
        this.menuCurrentState = MenuState.CLOSED;
        this.addNew.setVisibility(View.VISIBLE);

        this.invalidate();

    }

    private int getMenuWidth() {
        return this.menu.getLayoutParams().width;
    }

    private void calculateChildDimensions() {
        this.content.getLayoutParams().height = this.getHeight();
        this.content.getLayoutParams().width = this.getWidth();

        this.menu.getLayoutParams().width = this.getWidth() - menuMargin;
        this.menu.getLayoutParams().height = this.getHeight();

        this.addNew.getLayoutParams().width = this.getWidth() - menuMargin;
        this.addNew.getLayoutParams().height = this.getHeight();
    }
}
