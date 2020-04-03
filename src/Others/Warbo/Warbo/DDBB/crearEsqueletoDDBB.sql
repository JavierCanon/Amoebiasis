DROP DATABASE IF EXISTS warbo;

CREATE DATABASE warbo;

USE warbo;

DROP TABLE IF EXISTS type_maps;

CREATE TABLE type_maps (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name varchar(50)
);

INSERT INTO type_maps (name) VALUES ('sample');

-- CREAMOS LOS MAPAS
DROP TABLE IF EXISTS maps;

CREATE TABLE maps (
	id INT AUTO_INCREMENT PRIMARY KEY,
	created TIMESTAMP DEFAULT NOW(),
	name varchar(50),
	type_id INT,
	FOREIGN KEY (type_id)
        REFERENCES type_maps(id)
        ON DELETE CASCADE
);

INSERT INTO maps (name, type_id) VALUES ('testmap1.json',1);
INSERT INTO maps (name, type_id) VALUES ('testmap2.json',1);

-- CREAMOS TABLA CON LOS TIPO DE CHARS QUE HABRA
DROP TABLE IF EXISTS type_chars;

CREATE TABLE type_chars(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name varchar(100)
);

INSERT INTO type_chars (name) VALUES ('sample');

-- CREAMOS LOS PJ BASE
DROP TABLE IF EXISTS chars;

CREATE TABLE chars(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name varchar(50),
	hp INT,
	attack INT,
	defense INT,
	speed INT,
	type_id INT,
	FOREIGN KEY (type_id)
        REFERENCES type_chars(id)
        ON DELETE CASCADE

);

INSERT INTO chars (name,hp,attack,defense,speed,type_id) VALUES ('Guerrero',80,6,10,8,1);
INSERT INTO chars (name,hp,attack,defense,speed,type_id) VALUES ('Arquero',60,8,6,6,1);
INSERT INTO chars (name,hp,attack,defense,speed,type_id) VALUES ('Mago',70,10,8,4,1);

-- CREAMOS LOS TIPOS DE ARMAS
DROP TABLE IF EXISTS weapons;

CREATE TABLE weapons(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name varchar(50),
	weapon varchar(50),
	img varchar(100),
	hp INT,
	attack INT,
	defense INT,
	speed INT,
	UNIQUE (name)

);

INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Espada corta','sword','shortsword.png',0,1,0,1);
INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Espada larga','sword','longsword.png',0,3,0,0);
INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Gran espada','sword','greatsword.png',0,5,0,-1);
INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Arco corto','bow','shortbow.png',0,1,0,1);
INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Arco largo','bow','longbow.png',0,3,0,0);
INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Gran arco','bow','greatbow.png',0,5,0,-1);
INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Vara','staff','rod.png',0,1,0,1);
INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Bastón','staff','staff.png',0,3,0,0);
INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Gran bastón','staff','greatstaff.png',0,5,0,-1);
INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Mini escudo','shield','buckler.png',0,0,1,0);
INSERT INTO weapons (name,weapon,img,hp,attack,defense,speed) VALUES ('Escudo','shield','shield.png',0,0,2,0);

-- TABLA DE USUARIOS
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name varchar(50),
	password varchar(300),
    pj varchar(20),
    created TIMESTAMP DEFAULT NOW(),
    admin boolean DEFAULT false,
    UNIQUE (name)
);


-- TABLA D PJS POR USUARIO
DROP TABLE IF EXISTS users_chars;

CREATE TABLE users_chars (
	id INT AUTO_INCREMENT PRIMARY KEY,
	created TIMESTAMP DEFAULT NOW(),
	name varchar(50),
	map varchar(50),
	lvl INT,
	hp INT,
	attack INT,
	defense INT,
	speed INT,
	image varchar(50),
	weapon varchar(50),
	head varchar(50),
	xPos INT,
	yPos INT,
	action INT,
	direction INT,
	width INT,
	height INT,
	user_id INT,
	class varchar(50),
	sound varchar(50),
	kills INT,
	FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    UNIQUE (name)

);

DROP TABLE IF EXISTS inventories;


-- INVENTARIO DE CADA PJ
CREATE TABLE inventories(
	id INT AUTO_INCREMENT PRIMARY KEY,
	users_chars_id INT,
	FOREIGN KEY (users_chars_id)
        REFERENCES users_chars(id)
        ON DELETE CASCADE
);


DROP TABLE IF EXISTS tiles;


-- TILES DEL MAPA
CREATE TABLE tiles(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name varchar(50)
);

INSERT INTO tiles (name) VALUES ('0 - Hierba');
INSERT INTO tiles (name) VALUES ('1 - Piedra');
INSERT INTO tiles (name) VALUES ('2 - Ladrillo');


-- SPRITES PARA LA PERSONALIZACION DEL PJ
-- DROP TABLE IF EXISTS sprites;

-- CREATE TABLE sprites(
-- 	id INT AUTO_INCREMENT PRIMARY KEY,

-- );


-- EQUIPAMIENTO
DROP TABLE IF EXISTS items;

CREATE TABLE items(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name varchar(50),
	hp INT,
	attack INT,
	defense INT,
	speed INT
);

INSERT INTO items (name,hp,attack,defense,speed) VALUES ('Armadura tela',10,0,1,1);
INSERT INTO items (name,hp,attack,defense,speed) VALUES ('Armadura cuero',20,0,2,0);
INSERT INTO items (name,hp,attack,defense,speed) VALUES ('Armadura placas',30,0,3,-1);
INSERT INTO items (name,hp,attack,defense,speed) VALUES ('Daga',0,2,0,2);
INSERT INTO items (name,hp,attack,defense,speed) VALUES ('Espada',0,3,0,0);
INSERT INTO items (name,hp,attack,defense,speed) VALUES ('Mandoble',0,5,0,-1);
INSERT INTO items (name,hp,attack,defense,speed) VALUES ('Escudo',0,0,5,0);


-- UNION DE INVENTARIO DE PJS E ITEMS
DROP TABLE IF EXISTS inventories_characters;

CREATE TABLE inventories_characters(
	id INT AUTO_INCREMENT PRIMARY KEY,
	inventory_id INT,
	item_id INT,
	FOREIGN KEY (inventory_id)
        REFERENCES inventories(id)
        ON DELETE CASCADE,
	FOREIGN KEY (item_id)
        REFERENCES items(id)
        ON DELETE CASCADE
);
