package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private String username;
    private String password;
    private ArrayList<Board> boardList = new ArrayList<>();
    public int numSolved, highscore;

    public Account(String username, String password){
        this.username = username;
        this.password = password;
        numSolved = 0;
        highscore = 0;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void resetPassword(String password) {
        this.password = password;
    }

    public ArrayList<Board> getBoardList() {
        return boardList;
    }

    public void addToBoardList(Board board) {
        if(!boardList.contains(board))
            boardList.add(board);
    }

    public void setBoardList(ArrayList<Board> boardList){
        this.boardList = boardList;
    }

    public boolean equals(Account account){
        return account.getUsername().equals(this.getUsername());
    }

    public void resetBoardList(){
        boardList.clear();
    }
}
