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
    if (message.action === "garbRemoval") {
        if (player.getAgentId() == message.caretakerId) {
            $("#clueDiv").append("<div class='trashError'>"+message.trash+"</div>");
            playAlertSound();
        }
    } else if (message.action === "postComment") {
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
    } else if (message.action === "confirmLogin") {
        var team = message.team;
        if (team > 0) {
            player.setTeam(team);
            openDoors();
            showCommunicationInterface();
            updateClues();
        } else {
            resetLoginDisplay();
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
            $("#clueDiv").append("<div class='clue' clueNumber="+clues[i].clueNumber+">"+clues[i].clueContent+"</div>");
        }
    }
}

/**
 * shows communication interface.
 * @returns {undefined}
 */
function showCommunicationInterface() {
     $("#safeDiv").hide();
     $("#commentDiv").show();
     $("#clueDiv").show();
     $("#codeEntry").show();
     $("#commentEntry").show();
     $("#clueInputDiv").hide();
     $("textarea").show();
}

function showLoginDiv() {
    $("#safeDiv").show();
     $("#commentDiv").hide();
     $("#clueDiv").hide();
     $("#codeEntry").hide();
     $("#commentEntry").hide();
     $("#clueInputDiv").hide();
     $("textarea").hide();
}

/**
 * handle enter key press in code text area.
 */
$("#codeTextArea").keypress(function(event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13'){
        var code = $(this).val();
        var playerAction = {
            action: "processCode",
            agentId: player.getAgentId(),
            team: player.getTeam(),
            code: code
        };
        $("#codeTextArea").val("").blur();
        socket.send(JSON.stringify(playerAction));
    }
});

/**
 * handle enter key in comment text area.
 */
$("#commentTextArea").keypress(function(event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(keycode == '13'){
        var comment = $(this).val();
        var playerAction = {
            action: "postComment",
            agentId: agentId,
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
}




/**
 * reset login display.
 * @returns {undefined}
 */
function resetLoginDisplay() {
    number = "";
    agentId = "";
    passcode = "";
    player = "";
    $("#digitalDisplay").html("Unauthorized Access detected.  Please Enter agentId.");
}

/**
 * KeyPad functionality
 * @type type
 */

$("#safeDiv Button").click(function() {
    var buttonValue = $(this).html();
    if ($.isNumeric(buttonValue)) {
        number = number + buttonValue;
    } else if (buttonValue === "Clear") {
        number = "";
    }
    $("#digitalDisplay").html(number);
    if (buttonValue === "Enter") {
        if (number.length > 0) {
            if (agentId.length === 0) {
                agentId = number;
                number = "";
                $("#digitalDisplay").html("Enter Passcode");
            }  else {
                passcode = number;
                player = createPlayer(agentId, passcode);
                player.attemptLogin();
            }
            
        }
    }
    playSound();
});

/**
 * show clue input div.
 * @returns {undefined}
 */
function showClueEntry() {
    $("#safeDiv").hide();
    $("#commentDiv").hide();
    $("#clueDiv").show();
    $("#codeEntry").hide();
    $("#commentEntry").hide();
    $("#clueInputDiv").show();
    $("#agentIdFieldBox").show();
    $("#cluebox").show();
}

$("#cluesubmit").click(function(){
    var clueAgentId = $("#agentIdFieldBox").val();
    var clue = $("#cluebox").val();
    var playerAction = {
        action: "updateClueList",
        agentId: player.getAgentId(),
        clueAgentId: clueAgentId,
        clue: clue
    };
    $("#agentIdFieldBox").val("").blur();
    $("#cluebox").val("").blur();
    socket.send(JSON.stringify(playerAction));
});


/**
 * create new player.
 * @param {type} agentId
 * @param {type} password
 * @returns {createPlayer.newplayer}
 */
function createPlayer(agentId, password) {
	
    var newplayer = {
	agentId: agentId,
	password: password,
	team: "",
        setAgentId: function(agentId) {
            this.agentId = agentId;
        },
        getAgentId: function() {
            return this.agentId;
        },
        
        setTeam: function(side) {
            this.team = side;
        },
        getTeam: function() {
            return this.team;
        },
        attemptLogin: function () {
            var PlayerAction = {
                action: "login",
                agentId: agentId,
                passcode: password
            };
            socket.send(JSON.stringify(PlayerAction));
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
        }
    }, 50);
    
}