package com.domain.apps.snapadeal.adapter.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.domain.apps.snapadeal.R;
import com.domain.apps.snapadeal.classes.Notification;
import com.domain.apps.snapadeal.controllers.notification.NotificationController;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Notification> items = new ArrayList<>();

    public List<Notification> getItems() {
        return items;
    }

    public void setItems(List<Notification> items) {
        this.items = items;
    }

    private Context ctx;
    private ClickListener mClickListener;

    public void setClickListener(final ClickListener mItemClickListener) {
        this.mClickListener = mItemClickListener;
    }

    public NotificationAdapter(Context context, List<Notification> items) {
        this.items = items;
        ctx = context;
    }


    public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView notificationCV;
        LinearLayout notif_layout;
        CircularImageView image;
        TextView label;
        TextView description;
        ImageButton more;


        public NotificationViewHolder(View v) {
            super(v);

            notificationCV = (CardView) v.findViewById(R.id.notification_cv);
            notif_layout = (LinearLayout) v.findViewById(R.id.notif_layout);
            image = (CircularImageView) v.findViewById(R.id.image);
            label = (TextView) v.findViewById(R.id.notif_label);
            description = (TextView) v.findViewById(R.id.description);
            more = (ImageButton) v.findViewById(R.id.notif_menu);


            //set click listeners
            notificationCV.setOnClickListener(this);
            more.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.notification_cv) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(view, getPosition());
                }
            } else if (view.getId() == R.id.notif_menu) {
                if (mClickListener != null) {
                    mClickListener.onMoreButtonClick(view, getPosition());
                }
            }

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        vh = new NotificationViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NotificationViewHolder) {
            NotificationViewHolder view = (NotificationViewHolder) holder;


            final Notification notif = items.get(position);
            view.label.setText( notif.getLabel_description() );
            view.description.setText( notif.getLabel() );


            if (notif.getImages() != null) {
                Glide.with(ctx)
                        .load(notif.getImages().getUrl200_200())
                        .centerCrop().placeholder(R.drawable.def_logo)
                        .into(view.image);
            } else {
                Glide.with(ctx).load(R.drawable.def_logo)
                        .centerCrop().into(view.image);
            }


            if (notif.getStatus() == 1) {   //SEEN
                view.notif_layout.setBackgroundColor(ContextCompat.getColor(ctx, R.color.defaultWhiteColor));
            } else {
                view.notif_layout.setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorPrimaryTransparent));
            }

        }
    }


    public void addItem(Notification item) {
        if (item != null) {
            if (items.add(item)) {
                NotificationController.insertNotification(item);
                notifyDataSetChanged();
            }
        }

    }

    public void updateItem(final int position, final Notification item) {
        if (item != null && position >= 0) {
            items.set(position, item);
            NotificationController.updateNotification(item);
            notifyDataSetChanged();
        }


    }

    public void removeItem(final int noti_id, final int position) {

        Notification item = NotificationController.getNotificationDetail(noti_id);
        if (item != null) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            items.remove(item);
            NotificationController.removeNotification(item, realm);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
            realm.commitTransaction();
        }

    }


    public void addAll(final List<Notification> productList) {
        int size = productList.size();

        items.clear();
        if (size > 0) {
            //remove all data before adding new items
            for (int i = 0; i < size; i++) {
                items.add(productList.get(i));
            }

            notifyDataSetChanged();
        }


    }


    public void removeAll() {
        items.clear();

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ClickListener {
        void onItemClick(View view, int pos);
        void onMoreButtonClick(View view, int position);

    }


}