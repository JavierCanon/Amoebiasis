$(document).ready(function() {
    // INICIALIZAR VARIABLES GLOBALES
    var tileWidth = 120;
    var tileHeight = tileWidth / 2;
    var FADE_TIME = 150; // ms
    var TYPING_TIMER_LENGTH = 400; // ms
    var COLORS = [
        '#e21400', '#91580f', '#f8a700', '#f78b00',
        '#58dc00', '#287b00', '#a8f07a', '#4ae8c4',
        '#3b88eb', '#3824aa', '#a700ff', '#d300e7'
    ];
    var cont = 0;
    var $window = $(window);
    var $usernameInput = $('.usernameInput');
    var $imageInput = $('.imageInput');
    var $weaponInput = $('.weaponInput');
    var $headInput = $('.headInput');
    var $xPosInput = $('.xPosInput');
    var $yPosInput = $('.yPosInput');
    var $directionInput = $('.directionInput');
    var $actionInput = $('.actionInput');
    var $widthInput = $('.widthInput');
    var $hpInput = $('.hpInput');
    var $attackInput = $('.attackInput');
    var $defenseInput = $('.defenseInput');
    var $speedInput = $('.speedInput');
    var $soundInput = $('.soundInput');
    var $heightInput = $('.heightInput');
    var $classInput = $('.classInput');
    var $messages = $('.messages');
    var $inputMessage = $('.inputMessage');
    var $loginPage = $('.login.page');
    var $chatPage = $('.chat.page');
    var hBar = $('.health-bar'),
        bar = hBar.find('.bar'),
        hit = hBar.find('.hit');

    var username;
    var playersonline = [];
    var connected = false;
    var typing = false;
    var lastTypingTime;
    var $currentInput = $usernameInput.focus();

    var socket = io();
    // SE UTILIZA REQUIRE.JS PARA CARGAR LOS SCRIPTS NECESARIOS
    require([
            'jsiso/canvas/Control',
            'jsiso/canvas/Input',
            'jsiso/img/load',
            'jsiso/json/load',
            'jsiso/tile/Field',
            'jsiso/pathfind/pathfind',
            'jsiso/particles/EffectLoader',
            'jsiso/utils'
        ],
        function(CanvasControl, CanvasInput, imgLoader, jsonLoader, TileField, pathfind) {

            // -- FPS --------------------------------
            window.requestAnimFrame = (function() {
                return window.requestAnimationFrame ||
                    window.webkitRequestAnimationFrame ||
                    window.mozRequestAnimationFrame ||
                    window.oRequestAnimationFrame ||
                    window.msRequestAnimationFrame ||
                    function(callback) {
                        window.setTimeout(callback, 1000 / 60);
                    };
            })();
            // ---------------------------------------


            function launch() {
                // SE CARGAN LOS JSON NECESARIOS
                jsonLoader(['../static/json/map.json', '../static/json/imageFiles.json']).then(function(jsonResponse) {
                    // ESPECIFICAR IMÁGENES
                    var imgs = [];
                    for (var property in jsonResponse[0].tilesets[0].tiles) {
                        if (jsonResponse[0].tilesets[0].tiles.hasOwnProperty(property)) {
                            imgs.push(jsonResponse[0].tilesets[0].tiles[property].image);
                        }
                    }

                    var images = [{
                        graphics: imgs,
                    }, {
                        graphics: jsonResponse[1].playerImages
                    }];
                    // FIN

                    // CARGAR IMÁGENES
                    imgLoader(images).then(function(imgResponse) {
                        // FUNCIÓN PARA ADAPTAR EL JSON A LAS NECESIDADES DE JSISO
                        var finalMap = [],
                            finalHeight = [],
                            objectMap = [];
                        var tempMap = [],
                            tempHeight = [],
                            tempObjects = [];
                        var j = 0;
                        for (var i = 0; i < jsonResponse[0].layers[0].data.length; i++) {
                            for (var z = 1; z < jsonResponse[0].layers.length - 1; z++) {
                                if (jsonResponse[0].layers[z].data[i] !== 0) {
                                    if (tempMap[j] === undefined) {
                                        tempMap.push(jsonResponse[0].layers[z].data[i] - 1);
                                        tempHeight.push(z);
                                    } else {
                                        tempMap[j] = jsonResponse[0].layers[z].data[i] - 1;
                                        tempHeight[j] = z;
                                    }
                                }
                            }
                            if (tempMap[j] === undefined) {
                                tempMap.push(jsonResponse[0].layers[0].data[i] - 1);
                                tempHeight.push(0);
                            }
                            tempObjects.push(jsonResponse[0].layers[jsonResponse[0].layers.length - 1].data[i]);
                            j++;
                            if ((i + 1) % jsonResponse[0].width === 0 && i !== 0) {
                                finalMap.push(tempMap);
                                finalHeight.push(tempHeight);
                                objectMap.push(tempObjects);
                                tempMap = [];
                                tempHeight = [];
                                tempObjects = [];
                                j = 0;
                            }
                        }
                        // FIN

                        // INSTANCIAR SISTEMA DE RENDERIZADO
                        var tileEngine = new main(0, 0, 15, 15, imgResponse[1]);
                        // CONFIGURAR CAPAS Y PROPIEDADES
                        tileEngine.init([{
                            zIndex: 0,
                            title: "Ground Layer", // NOMBRE DE LA CAPA
                            layout: finalMap, // ARRAY DE LA CAPA
                            graphics: imgResponse[0].files, // IMÁGENES
                            graphicsDictionary: imgResponse[0].dictionary,
                            applyInteractions: true, // INTERACCIONAR CON RATÓN (SI/NO)
                            shadowDistance: {
                                color: '0,0,33',
                                distance: 4,
                                darkness: 3
                            },
                            shadow: {
                                offset: tileHeight,
                                verticalColor: '(5, 5, 30, 0.4)',
                                horizontalColor: '(6, 5, 50, 0.5)'
                            },
                            heightMap: { // ALTURA DE LA CAPA
                                map: finalHeight, // ARRAY CON LA ALTURA
                                offset: 0, // POSICIÓN
                                heightTile: imgResponse[0].files["height.png"] // IMÁGEN PARA APILAR
                            },
                            tileHeight: tileHeight, // ALTURA DE CELDA
                            tileWidth: tileWidth // ANCHURA DE CELDA
                        }, {
                            // LO MISMO DE NUEVO
                            zIndex: 1, 
                            title: "Object Layer",
                            layout: objectMap,
                            graphics: imgResponse[0].files,
                            graphicsDictionary: imgResponse[0].dictionary,
                            zeroIsBlank: true,
                            applyInteractions: true,
                            alphaWhenFocusBehind: {
                                objectApplied: imgResponse[1].files["main.png"],
                                apply: true
                            },
                            shadowDistance: {
                                color: false,
                                distance: 4,
                                darkness: 2
                            },
                            heightMap: {
                                map: finalHeight,
                                offset: tileHeight,
                                heightMapOnTop: true
                            },
                            tileHeight: tileHeight,
                            tileWidth: tileWidth
                        }]);
                    });
                });
            }


            function main(x, y, xrange, yrange, playerImages) {
                // SE CREA AL JUGADOR
                var player = {
                    name: cleanInput($usernameInput.val().trim()),
                    class: cleanInput($classInput.val().trim()),
                    image: playerImages.files[cleanInput($imageInput.val().trim())],
                    weapon: playerImages.files[cleanInput($weaponInput.val().trim())],
                    head: playerImages.files[cleanInput($headInput.val().trim())],
                    xPos: parseInt(cleanInput($xPosInput.val().trim())),
                    yPos: parseInt(cleanInput($yPosInput.val().trim())),
                    direction: parseInt(cleanInput($directionInput.val().trim())),
                    action: parseInt(cleanInput($actionInput.val().trim())),
                    width: parseInt(cleanInput($widthInput.val().trim())),
                    height: parseInt(cleanInput($heightInput.val().trim())),
                    hp: parseInt(cleanInput($hpInput.val().trim())),
                    attack: parseInt(cleanInput($attackInput.val().trim())),
                    defense: parseInt(cleanInput($defenseInput.val().trim())),
                    speed: parseInt(cleanInput($speedInput.val().trim())),
                    animation: false
                };

                var hitted = new Audio('static/music/hit.wav');
                var hit_sound = new Audio('static/music/' + cleanInput($soundInput.val().trim()));

                // ENEMIGOS QUE AL FINAL NO SE IMPLEMENTARON
                /*var enemy = [{
                    id: 0,
                    image: playerImages.files["antlion.png"],
                    xPos: 0,
                    yPos: 0,
                    direction: 1,
                    width: 128,
                    height: 128
                }, {
                    id: 1,
                    image: playerImages.files["wyvern.png"],
                    xPos: 10,
                    yPos: 10,
                    direction: 1,
                    width: 128,
                    height: 128
                }, {
                    id: 2,
                    image: playerImages.files["antlion.png"],
                    xPos: 5,
                    yPos: 5,
                    direction: 1,
                    width: 128,
                    height: 128
                }, {
                    id: 3,
                    image: playerImages.files["wyvern.png"],
                    xPos: 3,
                    yPos: 4,
                    direction: 1,
                    width: 128,
                    height: 128
                }];*/

                var mapLayers = [];
                var tile_coordinates = {};
                var startY = y;
                var startX = x;
                var rangeX = xrange;
                var rangeY = yrange;
                var calculatePaths = 0;
                var canAttack = true;
                var canMove = true;
                var canRotate = true;

                // INSTANCIAR CANVAS
                var context = CanvasControl.create("canvas_id", window.innerWidth, window.innerHeight, {
                    background: "#000022",
                    display: "block",
                    marginLeft: "auto",
                    marginRight: "auto",
                });


                // INSTANCIAR INPUT
                var input = new CanvasInput(document, CanvasControl());
                // DEFINIR INPUT
                input.keyboard(function(key, pressed) {
                    if (pressed) {
                        switch (key) {
                            case 38: //Arriba
                                if (canMove && canAttack) {
                                    canMove = false;
                                    var ms = 1000 - (player.speed * 100);
                                    setTimeout(function() {
                                        canMove = true;
                                    }, ms);
                                    player.direction = 3;
                                    var movimiento = true;
                                    playersonline.map(function(e) {
                                        if (player.xPos == e.xPos && player.yPos - 1 == e.yPos) {
                                            socket.emit('rotation', { player: player });
                                            movimiento = false;
                                        }
                                    });
                                    if (Number(mapLayers[1].getTile([player.xPos], [player.yPos - 1])) === 0 && movimiento) {
                                        player.yPos--;
                                        socket.emit('move', { player: player, playersonline: playersonline });
                                        mapLayers[1].applyFocus(player.xPos, player.yPos);
                                        if (startX > 0 && player.yPos <= mapLayers[0].getLayout().length - 1 - rangeY / 2) {
                                            mapLayers.map(function(layer) {
                                                layer.move("down");
                                            });
                                            startX--;
                                        }
                                    }
                                    if (player.animation) {
                                        clearInterval(player.animation);
                                        player.animation = undefined;
                                    }
                                    requestAnimFrame(draw);
                                }
                                break;
                            case 39: //Derecha
                                if (canMove && canAttack) {
                                    canMove = false;
                                    var ms = 1000 - (player.speed * 100);
                                    setTimeout(function() {
                                        canMove = true;
                                    }, ms);
                                    player.direction = 5;
                                    var movimiento = true;
                                    playersonline.map(function(e) {
                                        if (player.xPos + 1 == e.xPos && player.yPos == e.yPos) {
                                            socket.emit('rotation', { player: player });
                                            movimiento = false;
                                        }
                                    });
                                    if (Number(mapLayers[1].getTile([player.xPos + 1], [player.yPos])) === 0 && movimiento) {
                                        player.xPos++;
                                        socket.emit('move', { player: player, playersonline: playersonline });
                                        mapLayers[1].applyFocus(player.xPos, player.yPos);
                                        if (startY + rangeY < mapLayers[0].getLayout().length && player.xPos >= 0 + 1 + rangeX / 2) {
                                            mapLayers.map(function(layer) {
                                                layer.move("left");
                                            });
                                            startY++;
                                        }
                                    }
                                    if (player.animation) {
                                        clearInterval(player.animation);
                                        player.animation = undefined;
                                    }
                                    requestAnimFrame(draw);
                                }
                                break;
                            case 40: //Abajo
                                if (canMove && canAttack) {
                                    canMove = false;
                                    var ms = 1000 - (player.speed * 100);
                                    setTimeout(function() {
                                        canMove = true;
                                    }, ms);
                                    player.direction = 7;
                                    var movimiento = true;
                                    playersonline.map(function(e) {
                                        if (player.xPos == e.xPos && player.yPos + 1 == e.yPos) {
                                            socket.emit('rotation', { player: player });
                                            movimiento = false;
                                        }
                                    });
                                    if (Number(mapLayers[1].getTile([player.xPos], [player.yPos + 1])) === 0 && movimiento) {
                                        player.yPos++;
                                        socket.emit('move', { player: player, playersonline: playersonline });
                                        mapLayers[1].applyFocus(player.xPos, player.yPos);
                                        if (startX + rangeX < mapLayers[0].getLayout().length && player.yPos >= 0 + 1 + rangeY / 2) {
                                            mapLayers.map(function(layer) {
                                                layer.move("right");
                                            });
                                            startX++;
                                        }
                                    }
                                    if (player.animation) {
                                        clearInterval(player.animation);
                                        player.animation = undefined;
                                    }
                                    requestAnimFrame(draw);
                                }
                                break;
                            case 37: //Izquierda
                                if (canMove && canAttack) {
                                    canMove = false;
                                    var ms = 1000 - (player.speed * 100);
                                    setTimeout(function() {
                                        canMove = true;
                                    }, ms);
                                    player.direction = 1;
                                    var movimiento = true;
                                    playersonline.map(function(e) {
                                        if (player.xPos - 1 == e.xPos && player.yPos == e.yPos) {
                                            socket.emit('rotation', { player: player });
                                            movimiento = false;
                                        }
                                    });
                                    if (Number(mapLayers[1].getTile([player.xPos - 1], [player.yPos])) === 0 && movimiento) {
                                        player.xPos--;
                                        socket.emit('move', { player: player, playersonline: playersonline });
                                        mapLayers[1].applyFocus(player.xPos, player.yPos);
                                        if (startY > 0 && player.xPos <= mapLayers[0].getLayout().length - 1 - rangeX / 2) {
                                            mapLayers.map(function(layer) {
                                                layer.move("up");
                                            });
                                            startY--;
                                        }
                                    }
                                    if (player.animation) {
                                        clearInterval(player.animation);
                                        player.animation = undefined;
                                    }
                                    requestAnimFrame(draw);
                                }
                                break;
                            case 69: //E
                                if (canAttack && canRotate) {
                                    canRotate = false;
                                    var ms = 1000 - (player.speed * 100);
                                    setTimeout(function() {
                                        canRotate = true;
                                    }, ms);
                                    player.direction += 2;
                                    if (player.direction > 7) {
                                        player.direction = 1;
                                    }
                                    socket.emit('rotation', { player: player });
                                }
                                break;
                            case 81: //Q
                                if (canAttack && canRotate) {
                                    canRotate = false;
                                    var ms = 1000 - (player.speed * 100);
                                    setTimeout(function() {
                                        canRotate = true;
                                    }, ms);
                                    player.direction -= 2;
                                    if (player.direction < 1) {
                                        player.direction = 7;
                                    }
                                    socket.emit('rotation', { player: player });
                                }
                                break;
                            case 87: //W
                                if (canAttack && canRotate) {
                                    canRotate = false;
                                    var ms = 1000 - (player.speed * 100);
                                    setTimeout(function() {
                                        canRotate = true;
                                    }, ms);
                                    if (player.direction == 1) {
                                        player.direction = 5;
                                    } else if (player.direction == 3) {
                                        player.direction = 7;
                                    } else if (player.direction == 5) {
                                        player.direction = 1;
                                    } else { //case 7
                                        player.direction = 3;
                                    }
                                    socket.emit('rotation', { player: player });
                                }
                                break;
                            case 32: //Espacio
                                if (canAttack) {
                                    canAttack = false;
                                    //var dps = 2000 - (player.speed * 100);
                                    var dps = 800;
                                    $('#autoattack').toggleClass('disabled');
                                    setTimeout(function() {
                                        canAttack = true;
                                        $('#autoattack').toggleClass('disabled');
                                    }, dps);
                                    if (player.class == 'Guerrero') {
                                        player.action = 12;
                                    } else if (player.class == 'Mago') {
                                        player.action = 24;
                                    } else { //Arquero
                                        player.action = 28;
                                    }

                                    socket.emit('attacking', { attacker: player });
                                    play(hit_sound);
                                    playersonline.map(function(e) {
                                        var message;
                                        if (e.name != player.name) {
                                            switch (player.class) {
                                                case 'Guerrero':
                                                    switch (player.direction) {
                                                        case 3:
                                                            if (player.xPos === e.xPos && (player.yPos - 1) === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                        case 5:
                                                            if ((player.xPos + 1) === e.xPos && player.yPos === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                        case 7:
                                                            if (player.xPos === e.xPos && (player.yPos + 1) === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                        case 1:
                                                            if ((player.xPos - 1) === e.xPos && player.yPos === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                    }
                                                    break;
                                                case 'Mago':
                                                    switch (player.direction) {
                                                        case 3:
                                                            if (player.xPos === e.xPos && (player.yPos - 1) === e.yPos || player.xPos === e.xPos && (player.yPos - 2) === e.yPos || player.xPos === e.xPos && (player.yPos - 3) === e.yPos || (player.xPos + 1) === e.xPos && (player.yPos - 1) === e.yPos || (player.xPos + 1) === e.xPos && (player.yPos - 2) === e.yPos || (player.xPos + 1) === e.xPos && (player.yPos - 3) === e.yPos || (player.xPos - 1) === e.xPos && (player.yPos - 1) === e.yPos || (player.xPos - 1) === e.xPos && (player.yPos - 2) === e.yPos || (player.xPos - 1) === e.xPos && (player.yPos - 3) === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                        case 5:
                                                            if ((player.xPos + 1) === e.xPos && player.yPos === e.yPos || (player.xPos + 2) === e.xPos && player.yPos === e.yPos || (player.xPos + 3) === e.xPos && player.yPos === e.yPos || (player.xPos + 1) === e.xPos && (player.yPos + 1) === e.yPos || (player.xPos + 2) === e.xPos && (player.yPos + 1) === e.yPos || (player.xPos + 3) === e.xPos && (player.yPos + 1) === e.yPos || (player.xPos + 1) === e.xPos && (player.yPos - 1) === e.yPos || (player.xPos + 2) === e.xPos && (player.yPos - 1) === e.yPos || (player.xPos + 3) === e.xPos && (player.yPos - 1) === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                        case 7:
                                                            if (player.xPos === e.xPos && (player.yPos + 1) === e.yPos || player.xPos === e.xPos && (player.yPos + 2) === e.yPos || player.xPos === e.xPos && (player.yPos + 3) === e.yPos || (player.xPos + 1) === e.xPos && (player.yPos + 1) === e.yPos || (player.xPos + 1) === e.xPos && (player.yPos + 2) === e.yPos || (player.xPos + 1) === e.xPos && (player.yPos + 3) === e.yPos || (player.xPos - 1) === e.xPos && (player.yPos + 1) === e.yPos || (player.xPos - 1) === e.xPos && (player.yPos + 2) === e.yPos || (player.xPos - 1) === e.xPos && (player.yPos + 3) === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                        case 1:
                                                            if ((player.xPos - 1) === e.xPos && player.yPos === e.yPos || (player.xPos - 2) === e.xPos && player.yPos === e.yPos || (player.xPos - 3) === e.xPos && player.yPos === e.yPos || (player.xPos - 1) === e.xPos && (player.yPos + 1) === e.yPos || (player.xPos - 2) === e.xPos && (player.yPos + 1) === e.yPos || (player.xPos - 3) === e.xPos && (player.yPos + 1) === e.yPos || (player.xPos - 1) === e.xPos && (player.yPos - 1) === e.yPos || (player.xPos - 2) === e.xPos && (player.yPos - 1) === e.yPos || (player.xPos - 3) === e.xPos && (player.yPos - 1) === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                    }
                                                    break;
                                                case 'Arquero':
                                                    switch (player.direction) {
                                                        case 3:
                                                            if (player.xPos === e.xPos && (player.yPos - 1) === e.yPos || player.xPos === e.xPos && (player.yPos - 2) === e.yPos || player.xPos === e.xPos && (player.yPos - 3) === e.yPos || player.xPos === e.xPos && (player.yPos - 4) === e.yPos || player.xPos === e.xPos && (player.yPos - 5) === e.yPos || player.xPos === e.xPos && (player.yPos - 6) === e.yPos || player.xPos === e.xPos && (player.yPos - 7) === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                        case 5:
                                                            if ((player.xPos + 1) === e.xPos && player.yPos === e.yPos || (player.xPos + 2) === e.xPos && player.yPos === e.yPos || (player.xPos + 3) === e.xPos && player.yPos === e.yPos || (player.xPos + 4) === e.xPos && player.yPos === e.yPos || (player.xPos + 5) === e.xPos && player.yPos === e.yPos || (player.xPos + 6) === e.xPos && player.yPos === e.yPos || (player.xPos + 7) === e.xPos && player.yPos === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                        case 7:
                                                            if (player.xPos === e.xPos && (player.yPos + 1) === e.yPos || player.xPos === e.xPos && (player.yPos + 2) === e.yPos || player.xPos === e.xPos && (player.yPos + 3) === e.yPos || player.xPos === e.xPos && (player.yPos + 4) === e.yPos || player.xPos === e.xPos && (player.yPos + 5) === e.yPos || player.xPos === e.xPos && (player.yPos + 6) === e.yPos || player.xPos === e.xPos && (player.yPos + 7) === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                        case 1:
                                                            if ((player.xPos - 1) === e.xPos && player.yPos === e.yPos || (player.xPos - 2) === e.xPos && player.yPos === e.yPos || (player.xPos - 3) === e.xPos && player.yPos === e.yPos || (player.xPos - 4) === e.xPos && player.yPos === e.yPos || (player.xPos - 5) === e.xPos && player.yPos === e.yPos || (player.xPos - 6) === e.xPos && player.yPos === e.yPos || (player.xPos - 7) === e.xPos && player.yPos === e.yPos) {
                                                                if (e.xPos > 14 || e.yPos > 14) {
                                                                    e.hp = e.hp - (player.attack * ((10 / (e.defense + 10)) + 1));
                                                                    message = "Has atacado a " + e.name;
                                                                    log(message, {});
                                                                    socket.emit('hit', { enemy: e, attacker: player.name });
                                                                }
                                                            }
                                                            break;
                                                    }
                                                    break;
                                            }
                                        }

                                    });
                                }
                                break;
                        }
                    }
                });

                function drawMap() {
                    for (var i = startY, n = startY + rangeY; i < n; i++) {
                        for (var j = startX, h = startX + rangeX; j < h; j++) {
                            mapLayers.map(function(layer) {
                                //layer.setLight(player.xPos, player.yPos);
                                if (i === player.xPos && j === player.yPos && layer.getTitle() === "Object Layer") {
                                    layer.draw(i, j, player.image, player.width, player.action, player.height, player.direction);
                                    layer.draw(i, j, player.head, player.width, player.action, player.height, player.direction);
                                    layer.draw(i, j, player.weapon, player.width, player.action, player.height, player.direction);
                                    player.action++;
                                    if (player.action == 4 || player.action == 15 || player.action == 27 || player.action == 31) {
                                        player.action = 0;
                                    }
                                } else {
                                    layer.draw(i, j);
                                }
                                // enemy.map(function(e) {
                                //     if (i === e.xPos && j === e.yPos && layer.getTitle() === "Object Layer") {
                                //         layer.draw(i, j, e.image, e.width, e.direction);
                                //     }
                                // });
                                playersonline.map(function(e) {
                                    if (player.name != e.name) {
                                        if (i === e.xPos && j === e.yPos && layer.getTitle() === "Object Layer") {
                                            layer.draw(i, j, e.image, e.width, e.action, e.height, e.direction);
                                            layer.draw(i, j, e.head, e.width, e.action, e.height, e.direction);
                                            layer.draw(i, j, e.weapon, e.width, e.action, e.height, e.direction);
                                            e.action++;
                                            if (e.action == 4 || e.action == 15 || e.action == 27 || e.action == 31) {
                                                e.action = 0;
                                            }
                                        }
                                    }
                                });
                            });
                        }
                    }
                }

                function draw() {
                    context.clearRect(0, 0, CanvasControl().width, CanvasControl().height);
                    /*calculatePaths++;
                    if (calculatePaths === 3) {
                        enemy.map(function(e) {
                            //con el array de pj comprobar la pos del mas cercano al enemiho
                            pathfind(e.id, [e.xPos, e.yPos], [player.xPos, player.yPos], mapLayers[1].getLayout(), true, true).then(function(data) {
                                if (data.length > 0 && data[1] !== undefined) {
                                    e.xPos = data[1].x;
                                    e.yPos = data[1].y;
                                }
                            });
                        });
                        calculatePaths = 0;

                    }*/
                    drawMap();
                    player.animation = setInterval(drawMap, 200);
                    //rain.Draw(CanvasControl().width / 4, 0);
                    //requestAnimFrame(draw);
                }

                window.addEventListener('keydown', function(e) {
                    if (e.keyCode == 13) {
                        $('#gamechat').toggleClass('no-active');
                    }
                });

                function addParticipantsMessage(data) {
                    var message = '';
                    if (data.numUsers === 1) {
                        message += "Estás S-O-L-O...";
                    } else {
                        message += "Hay " + data.numUsers + " usuarios conectados";
                    }
                    log(message);
                }

                // Sets the client's username
                function setUsername() {
                    username = cleanInput($usernameInput.val().trim());

                    // If the username is valid
                    if (username) {
                        //$loginPage.fadeOut();
                        $chatPage.show();
                        //$loginPage.off('click');
                        $currentInput = $inputMessage.focus();

                        // Tell the server who you are
                        socket.emit('add user', {
                            name: username,
                            class: cleanInput($classInput.val().trim()),
                            image: cleanInput($imageInput.val().trim()),
                            weapon: cleanInput($weaponInput.val().trim()),
                            head: cleanInput($headInput.val().trim()),
                            xPos: parseInt(cleanInput($xPosInput.val().trim())),
                            yPos: parseInt(cleanInput($yPosInput.val().trim())),
                            direction: parseInt(cleanInput($directionInput.val().trim())),
                            action: parseInt(cleanInput($actionInput.val().trim())),
                            width: parseInt(cleanInput($widthInput.val().trim())),
                            height: parseInt(cleanInput($heightInput.val().trim())),
                            hp: parseInt(cleanInput($hpInput.val().trim())),
                            attack: parseInt(cleanInput($attackInput.val().trim())),
                            defense: parseInt(cleanInput($defenseInput.val().trim())),
                            speed: parseInt(cleanInput($speedInput.val().trim())),
                            kills: 0
                        });

                    }
                }
                setUsername();

                // Sends a chat message
                function sendMessage() {
                    var message = $inputMessage.val();
                    // Prevent markup from being injected into the message
                    message = cleanInput(message);
                    // if there is a non-empty message and a socket connection
                    if (message && connected) {
                        $inputMessage.val('');
                        addChatMessage({
                            username: username,
                            message: message
                        });
                        // tell server to execute 'new message' and send along one parameter
                        socket.emit('new message', message);
                    }
                }

                // Log a message
                function log(message, options) {
                    var $el = $('<li>').addClass('log').text(message);
                    addMessageElement($el, options);
                }

                // Adds the visual chat message to the message list
                function addChatMessage(data, options) {
                    // Don't fade the message in if there is an 'X was typing'
                    var $typingMessages = getTypingMessages(data);
                    options = options || {};
                    if ($typingMessages.length !== 0) {
                        options.fade = false;
                        $typingMessages.remove();
                    }

                    var $usernameDiv = $('<span class="username"/>')
                        .text(data.username)
                        .css('color', getUsernameColor(data.username));
                    var $messageBodyDiv = $('<span class="messageBody">')
                        .text(data.message);

                    var typingClass = data.typing ? 'typing' : '';
                    var $messageDiv = $('<li class="message"/>')
                        .data('username', data.username)
                        .addClass(typingClass)
                        .append($usernameDiv, $messageBodyDiv);

                    addMessageElement($messageDiv, options);
                }

                // Adds the visual chat typing message
                function addChatTyping(data) {
                    data.typing = true;
                    data.message = 'is typing';
                    addChatMessage(data);
                }

                // Removes the visual chat typing message
                function removeChatTyping(data) {
                    getTypingMessages(data).fadeOut(function() {
                        $(this).remove();
                    });
                }

                // Adds a message element to the messages and scrolls to the bottom
                // el - The element to add as a message
                // options.fade - If the element should fade-in (default = true)
                // options.prepend - If the element should prepend
                //   all other messages (default = false)
                function addMessageElement(el, options) {
                    var $el = $(el);

                    // Setup default options
                    if (!options) {
                        options = {};
                    }
                    if (typeof options.fade === 'undefined') {
                        options.fade = true;
                    }
                    if (typeof options.prepend === 'undefined') {
                        options.prepend = false;
                    }

                    // Apply options
                    if (options.fade) {
                        $el.hide().fadeIn(FADE_TIME);
                    }
                    if (options.prepend) {
                        $messages.prepend($el);
                    } else {
                        $messages.append($el);
                    }
                    $messages[0].scrollTop = $messages[0].scrollHeight;
                }

                // Prevents input from having injected markup
                function cleanInput(input) {
                    return $('<div/>').text(input).text();
                }

                // Updates the typing event
                function updateTyping() {
                    if (connected) {
                        if (!typing) {
                            typing = true;
                            socket.emit('typing');
                        }
                        lastTypingTime = (new Date()).getTime();

                        setTimeout(function() {
                            var typingTimer = (new Date()).getTime();
                            var timeDiff = typingTimer - lastTypingTime;
                            if (timeDiff >= TYPING_TIMER_LENGTH && typing) {
                                socket.emit('stop typing');
                                typing = false;
                            }
                        }, TYPING_TIMER_LENGTH);
                    }
                }

                // Gets the 'X is typing' messages of a user
                function getTypingMessages(data) {
                    return $('.typing.message').filter(function(i) {
                        return $(this).data('username') === data.username;
                    });
                }

                // Gets the color of a username through our hash function
                function getUsernameColor(username) {
                    // Compute hash code
                    var hash = 7;
                    for (var i = 0; i < username.length; i++) {
                        hash = username.charCodeAt(i) + (hash << 5) - hash;
                    }
                    // Calculate color
                    var index = Math.abs(hash % COLORS.length);
                    return COLORS[index];
                }

                // Keyboard events

                $window.keydown(function(event) {
                    // Auto-focus the current input when a key is typed
                    if (!(event.ctrlKey || event.metaKey || event.altKey)) {
                        $currentInput.focus();
                    }
                    // When the client hits ENTER on their keyboard
                    if (event.which === 13) {
                        if (username) {
                            sendMessage();
                            socket.emit('stop typing');
                            typing = false;
                        }
                    }
                });

                $inputMessage.on('input', function() {
                    updateTyping();
                });

                // Click events

                // Focus input when clicking anywhere on login page
                $loginPage.click(function() {
                    $currentInput.focus();
                });

                // Focus input when clicking on the message input's border
                $inputMessage.click(function() {
                    $inputMessage.focus();
                });

                // Socket events

                // Whenever the server emits 'login', log the login message
                socket.on('login', function(data) {
                    connected = true;
                    // Display the welcome message
                    var message = "Bienvenido a Warbo";
                    log(message, {
                        prepend: true
                    });
                    addParticipantsMessage(data);
                });

                // Whenever the server emits 'new message', update the chat body
                socket.on('new message', function(data) {
                    addChatMessage(data);
                });

                // Whenever the server emits 'user joined', log it in the chat body
                socket.on('user joined', function(data) {
                    log(data.username + ' joined');
                    addParticipantsMessage(data);
                    //DIBJAMOS EL NUEVO PJ
                    for (var i = data.playersonline.length - 1; i >= 0; i--) {
                        data.playersonline[i].image = playerImages.files[data.playersonline[i].image];
                        data.playersonline[i].weapon = playerImages.files[data.playersonline[i].weapon];
                        data.playersonline[i].head = playerImages.files[data.playersonline[i].head];
                    }
                    playersonline = data.playersonline;

                    console.log("El array en el servidor al añadir a alguien es asi: " + JSON.stringify(playersonline));
                    if (player.animation) {
                        clearInterval(player.animation);
                        player.animation = undefined;
                    }
                    draw();
                });

                // Whenever the server emits 'user left', log it in the chat body
                socket.on('user left', function(data) {
                    playersonline = data.playersonline;
                    if (player.animation) {
                        clearInterval(player.animation);
                        player.animation = undefined;
                    }
                    draw();
                    log(data.username + ' left');
                    addParticipantsMessage(data);
                    removeChatTyping(data);
                });

                // Whenever the server emits 'typing', show the typing message
                socket.on('typing', function(data) {
                    addChatTyping(data);
                });

                // Cuando se mueve alguien...
                socket.on('someone moved', function(data) {
                    for (var i = data.playersonline.length - 1; i >= 0; i--) {
                        data.playersonline[i].image = playerImages.files[data.playersonline[i].image];
                        data.playersonline[i].weapon = playerImages.files[data.playersonline[i].weapon];
                        data.playersonline[i].head = playerImages.files[data.playersonline[i].head];
                    }
                    playersonline = data.playersonline;
                    if (player.animation) {
                        clearInterval(player.animation);
                        player.animation = undefined;
                    }
                    draw();
                });

                // Cuando alguien cambia de dirección
                socket.on('someone rotated', function(data) {
                    for (var i = playersonline.length - 1; i >= 0; i--) {
                        playersonline[i].image = playerImages.files[data.playersonline[i].image];
                        playersonline[i].weapon = playerImages.files[data.playersonline[i].weapon];
                        playersonline[i].head = playerImages.files[data.playersonline[i].head];
                        if (playersonline[i].name == data.rotated.name) {
                            playersonline[i].direction = data.rotated.direction;
                        }
                    }
                    if (player.animation) {
                        clearInterval(player.animation);
                        player.animation = undefined;
                    }
                    draw();
                });

                // Cuando alguien ataca
                socket.on('someone attacked', function(data) {
                    for (var i = playersonline.length - 1; i >= 0; i--) {
                        playersonline[i].image = playerImages.files[data.playersonline[i].image];
                        playersonline[i].weapon = playerImages.files[data.playersonline[i].weapon];
                        playersonline[i].head = playerImages.files[data.playersonline[i].head];
                        if (playersonline[i].name == data.attacker.name) {
                            playersonline[i].action = data.attacker.action;
                        }
                    }
                    if (player.animation) {
                        clearInterval(player.animation);
                        player.animation = undefined;
                    }
                    draw();
                });

                //Cuando se golpea a alguien
                socket.on('someone hitted', function(data) {

                    console.log("Usuario: " + player.name);
                    var enemyName = JSON.stringify(data.enemy.name).replace(/"/g, '');
                    var enemyHP = JSON.stringify(data.enemy.hp).replace(/"/g, '');
                    var attackerName = JSON.stringify(data.attacker).replace(/"/g, '');

                    for (var i = data.playersonline.length - 1; i >= 0; i--) {
                        playersonline[i].image = playerImages.files[data.playersonline[i].image];
                        playersonline[i].weapon = playerImages.files[data.playersonline[i].weapon];
                        playersonline[i].head = playerImages.files[data.playersonline[i].head];
                        console.log("Vida de " + playersonline[i].name + " : " + playersonline[i].hp);
                        if (playersonline[i].name === enemyName) {
                            playersonline[i].hp = enemyHP;
                        }
                        if (playersonline[i].hp <= 0 && playersonline[i].name == player.name) {
                            socket.emit('die', { name: player.name, attacker: attackerName });
                            window.location.href = "/character";
                        }
                    }

                    if (player.name === enemyName) {
                        var message = "Te quedan " + enemyHP;
                        log(message, {});
                        play(hitted);
                        console.log("He sido golpeado: " + data.enemy.name);
                        var total = hBar.data('total'),
                            value = hBar.data('value');

                        // calculate the percentage of the total width
                        var damage = value - enemyHP;
                        var newValue = enemyHP;
                        var barWidth = (newValue / total) * 100;
                        var hitWidth = (damage / value) * 100 + "%";

                        // show hit bar and set the width
                        hit.css('width', hitWidth);
                        hBar.data('value', newValue);

                        setTimeout(function() {
                            hit.css({ 'width': '0' });
                            bar.css('width', barWidth + "%");
                        }, 500);
                    }

                });

                //cuando muere alguien
                socket.on('someone die', function(data) {
                    playersonline = data.playersonline;
                    for (var i = data.playersonline.length - 1; i >= 0; i--) {
                        playersonline[i].image = playerImages.files[data.playersonline[i].image];
                        playersonline[i].weapon = playerImages.files[data.playersonline[i].weapon];
                        playersonline[i].head = playerImages.files[data.playersonline[i].head];
                        if(playersonline[i].name === player.name) {
                            $('#killCount').text(playersonline[i].kills);
                        }
                    }
                    if (player.animation) {
                        clearInterval(player.animation);
                        player.animation = undefined;
                    }
                    draw();

                });

                // Whenever the server emits 'stop typing', kill the typing message
                socket.on('stop typing', function(data) {
                    removeChatTyping(data);
                });

                socket.on('disconnect', function() {
                    log('you have been disconnected');
                    // socket.emit('die', { playersonline: playersonline, name: player.name });
                    // window.location.href = "/character";
                });

                socket.on('reconnect', function() {
                    //log('you have been reconnected (not)');
                    // if (username) {
                    //     socket.emit('add user', username);
                    // }
                    // log('you have been disconnected');
                    socket.emit('die', { playersonline: playersonline, name: player.name });
                    window.location.href = "/character";
                });
                //
                // socket.on('reconnect_error', function() {
                //     log('attempt to reconnect has failed');
                // });
                //

                //funcion to rexu para reiniciar un adio
                function play(sound) {
                    if (!sound.paused) sound.pause();
                    sound.currentTime = 0;
                    sound.play();
                }

                return {
                    init: function(layers) {
                        for (var i = 0; i < 0 + layers.length; i++) {
                            mapLayers[i] = new TileField(context, CanvasControl().height, CanvasControl().width);
                            mapLayers[i].setup(layers[i]);
                            mapLayers[i].flip("horizontal");
                            mapLayers[i].rotate("left");
                            mapLayers[i].align("h-center", CanvasControl().width, xrange, 0);
                            mapLayers[i].align("v-center", CanvasControl().height, yrange, 0);
                        }
                        // rain = new EffectLoader().getEffect("rain", context, utils.range(-100, CanvasControl().height), utils.range(-100, CanvasControl().width));
                        if (player.animation) {
                            clearInterval(player.animation);
                            player.animation = undefined;
                        }
                        draw();
                    }
                };
            }
            launch();
        });
});
