// Generated code from Butter Knife. Do not modify!
package com.domain.apps.snapadeal.activities;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.domain.apps.snapadeal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class IntroSliderActivity_ViewBinding implements Unbinder {
  private IntroSliderActivity target;

  @UiThread
  public IntroSliderActivity_ViewBinding(IntroSliderActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public IntroSliderActivity_ViewBinding(IntroSliderActivity target, View source) {
    this.target = target;

    target.viewPager = Utils.findRequiredViewAsType(source, R.id.view_pager, "field 'viewPager'", ViewPager.class);
    target.layoutDots = Utils.findRequiredViewAsType(source, R.id.layoutDots, "field 'layoutDots'", LinearLayout.class);
    target.btnNext = Utils.findRequiredViewAsType(source, R.id.btn_next, "field 'btnNext'", Button.class);
    target.btnSkip = Utils.findRequiredViewAsType(source, R.id.btn_skip, "field 'btnSkip'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    IntroSliderActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
    target.layoutDots = null;
    target.btnNext = null;
    target.btnSkip = null;
  }
}
