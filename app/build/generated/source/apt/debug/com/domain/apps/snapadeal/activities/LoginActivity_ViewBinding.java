// Generated code from Butter Knife. Do not modify!
package com.domain.apps.snapadeal.activities;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.domain.apps.snapadeal.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target.login = Utils.findRequiredViewAsType(source, R.id.login, "field 'login'", MaterialEditText.class);
    target.password = Utils.findRequiredViewAsType(source, R.id.password, "field 'password'", MaterialEditText.class);
    target.connect = Utils.findRequiredViewAsType(source, R.id.connect, "field 'connect'", Button.class);
    target.forgotpassword = Utils.findRequiredViewAsType(source, R.id.forgotpassword, "field 'forgotpassword'", TextView.class);
    target.signup = Utils.findRequiredViewAsType(source, R.id.signup, "field 'signup'", Button.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", SpinKitView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.login = null;
    target.password = null;
    target.connect = null;
    target.forgotpassword = null;
    target.signup = null;
    target.progressBar = null;
  }
}
