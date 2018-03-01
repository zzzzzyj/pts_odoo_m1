package com.pts_odoo_m1;

import android.view.KeyEvent;

import com.facebook.react.ReactActivity;
import com.github.kevinejohn.keyevent.KeyEventModule;

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "pts_odoo_m1";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        KeyEventModule.getInstance().onKeyDownEvent(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        KeyEventModule.getInstance().onKeyUpEvent(keyCode, event);
        return super.onKeyUp(keyCode, event);
    }
}
