// Generated code from Butter Knife. Do not modify!
package com.domain.apps.snapadeal.activities;

import android.view.View;
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

public class OfferDetailActivity_ViewBinding implements Unbinder {
  private OfferDetailActivity target;

  @UiThread
  public OfferDetailActivity_ViewBinding(OfferDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public OfferDetailActivity_ViewBinding(OfferDetailActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'toolbar'", Toolbar.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", SpinKitView.class);
    target.loading = Utils.findRequiredViewAsType(source, R.id.loading, "field 'loading'", LinearLayout.class);
    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.toolbar_title, "field 'toolbarTitle'", TextView.class);
    target.offer_label = Utils.findRequiredViewAsType(source, R.id.offer_label, "field 'offer_label'", TextView.class);
    target.image = Utils.findRequiredViewAsType(source, R.id.image, "field 'image'", ImageView.class);
    target.distanceView = Utils.findRequiredViewAsType(source, R.id.distanceView, "field 'distanceView'", TextView.class);
    target.priceView = Utils.findRequiredViewAsType(source, R.id.priceView, "field 'priceView'", TextView.class);
    target.detailOffer = Utils.findRequiredViewAsType(source, R.id.detail_offer, "field 'detailOffer'", TextView.class);
    target.offerUpTo = Utils.findRequiredViewAsType(source, R.id.offer_up_to, "field 'offerUpTo'", TextView.class);
    target.storeBtn = Utils.findRequiredViewAsType(source, R.id.storeBtn, "field 'storeBtn'", TextView.class);
    target.storeBtnLayout = Utils.findRequiredViewAsType(source, R.id.storeBtnLayout, "field 'storeBtnLayout'", LinearLayout.class);
    target.adView = Utils.findRequiredViewAsType(source, R.id.adView, "field 'adView'", AdView.class);
    target.ads = Utils.findRequiredViewAsType(source, R.id.ads, "field 'ads'", LinearLayout.class);
    target.mScroll = Utils.findRequiredViewAsType(source, R.id.mScroll, "field 'mScroll'", ParallaxScrollView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OfferDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.progressBar = null;
    target.loading = null;
    target.toolbarTitle = null;
    target.offer_label = null;
    target.image = null;
    target.distanceView = null;
    target.priceView = null;
    target.detailOffer = null;
    target.offerUpTo = null;
    target.storeBtn = null;
    target.storeBtnLayout = null;
    target.adView = null;
    target.ads = null;
    target.mScroll = null;
  }
}
