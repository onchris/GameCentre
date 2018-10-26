package fall2018.csc2017.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private String username;
    private String password;
    private List<BoardManager> boardList = new ArrayList<>();
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

    public List<BoardManager> getBoardList() {
        return boardList;
    }

    public void addToBoardList(BoardManager board) {
        if(!boardList.contains(board))
            boardList.add(board);
    }

    public boolean equals(Account account){
        return account.getUsername().equals(this.getUsername());
    }

    public void resetBoardList(){
        boardList.clear();
    }
}
