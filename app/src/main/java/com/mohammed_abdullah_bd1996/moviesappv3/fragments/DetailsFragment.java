package com.mohammed_abdullah_bd1996.moviesappv3.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mohammed_abdullah_bd1996.moviesappv3.Controller;
import com.mohammed_abdullah_bd1996.moviesappv3.R;
import com.mohammed_abdullah_bd1996.moviesappv3.SquaredImageView;
import com.mohammed_abdullah_bd1996.moviesappv3.adapters.ReviewsAdapter;
import com.mohammed_abdullah_bd1996.moviesappv3.models.ItemModel;
import com.mohammed_abdullah_bd1996.moviesappv3.models.ModelOfReview;
import com.mohammed_abdullah_bd1996.moviesappv3.parsers.JsonParser;
import com.mohammed_abdullah_bd1996.moviesappv3.urls.URLS;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Start of DetailsFragment class
public class DetailsFragment extends Fragment {
    //the fields of the class
    //*******************************************************************************
    public static final String ARG_ITEM_ID = "item_id"; // id of the item
    private static ItemModel itemModel;
    private static String movie_trial_id;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 3;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    //********************************************************************************
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected ReviewsAdapter mAdapter;
    protected List<ModelOfReview> mDataset;
    protected RecyclerView.LayoutManager mLayoutManager;
    //*********************************************************************************

    /**
     * constructor
     */

    public DetailsFragment() {

    }

    //**************************************************************************************
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize dataSet
        mDataset = new ArrayList<>();
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            // this like in the sunshine
            itemModel = (ItemModel) getArguments().getSerializable(ARG_ITEM_ID);
        }
    }
//**********************************************************************************************
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        // populate recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        // initialize adapter
        mAdapter = new ReviewsAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        TextView tv = (TextView) view.findViewById(R.id.original_title); // for title of the movie
        TextView tv1 = (TextView) view.findViewById(R.id.release_date);  // for release data
        TextView tv3 = (TextView) view.findViewById(R.id.overview);      //for overview of the movies
        //button for load the reviews
        final Button btn = (Button) view.findViewById(R.id.load);
        //event
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setVisibility(View.GONE);
                initiateReviewAndTrial(1, itemModel.getId());
            }
        });
        SquaredImageView imageView = (SquaredImageView) view.findViewById(R.id.feedImage1);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.progressBar);
        // for show rating bar
        RatingBar ratingbar = (RatingBar) view.findViewById(R.id.ratingBar);
        //get average of the votes
        ratingbar.setRating(Float.parseFloat(itemModel.getVote_average()) / 2);

        tv.setText(itemModel.getOriginal_title());
        tv1.setText(itemModel.getRelease_date());
        tv3.setText(itemModel.getOverview());


        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        // get movie trial id
        initiateReviewAndTrial(0, itemModel.getId());
        // Feed image
        if (itemModel.getBackdrop_path() != null) {
            // show progressBar
            pb.setVisibility(View.VISIBLE);
            // Adapter re-use is automatically detected and the previous download canceled.
            //for Backdrop
            Picasso.with(getActivity()).load(itemModel.getBackdrop_path())
                    .placeholder(R.drawable.rectangle)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if (pb != null) {
                                pb.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }
        // for Action Button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie_trial_id == null)
                    Snackbar.make(view, "There is no trial video to show :(", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                watchYoutubeVideo(movie_trial_id);
            }
        });
        return view;
    }
//****************************************************************************************************
    /**
     * Set RecyclerView's LayoutManager to the one given.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }
//***************************************************************************************************
    //I allow users to view and play trailers (  in the youtube app).
    // this intent for call youtube app for watching the video
    public void watchYoutubeVideo(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }
//******************************************************************************************************
    private void initiateReviewAndTrial(final int index, String idOfMovie) {
        String Url = "";
        switch (index) {
            case 0:
                Url = URLS.MAIN_URL_FROM_DESCRIPTION + idOfMovie + URLS.VEDIO_TERIAL;
                break;
            case 1:
                Url = URLS.MAIN_URL_FROM_DESCRIPTION + idOfMovie + URLS.VEDIO_REVIEW;
                break;
            default:
                Url = URLS.MAIN_URL_FROM_DESCRIPTION + idOfMovie + URLS.VEDIO_TERIAL;
        }
        /**
         * Execute the background task, which uses {@link AsyncTask} to load the data.
         */
        // We first check for cached request
        Cache cache = Controller.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Url);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                if (index == 0) {
                    // retrieve trial id
                    movie_trial_id = JsonParser.parseTrailer(data);
                } else {
                    // retrieve review
                    clearDataSet();
                    Iterator iterator = JsonParser.parseJsonReview(data).iterator();
                    while (iterator.hasNext()) {
                        ModelOfReview modelOfReview = (ModelOfReview) iterator.next();
                        mDataset.add(modelOfReview);
                        mAdapter.notifyItemInserted(mDataset.size() - 1);
                    }

                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (index == 0) {
                    // retrieve trial id
                    movie_trial_id = JsonParser.parseTrailer(response);

                } else {
                    // retrieve review
                    clearDataSet();
                    Iterator iterator = JsonParser.parseJsonReview(response).iterator();
                    while (iterator.hasNext()) {
                        ModelOfReview modelOfReview = (ModelOfReview) iterator.next();
                        mDataset.add(modelOfReview);
                        mAdapter.notifyItemInserted(mDataset.size() - 1);
                    }

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to volley request queue
        Controller.getInstance().addToRequestQueue(strReq);

    }
//******************************************************************************************
    // function for clearing the data set
    private void clearDataSet() {
        if (mDataset != null) {
            mDataset.clear();
            mAdapter.notifyDataSetChanged();
        }
    }
}//End of the DetailsFragment class
