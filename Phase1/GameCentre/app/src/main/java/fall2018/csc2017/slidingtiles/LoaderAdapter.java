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
    private ArrayList<BoardManager> boardList;
    private Context ctx;
    private long lastTimeClicked;
    private AlertDialog ad;
    public Account account;
    static class ViewHolder {
        TextView text;
        TextView textDifficulty;
        TextView textUndos;
        Button button;
    }
    public LoaderAdapter(ArrayList<BoardManager> boardList, Context ctx){
        this.boardList = boardList;
        this.ctx = ctx;
        ad = UtilityManager.alertDialogBuilder(null, "You can't undelete a game! Make your choice!", ctx);
    }
    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public BoardManager getItem(int i) {
        return boardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private String getDifficulty(int i){
        BoardManager b = boardList.get(i);
        String returnString = b.getBoard().getTilesDimension();
        String[] stringArray = returnString.split(",");
        return stringArray[0] + "x" + stringArray[1];
    }

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

