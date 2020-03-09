create table if not exists users(
id serial primary key,
name varchar(50),
password varchar,
email varchar(100));

create table if not exists wallet(
id serial primary key,
name varchar(60),
value numeric(10, 2));

create table if not exists users_wallet(
id serial primary key,
wallet integer,
users integer,
foreign key(users) references users(id),
foreign key(wallet) references wallet(id));

create table if not exists wallet_items(
id serial primary key,
wallet integer,
users integer,
type varchar(2),
description varchar(500),
value numeric (10,2),
foreign key(wallet) references wallet(id));