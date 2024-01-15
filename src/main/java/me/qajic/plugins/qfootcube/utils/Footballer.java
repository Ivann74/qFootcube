package me.qajic.plugins.qfootcube.utils;

import java.util.UUID;

public class Footballer {
    private String username;
    private Integer goals;
    private Integer assists;
    private Integer matches;
    private Integer wins;
    private Integer ties;
    private Integer win_streak;
    private Integer best_win_streak;
    private String custom_score_message;
    private String particle;
    private String goal_explosion;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGoals() {
        return this.goals != null ? this.goals : 0;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return this.assists != null ? this.assists : 0;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getMatches() {
        return this.matches != null ? this.matches : 0;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getWins() {
        return this.wins != null ? this.wins : 0;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getTies() {
        return this.ties != null ? this.ties : 0;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }
    public int getBestWinStreak() {
        return this.best_win_streak != null ? this.best_win_streak : 0;
    }

    public void setBestWinStreak(int ws) {
        this.best_win_streak = ws;
    }
}
