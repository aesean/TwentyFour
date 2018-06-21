package com.aesean.twentyfour;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * BaseActivity
 *
 * @author xl
 * @version V1.0
 * @since 16/7/13
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    protected boolean hideSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return false;
        }
        View currentFocus = this.getCurrentFocus();
        return currentFocus != null && inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (shouldHideKeyboard(ev)) {
                hideSoftInput();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    protected boolean shouldHideKeyboard(MotionEvent event) {
        return false;
    }

    protected static boolean hitView(View v, MotionEvent event) {
        if (v.getVisibility() != View.VISIBLE) {
            return false;
        }
        int[] l = {0, 0};
        v.getLocationInWindow(l);
        int left = l[0];
        int top = l[1];
        int bottom = top + v.getHeight();
        int right = left + v.getWidth();
        float x = event.getX();
        float y = event.getY();
        return (x >= left && x <= right && y >= top && y <= bottom);
    }
}
