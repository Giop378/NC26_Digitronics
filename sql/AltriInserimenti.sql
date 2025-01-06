-- Inserimento categorie del sito Digitronics
INSERT INTO categoria (nome, descrizione, immagine) VALUES
('Smartphone', 'Dispositivi mobili per comunicazione e produttività', './images/categorie/smartphone.jpg'),
('Tablet', 'Dispositivi portatili per lavoro e intrattenimento', './images/categorie/tablet.jpg'),
('Accessori', 'Articoli complementari come custodie, caricabatterie e altro', './images/categorie/accessori.jpg'),
('Smartwatch', 'Orologi intelligenti con funzioni di monitoraggio e connettività', './images/categorie/smartwatch.jpg');

-- Inserimento metodi di spedizione
INSERT INTO metodospedizione (nome, descrizione, costo) VALUES
('Ritiro in negozio', 'Ritiro gratuito presso il nostro negozio.', 0),
('Standard', 'Spedizione standard con consegna in 5-7 giorni lavorativi.', 5.00),
('Express', 'Spedizione express con consegna in 1-2 giorni lavorativi.', 15.00);