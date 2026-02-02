package bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Match implements Serializable {
    private static final long serialVersionUID = 1L;

    private int matchId;
    private int tournamentId;
    private int roundNo;
    private int matchIndex; // 0始まり

    private Integer player1Id;
    private Integer player2Id;
    private Integer winnerId;

    private String player1Name;
    private String player2Name;
    private String winnerName;

    private Timestamp decidedAt;

    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }

    public int getTournamentId() { return tournamentId; }
    public void setTournamentId(int tournamentId) { this.tournamentId = tournamentId; }

    public int getRoundNo() { return roundNo; }
    public void setRoundNo(int roundNo) { this.roundNo = roundNo; }

    public int getMatchIndex() { return matchIndex; }
    public void setMatchIndex(int matchIndex) { this.matchIndex = matchIndex; }

    public Integer getPlayer1Id() { return player1Id; }
    public void setPlayer1Id(Integer player1Id) { this.player1Id = player1Id; }

    public Integer getPlayer2Id() { return player2Id; }
    public void setPlayer2Id(Integer player2Id) { this.player2Id = player2Id; }

    public Integer getWinnerId() { return winnerId; }
    public void setWinnerId(Integer winnerId) { this.winnerId = winnerId; }

    public String getPlayer1Name() { return player1Name; }
    public void setPlayer1Name(String player1Name) { this.player1Name = player1Name; }

    public String getPlayer2Name() { return player2Name; }
    public void setPlayer2Name(String player2Name) { this.player2Name = player2Name; }

    public String getWinnerName() { return winnerName; }
    public void setWinnerName(String winnerName) { this.winnerName = winnerName; }

    public Timestamp getDecidedAt() { return decidedAt; }
    public void setDecidedAt(Timestamp decidedAt) { this.decidedAt = decidedAt; }

    // 表示用（1始まり）
    public int getDisplayMatchNo() { return matchIndex + 1; }
}
