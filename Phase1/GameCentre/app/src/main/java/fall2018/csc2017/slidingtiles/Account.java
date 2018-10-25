package fall2018.csc2017.slidingtiles;

import java.util.ArrayList;
import java.util.List;

public class Account {
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

    public void resetBoardList(){
        boardList.clear();
    }
}
