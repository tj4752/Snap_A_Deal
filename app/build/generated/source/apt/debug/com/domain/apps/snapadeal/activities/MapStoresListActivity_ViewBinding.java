// Generated code from Butter Knife. Do not modify!
package com.domain.apps.snapadeal.activities;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.domain.apps.snapadeal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MapStoresListActivity_ViewBinding implements Unbinder {
  private MapStoresListActivity target;

  @UiThread
  public MapStoresListActivity_ViewBinding(MapStoresListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MapStoresListActivity_ViewBinding(MapStoresListActivity target, View source) {
    this.target = target;

    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.toolbar_title, "field 'toolbarTitle'", TextView.class);
    target.toolbarDescription = Utils.findRequiredViewAsType(source, R.id.toolbar_description, "field 'toolbarDescription'", TextView.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.name, "field 'name'", TextView.class);
    target.ratingBar2 = Utils.findRequiredViewAsType(source, R.id.ratingBar2, "field 'ratingBar2'", RatingBar.class);
    target.rate = Utils.findRequiredViewAsType(source, R.id.rate, "field 'rate'", TextView.class);
    target.closeLayout = Utils.findRequiredViewAsType(source, R.id.closeLayout, "field 'closeLayout'", ImageButton.class);
    target.store_focus_layout = Utils.findRequiredViewAsType(source, R.id.store_focus_layout, "field 'store_focus_layout'", LinearLayout.class);
    target.fab = Utils.findRequiredViewAsType(source, R.id.fab, "field 'fab'", FloatingActionButton.class);
    target.content_my_store = Utils.findRequiredViewAsType(source, R.id.content_my_store, "field 'content_my_store'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MapStoresListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbarTitle = null;
    target.toolbarDescription = null;
    target.name = null;
    target.ratingBar2 = null;
    target.rate = null;
    target.closeLayout = null;
    target.store_focus_layout = null;
    target.fab = null;
    target.content_my_store = null;
  }
}
