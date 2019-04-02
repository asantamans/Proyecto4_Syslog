# Proyecto4_Syslog
Especificacions:
--> El component ha de poder crear una conexió a una base de dades MySQL
--> El component ha de registrar per cada base de dades les següents dades:
--> Usuari de conexió
--> Tipus de consulta (Consulta, Modificació, Crida a Stored)
--> Sentencia executada
--> Data i hora de la sentencia executada
--> Num de registres retornats
--> El component ha de permetre consultar les dades per:
--> Base de dades i usuari (llistat de les sentencies executades, data i hora, tipus)
--> Base de dades, usuari i tipus (llistat de les sentencies executades, data i hora)
--> Base de dades, tipus (llistat de les sentencies executades, data i hora, usuari)
--> Cada vegada que s'executi una sentencia contra la base de dades mitjançant events s'han d'emmagatzemar les dades requerides
--> El component ha de lliurar-se empaquetat en un fitxer jar
--> S'ha de crear un main (fora del component) i fer un codi que demostri que totes les funcionalitats estan implementades
Especificacions NO funcionals

Especificacions no funcionals:
--> El component ha de tenir al menys 2 classes, una que gestiona l'accés a dades i l'altra que registra l'ús que es fa de la connexió a la base de dades
