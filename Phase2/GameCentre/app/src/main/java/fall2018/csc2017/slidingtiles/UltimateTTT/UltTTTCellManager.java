package fall2018.csc2017.slidingtiles.UltimateTTT;

import android.widget.ImageButton;

class UltTTTCellManager {
    /**
     * The image buttons
     */
    private ImageButton[] ImageButtons;

    /**
     * Manage the cells of ultimate tic tac toe game
     *
     * @param connector the connector for ultimate tic tac toe game
     */
    UltTTTCellManager(UltTTTConnector connector) {
        ImageButtons = connector.getImageButtons();
    }

    /**
     * Enable all image buttons
     */
    void enableAll() {
        for (android.widget.ImageButton ImageButton : ImageButtons) ImageButton.setEnabled(true);
    }

    /**
     * Disable all image buttons
     */
    void disableAll() {
        for (android.widget.ImageButton ImageButton : ImageButtons) ImageButton.setEnabled(false);
    }

    /**
     * Enable certain image button
     *
     * @param id the id of image button that need to be enabled
     */
    private void enable(int id) {
        ImageButtons[id].setEnabled(true);
    }

    /**
     * Disable certain image button
     *
     * @param id the id of image button that need to be disabled
     */
    private void disable(int id) {
        ImageButtons[id].setEnabled(false);
    }

    /**
     * Disable used cells
     *
     * @param used_cells the uesd cells that need to be disabled
     */
    void disableUsedCells(String[] used_cells) {
        for (String used_cell : used_cells) {
            try {
                disable(Integer.parseInt(used_cell));
            } catch (NumberFormatException e) {

            }
        }
    }

    /**
     * Disable blocks that have a winner
     *
     * @param disableBlock the block that need to be disabled
     */
    void disableWinnerBlocks(String disableBlock) {
        for (int i = 0; i < disableBlock.length(); i++)
            disableBlock(disableBlock.charAt(i) - 48);
    }

    /**
     * Enable block
     *
     * @param nextActiveBlock the block to be enabled
     */
    void enableBlock(int nextActiveBlock) {
        if (nextActiveBlock != Integer.MAX_VALUE)
            for (int i = nextActiveBlock * 9; i < (nextActiveBlock + 1) * 9; i++)
                enable(i);
        else {
            enableAll();
        }
    }

    /**
     * Disable block
     *
     * @param id the id of the block that need to be disabled
     */
    private void disableBlock(int id) {
        for (int i = id * 9; i < (id + 1) * 9; i++)
            disable(i);
    }
}
