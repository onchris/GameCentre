package fall2018.csc2017.slidingtiles.UltimateTTT;

import android.widget.ImageButton;

class UltTTTCellManager {

    private ImageButton[] ImageButtons;

    UltTTTCellManager(UltTTTConnector connector) {
        ImageButtons = connector.getImageButtons();
    }

    void enableAll() {
        for (android.widget.ImageButton ImageButton : ImageButtons) ImageButton.setEnabled(true);
    }

    void disableAll() {
        for (android.widget.ImageButton ImageButton : ImageButtons) ImageButton.setEnabled(false);
    }

    private void enable(int id) {
        ImageButtons[id].setEnabled(true);
    }

    private void disable(int id) {
        ImageButtons[id].setEnabled(false);
    }

    void disableUsedCells(String[] used_cells) {
        for (String used_cell : used_cells) {
            try {
                disable(Integer.parseInt(used_cell));
            } catch (NumberFormatException e) {

            }
        }
    }

    void disableWinnerBlocks(String disableBlock) {
        for (int i = 0; i < disableBlock.length(); i++)
            disableBlock(disableBlock.charAt(i) - 48);
    }

    void enableBlock(int nextActiveBlock) {
        if (nextActiveBlock != Integer.MAX_VALUE)
            for (int i = nextActiveBlock * 9; i < (nextActiveBlock + 1) * 9; i++)
                enable(i);
        else {
            enableAll();
        }
    }

    private void disableBlock(int id) {
        for (int i = id * 9; i < (id + 1) * 9; i++)
            disable(i);
    }
}
