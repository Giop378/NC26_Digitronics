Drop database if exists Digitronics;
create database Digitronics;
use Digitronics;

create table categoria (
	nome varchar(50) not null primary key,
    descrizione text not null,
    immagine varchar(255));

create table prodotto(
	IdProdotto int(5) zerofill auto_increment primary key,
	nome varchar(255) not null,
	prezzo decimal(10, 2) not null,
	descrizione text not null,
    immagine text not null,
    vetrina boolean not null,
    quantità int not null,
    nomecategoria varchar(50) not null,
    foreign key(nomecategoria) references categoria(nome));

create table utente(
	IdUtente int(10) zerofill auto_increment  primary key,
    nome varchar(50) not null,
    cognome varchar(50) not null,
    email varchar(255) not null  unique,
    passwordhash varchar(255) not null,
    datadinascita date not null,
    ruolo boolean not null
    );

create table itemCarrello(
	IdProdotto int(5) zerofill,
    IdUtente int(10) zerofill,
    quantità int not null,
	primary key(IdProdotto, IdUtente),
    foreign key(IdProdotto) references prodotto(IdProdotto)
		on update cascade
        on delete cascade,
    foreign key(IdUtente) references utente(IdUtente) );


create table metodospedizione (
	nome varchar(50) not null primary key,
	descrizione text not null,
    costo decimal(10, 2) not null
);

create table ordine  (
	IdOrdine int(10) zerofill auto_increment primary key,
    totale decimal(10, 2) not null,
    IdUtente int(10) zerofill,
    cap int(5) zerofill not null,
    città varchar(255) not null,
    numerocivico int not null, 
    nome varchar(255) not null,
    cognome varchar(255) not null,
    via text not null,
    telefono varchar(20),
    nomemetodospedizione varchar(50) not null,
    dataordine date not null,
    foreign key(nomemetodospedizione) references metodospedizione(nome), 
    foreign key(idutente) references utente(idutente));

create table itemordine(
	IdItemOrdine int(10) zerofill auto_increment primary key,
    nome varchar(255) not null,
    immagine text not null,
    prezzo decimal (10, 2) not null,
    quantità int not null,
    IdProdotto int(5) zerofill,
    IdOrdine int(10) zerofill not null,
    foreign key(idprodotto) references prodotto(idprodotto)
		on update cascade
        on delete set null,
    foreign key(idordine) references ordine(idordine)
);

create table recensione(
	IdRecensione int(10) zerofill auto_increment primary key,
    titolo varchar(255) not null,
    descrizione text not null,
    punteggio int not null,
	IdProdotto int(5) zerofill,
    idutente int(10) zerofill not null,
    foreign key(idprodotto) references prodotto(idprodotto)
		on update cascade
        on delete set null,
	foreign key(idutente) references utente(idutente)
    );
    