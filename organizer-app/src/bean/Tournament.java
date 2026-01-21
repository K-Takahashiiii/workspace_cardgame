package bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Tournament {

    private int tournamentId;
    private String name;
    private int organizerId;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private int maxParticipants;
    private int currentParticipants;
    private String description;

    // 追加フィールド（大会詳細を統合）
    private String venue;                 // 会場
    private String gameTitle;             // 使用ゲーム
    private String tournamentFormat;      // 形式
    private int entryFeeYen;              // 参加費(円)
    private LocalDateTime registrationDeadline; // 登録締め切り
    private String matchFormat;           // 試合形式
    private int timeLimitMinutes;         // 制限時間(分)
    private String drawRule;              // 引き分け時
    private String prizeFirst;            // 優勝賞品
    private String prizeSecond;           // 準優勝賞品
    private String prizeThird;            // 3位賞品

    private int status; // 0=受付前,1=受付中,2=開催中,3=終了
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Tournament() {}

    public Tournament(
        int tournamentId,
        String name,
        int organizerId,
        LocalDate eventDate,
        LocalTime eventTime,
        int maxParticipants,
        int currentParticipants,
        String description,

        String venue,
        String gameTitle,
        String tournamentFormat,
        int entryFeeYen,
        LocalDateTime registrationDeadline,
        String matchFormat,
        int timeLimitMinutes,
        String drawRule,
        String prizeFirst,
        String prizeSecond,
        String prizeThird,

        int status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.tournamentId = tournamentId;
        this.name = name;
        this.organizerId = organizerId;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.description = description;

        this.venue = venue;
        this.gameTitle = gameTitle;
        this.tournamentFormat = tournamentFormat;
        this.entryFeeYen = entryFeeYen;
        this.registrationDeadline = registrationDeadline;
        this.matchFormat = matchFormat;
        this.timeLimitMinutes = timeLimitMinutes;
        this.drawRule = drawRule;
        this.prizeFirst = prizeFirst;
        this.prizeSecond = prizeSecond;
        this.prizeThird = prizeThird;

        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ---- 追加フィールド getters/setters ----

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getTournamentFormat() {
        return tournamentFormat;
    }

    public void setTournamentFormat(String tournamentFormat) {
        this.tournamentFormat = tournamentFormat;
    }

    public int getEntryFeeYen() {
        return entryFeeYen;
    }

    public void setEntryFeeYen(int entryFeeYen) {
        this.entryFeeYen = entryFeeYen;
    }

    public LocalDateTime getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(LocalDateTime registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getMatchFormat() {
        return matchFormat;
    }

    public void setMatchFormat(String matchFormat) {
        this.matchFormat = matchFormat;
    }

    public int getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(int timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public String getDrawRule() {
        return drawRule;
    }

    public void setDrawRule(String drawRule) {
        this.drawRule = drawRule;
    }

    public String getPrizeFirst() {
        return prizeFirst;
    }

    public void setPrizeFirst(String prizeFirst) {
        this.prizeFirst = prizeFirst;
    }

    public String getPrizeSecond() {
        return prizeSecond;
    }

    public void setPrizeSecond(String prizeSecond) {
        this.prizeSecond = prizeSecond;
    }

    public String getPrizeThird() {
        return prizeThird;
    }

    public void setPrizeThird(String prizeThird) {
        this.prizeThird = prizeThird;
    }

    // ---- 既存フィールド ----

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
