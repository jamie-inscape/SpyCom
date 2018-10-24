/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * websocket.js
 */

window.onload = init;
var socket;
var number = "";
var agentId = "";
var passcode = "";
var player;
var clues = [];

/**
 * onMessage.
 * @param {type} event
 * @returns {undefined}
 */
function onMessage(event) {
    var message = JSON.parse(event.data);
    if (message.action === "postComment") {
        var team = message.team;
        var comment = message.comment;
        if (player.getTeam() === team) {
            $("#commentDiv").append("<div class='comment'>" + comment + "</div>");
            playWooshSound();
        }
    } else if (message.action === "updateClueList") {
        if (player.getTeam() == message.team) {
            var clueNumber = message.clueNumber;
            var clueContent = message.clue;
            clues[clueNumber] = createClue(clueNumber, clueContent);
            updateClues();
        }  
    }
}



/**
 * update clues.
 * @returns {undefined}
 */
function updateClues() {
    //$("#clueDiv").html("");
    var currentClues = [];
    $("div.clue").each(function(){
        var clueNumber = $(this).attr("clueNumber");
        currentClues.push(clueNumber);
    });
    for(i = 0; i < clues.length; i++) {
        if(!(i in currentClues)) {
            $("#clueDiv").append("<div class='clue' clueNumber="+clues[i].clueNumber+">"+(clues[i].clueNumber+1)+" : "+clues[i].clueContent+"</div>");
        }
    }
}

/**
 * shows communication interface.
 * @returns {undefined}
 */
function showCommunicationInterface() {
     $("#commentDiv").show();
     $("#clueDiv").show();
     $("#commentEntry").show();
     $("#codeEntry").show();
     $("textarea").show();
}

/**
 * hide communication interface.
 * @returns {undefined}
 */
function hideCommunicationInterface() {
    $("#commentDiv").hide();
    $("#clueDiv").hide();
    $("#commentEntry").hide();
    $("#codeEntry").hide();
}

/**
 * handle enter key in comment text area.
 */
$("#commentTextArea").keypress(function(event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13'){
        var comment = $(this).val();
        var playerAction = {
            action: "postComment",
            agentId: player.getAgentId(),
            comment: comment,
            team: player.getTeam()
        };
        $("#commentTextArea").val("").blur();
        socket.send(JSON.stringify(playerAction));
    }
});

/**
 * play button click sound.
 * @returns {undefined}
 */
function playSound() {
    var sound = document.getElementById("clickSound");
    sound.play();
}

/**
 * play alert sound.
 * @returns {undefined}
 */
function playAlertSound() {
    var sound = document.getElementById("alertSound");
    sound.play();
}

/**
 * play woosh sound.
 * @returns {undefined}
 */
function playWooshSound() {
    var sound = document.getElementById("wooshSound");
    sound.play();
}

/**
 * initialize.
 * @returns {undefined}
 */
function init() {
    socket = new WebSocket("ws://localhost:8080/SpyCom/actions");
    socket.onmessage = onMessage;
    socket.onclose = function(event) {
        location.reload();
    }
    socket.onerror = function(event) {
        location.reload();
    };
    initializeCanvas();
    setTimeout(function() {
        openDoors();
    }, 1000);
}
    
/**
 * show voluntary code div.
 * @returns {undefined}
 */
function showVoluntaryCodeDiv() {
    $("#voluntaryCodeDiv").show();
}


/**
 * create new player.
 * @param {type} agentId
 * @param {type} password
 * @returns {createPlayer.newplayer}
 */
function createPlayer() {
    var agentId = parseInt(Math.random()*10000, 10);   
    var url = new URL(location.href);
    var team = parseInt(url.searchParams.get("team"));
    var newplayer = {
	agentId: agentId,
	team: team,
        getAgentId: function() {
            return this.agentId;
        },
        setTeam: function(side) {
            this.team = side;
        },
        getTeam: function() {
            return this.team;
        }
    }
    return newplayer;
}

/**
 * create new clue.
 * @param {type} clueNumber
 * @param {type} clueContent
 * @returns {createClue.newClue}
 */
function createClue(clueNumber, clueContent) {
    var newClue = {
        clueNumber:clueNumber,
        clueContent: clueContent
    }
    return newClue;
}

function initializeCanvas() {
    var canvas = document.getElementById("canvas");
    var context2D = canvas.getContext("2d");
    var leftDoor = document.getElementById("leftDoor");
    var rightDoor = document.getElementById("rightDoor");
    context2D.drawImage(leftDoor, 0, 0);
    context2D.drawImage(rightDoor, canvas.width / 2, 0);
}

function openDoors() {
    var canvas = document.getElementById("canvas");
    var context2D = canvas.getContext("2d");
    var leftDoor = document.getElementById("leftDoor");
    var rightDoor = document.getElementById("rightDoor");
    var position = 0;
    var animationInterval = setInterval(function() {
        context2D.clearRect(0, 0, canvas.width, canvas.height);
        context2D.drawImage(leftDoor, -position, 0);
        context2D.drawImage(rightDoor, canvas.width / 2 + position, 0);
        position+=10;
        if (position > canvas.width/2) {
            clearInterval(animationInterval);
            $("#canvas").hide();
            showVoluntaryCodeDiv();
        }
    }, 50);
    
}

$("#summonVoluntaryCodeDiv").click(function(event) {
    hideCommunicationInterface();
    $("#voluntaryCodeDiv").show();
    
});

$("input.codeBox").keyup(function(e) {
    var id = $(this).attr("id");
    var theNum = id.replace( /^\D+/g, '');
    if (theNum != "5") {
        theNum = parseInt(theNum) + 1;
        var newId = "#codeBox"+theNum;
        $(newId).focus();
    }
});

$("#voluntaryCodeDiv button").click(function(event) {
    var id = $(this).attr("id");
    if (player == null)
        player = createPlayer();
    if (id == "enterButton") {
        var code = "";
        $("input.codeBox").each(function() {
            code += $(this).val();
        });
        
        var playerAction = {
            action: "checkCode",
            code: code,
            team: player.getTeam()
        };
        socket.send(JSON.stringify(playerAction));
    } else {
        var playerAction = {
            action: "updateClueList",
            team: player.getTeam()
        };
        socket.send(JSON.stringify(playerAction));
    }
    
    showCommunicationInterface();
    $("#voluntaryCodeDiv").hide();
});