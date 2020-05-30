package com.domain.apps.snapadeal.adapter.messenger;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.domain.apps.snapadeal.R;
import com.domain.apps.snapadeal.appconfig.AppConfig;
import com.domain.apps.snapadeal.classes.Discussion;
import com.domain.apps.snapadeal.utils.DateUtils;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Droideve on 1/5/2016.
 */

public class ListDiscussionAdapter extends RecyclerView.Adapter<ListDiscussionAdapter.mViewHolder> {


    private LayoutInflater infalter;
    private List<Discussion> data;
    private Context context;
    private ClickListener clickListener;
    private TouchListener touchListener;


    public ListDiscussionAdapter(Context context, List<Discussion> data) {
        this.data = data;
        this.infalter = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public ListDiscussionAdapter.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = infalter.inflate(R.layout.layout_list_discussion, parent, false);

        mViewHolder holder = new mViewHolder(rootView);

        return holder;
    }


    public void removeAll() {
        data = new ArrayList<>();
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(ListDiscussionAdapter.mViewHolder holder, int position) {

        if (AppConfig.APP_DEBUG) {
            Log.e("onBindViewHolder", String.valueOf(data.get(position).getNbrMessage()));
        }

        if (data.get(position).getNbrMessage() > 0) {

            holder.nbrMessage.setText(data.get(position).getNbrMessage() + "");
            holder.nbrMessage.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);

        } else {

            holder.nbrMessage.setVisibility(View.GONE);
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(
                    DateUtils.prepareOutputDate(data.get(position).getCreatedAt(), null, context)
            );
        }

        holder.name.setText(
                data.get(position).getSenderUser().getName()
        );

        holder.name.setMaxLines(1);
        holder.name.setSingleLine();

        if (data.get(position).getMessages().size() > 0) {
            holder.short_msg.setText(data.get(position).getMessages().get(0).getMessage());
        }

        if (data.get(position).getSenderUser().getImages() != null) {

            Glide.with(context).load(data.get(position).getSenderUser().getImages().getUrl200_200())
                    .placeholder(R.drawable.profile_placeholder)
                    .centerCrop().into(holder.photo);
        } else {

            Glide.with(context).load(R.drawable.profile_placeholder)
                    .placeholder(R.drawable.profile_placeholder)
                    .centerCrop().into(holder.photo);
        }

    }


    public void remove(int pos) {
        try {

            data.remove(pos);
            notifyDataSetChanged();
        } catch (Exception e) {

        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public void addItem(Discussion discussion) {
        data.add(discussion);
        notifyItemInserted(data.size());
    }


    public void addItem(int pos, Discussion discussion) {
        try {
            data.add(pos, discussion);
            notifyDataSetChanged();
        } catch (Exception e) {

        }

    }

    public Discussion getItem(int position) {
        return data.get(position);
    }

    public void setTouchListener(TouchListener touchListener) {

        this.touchListener = touchListener;

    }

    public void setClickListener(ClickListener clicklistener) {

        this.clickListener = clicklistener;

    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }

    public interface TouchListener {
        void itemTouched(View view, int position);
    }

    public class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {


        public CircularImageView photo;
        public TextView name;
        public TextView short_msg;
        public TextView date;
        public TextView nbrMessage;


        public mViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            short_msg = itemView.findViewById(R.id.short_msg);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            nbrMessage = itemView.findViewById(R.id.nbrMessage);

        }


        @Override
        public void onClick(View v) {


            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }

            //delete(getPosition());

        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {


            if (touchListener != null) {
                touchListener.itemTouched(v, getPosition());
            }

            return false;
        }
    }


}
