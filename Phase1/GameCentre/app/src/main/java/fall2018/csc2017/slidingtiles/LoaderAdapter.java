package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LoaderAdapter extends BaseAdapter {
    private ArrayList<Board> boardList;
    private Context ctx;
    static class ViewHolder {
        TextView text;
        Button button;
    }
    public LoaderAdapter(ArrayList<Board> boardList, Context ctx){
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

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder v = new ViewHolder();
        if(view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_loadlist, viewGroup, false  );
            v.button = (Button) view.findViewById(R.id.button_load);
            v.text = (TextView) view.findViewById(R.id.text_game);
            view.setTag(v);
        }
        else
            v = (ViewHolder) view.getTag();

        v.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoardManager bm = new BoardManager((Board)getItem(i));
                GameSelection.saveToFile(StartingActivity.TEMP_SAVE_FILENAME, bm, ctx);
                Intent tmp = new Intent(view.getContext(), GameActivity.class);
                ctx.startActivity(tmp);
            }
        });
        v.text.setText("Game " + i);
        return view;
    }
}

