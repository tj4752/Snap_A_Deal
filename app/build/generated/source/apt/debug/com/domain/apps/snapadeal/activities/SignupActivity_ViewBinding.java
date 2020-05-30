// Generated code from Butter Knife. Do not modify!
package com.domain.apps.snapadeal.activities;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.domain.apps.snapadeal.R;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SignupActivity_ViewBinding implements Unbinder {
  private SignupActivity target;

  @UiThread
  public SignupActivity_ViewBinding(SignupActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignupActivity_ViewBinding(SignupActivity target, View source) {
    this.target = target;

    target.userimage = Utils.findRequiredViewAsType(source, R.id.userimage, "field 'userimage'", CircularImageView.class);
    target.getImage = Utils.findRequiredViewAsType(source, R.id.getImage, "field 'getImage'", Button.class);
    target.country = Utils.findRequiredViewAsType(source, R.id.country, "field 'country'", MaterialAutoCompleteTextView.class);
    target.codeCountry = Utils.findRequiredViewAsType(source, R.id.codeCountry, "field 'codeCountry'", TextView.class);
    target.mobile = Utils.findRequiredViewAsType(source, R.id.mobile, "field 'mobile'", MaterialEditText.class);
    target.next = Utils.findRequiredViewAsType(source, R.id.next, "field 'next'", Button.class);
    target.infosLayout = Utils.findRequiredViewAsType(source, R.id.infos_layout, "field 'infosLayout'", LinearLayout.class);
    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", Button.class);
    target.email = Utils.findRequiredViewAsType(source, R.id.email, "field 'email'", MaterialEditText.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.name, "field 'name'", MaterialEditText.class);
    target.login = Utils.findRequiredViewAsType(source, R.id.login, "field 'login'", MaterialEditText.class);
    target.password = Utils.findRequiredViewAsType(source, R.id.password, "field 'password'", MaterialEditText.class);
    target.cpassword = Utils.findRequiredViewAsType(source, R.id.cpassword, "field 'cpassword'", MaterialEditText.class);
    target.signup = Utils.findRequiredViewAsType(source, R.id.signup, "field 'signup'", Button.class);
    target.connectLayout = Utils.findRequiredViewAsType(source, R.id.connect_layout, "field 'connectLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SignupActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.userimage = null;
    target.getImage = null;
    target.country = null;
    target.codeCountry = null;
    target.mobile = null;
    target.next = null;
    target.infosLayout = null;
    target.back = null;
    target.email = null;
    target.name = null;
    target.login = null;
    target.password = null;
    target.cpassword = null;
    target.signup = null;
    target.connectLayout = null;
  }
}
