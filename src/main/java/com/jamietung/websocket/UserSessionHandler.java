/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamietung.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.websocket.Session;
import com.jamietung.model.User;
import com.jamietung.model.Team;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.json.spi.JsonProvider;

/**
 *
 * @author jamietung
 */
@ApplicationScoped
public class UserSessionHandler {
    
    private final Set<Session> sessions = new HashSet<>();
    private final Set<User> users = new HashSet<>();
    private final Set<Team> teams = new HashSet<>();
    

    public void addSession(Session session) {
        sessions.add(session);
        for (User player : users) {
                JsonObject addMessage = createAddMessage(player);
                sendToSession(session, addMessage);
        }
    }
    
    
    /**
     * Add a team.
     * @param aTeam 
     */
    public void addTeam(Team aTeam) {
        teams.add(aTeam);
    }

    /**
     * confirm login.
     * @param session
     * @param team 
     */
    public void confirmLogin(Session session, int team) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject loginMessage = provider.createObjectBuilder()
                .add("action", "confirmLogin")
                .add("team", team)
                .build();
        sendToSession(session, loginMessage);
        //sendClues(team);
    }
    
    /**
     * Send Comment
     * @param team
     * @param userId
     * @param comment 
     */
    public void sendComment(int team, String userId, String comment) {
        String combinedComment = userId + ": " + comment;
        JsonProvider provider = JsonProvider.provider();
        JsonObject message = provider.createObjectBuilder()
                .add("action", "postComment")
                .add("team", team)
                .add("comment", combinedComment)
                .build();
        sendToAllConnectedSessions(message);
        
    }
    
    /**
     * remove session.
     * @param session 
     */
    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    /**
     * Create add message 
     * @param user
     * @return 
     */
    private JsonObject createAddMessage(User user) {
           JsonProvider provider = JsonProvider.provider();
           JsonObject addMessage = provider.createObjectBuilder()
                   .add("action", "add")
                   .add("id", user.getId())
                   .add("name", user.getName())
                   .add("status", user.getStatus())
                   .build();
           return addMessage;
    }

    /**
     * tells caretaker that someone has found a clue.
     * @param agentId
     * @param code
     * @param team 
     */
    public void notifyCaretaker(String agentId, String code, int team) {
        String caretakerId = "";
        if (team == 1) {
            //caretakerId = team1CaretakerId;
        } else if (team == 2) {
            //caretakerId = team2CaretakerId;
        }
        String trash = agentId + ":" + code;
        JsonProvider provider = JsonProvider.provider();
        JsonObject notifyCaretakerMessage = provider.createObjectBuilder()
                .add("action", "garbRemoval")
                .add("trash", trash)
                .add("caretakerId", caretakerId)
                .build();
        sendToAllConnectedSessions(notifyCaretakerMessage);
    }
    
    
    
    /**
     * send to all sessions / users.
     * @param message 
     */
     private void sendToAllConnectedSessions(JsonObject message) {
        for(Session session : sessions) {
            sendToSession(session, message);
        }
     }

     
    /**
     * send to specific session / user.
     * @param session
     * @param message 
     */
    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
            
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(UserSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }	
    }

    /**
     * process clue.
     * @param agentId
     * @param clueAgentId
     * @param clue 
     */
    public void processClue(String agentId, String clueAgentId, String clue) {
//        if (agentId.equals(team1CaretakerId) || agentId.equals(team2CaretakerId)) {
//            int team = 0;
//            if (agentId.equals(team1CaretakerId)) team = 1;
//            else if (agentId.equals(team2CaretakerId)) team = 2;
//            SQLLiteDatabase.addClue(clue, team);
//            //sendClues(team);
//        }

    }

    
    
    
    /**
     * Send Clues.
     * @param team 
     */
    private void sendClues(Team team) {
        List<String> clues = team.getCluesToSend();
        JsonProvider provider = JsonProvider.provider();
        String faction = "";
        if (team.getTeamNumber() == 143524) {
            faction = "white";
        } else if (team.getTeamNumber() == 343295) {
            faction = "black";
        }
        int i = 0;
        JsonObject updateFactionMessage = provider.createObjectBuilder()
                .add("action", "updateFaction")
                .add("team", team.getTeamNumber())
                .add("faction", faction)
                .build();
        sendToAllConnectedSessions(updateFactionMessage);
        for (String aClue : clues) {
            JsonObject updateClueListMessage = provider.createObjectBuilder()
                .add("action", "updateClueList")
                .add("team", team.getTeamNumber())
                .add("clueNumber", i++)
                .add("faction", faction)
                .add("clue", aClue)
                .build();
        sendToAllConnectedSessions(updateClueListMessage);
        }
    }

    /**
     * Check code to see if valid or revealed.
     * @param code
     * @param team 
     */
    public void checkCode(String code, int team) {
        Team currentTeam = null;
        for(Team aTeam : teams) {
            if (aTeam.getTeamNumber() == team) {
                currentTeam = aTeam;
                break;
            }
        }
        if (currentTeam != null) {
            boolean isValidCode = currentTeam.inputCode(code);
            if (isValidCode) {
                sendClues(currentTeam);
            }
        }
    }

    /**
     * update clues for team.
     * @param team 
     */
    public void updateClues(int team) {
        for (Team aTeam : teams) {
            if (aTeam.getTeamNumber() == team) {
                sendClues(aTeam);
                break;
            }
        }
    }
    
    
    
}
