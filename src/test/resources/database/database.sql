
DROP ALL OBJECTS;
CREATE SCHEMA IF NOT EXISTS Digitronics;
SET SCHEMA Digitronics;

CREATE TABLE categoria (
                           nome VARCHAR(50) NOT NULL PRIMARY KEY,
                           descrizione TEXT NOT NULL,
                           immagine VARCHAR(255)
);

CREATE TABLE prodotto(
                         IdProdotto INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         prezzo DECIMAL(10, 2) NOT NULL,
                         descrizione TEXT NOT NULL,
                         immagine TEXT NOT NULL,
                         vetrina BOOLEAN NOT NULL,
                         quantità INT NOT NULL,
                         nomecategoria VARCHAR(50) NOT NULL,
                         FOREIGN KEY (nomecategoria) REFERENCES categoria(nome)
);

CREATE TABLE utente(
                       IdUtente INT AUTO_INCREMENT PRIMARY KEY,
                       nome VARCHAR(50) NOT NULL,
                       cognome VARCHAR(50) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       passwordhash VARCHAR(255) NOT NULL,
                       datadinascita DATE NOT NULL,
                       ruolo BOOLEAN NOT NULL
);

CREATE TABLE itemCarrello(
                             IdProdotto INT,
                             IdUtente INT,
                             quantità INT NOT NULL,
                             PRIMARY KEY (IdProdotto, IdUtente),
                             FOREIGN KEY (IdProdotto) REFERENCES prodotto(IdProdotto)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE,
                             FOREIGN KEY (IdUtente) REFERENCES utente(IdUtente)
);

CREATE TABLE metodospedizione (
                                  nome VARCHAR(50) NOT NULL PRIMARY KEY,
                                  descrizione TEXT NOT NULL,
                                  costo DECIMAL(10, 2) NOT NULL
);

CREATE TABLE ordine (
                        IdOrdine INT AUTO_INCREMENT PRIMARY KEY,
                        totale DECIMAL(12, 2) NOT NULL,
                        IdUtente INT,
                        cap VARCHAR(5) NOT NULL,
                        città VARCHAR(255) NOT NULL,
                        numerocivico VARCHAR(12) NOT NULL,
                        nome VARCHAR(255) NOT NULL,
                        cognome VARCHAR(255) NOT NULL,
                        via VARCHAR(255) NOT NULL,
                        telefono VARCHAR(20),
                        nomemetodospedizione VARCHAR(50) NOT NULL,
                        dataordine DATE NOT NULL,
                        FOREIGN KEY (nomemetodospedizione) REFERENCES metodospedizione(nome),
                        FOREIGN KEY (IdUtente) REFERENCES utente(IdUtente)
);

CREATE TABLE itemordine(
                           IdItemOrdine INT AUTO_INCREMENT PRIMARY KEY,
                           nome VARCHAR(255) NOT NULL,
                           immagine TEXT NOT NULL,
                           prezzo DECIMAL(10, 2) NOT NULL,
                           quantità INT NOT NULL,
                           IdProdotto INT,
                           IdOrdine INT NOT NULL,
                           FOREIGN KEY (IdProdotto) REFERENCES prodotto(IdProdotto)
                               ON UPDATE CASCADE
                               ON DELETE SET NULL,
                           FOREIGN KEY (IdOrdine) REFERENCES ordine(IdOrdine)
);

CREATE TABLE recensione(
                           IdRecensione INT AUTO_INCREMENT PRIMARY KEY,
                           titolo VARCHAR(255) NOT NULL,
                           descrizione TEXT NOT NULL,
                           punteggio INT NOT NULL,
                           IdProdotto INT,
                           IdUtente INT NOT NULL,
                           FOREIGN KEY (IdProdotto) REFERENCES prodotto(IdProdotto)
                               ON UPDATE CASCADE
                               ON DELETE SET NULL,
                           FOREIGN KEY (IdUtente) REFERENCES utente(IdUtente)
);
