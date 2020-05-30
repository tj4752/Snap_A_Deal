// Generated code from Butter Knife. Do not modify!
package com.domain.apps.snapadeal.activities;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.domain.apps.snapadeal.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdView;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EventDetailActivity_ViewBinding implements Unbinder {
  private EventDetailActivity target;

  @UiThread
  public EventDetailActivity_ViewBinding(EventDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EventDetailActivity_ViewBinding(EventDetailActivity target, View source) {
    this.target = target;

    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.toolbar_title, "field 'toolbarTitle'", TextView.class);
    target.scrollView = Utils.findRequiredViewAsType(source, R.id.mScroll, "field 'scrollView'", ParallaxScrollView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", SpinKitView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'toolbar'", Toolbar.class);
    target.image = Utils.findRequiredViewAsType(source, R.id.iv_zoom, "field 'image'", ImageView.class);
    target._participate = Utils.findRequiredViewAsType(source, R.id.participate, "field '_participate'", Button.class);
    target._unparticipate = Utils.findRequiredViewAsType(source, R.id.unparticipate, "field '_unparticipate'", Button.class);
    target.description = Utils.findRequiredViewAsType(source, R.id.event_desc, "field 'description'", TextView.class);
    target.date = Utils.findRequiredViewAsType(source, R.id.event_date, "field 'date'", TextView.class);
    target.event_title = Utils.findRequiredViewAsType(source, R.id.event_title, "field 'event_title'", TextView.class);
    target.website = Utils.findRequiredViewAsType(source, R.id.event_website, "field 'website'", TextView.class);
    target.tel = Utils.findRequiredViewAsType(source, R.id.event_tel, "field 'tel'", TextView.class);
    target.layout_phone = Utils.findRequiredViewAsType(source, R.id.layout_phone, "field 'layout_phone'", LinearLayout.class);
    target.layout_website = Utils.findRequiredViewAsType(source, R.id.layout_website, "field 'layout_website'", LinearLayout.class);
    target.event_store_layout = Utils.findRequiredViewAsType(source, R.id.event_store_layout, "field 'event_store_layout'", LinearLayout.class);
    target.event_store_view = Utils.findRequiredViewAsType(source, R.id.event_store_view, "field 'event_store_view'", TextView.class);
    target.mAdView = Utils.findRequiredViewAsType(source, R.id.adView, "field 'mAdView'", AdView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    EventDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbarTitle = null;
    target.scrollView = null;
    target.progressBar = null;
    target.toolbar = null;
    target.image = null;
    target._participate = null;
    target._unparticipate = null;
    target.description = null;
    target.date = null;
    target.event_title = null;
    target.website = null;
    target.tel = null;
    target.layout_phone = null;
    target.layout_website = null;
    target.event_store_layout = null;
    target.event_store_view = null;
    target.mAdView = null;
  }
}
