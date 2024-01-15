package me.qajic.plugins.qfootcube.utils;

import java.util.UUID;

public class Footballer {
    UUID uuid;
    int goals;
    int assists;
    int matches;
    int wins;
    int ties;
    int best_win_streak;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getGoals() {
        return this.goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return this.assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getMatches() {
        return this.matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getWins() {
        return this.wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getTies() {
        return this.ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }
    public int getBestWinStreak() {
        return this.best_win_streak;
    }

    public void setBestWinStreak(int ws) {
        this.best_win_streak = ws;
    }
}
