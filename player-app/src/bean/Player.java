package bean;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userNum;           // USER_NUM
    private String telephoneNum;   // TELEPHONE_NUM
    private String name;           // NAME
    private String password;       // PASSWORD（ハッシュ保存想定でも型は同じ）
    private String playerName;     // PLAYER_NAME

    public Player() {}

    // 新規登録用（USER_NUMはDB採番）
    public Player(String telephoneNum, String name, String password, String playerName) {
        this.telephoneNum = telephoneNum;
        this.name = name;
        this.password = password;
        this.playerName = playerName;
    }

    // 取得用（全項目）
    public Player(int userNum, String telephoneNum, String name, String password, String playerName) {
        this.userNum = userNum;
        this.telephoneNum = telephoneNum;
        this.name = name;
        this.password = password;
        this.playerName = playerName;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public String getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(String telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
