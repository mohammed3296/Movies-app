package com.mohammed_abdullah_bd1996.moviesappv3.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mohammed_abdullah_bd1996.moviesappv3.R;
import com.mohammed_abdullah_bd1996.moviesappv3.fragments.DetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction
            Bundle arguments = new Bundle();
            arguments.putSerializable(DetailsFragment.ARG_ITEM_ID,
                    getIntent().getSerializableExtra(DetailsFragment.ARG_ITEM_ID));
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }


       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

    }
}
