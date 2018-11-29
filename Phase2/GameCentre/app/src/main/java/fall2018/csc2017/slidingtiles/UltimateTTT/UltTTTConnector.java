package fall2018.csc2017.slidingtiles.UltimateTTT;

import android.app.Activity;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import fall2018.csc2017.slidingtiles.R;

public class UltTTTConnector {
    /**
     * The game activity of Ultimate Tic Tac Toe game
     */
    public UltimateTTTGameActivity activity;
    /**
     * The text view of player1's score
     */
    TextView scoreP1;
    /**
     * The text view of player2's score
     */
    TextView scoreP2;
    /**
     * The reset button
     */
    ImageButton breset;
    /**
     * The undo button
     */
    ImageButton bundo;
    /**
     * The backend of ultimate tic tac toe game
     */
    UltimateTTTBackend backend;
    /**
     * The image buttons of the board
     */
    private ImageButton[] ImageButtons;
    /**
     * The tables
     */
    private TableLayout tables[];

    /**
     * Connect the activity
     *
     * @param activity the activity of ultimate tic tac toe game
     */
    UltTTTConnector(UltimateTTTGameActivity activity) {
        this.activity = activity;
        bind();
    }

    /**
     * Bind activity information with views
     */
    private void bind() {
        scoreP1 = activity.findViewById(R.id.textView3);
        scoreP2 = activity.findViewById(R.id.textView4);
        backend = new UltimateTTTBackend(activity);
        breset = activity.findViewById(R.id.imageButton4);
        bundo = activity.findViewById(R.id.imageButton3);
        ImageButtons =
                new ImageButton[]{
                        activity.findViewById(R.id.b00), activity.findViewById(R.id.b01),
                        activity.findViewById(R.id.b02), activity.findViewById(R.id.b03),
                        activity.findViewById(R.id.b04), activity.findViewById(R.id.b05),
                        activity.findViewById(R.id.b06), activity.findViewById(R.id.b07),
                        activity.findViewById(R.id.b08), activity.findViewById(R.id.b09),
                        activity.findViewById(R.id.b10), activity.findViewById(R.id.b11),
                        activity.findViewById(R.id.b12), activity.findViewById(R.id.b13),
                        activity.findViewById(R.id.b14), activity.findViewById(R.id.b15),
                        activity.findViewById(R.id.b16), activity.findViewById(R.id.b17),
                        activity.findViewById(R.id.b18), activity.findViewById(R.id.b19),
                        activity.findViewById(R.id.b20), activity.findViewById(R.id.b21),
                        activity.findViewById(R.id.b22), activity.findViewById(R.id.b23),
                        activity.findViewById(R.id.b24), activity.findViewById(R.id.b25),
                        activity.findViewById(R.id.b26), activity.findViewById(R.id.b27),
                        activity.findViewById(R.id.b28), activity.findViewById(R.id.b29),
                        activity.findViewById(R.id.b30), activity.findViewById(R.id.b31),
                        activity.findViewById(R.id.b32), activity.findViewById(R.id.b33),
                        activity.findViewById(R.id.b34), activity.findViewById(R.id.b35),
                        activity.findViewById(R.id.b36), activity.findViewById(R.id.b37),
                        activity.findViewById(R.id.b38), activity.findViewById(R.id.b39),
                        activity.findViewById(R.id.b40), activity.findViewById(R.id.b41),
                        activity.findViewById(R.id.b42), activity.findViewById(R.id.b43),
                        activity.findViewById(R.id.b44), activity.findViewById(R.id.b45),
                        activity.findViewById(R.id.b46), activity.findViewById(R.id.b47),
                        activity.findViewById(R.id.b48), activity.findViewById(R.id.b49),
                        activity.findViewById(R.id.b50), activity.findViewById(R.id.b51),
                        activity.findViewById(R.id.b52), activity.findViewById(R.id.b53),
                        activity.findViewById(R.id.b54), activity.findViewById(R.id.b55),
                        activity.findViewById(R.id.b56), activity.findViewById(R.id.b57),
                        activity.findViewById(R.id.b58), activity.findViewById(R.id.b59),
                        activity.findViewById(R.id.b60), activity.findViewById(R.id.b61),
                        activity.findViewById(R.id.b62), activity.findViewById(R.id.b63),
                        activity.findViewById(R.id.b64), activity.findViewById(R.id.b65),
                        activity.findViewById(R.id.b66), activity.findViewById(R.id.b67),
                        activity.findViewById(R.id.b68), activity.findViewById(R.id.b69),
                        activity.findViewById(R.id.b70), activity.findViewById(R.id.b71),
                        activity.findViewById(R.id.b72), activity.findViewById(R.id.b73),
                        activity.findViewById(R.id.b74), activity.findViewById(R.id.b75),
                        activity.findViewById(R.id.b76), activity.findViewById(R.id.b77),
                        activity.findViewById(R.id.b78), activity.findViewById(R.id.b79),
                        activity.findViewById(R.id.b80)
                };

        tables =
                new TableLayout[]{
                        activity.findViewById(R.id.table0),
                        activity.findViewById(R.id.table1),
                        activity.findViewById(R.id.table2),
                        activity.findViewById(R.id.table3),
                        activity.findViewById(R.id.table4),
                        activity.findViewById(R.id.table5),
                        activity.findViewById(R.id.table6),
                        activity.findViewById(R.id.table7),
                        activity.findViewById(R.id.table8),
                };
    }

    /**
     * Gets the image buttons
     *
     * @return the image buttons
     */
    ImageButton[] getImageButtons() {
        return ImageButtons;
    }

    /**
     * Gets the tables
     *
     * @return the tables
     */
    TableLayout[] getTables() {
        return tables;
    }

    /**
     * Gets the activity
     *
     * @return the activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Gets the index of the image button
     *
     * @param b the image button
     * @return the index of the image button
     */
    int getIndex(ImageButton b) {
        for (int i = 0; i < ImageButtons.length; i++) {
            if (ImageButtons[i].equals(b)) {
                return i;
            }
        }
        if (b.equals(breset))
            return 100;
        if (b.equals(bundo))
            return 200;
        return -1;
    }

}
