/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.util.ui;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class BaseActivityView extends AppCompatActivity implements BaseView {
    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
