package com.mohammed_abdullah_bd1996.moviesappv3.activites;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.mohammed_abdullah_bd1996.moviesappv3.R;
import com.mohammed_abdullah_bd1996.moviesappv3.fragments.ItemsFragment;
public class MainActivity extends AppCompatActivity {
    //variable for check that device is tablet or phone
    public static boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this for show bar above the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //I optimize my app experience for tablet.
        //check that device is tablet or phone
        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }
        //recreating activities using onSaveInstanceState
        if (savedInstanceState == null) {
            ItemsFragment firstFragment = new ItemsFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }
    }
}
