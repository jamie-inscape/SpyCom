/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamietung.websocket;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author jamietung
 */
@ApplicationScoped
@ServerEndpoint("/actions")
public class SpyWebSocketServer {
    @Inject
	private UserSessionHandler sessionHandler;
    
    @OnOpen
        public void open(Session session) {
            sessionHandler.addSession(session);
    }

    @OnClose
        public void close(Session session) {
            sessionHandler.removeSession(session);
    }

    @OnError
        public void onError(Throwable error) {
            Logger.getLogger(SpyWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }
        
    @OnMessage
        public void handleMessage(String message, Session session) {
            try (JsonReader reader = Json.createReader(new StringReader(message))) {
                JsonObject jsonMessage = reader.readObject();
                System.out.println(jsonMessage);
                if ("addUser".equals(jsonMessage.getString("action"))) {
                    
                } else if ("login".equals(jsonMessage.getString("action"))) {
                    String agentId = jsonMessage.getString("agentId");
                    String password = jsonMessage.getString("passcode");
                    checkLogin(agentId, password, session);
                } else if ("processCode".equals(jsonMessage.getString("action"))) {
                    String agentId = jsonMessage.getString("agentId");
                    String code = jsonMessage.getString("code");
                    int team = jsonMessage.getInt("team");
                    sessionHandler.notifyCaretaker(agentId, code, team);
                } else if ("postComment".equals(jsonMessage.getString("action"))) {
                    String comment = jsonMessage.getString("comment");
                    String agentId = jsonMessage.getString("agentId");
                    int team = jsonMessage.getInt("team");
                    sessionHandler.sendComment(team, agentId, comment);
                } else if ("updateClueList".equals(jsonMessage.getString("action"))) {
                    String agentId = jsonMessage.getString("agentId");
                    String clueAgentId = jsonMessage.getString("clueAgentId");
                    String clue = jsonMessage.getString("clue");
                    sessionHandler.processClue(agentId, clueAgentId, clue);
                }
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        
    
        
        /**
         * Check login agentId and password.
         * @param agentId
         * @param password 
         */
        public void checkLogin(String agentId, String password, Session session) {
            int team = SQLLiteDatabase.checkLogin(agentId, password);
            sessionHandler.confirmLogin(session, team);
        }
}
