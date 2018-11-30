package fall2018.csc2017.slidingtiles;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fall2018.csc2017.slidingtiles.UtilityManager.loadAccountList;
import static fall2018.csc2017.slidingtiles.UtilityManager.makeCustomToastText;

/**
 * The class for general score board which shows the scores for all three games
 */
public class GeneralScoreboard extends AppCompatActivity implements ScoreFragment.OnFragmentInteractionListener {
    /**
     * The viewpager
     */
    private ViewPager viewPager;
    /**
     * The fragment pager adapter
     */
    private FragmentPagerAdapter fragmentPagerAdapter;
    /**
     * The username
     */
    private String username;
    /**
     * The text view of user name
     */
    private TextView displayUsername;
    /**
     * The current fragment
     */
    private Fragment currentFragment;
    /**
     * The list view for current score
     */
    private ListView currentScoreDisplay;
    /**
     * A check whether the user is guest or not
     */
    private boolean isGuest;
    /**
     * A check whether the scoreboard is global or not
     */
    private boolean isGlobalScoreboard;
    /**
     * The current account
     */
    private Account currentAccount;
    /**
     * The list of user's scores
     */
    private List<String> displayUserScoresList, defaultList;
    /**
     * The fragment score manager
     */
    private ScoreManager fragmentScoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_scores);
        username = getIntent().getStringExtra("username");
        currentAccount = username == null ? null : new AccountManager(loadAccountList(this)).getAccountFromUsername(username);
        isGuest = currentAccount == null;
        viewPager = findViewById(R.id.vp_scores);
        displayUsername = findViewById(R.id.text_generalscore_user);
        displayUsername.setText(
                getString(R.string.gsb_logged_as, username == null ? "Guest" : username));
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        isGlobalScoreboard = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
        ;
    }

    /**
     * Performing when background image view is clicked
     *
     * @param view the view for general score board
     */
    public void backgroundImageViewOnClick(View view) {
        if (isGuest) {
            makeCustomToastText(getString(R.string.gsb_no_score), this);
            return;
        }
        getDisplayScoreboard();
        fragmentScoreManager = ((ScoreFragment) currentFragment).getScoreManager();
        displayUserScoresList = fragmentScoreManager.getDisplayUserScoresList();
        defaultList = fragmentScoreManager.getDisplayGameScoresList();
        if (isGlobalScoreboard) {
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_scorelist, displayUserScoresList);
            currentScoreDisplay.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            isGlobalScoreboard = !isGlobalScoreboard;
        } else {
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                    R.layout.activity_scorelist, defaultList);
            currentScoreDisplay.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            isGlobalScoreboard = !isGlobalScoreboard;
        }
    }

    @Override
    public void getDisplayScoreboard() {
        currentFragment = fragmentPagerAdapter.getItem(viewPager.getCurrentItem());
        if (currentFragment instanceof ScoreFragment) {
            currentScoreDisplay = ((ScoreFragment) currentFragment).getScoresListDisplay();
        }
    }

    /**
     * The class for fragment pager adapter
     */
    public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
        /**
         * The list of fragments
         */
        private List<Fragment> fragments = new ArrayList<>();

        /**
         * The fragment pager adapter
         *
         * @param fm the fragment manager
         */
        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(ScoreFragment.newInstance("Sliding Tiles", username));
            fragments.add(ScoreFragment.newInstance("Obstacle Dodger", username));
            fragments.add(ScoreFragment.newInstance("Ultimate TTT", username));
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
