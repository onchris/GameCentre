package fall2018.csc2017.slidingtiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static fall2018.csc2017.slidingtiles.UtilityManager.saveBoardManagerToFile;
import static fall2018.csc2017.slidingtiles.UtilityManager.saveBoardsToAccounts;

public class LoaderAdapter extends BaseAdapter {
    /**
     * The list of boards that this adapter will handle
     */
    private ArrayList<BoardManager> boardList = new ArrayList<>(); //TODO: refactor loaderAdapter to be generic?
    /**
     * The current context the adapter will show in
     */
    private Context ctx;
    /**
     * Records the last time a user clicked on an item
     * which checks for double clicks for item deletion
     */
    private long lastTimeClicked;
    /**
     * AlertDialog to warn user of deletion of items
     */
    private AlertDialog ad;
    /**
     * Current account
     */
    public Account account;
    /**
     * A subclass which holds each individual view components for ease of retrieval
     */
    static class ViewHolder {
        TextView text;
        TextView textDifficulty;
        TextView textUndos;
        Button button;
    }

    /**
     * LoaderAdapter constructor
     * @param boardList the current board list to be building with
     * @param ctx the current context
     */
    public LoaderAdapter(ArrayList<BoardManager> boardList, Context ctx){
        this.boardList = boardList;
        this.ctx = ctx;
        ad = UtilityManager.alertDialogBuilder(null, "You can't undelete a game! Make your choice!", ctx);
    }

    /**
     * Gets the count of the boardList
     * @return the size of the boardList
     */
    @Override
    public int getCount() {
        return boardList.size();
    }

    /**
     * Gets the board at i position in the view
     * @param i the position that the item is in
     * @return the BoardManager which has board and other informations
     */
    @Override
    public BoardManager getItem(int i) {
        return boardList.get(i);
    }

    /**
     * Gets the item id. (Required implementation, not used by anything)
     * @param i the position that the item is in
     * @return the position of the item
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Gets the dimension of the board as a string
     * @param i the position that the item is in
     * @return the dimensions of the board as a string
     */
    private String getDifficulty(int i){
        BoardManager b = boardList.get(i);
        String returnString = b.getBoard().getTilesDimension();
        String[] stringArray = returnString.split(",");
        return stringArray[0] + "x" + stringArray[1];
    }

    /**
     * Gets the item as display it accordingly as a view component
     * @param i the position that the item is in
     * @param view the current uninitialized view
     * @param viewGroup the view that items will be shown in
     * @return the display of each individual items as a view component
     */
    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        ViewHolder v = new ViewHolder();
        if(view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_loadlist, viewGroup, false  );
            v.button = view.findViewById(R.id.button_load);
            v.text = view.findViewById(R.id.text_game);
            v.textDifficulty = view.findViewById(R.id.text_difficulty);
            v.textUndos = view.findViewById(R.id.text_undos);
            view.setTag(v);
        }
        else
            v = (ViewHolder) view.getTag();

        v.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoardManager bm = getItem(i);
                if(bm.isUseImage())
                    GameActivity.IMAGE_SET = bm.getCustomImageSet();
                saveBoardManagerToFile(UtilityManager.TEMP_SAVE_FILENAME, bm, ctx);
                Intent tmp = new Intent(view.getContext(), GameActivity.class);
                tmp.putExtra("account", account);
                tmp.putExtra("boardList", boardList);
                tmp.putExtra("boardIndex", i);
                ctx.startActivity(tmp);
            }
        });
        v.text.setText("Game " + i);
        v.textDifficulty.setText(getDifficulty(i));
        v.textUndos.setText("Undos: " + ((BoardManager)getItem(i)).getNumCanUndo());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currTime = System.currentTimeMillis();
                if (currTime - lastTimeClicked < ViewConfiguration.getDoubleTapTimeout()) {
                    deleteFromBoard(i, viewGroup.getContext());
                }
                lastTimeClicked = currTime;
            }
        });
        return view;
    }

    /**
     * Deletes the item from the list of items as well as it's appearance
     * @param position the position that the item is in
     * @param ctx the current context the dialogs will be shown on
     */
    private void deleteFromBoard(final int position, final Context ctx){
        ad.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boardList.remove(position);
                saveBoardsToAccounts(ctx, account, boardList);
                notifyDataSetChanged();
            }
        });
        ad.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        TextView titleText = new TextView(ctx);
        titleText.setText("Delete Game " + position + "?");
        titleText.setPadding(10,10,10,10);
        titleText.setTextSize(30);
        titleText.setGravity(Gravity.CENTER);
        ad.setCustomTitle(titleText);
        ad.show();
    }
}

