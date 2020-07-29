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
    age integer,
    img blob,
    phone varchar(20),
    creation_date TIMESTAMP default CURRENT_TIMESTAMP,
    update_date TIMESTAMP on update CURRENT_TIMESTAMP default CURRENT_TIMESTAMP, 
    primary key (user_id)  
);

insert into USER(name, surname, age, phone, password, email) values
	('Gary', 'Patrick', 20, '+995595341221','$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'gpat14@agruni.edu.ge'),
    ('Jerica', 'Walker', 23, '+995595345254', '$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'jwal17@freeuni.edu.ge'),
    ('Sophie', 'Lee', 21, '+995595451221','$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'slee17@agruni.edu.ge'),
    ('Ellie', 'Williams', 20, '+995595121221','$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'ewill17@freeuni.edu.ge'),
    ('Hannah', 'Johnson', 19, '+995595341233', '$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'hjohn18@freeuni.edu.ge'),
    ('lasha', 'loladze', 22, '+995595931221','$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'llola16@freeeun.edu.ge'),
    ('john', 'doe', 17, '+995599349961', '$2a$11$DC3cUThCJ1Yo6R.nsse5.ucd.DhEVx.gHfvcp3D5FbAeK4U4nNFt2', 'jdoe16@freeuni.edu.ge');


SET FOREIGN_KEY_CHECKS=1;

select * from USER;