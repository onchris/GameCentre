package fall2018.csc2017.slidingtiles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A general scoreboard to view scores
 */
public class GeneralScoreboard extends AppCompatActivity {
    /**
     * the viewpager for the this general scoreboard
     */
    private ViewPager viewPager;
    /**
     * adapter for the viewpager
     */
    private FragmentPagerAdapter fragmentPagerAdapter;
    /**
     * username of the account holder
     */
    private String username;
    /**
     * the extview for the username
     */
    private TextView displayUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_scores);
        viewPager = findViewById(R.id.vp_scores);
        username = getIntent().getStringExtra("username");
        displayUsername = findViewById(R.id.text_generalscore_user);
        displayUsername.setText(
                getString(R.string.gsb_logged_as, username == null ? "Guest" : username));
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();;
    }

    /**
     * A fragment pager adapter for different games
     */
    public class FragmentPagerAdapter extends FragmentStatePagerAdapter{
        private List<Fragment> fragments = new ArrayList<>();

        /**
         * Constructor for this fragment pager adapter
         * @param fm the fragment manager
         */
        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(ScoreFragment.newInstance("Sliding Tiles", username));
            fragments.add(ScoreFragment.newInstance("Obstacle Dodger", username));
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
