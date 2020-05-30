package com.domain.apps.snapadeal.adapter.navigation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.domain.apps.snapadeal.R;
import com.domain.apps.snapadeal.classes.HeaderItem;
import com.domain.apps.snapadeal.classes.Item;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.util.List;


public class SimpleListAdapterNavDrawer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater infalter;
    private List<Item> data;
    private Context context;
    private ClickListener clickListener;

    public SimpleListAdapterNavDrawer(Context context, List<Item> data) {
        this.data = data;
        this.infalter = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == 1) {
            View rootView = infalter.inflate(R.layout.menu_custom_row_drawer, parent, false);
            return new mViewHolder(rootView);
        } else {
            View rootView = infalter.inflate(R.layout.navigation_drawer_header, parent, false);
            return new mHeaderViewHolder(rootView);
        }


    }


    public List<Item> getData() {

        return this.data;
    }

    public void update(int position, Item item) {
        this.data.get(position).setNotify(item.getNotify());
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderViews, int position) {


        if (holderViews instanceof mViewHolder) {

            mViewHolder holder = (mViewHolder) holderViews;
            holder.nameItem.setText(data.get(position).getName());

            if (!data.get(position).isEnabled()) {
                holder.root.setVisibility(View.GONE);
            }

            if (data.get(position).getNotify() > 0) {
                holder.notify.setVisibility(View.VISIBLE);
                holder.notify.setText(String.valueOf(data.get(position).getNotify()));
            } else {
                holder.notify.setVisibility(View.GONE);
            }


            if (data.get(position).getIconDraw() != null) {

                Drawable yourDrawable = MaterialDrawableBuilder.with(context) // provide a context
                        .setIcon(data.get(position).getIconDraw()) // provide an icon
                        .setColor(context.getResources().getColor(R.color.gray)) // set the icon color
                        .setSizeDp(24) // set the icon size
                        .build();


                holder.imageItem.setImageDrawable(yourDrawable);
                if (!data.get(position).isEnabled()) {

                    holder.root.setVisibility(View.GONE);
                }
            }

        } else if (holderViews instanceof mHeaderViewHolder) {

            mHeaderViewHolder holder = (mHeaderViewHolder) holderViews;
            if (!data.get(position).isEnabled()) {
                holder.root.setVisibility(View.GONE);
            }

        }

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0 && data.get(position) instanceof HeaderItem) {
            return 0;
        }
        return 1;
    }

    public void setClickListener(ClickListener clicklistener) {

        this.clickListener = clicklistener;

    }


    public interface ClickListener {
        void itemClicked(View view, int position);
    }

    public class mHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View root;
        mHeaderViewHolder(View itemView) {
            super(itemView);
            root = itemView;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //for item menu
        TextView nameItem;
        ImageView imageItem;
        TextView notify;
        View root;

        public mViewHolder(View itemView) {
            super(itemView);

            root = itemView;
            itemView.setOnClickListener(this);
            //for item menu
            nameItem = itemView.findViewById(R.id.itemtext);
            imageItem = itemView.findViewById(R.id.itemimage);
            notify = itemView.findViewById(R.id.notify);

        }


        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }


}




