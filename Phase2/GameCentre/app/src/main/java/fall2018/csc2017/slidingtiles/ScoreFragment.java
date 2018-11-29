package fall2018.csc2017.slidingtiles;

import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreFragment extends Fragment {

    private TextView gameName;
    private String username;
    private ScoreManager scoreManager;
    private List<String> displayList = new ArrayList<>();
    private ListView scoresListDisplay;
    private OnFragmentInteractionListener mListener;
    public ScoreFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScoreFragment.
     */
    public static ScoreFragment newInstance(String game, String account) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putString("game", game);
        args.putString("account", account);
        fragment.setArguments(args);
        return fragment;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scores, container, false);
    }

    public ListView getScoresListDisplay() {
        return scoresListDisplay;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gameName = getView().findViewById(R.id.frag_textview);
        scoresListDisplay = getView().findViewById(R.id.lv_scores);
        String game = getArguments().getString("game", "Scoreboard");
        gameName.setText(game);
        username = getArguments().getString("account", "Guest");
        if(game.equals("Sliding Tiles")) {
            scoreManager = new SlidingTilesScoreManager(username, getContext(), 0);
        } else if (game.equals("Obstacle Dodger")) {
            scoreManager = new ObDodgerScoreManager(username, getContext(), 0);
        }
        displayList = scoreManager.getDisplayGameScoresList();
        ArrayAdapter adapter
                = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                R.layout.activity_scorelist, displayList);
        scoresListDisplay.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Fragment interface for getting instance scoreboard
     */
    public interface OnFragmentInteractionListener {
        void getDisplayScoreboard();
    }
}
