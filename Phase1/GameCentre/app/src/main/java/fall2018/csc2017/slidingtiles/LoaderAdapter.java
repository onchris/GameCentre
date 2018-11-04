package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class LoaderAdapter extends BaseAdapter {
    private ArrayList<BoardManager> boardList;
    private Context ctx;
    private long lastTimeClicked;
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
    }
    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Object getItem(int i) {
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
            v.button = (Button) view.findViewById(R.id.button_load);
            v.text = (TextView) view.findViewById(R.id.text_game);
            v.textDifficulty = (TextView) view.findViewById(R.id.text_difficulty);
            v.textUndos = (TextView) view.findViewById(R.id.text_undos);
            view.setTag(v);
        }
        else
            v = (ViewHolder) view.getTag();

        v.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoardManager bm = (BoardManager) getItem(i);
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
                    Log.e("Double click detection system 9000", "yeee");
                }
                lastTimeClicked = currTime;
            }
        });
        return view;

    }
}

