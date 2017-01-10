package com.mohammed_abdullah_bd1996.moviesappv3.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohammed_abdullah_bd1996.moviesappv3.R;
import com.mohammed_abdullah_bd1996.moviesappv3.models.ModelOfReview;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// butterknife for finding view
// Start of ReviewsAdapter class
public class ReviewsAdapter extends
        RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    private List<ModelOfReview> mDataSet; //list of ModelOfReview

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Start of static class ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.txtUrl)
        TextView url;
        @BindView(R.id.txtReviewMsg)
        TextView content; // i  allow users to read reviews of a selected movie

        public TextView getAuthor() {
            return author;
        }

        public TextView getUrl() {
            return url;
        }

        public TextView getContent() {
            return content;
        }

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            ButterKnife.bind(this, v);
        }
    }    //End of static class ViewHolder
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * constructor
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public ReviewsAdapter(List<ModelOfReview> dataSet) {
        mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.reviews_item_forecast, viewGroup, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        if (mDataSet.get(position) != null) {
            viewHolder.getAuthor().setText(mDataSet.get(position).getAuthor());
            // Checking for null feed url
            if (mDataSet.get(position).getUrl() != null) {
                // set text with the url
                viewHolder.getUrl().setText(Html.fromHtml("<a href=\"" + mDataSet.get(position).getUrl() + "\">"
                        + mDataSet.get(position).getUrl() + "</a> "));//for show url
                // Making url clickable
                viewHolder.getUrl().setMovementMethod(LinkMovementMethod.getInstance());
                viewHolder.getUrl().setVisibility(View.VISIBLE);
            } else {
                // url is null, remove from the view
                viewHolder.getUrl().setVisibility(View.GONE);
            }
            viewHolder.getContent().setText(mDataSet.get(position).getContent());
        }
    }

    // Return the size of your dataset invoked by the layout manager
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
} //End of ReviewsAdapter clas