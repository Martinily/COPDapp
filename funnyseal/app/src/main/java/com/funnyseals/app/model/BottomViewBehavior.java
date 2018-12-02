package com.funnyseals.app.model;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BottomViewBehavior extends CoordinatorLayout.Behavior<View> {
    public BottomViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.abs(dependency.getTop());
        child.setTranslationY(translationY);

        return true;
    }
}
