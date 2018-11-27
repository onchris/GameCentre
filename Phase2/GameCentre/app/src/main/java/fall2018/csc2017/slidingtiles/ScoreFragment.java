package fall2018.csc2017.slidingtiles;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private OnFragmentInteractionListener mListener;
    private ListView scoresListDisplay;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scores, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gameName = getView().findViewById(R.id.frag_textview);
        scoresListDisplay = getView().findViewById(R.id.lv_scores);
        String game = getArguments().getString("game", "Scoreboard");
        gameName.setText(game);
        username = getArguments().getString("user", "Guest");

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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
