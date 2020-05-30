// Generated code from Butter Knife. Do not modify!
package com.domain.apps.snapadeal.activities;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.domain.apps.snapadeal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ListStoresActivity_ViewBinding implements Unbinder {
  private ListStoresActivity target;

  @UiThread
  public ListStoresActivity_ViewBinding(ListStoresActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ListStoresActivity_ViewBinding(ListStoresActivity target, View source) {
    this.target = target;

    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.toolbar_title, "field 'toolbarTitle'", TextView.class);
    target.toolbarDescription = Utils.findRequiredViewAsType(source, R.id.toolbar_description, "field 'toolbarDescription'", TextView.class);
    target.frame = Utils.findRequiredViewAsType(source, R.id.frame, "field 'frame'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ListStoresActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbarTitle = null;
    target.toolbarDescription = null;
    target.frame = null;
  }
}
