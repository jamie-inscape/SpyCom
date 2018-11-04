/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jamietung.websocket;

import com.jamietung.model.Team;
import java.util.Arrays;

/**
 *
 * @author jamietung
 */
public class InitializationSingleton {
    
    private static InitializationSingleton uniqueInstance = null;
    private static boolean initialized = false;
    
    private InitializationSingleton() {
        
    }
    
    
    public static boolean getStatus() {
        return initialized;
    }
    
    public static InitializationSingleton getInitializationSingleton() {
        if (uniqueInstance == null) {
            uniqueInstance = new InitializationSingleton();
        }
        return uniqueInstance;
    }
    
    public static void initialize(UserSessionHandler sessionHandler) {
        initialized = true;
        Team testTeam1 = new Team(555555);
        String[] codes = {"fdadf", "blueb"};
        testTeam1.setCodes(Arrays.asList(codes));
        String[] clues = {"There is no spoon.", "Don't think you can! Know you can!"};
        testTeam1.setClues(Arrays.asList(clues));
        sessionHandler.addTeam(testTeam1);
        
        Team whiteTeam = new Team(143524);
        String[] whiteCodes = {
            "7h703","fpapt","1b60f","231jn","iw40x","z03h7","1o0v6","7rin2",
            "x87d0","467jm","p0c25","50bul","73mou","k15x2","gbhru","zaywz",
            "nmc1k","71nt8","2tx21","1482d","cft1o","81n0v","1n85e","8380u","26mx3"};
        whiteTeam.setCodes(Arrays.asList(whiteCodes));
        String[] whiteClues = {
            "\"The pounding of metal to the west\" was found written on an enemy napkin",
            "The number -79.501874 is thought to have significance",
            "A captured spy has revealed that the secret documents are \"contained within\"",
            "\"Percy\" was intercepted in enemy communications",
            "HQ operatives intercepted this telephone code: 86735667",
            "Wire taps revealed talks of \"taking a flight south\"",
            "\"g9ami 93 4u\" was part of an encrypted message intercepted by HQ",
            "HQ has deduced that the telephone code found earlier spells something",
            "Part of a decrypter was discovered: 0=D; 1=2; 2=E; 3=F; 4=C; 5=Y; 6=G; 7=K; 8=M; 9=O",
            "Informants have revealed that the \"Percy\" was an inventor",
            "The number 44.103348 was found on several enemy computers",
            " -- . . - .. -. --. was intercepted during transmission seconds ago",
            "The number -79.501874, found earlier, is now thought to be part of a coordinate system",
            "The second part of the decrypter was found: a=R; b=L; c=7; d=I; e=Q; f=6; g=N; h=B; i=9; j=4; k=5; l=H; m=T; n=W; o=U; p=1; q=J; r=V; s=S; t=X; u=P; v=A; w=Z; x=8; y=3; z=0;",
            "Fellow agent #472's dying words included something about \"Elektromagnetische Wellen\"",
            "Eastern European operatives continue to overhear mention of \"kov\"",
            "Double-agent #65 has discovered that the secret documents reside on a floppy disc. Find this disc!",
            "US2495429A was found on a captured agent",
            "British surveillance intercepted a conversation mentioning 'warm'",
            "The \"Percy\" found earlier has a last name of \"Spencer\"",
            "The following sequence was discovered written on an enemy matchbook: 49 6e 20 74 68 65 20 4d 69 63 72 6f 77 61 76 65",
            "The matchbox sequence found earlier is now thought to translate to letters"
        };
        whiteTeam.setClues(Arrays.asList(whiteClues));
        sessionHandler.addTeam(whiteTeam);
        
        Team blackTeam = new Team(343295);
        String[] blackCodes = {"nnb24","3wgb8","m0850","imbc3","8d36t","5mf67","wmr0d",
            "5n6a7","3c7o1","e35j0","524pn","0u0a2","e8aqo","yhr73","7c0v4","212ft",
            "c813q","4z554","d5g60","53vu2","u68mj","n15a1","f407p","uvrii","112rx"};
        blackTeam.setCodes(Arrays.asList(blackCodes));
        String[] blackClues = {
            "\"See smoke rise to the north\" was found written on an enemy napkin",
            "The number -79.501936 is thought to have significance",
            "A captured spy has bragged that the secret documents are \"well hidden\" and \"out of sight\"",
            "\"S 1,3\" was intercepted in enemy communications",
            "HQ operatives intercepted this telephone code: 86735667",
            "Wire taps revealed talks of \"taking a flight north\"",
            "\"n2sm 93 un\" was part of an ciphered message intercepted by HQ",
            "HQ has deduced that the telephone code found earlier spells something",
            "Part of a cipher was discovered: 0=D; 1=2; 2=E; 3=F; 4=C; 5=Y; 6=G; 7=K; 8=M; 9=O",
            "Informants have revealed that the \"S\" in \"S 1,3\" (found earlier) means \"south\"",
            "The number 44.103519 was found on several enemy computers",
            ".--. .... .. .-.. --- ... --- .--. .... -.-- was intercepted during transmission seconds ago",
            "The number -79.501936, found earlier, is now thought to be part of a coordinate system",
            "The last two characters of the encryption found earlier are now known to be a person's initials",
            "The second part of the cipher was found: a=R; b=L; c=7; d=I; e=Q; f=6; g=N; h=B; i=9; j=4; k=5; l=H; m=T; n=W; o=U; p=1; q=J; r=V; s=S; t=X; u=P; v=A; w=Z; x=8; y=3; z=0; ",
            "Fellow agent #472's dying words included something about \"kubuni mfumo rahisi\"",
            "Eastern European operatives continue to overhear mention of \"zelená\"",
            "Double-agent #65 has discovered that the secret documents reside on a floppy disc. Find this disc!",
            "The following sequence was discovered written on an enemy matchbook: 42 65 68 69 6E 64 54 69 6C 65",
            "British surveillance intercepted a conversation mentioning '12 hands up'",
            "The \"1,3\" found earlier is now thought to be part of a coordinate system - ie. south 1,3",
            "The matchbox sequence found earlier is hex code that is now thought to translate to letters",
            "The code IN20d25F42 was found on a captured agent"
        };
        
        blackTeam.setClues(Arrays.asList(blackClues));
        sessionHandler.addTeam(blackTeam);
    }
    
}
