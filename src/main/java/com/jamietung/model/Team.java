/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamietung.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jamietung
 */
public class Team {
    
    private List<String> clues;
    private int teamNumber;
    private int numberRevealed;
    private List<String> codes;

    public Team(int teamNumber) {
        this.teamNumber = teamNumber;
        this.numberRevealed = 0;
        this.clues = new ArrayList<>();
        this.codes = new ArrayList<>();
    }
    

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public int getNumberRevealed() {
        return numberRevealed;
    }

    public void setNumberRevealed(int numberRevealed) {
        this.numberRevealed = numberRevealed;
    }

    public List<String> getClues() {
        return clues;
    }

    public void setClues(List<String> clues) {
        for (String clue : clues) {
            this.clues.add(clue);
        }
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        for (String code : codes){ 
            this.codes.add(code);
        }
    }

    /**
     * Return true if code is in this.codes
     * @param code
     * @return 
     */
    public boolean inputCode(String code) {
        this.numberRevealed++;
        return true;
    }
    
    /**
     * Add clue to list of clues.
     * @param clue 
     */
    public void addClue(String clue) {
        this.clues.add(clue);
    }
    
    /**
     * Return a list of clues to be sent;
     * @return 
     */
    public List<String> getCluesToSend() {
        return this.clues.subList(0, this.numberRevealed);
    }
    
}
