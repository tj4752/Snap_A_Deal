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

public class FavoriteStoreActivity_ViewBinding implements Unbinder {
  private FavoriteStoreActivity target;

  @UiThread
  public FavoriteStoreActivity_ViewBinding(FavoriteStoreActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FavoriteStoreActivity_ViewBinding(FavoriteStoreActivity target, View source) {
    this.target = target;

    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.toolbar_title, "field 'toolbarTitle'", TextView.class);
    target.toolbarDescription = Utils.findRequiredViewAsType(source, R.id.toolbar_description, "field 'toolbarDescription'", TextView.class);
    target.storeContent = Utils.findRequiredViewAsType(source, R.id.store_content, "field 'storeContent'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FavoriteStoreActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbarTitle = null;
    target.toolbarDescription = null;
    target.storeContent = null;
  }
}
