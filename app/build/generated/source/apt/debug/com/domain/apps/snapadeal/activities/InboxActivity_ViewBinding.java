// Generated code from Butter Knife. Do not modify!
package com.domain.apps.snapadeal.activities;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.domain.apps.snapadeal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class InboxActivity_ViewBinding implements Unbinder {
  private InboxActivity target;

  @UiThread
  public InboxActivity_ViewBinding(InboxActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public InboxActivity_ViewBinding(InboxActivity target, View source) {
    this.target = target;

    target.toolbarTitle = Utils.findRequiredViewAsType(source, R.id.toolbar_title, "field 'toolbarTitle'", TextView.class);
    target.toolbarDescription = Utils.findRequiredViewAsType(source, R.id.toolbar_description, "field 'toolbarDescription'", TextView.class);
    target.listdiscussion = Utils.findRequiredViewAsType(source, R.id.listdiscussion, "field 'listdiscussion'", RecyclerView.class);
    target.messageInput = Utils.findRequiredViewAsType(source, R.id.message_input, "field 'messageInput'", EditText.class);
    target.sendButton = Utils.findRequiredViewAsType(source, R.id.send_button, "field 'sendButton'", ImageButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    InboxActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbarTitle = null;
    target.toolbarDescription = null;
    target.listdiscussion = null;
    target.messageInput = null;
    target.sendButton = null;
  }
}
