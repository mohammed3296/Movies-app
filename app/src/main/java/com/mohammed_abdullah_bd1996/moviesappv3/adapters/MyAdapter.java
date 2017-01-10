package com.mohammed_abdullah_bd1996.moviesappv3.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mohammed_abdullah_bd1996.moviesappv3.FavoriteStore;
import com.mohammed_abdullah_bd1996.moviesappv3.R;
import com.mohammed_abdullah_bd1996.moviesappv3.SquaredImageView;
import com.mohammed_abdullah_bd1996.moviesappv3.activites.DetailsActivity;
import com.mohammed_abdullah_bd1996.moviesappv3.activites.MainActivity;
import com.mohammed_abdullah_bd1996.moviesappv3.fragments.DetailsFragment;
import com.mohammed_abdullah_bd1996.moviesappv3.models.ItemModel;
import com.mohammed_abdullah_bd1996.moviesappv3.models.ModelOfFavoriteMovie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// Start of MyAdapter
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    //=================================================================================================
    private static final String TAG = "CustomAdapter";
    private static Context mContext;
    private List<ItemModel> mDataSet; // list of item model
    private final ArrayList<Integer> likedPositions = new ArrayList<>();
    private int lastCheckedPosition = -1;

    //=================================================================================================

     /*
     * constructor for class MyAdapter with
     *
     * @param mContext // type is Context
     * @param mDataSet // type is list
     */

    public MyAdapter(Context mContext, List<ItemModel> mDataSet) {
        this.mDataSet = mDataSet;
        this.mContext = mContext;
       // Toast.makeText(mContext, R.string.action_favorites,Toast.LENGTH_LONG).show();
    }

    //=================================================================================================
    //Start of viewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        private final SquaredImageView imageView;
        private final Button favorite;
        private final ProgressBar mProgress;

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //constructor from ViewHolder class
        public ViewHolder(View v) {

            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainActivity.mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putSerializable(DetailsFragment.ARG_ITEM_ID, mDataSet.get(getPosition()));
                        DetailsFragment fragment = new DetailsFragment();
                        fragment.setArguments(arguments);
                        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        //create intent from DetailsActivity
                        Context context = v.getContext();
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra(DetailsFragment.ARG_ITEM_ID, mDataSet.get(getPosition()));
                        context.startActivity(intent);
                    }
                }
            });
            imageView = (SquaredImageView) v.findViewById(R.id.feedImage1);
            favorite = (Button) v.findViewById(R.id.favorite_button);
            mProgress = (ProgressBar) v.findViewById(R.id.progressBar);
        }

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        public SquaredImageView getImageView() {
            return imageView;
        }
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        public Button getFavorite() {
            return favorite;
        }
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        public ProgressBar getProgressBar() {
            return mProgress;
        }
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    }
    //End of viewHolder class
//===============================================================================================================
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Creatting a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_forecast, viewGroup, false); // inflatting the items
        return new ViewHolder(v); // return object of ViewHolder
    }
    //===============================================================================================================
    // Replace the contents of a view   invoked by the layout manager
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (mDataSet.get(position).getPoster_path() != null) {
            // showing the  progressBar over image
            viewHolder.getProgressBar().setVisibility(View.VISIBLE);
            // Adapter re-use is automatically detected and the previous download canceled.
            // using Picasso library for view
            //Picasso supports both download and error placeholders as optional features.
            //Transform images to better fit into layouts and to reduce memory size.
            /*Pass an instance of this class to the transform method.*/
            Picasso.with(mContext).load(mDataSet.get(position).getPoster_path())
                    .placeholder(R.drawable.rectangle)

                    .into(viewHolder.getImageView(), new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if (viewHolder.getProgressBar() != null) {
                                viewHolder.getProgressBar().setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onError() {
                        }
                    });
        } else {
            viewHolder.getImageView().setVisibility(View.GONE);
        }
        viewHolder.getFavorite().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCheckedPosition = position;
                notifyItemRangeChanged(0, mDataSet.size());
                if (!likedPositions.contains(position)) {
                    likedPositions.add(position);
                 //   updateHeartButton(viewHolder, true);
                }
                // adding to database
                addItem(position);
                //toast to tell the user that the movie is added in the favorites
                Toast.makeText(mContext,"Item is added in the favorites",Toast.LENGTH_LONG).show();
            }
        });
    }
    //===============================================================================================================
    //add this movie to favorites
    private void addItem(int position) {
        ModelOfFavoriteMovie item = new ModelOfFavoriteMovie();
        item.setTitleKey(mDataSet.get(position).getOriginal_title());
        item.setIdValue(mDataSet.get(position).getId());
        new FavoriteStore(mContext).update(item);
    }
    //===============================================================================================================
    @Override
    public int getItemCount() {
        return mDataSet.size(); // return size of dataset
    }
} //end of MyAdapter class