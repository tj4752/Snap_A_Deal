// Generated code from Butter Knife. Do not modify!
package com.domain.apps.snapadeal.activities;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.domain.apps.snapadeal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReviewsActivity_ViewBinding implements Unbinder {
  private ReviewsActivity target;

  @UiThread
  public ReviewsActivity_ViewBinding(ReviewsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ReviewsActivity_ViewBinding(ReviewsActivity target, View source) {
    this.target = target;

    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.toolbar_title, "field 'toolbarTitle'", TextView.class);
    target.toolbarDescription = Utils.findRequiredViewAsType(source, R.id.toolbar_description, "field 'toolbarDescription'", TextView.class);
    target.list = Utils.findRequiredViewAsType(source, R.id.list, "field 'list'", RecyclerView.class);
    target.refresh = Utils.findRequiredViewAsType(source, R.id.refresh, "field 'refresh'", SwipeRefreshLayout.class);
    target.contentMyStore = Utils.findRequiredViewAsType(source, R.id.content_my_store, "field 'contentMyStore'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReviewsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbarTitle = null;
    target.toolbarDescription = null;
    target.list = null;
    target.refresh = null;
    target.contentMyStore = null;
  }
}
