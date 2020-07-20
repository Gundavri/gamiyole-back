drop database if exists Gamiyole;
create database if not exists Gamiyole;
use Gamiyole;

SET FOREIGN_KEY_CHECKS=0; 
SET GLOBAL sql_mode = '';


-- users 
drop table if exists USER;
create table USER (
	user_id int auto_increment, 
    name varchar(63) not null,
    password varchar(1000) not null, 
    email varchar(63) not null UNIQUE,
    surname varchar(63) not null,
    img_dest varchar(255),
    phone int,
    creation_date TIMESTAMP default CURRENT_TIMESTAMP,
    update_date TIMESTAMP on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP, 
    primary key (user_id)  
);

insert into USER(name, password, email, surname) values
	('abcd', '$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'asdizxc1@zsc.com', 'asdaszxcqwe'),
    ('abcd', '$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'asdizxc2@zsc.com', 'asdaszxcqwe'),
    ('abcd', '$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'asdizxc3@zsc.com', 'asdaszxcqwe'),
    ('abcd', '$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'asdizxc4@zsc.com', 'asdaszxcqwe'),
    ('abcd', '$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'asdizxc5@zsc.com', 'asdaszxcqwe'),
    ('abcd', '$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'asdizxc6@zsc.com', 'asdaszxcqwe'),
    ('abcd', '$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'asdizxc36@zsc.com', 'asdaszxcqwe');


SET FOREIGN_KEY_CHECKS=1;

select * from user