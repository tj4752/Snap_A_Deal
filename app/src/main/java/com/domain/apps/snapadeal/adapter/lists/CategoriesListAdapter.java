package com.domain.apps.snapadeal.adapter.lists;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.domain.apps.snapadeal.AppController;
import com.domain.apps.snapadeal.R;
import com.domain.apps.snapadeal.classes.Category;
import com.domain.apps.snapadeal.utils.Utils;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;


public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.mViewHolder> {


    private LayoutInflater infalter;
    private List<Category> data;
    private Context context;
    private ClickListener clickListener;


    public CategoriesListAdapter(Context context, List<Category> data) {
        this.data = data;
        this.infalter = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public CategoriesListAdapter.mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = infalter.inflate(R.layout.item_category, parent, false);
        //
        mViewHolder holder = new mViewHolder(rootView);

        return holder;
    }


    @Override
    public void onBindViewHolder(final CategoriesListAdapter.mViewHolder holder, final int position) {


        holder.name.setText(data.get(position).getNameCat());

        if (data.get(position).getImages() != null) {

            /*Picasso.with(context)
                    .load(data.get(position).getImages().getUrl500_500())
                    .fit().centerCrop().into(holder.image);*/


            Glide.with(context)
                    .asBitmap()
                    .load(data.get(position).getImages().getUrl500_500())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            if (AppController.isRTL()) {
                                Bitmap output = Utils.flip(resource);
                                holder.image.setImageBitmap(output);
                            } else {
                                holder.image.setImageBitmap(resource);
                            }

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                            holder.image.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.def_logo, null));

                        }
                    });

        } else {
            holder.image.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.def_logo, null));
        }


        Drawable storeDrawable = new IconicsDrawable(context)
                .icon(CommunityMaterial.Icon.cmd_map_marker)
                .color(ResourcesCompat.getColor(context.getResources(), R.color.colorWhite, null))
                .sizeDp(12);

        if (AppController.isRTL()) {
            holder.stores.setCompoundDrawables(null, null, storeDrawable, null);
            holder.stores.setCompoundDrawablePadding(10);
        } else {
            holder.stores.setCompoundDrawables(storeDrawable, null, null, null);
            holder.stores.setCompoundDrawablePadding(10);
        }

        holder.stores.setText(String.format(
                context.getString(R.string.nbr_stores_message),
                String.valueOf(data.get(position).getNbr_stores())
        ));

    }


    public Category getItem(int position) {

        try {
            return data.get(position);
        } catch (Exception e) {
            return null;
        }

    }


    public void clear() {

        data = new ArrayList<Category>();
        notifyDataSetChanged();

    }

    public void addItem(Category item) {

        int index = (data.size());
        data.add(item);
        notifyItemInserted(index);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListener(ClickListener clicklistener) {

        this.clickListener = clicklistener;

    }


    public interface ClickListener {
        void itemClicked(View view, int position);
    }

    public class mViewHolder extends RecyclerView.ViewHolder {


        public TextView name;
        public ImageView image;
        public TextView stores;
        public View mainLayout;


        public mViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cat_name);
            image = itemView.findViewById(R.id.image);
            stores = itemView.findViewById(R.id.stores);
            mainLayout = itemView.findViewById(R.id.mainLayout);

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.itemClicked(view, getPosition());
                    }
                }
            });
        }


    }


}