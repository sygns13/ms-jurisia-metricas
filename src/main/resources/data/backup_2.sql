CREATE TABLE If Not Exists JURISDB_METRICS.SIJCabConsultaIA (
  id bigint unsigned primary key not null auto_increment,
  userId bigint DEFAULT NULL Comment 'ID de Usuario',
  codSede char(20) DEFAULT NULL Comment 'Codigo de Sede en el SIJ',
  codInstancia char(20) DEFAULT NULL Comment 'Codigo de Instancia en el SIJ',
  sede char(200) DEFAULT NULL Comment 'Nombre de Sede en el SIJ',
  instancia char(200) DEFAULT NULL Comment 'Nombre de Instancia en el SIJ',
  sessionUID char(50) DEFAULT NULL Comment 'ID de Sesión de consulta a la IA',
  regDate date Null Comment 'Fecha create',
  regDatetime datetime Null Comment 'Fecha Hora create',
  regTimestamp bigint Null Comment 'Epoch create'
)
ENGINE = INNODB,
CHARACTER SET utf8mb4,
COLLATE utf8mb4_general_ci,
COMMENT = 'Tabla de enlace de la Cabecera de Consultas a la IA (Inteligencia Artificial) con el SIJ';
-- Indexacion
ALTER TABLE JURISDB_METRICS.SIJCabConsultaIA
    ADD INDEX sij_userIdIDX (userId),
    ADD INDEX sij_codSedeIDX (codSede),
    ADD INDEX sij_codInstanciaIDX (codInstancia),
    ADD INDEX sij_sessionUIDIDX (sessionUID),
    ADD INDEX sij_regDateIDX (regDate),
    ADD INDEX sij_regDatetimeIDX (regDatetime),
    ADD INDEX sij_regTimestampIDX (regTimestamp);


CREATE TABLE If Not Exists JURISDB_METRICS.SIJDetailConsultaIA (
  id bigint unsigned primary key not null auto_increment,
  userId bigint DEFAULT NULL Comment 'ID de Usuario',
  codSede char(20) DEFAULT NULL Comment 'Codigo de Sede en el SIJ',
  codInstancia char(20) DEFAULT NULL Comment 'Codigo de Instancia en el SIJ',
  sede char(200) DEFAULT NULL Comment 'Nombre de Sede en el SIJ',
  instancia char(200) DEFAULT NULL Comment 'Nombre de Instancia en el SIJ',
  sessionUID char(50) DEFAULT NULL Comment 'ID de Sesión de consulta a la IA',
  regDate date Null Comment 'Fecha create',
  regDatetime datetime Null Comment 'Fecha Hora create',
  regTimestamp bigint Null Comment 'Epoch create'
)
ENGINE = INNODB,
CHARACTER SET utf8mb4,
COLLATE utf8mb4_general_ci,
COMMENT = 'Tabla de enlace de la Cabecera de Consultas a la IA (Inteligencia Artificial) con el SIJ';
-- Indexacion
ALTER TABLE JURISDB_METRICS.SIJDetailConsultaIA
    ADD INDEX sij_userIdIDX (userId),
    ADD INDEX sij_codSedeIDX (codSede),
    ADD INDEX sij_codInstanciaIDX (codInstancia),
    ADD INDEX sij_sessionUIDIDX (sessionUID),
    ADD INDEX sij_regDateIDX (regDate),
    ADD INDEX sij_regDatetimeIDX (regDatetime),
    ADD INDEX sij_regTimestampIDX (regTimestamp);
