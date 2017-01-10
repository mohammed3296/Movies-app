package com.mohammed_abdullah_bd1996.moviesappv3.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mohammed_abdullah_bd1996.moviesappv3.Controller;
import com.mohammed_abdullah_bd1996.moviesappv3.FavoriteStore;
import com.mohammed_abdullah_bd1996.moviesappv3.adapters.MyAdapter;
import com.mohammed_abdullah_bd1996.moviesappv3.models.ModelOfFavoriteMovie;
import com.mohammed_abdullah_bd1996.moviesappv3.models.ItemModel;
import com.mohammed_abdullah_bd1996.moviesappv3.parsers.JsonParser;
import com.mohammed_abdullah_bd1996.moviesappv3.urls.URLS;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mohammed_abdullah_bd1996.moviesappv3.R;
//start the class
public class ItemsFragment extends Fragment {
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected LayoutManagerType mCurrentLayoutManagerType;
    // for check if layoutManager is grid or linear
    public static int type;
    protected RecyclerView mRecyclerView;
    protected MyAdapter mAdapter;
    protected List<ItemModel> mDataset;
    protected RecyclerView.LayoutManager mLayoutManager;
    // for swipe to refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;
    // for menu
    private Menu menu;
    private boolean isGridtView;
    //**************************************************************************************************
    public ItemsFragment(){

    }
//**************************************************************************************************
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize dataSet
        mDataset = new ArrayList<>();
        setHasOptionsMenu(true);
        // toggle for change layout manager
        isGridtView = false;
    }
    //**************************************************************************************************
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_items, menu);
        this.menu = menu;
    }
    //**************************************************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_refresh:
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
                initiateRefresh(0);
                return true;
            case R.id.menu_top_rated:
                initiateRefresh(1);
                return true;
            case R.id.menu_favorite:
                showFavoriteItemsOnly(new FavoriteStore(getActivity()).findAll());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //**************************************************************************************************
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        // initialize adapter
        mAdapter = new MyAdapter(getActivity(), mDataset);
        mRecyclerView.setAdapter(mAdapter);



        // Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        mSwipeRefreshLayout.setColorScheme(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4 );

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        // load data
        // We make sure that the SwipeRefreshLayout is displaying it's refreshing indicator
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        // Start our refresh background task
        initiateRefresh(0);
        return view;
    }
//**************************************************************************************************
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
                // my observer
                type = 1;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                // my observer
                type = 0;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                // my observer
                type = 1;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }
    //**************************************************************************************************
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }
    //**************************************************************************************************
    // called immediately after onViewCreate
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh(0);
            }
        });
    }
    //**************************************************************************************************
    private void initiateRefresh(int i) {
        String Url = "";
        switch (i){
            case 0:
                Url = URLS.POPULAR_MOVIES;
                break;
            case 1:
                Url = URLS.TOP_RATED_MOVIES;
                break;
            default:
                Url = URLS.POPULAR_MOVIES;
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
                clearDataSet();
                Iterator iterator = JsonParser.parseJsonFeed(data).iterator();
                while (iterator.hasNext()){
                    ItemModel itemModel = (ItemModel)iterator.next();
                    mDataset.add(itemModel);
                    mAdapter.notifyItemInserted(mDataset.size() - 1);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                clearDataSet();
                Iterator iterator = JsonParser.parseJsonFeed(response).iterator();
                while (iterator.hasNext()){
                    ItemModel itemModel = (ItemModel)iterator.next();
                    mDataset.add(itemModel);
                    mAdapter.notifyItemInserted(mDataset.size()-1);
                }
                // disappear progress
                onRefreshComplete();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Stop the refreshing indicator
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to volley request queue
        Controller.getInstance().addToRequestQueue(strReq);

    }
    //**************************************************************************************************
    private void clearDataSet() {
        if (mDataset != null){
            mDataset.clear();
            mAdapter.notifyDataSetChanged();
        }
    }
//**************************************************************************************************
    /**
     * When the AsyncTask finishes, it calls onRefreshComplete(), which updates the data in the
     * ListAdapter and turns off the progress bar.
     */
    private void onRefreshComplete() {

        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);

    }
    //**************************************************************************************************
    private void showFavoriteItemsOnly(List<ModelOfFavoriteMovie> list){
        List<ItemModel> mList = new ArrayList<>();
        for(ModelOfFavoriteMovie model : list){
            String id = model.getIdValue();
            for (ItemModel itemModel : mDataset){
                if(id.equalsIgnoreCase(itemModel.getId())){
                    mList.add(itemModel);
                }
            }
        }
        clearDataSet();
        for (ItemModel itemModel : mList){
            mDataset.add(itemModel);
            mAdapter.notifyItemInserted(mDataset.size()-1);
        }

    }
}//end of the class
