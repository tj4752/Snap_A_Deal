// Generated code from Butter Knife. Do not modify!
package com.domain.apps.snapadeal.activities;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.domain.apps.snapadeal.R;
import com.github.ybq.android.spinkit.SpinKitView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SplashActivity_ViewBinding implements Unbinder {
  private SplashActivity target;

  @UiThread
  public SplashActivity_ViewBinding(SplashActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SplashActivity_ViewBinding(SplashActivity target, View source) {
    this.target = target;

    target.getting = Utils.findRequiredViewAsType(source, R.id.getting, "field 'getting'", EditText.class);
    target.getloc = Utils.findRequiredViewAsType(source, R.id.getloc, "field 'getloc'", Button.class);
    target.connect = Utils.findRequiredViewAsType(source, R.id.connect, "field 'connect'", Button.class);
    target.contentMyStore = Utils.findRequiredViewAsType(source, R.id.content_my_store, "field 'contentMyStore'", LinearLayout.class);
    target.splashImage = Utils.findRequiredViewAsType(source, R.id.splashImage, "field 'splashImage'", ImageView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", SpinKitView.class);
    target.findstore = Utils.findRequiredViewAsType(source, R.id.findstore, "field 'findstore'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SplashActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.getting = null;
    target.getloc = null;
    target.connect = null;
    target.contentMyStore = null;
    target.splashImage = null;
    target.progressBar = null;
    target.findstore = null;
  }
}
