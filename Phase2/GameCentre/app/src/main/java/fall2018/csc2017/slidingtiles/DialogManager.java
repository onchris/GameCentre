package fall2018.csc2017.slidingtiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import java.util.List;

import static fall2018.csc2017.slidingtiles.UtilityManager.makeCustomToastText;
import static fall2018.csc2017.slidingtiles.UtilityManager.newRandomBoard;
import static fall2018.csc2017.slidingtiles.UtilityManager.saveBoardsToAccounts;

public class DialogManager implements PopupMenu.OnMenuItemClickListener{
    private final Activity currentActivity;
    private int dialogResourceId;
    private List<BoardManager> boardList;
    private LoaderAdapter adapter;
    private Account account;
    public DialogManager(Activity currentActivity){
        this.currentActivity = currentActivity;
    }
    public void createDialog(int popupResourceId, View view, PopupMenu.OnMenuItemClickListener onClicker){
        PopupMenu popupMenu = new PopupMenu(currentActivity, view);
        popupMenu.setOnMenuItemClickListener(onClicker);
        popupMenu.inflate(popupResourceId);
        popupMenu.show();
    }
    public void generateBoard(int dialogPosition){
        switch (dialogPosition){
            case 1:
                boardList.add(new BoardManager(newRandomBoard(3,3)));
                adapter.notifyDataSetChanged();
                saveBoardsToAccounts(currentActivity, account, boardList);
                makeCustomToastText("3x3", currentActivity);
                break;
            case 2:
                boardList.add(new BoardManager(newRandomBoard(4,4)));
                adapter.notifyDataSetChanged();
                saveBoardsToAccounts(currentActivity, account, boardList);
                makeCustomToastText("4x4", currentActivity);
                break;
            case 3:
                boardList.add(new BoardManager(newRandomBoard(3,3)));
                adapter.notifyDataSetChanged();
                saveBoardsToAccounts(currentActivity, account, boardList);
                makeCustomToastText("5x5", currentActivity);
                break;
        }
    }
    public void setDialogLayout(int dialogResourceId){
        this.dialogResourceId = dialogResourceId;
    }
    public void setupComponents(List<BoardManager> boardList, LoaderAdapter adapter, Account account){
        this.boardList = boardList;
        this.adapter = adapter;
        this.account = account;
    }
    private boolean checkValidDialogInputs(String row, String column){
        if (row.equals("")||column.equals("")) {
            makeCustomToastText("Fields must not be empty", null);
            return false;
        } else if (Integer.parseInt(row) < 3 || Integer.parseInt(column) < 3){
            makeCustomToastText("Rows/Columns cannot be less than 3", currentActivity);
            return false;
        } else if (Integer.parseInt(row) > 31 || Integer.parseInt(column) > 31){
            makeCustomToastText("Rows/Columns cannot be larger than 31", currentActivity);
            return false;
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int dialogPosition = item.getItemId();
        if(dialogPosition == R.id.item1) {
            generateBoard(1);
            return true;
        }
        else if(dialogPosition == R.id.item2) {
            generateBoard(2);
            return true;
        }
        else if(dialogPosition == R.id.item3){
            generateBoard(3);
            return true;
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
            LayoutInflater inflater = currentActivity.getLayoutInflater();
            builder.setView(inflater.inflate(dialogResourceId, null));
            final Dialog dialog = builder.create();
            dialog.show();

            Button confirmButton = dialog.findViewById(R.id.button_confirm_difficulty);
            Button loadImageButton = dialog.findViewById(R.id.button_loadImage);
            final ImageView imagePreview = dialog.findViewById(R.id.iv_preview);
            final EditText rows = dialog.findViewById(R.id.text_row);
            final EditText columns = dialog.findViewById(R.id.text_column);
            final EditText undos = dialog.findViewById(R.id.text_undos);
            final EditText etUrl = dialog.findViewById(R.id.et_Url);
            final ImageResultReceiver resultReceiver = new ImageResultReceiver(new Handler(), imagePreview);
            resultReceiver.setReceiver(resultReceiver);

            loadImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = etUrl.getText().toString();
                    Intent imageIntent = new Intent(v.getContext(), ImageServiceIntent.class);
                    imageIntent.putExtra("receiver", resultReceiver);
                    imageIntent.putExtra("url", url);
                    imageIntent.putExtra("rows", 0);
                    imageIntent.putExtra("columns", 0);
                    currentActivity.startService(imageIntent);
                    if(resultReceiver.contentReceived())
                        makeCustomToastText("Load Successful!", currentActivity);
                }
            });
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox useImage = dialog.findViewById(R.id.cb_useImage);
                    if(useImage.isChecked()){
                        if(resultReceiver.contentReceived() && checkValidDialogInputs(rows.getText().toString(),columns.getText().toString() )){
                            int rowsInt = Integer.parseInt(rows.getText().toString());
                            int colsInt = Integer.parseInt(columns.getText().toString());
                            BoardManager bm = new BoardManager(newRandomBoard(rowsInt, colsInt));
                            bm.setCustomImageSet(resultReceiver.getBitmapList(
                                    ImageServiceIntent.bitmapSplitter
                                    (resultReceiver.getUnprocessedBitmap(), rowsInt , colsInt)));
                            bm.setNumCanUndo(Integer.parseInt(undos.getText().toString()));
                            bm.setUseImage(true);
                            boardList.add(bm);
                            adapter.notifyDataSetChanged();
                            saveBoardsToAccounts(currentActivity, account, boardList);
                            makeCustomToastText(rowsInt + "x" +colsInt, currentActivity);
                            dialog.dismiss();
                    } else if (resultReceiver.invalidImageLink()) {
                        makeCustomToastText("Invalid image link, make sure to copy image link address directly!", currentActivity);
                    } else {
                        makeCustomToastText("You must wait for your image to finish downloading!", currentActivity);
                    }
                    } else if (checkValidDialogInputs(rows.getText().toString(), columns.getText().toString())) {
                        Board randomBoard = newRandomBoard(Integer.parseInt(rows.getText().toString()),
                                Integer.parseInt(columns.getText().toString()));
                        BoardManager bm = new BoardManager(randomBoard);
                        bm.setNumCanUndo(Integer.parseInt(undos.getText().toString()));
                        boardList.add(bm);
                        adapter.notifyDataSetChanged();
                        saveBoardsToAccounts(currentActivity, account, boardList);
                        makeCustomToastText(randomBoard.getTilesDimension(), currentActivity);
                        dialog.dismiss();
                    }
                }
            });
            return true;
        }
    }
}
