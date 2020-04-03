// ==UserScript==
// @name         Agariopvp.eu PLUS
// @namespace    http://tampermonkey.net/
// @version      3
// @description  Snel Mass schieter & Split automatisch, Nu ook vertaling!
// @author       IkIsJolle
// @match        *.agariopvp.eu/play/*
// @run-at       document-start
// @grant        GM_getResourceText
// @resource html https://jollesnaas.github.io/AgarioPVP-Plus/
// ==/UserScript==

//The @resource html is for the custom Index, view source here: https://github.com/jollesnaas/AgarioPVP-Plus

//Mod Knoppen:

window.addEventListener('keydown', keydown);
window.addEventListener('keyup', keyup);

var EjectDown = false;

var speed = 25; //in ms

function keydown(event) {
    if (event.keyCode == 87 && EjectDown === false) { // key W
        EjectDown = true;
        setTimeout(eject, speed);
    }
    if (event.keyCode == 65) { //key A
        split();
        setTimeout(split, speed);
    }
    if (event.keyCode == 68) { //key D
        split();
        setTimeout(split, speed);
        setTimeout(split, speed*2);
        setTimeout(split, speed*3);
    }
    if (event.keyCode == 83) { //key S
        X = window.innerWidth/2;
        Y = window.innerHeight/2;
        $("canvas").trigger($.Event("mousemove", {clientX: X, clientY: Y}));
    }
}

function keyup(event) {
    if (event.keyCode == 87) { // key W
        EjectDown = false;
    }
}

function eject() {
    if (EjectDown) {
        window.onkeydown({keyCode: 87}); // key W
        window.onkeyup({keyCode: 87});
        setTimeout(eject, speed);
    }
}

function split() {
    $("body").trigger($.Event("keydown", { keyCode: 32})); //key space
    $("body").trigger($.Event("keyup", { keyCode: 32})); //jquery is required for split to work
}
