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
