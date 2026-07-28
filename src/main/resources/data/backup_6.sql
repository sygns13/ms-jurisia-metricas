-- =====================================================================
-- Métricas del módulo conversacional con Gemini (tópico Kafka
-- judicial-metrics-gemini-chats, publicado por ms-jurisia-consultaia).
-- Misma estructura general que las métricas del chat ChatGPT:
--   CabGeminiChat        (cabecera por conversación, espejo de CabConsultaIA)
--   DetailGeminiChat     (detalle por turno/mensaje, espejo de DetailConsultaIA)
--   DetailGeminiChatFiles(adjuntos por turno: nombre original + URI gs://)
--   SIJCabGeminiChat     (sedes/instancias por conversación, espejo de SIJCabConsultaIA)
--   SIJDetailGeminiChat  (sedes/instancias por turno, espejo de SIJDetailConsultaIA)
-- =====================================================================

CREATE TABLE If Not Exists JURISDB_METRICS.CabGeminiChat (
  id bigint unsigned primary key not null auto_increment,
  userId bigint DEFAULT NULL Comment 'ID de Usuario',
  model char(50) DEFAULT NULL Comment 'Modelo Gemini utilizado',
  countMessages int DEFAULT NULL Comment 'Cantidad de mensajes de la conversación',
  firstSendMessage text Comment 'Primer prompt enviado por el usuario',
  lastSendMessage text Comment 'Último prompt enviado por el usuario',
  firstResponseMessage text Comment 'Primera respuesta de la IA',
  lastResponseMessage text Comment 'Última respuesta de la IA',
  sessionUID char(50) NOT NULL Comment 'UUID de la conversación',
  hasFiles tinyint DEFAULT '0' Comment '1 si algún turno de la conversación incluyó adjuntos',
  countFiles int DEFAULT '0' Comment 'Cantidad total de adjuntos de la conversación',
  regDate date Null Comment 'Fecha create',
  regDatetime datetime Null Comment 'Fecha Hora create',
  regTimestamp bigint Null Comment 'Epoch create',
  updDate date Null Comment 'Fecha update',
  updDatetime datetime Null Comment 'Fecha Hora update',
  updTimestamp bigint Null Comment 'Epoch update'
)
ENGINE = INNODB,
CHARACTER SET utf8mb4,
COLLATE utf8mb4_general_ci,
COMMENT = 'Cabecera de conversaciones del chat con Gemini (métricas)';
-- Indexacion
ALTER TABLE JURISDB_METRICS.CabGeminiChat
    ADD INDEX cgc_userIdIDX (userId),
    ADD INDEX cgc_modelIDX (model),
    ADD INDEX cgc_sessionUIDIDX (sessionUID),
    ADD INDEX cgc_hasFilesIDX (hasFiles),
    ADD INDEX cgc_regDateIDX (regDate),
    ADD INDEX cgc_regDatetimeIDX (regDatetime),
    ADD INDEX cgc_regTimestampIDX (regTimestamp);


CREATE TABLE If Not Exists JURISDB_METRICS.DetailGeminiChat (
  id bigint unsigned primary key not null auto_increment,
  userId bigint DEFAULT NULL Comment 'ID de Usuario',
  model char(50) DEFAULT NULL Comment 'Modelo Gemini utilizado',
  roleSystem text Comment 'Instrucción de sistema utilizada',
  sendMessage text Comment 'Prompt enviado por el usuario',
  temperature decimal(3,1) DEFAULT NULL Comment 'Temperature del modelo',
  fechaSend datetime DEFAULT NULL Comment 'Fecha y hora de envío',
  fechaResponse datetime DEFAULT NULL Comment 'Fecha y hora de respuesta',
  responseMessage text Comment 'Respuesta generada por Gemini',
  timeSeconds double DEFAULT NULL Comment 'Tiempo total de procesamiento en segundos',
  ConfigurationsId int DEFAULT NULL Comment 'ID de la Configuración usada en consultaia',
  sessionUID char(50) NOT NULL Comment 'UUID de la conversación',
  status tinyint DEFAULT NULL Comment 'Status del turno (0 iniciado, 1 exitoso, 2 error)',
  hasFiles tinyint DEFAULT '0' Comment '1 si el turno incluyó adjuntos',
  regDate date Null Comment 'Fecha create',
  regDatetime datetime Null Comment 'Fecha Hora create',
  regTimestamp bigint Null Comment 'Epoch create'
)
ENGINE = INNODB,
CHARACTER SET utf8mb4,
COLLATE utf8mb4_general_ci,
COMMENT = 'Detalle por turno del chat con Gemini (métricas)';
-- Indexacion
ALTER TABLE JURISDB_METRICS.DetailGeminiChat
    ADD INDEX dgc_userIdIDX (userId),
    ADD INDEX dgc_modelIDX (model),
    ADD INDEX dgc_sessionUIDIDX (sessionUID),
    ADD INDEX dgc_statusIDX (status),
    ADD INDEX dgc_hasFilesIDX (hasFiles),
    ADD INDEX dgc_fechaSendIDX (fechaSend),
    ADD INDEX dgc_regDateIDX (regDate),
    ADD INDEX dgc_regDatetimeIDX (regDatetime),
    ADD INDEX dgc_regTimestampIDX (regTimestamp);


CREATE TABLE If Not Exists JURISDB_METRICS.DetailGeminiChatFiles (
  id bigint unsigned primary key not null auto_increment,
  detailGeminiChatId bigint DEFAULT NULL Comment 'ID del detalle (DetailGeminiChat) del turno',
  userId bigint DEFAULT NULL Comment 'ID de Usuario',
  sessionUID char(50) NOT NULL Comment 'UUID de la conversación',
  fileName varchar(255) DEFAULT NULL Comment 'Nombre original del archivo adjunto',
  mimeType varchar(150) DEFAULT NULL Comment 'MIME type del archivo',
  sizeBytes bigint DEFAULT NULL Comment 'Tamaño del archivo en bytes',
  gcsUri varchar(500) DEFAULT NULL Comment 'Dirección del archivo en GCS (gs://)',
  status tinyint DEFAULT NULL Comment 'Status del adjunto (1 activo)',
  regDate date Null Comment 'Fecha create',
  regDatetime datetime Null Comment 'Fecha Hora create',
  regTimestamp bigint Null Comment 'Epoch create'
)
ENGINE = INNODB,
CHARACTER SET utf8mb4,
COLLATE utf8mb4_general_ci,
COMMENT = 'Adjuntos por turno del chat con Gemini (métricas)';
-- Indexacion
ALTER TABLE JURISDB_METRICS.DetailGeminiChatFiles
    ADD INDEX dgcf_detailGeminiChatIdIDX (detailGeminiChatId),
    ADD INDEX dgcf_userIdIDX (userId),
    ADD INDEX dgcf_sessionUIDIDX (sessionUID),
    ADD INDEX dgcf_mimeTypeIDX (mimeType),
    ADD INDEX dgcf_regDateIDX (regDate);


CREATE TABLE If Not Exists JURISDB_METRICS.SIJCabGeminiChat (
  id bigint unsigned primary key not null auto_increment,
  userId bigint DEFAULT NULL Comment 'ID de Usuario',
  codSede char(20) DEFAULT NULL Comment 'Codigo de Sede en el SIJ',
  codInstancia char(20) DEFAULT NULL Comment 'Codigo de Instancia en el SIJ',
  sede char(200) DEFAULT NULL Comment 'Nombre de Sede en el SIJ',
  instancia char(200) DEFAULT NULL Comment 'Nombre de Instancia en el SIJ',
  sessionUID char(50) DEFAULT NULL Comment 'UUID de la conversación con Gemini',
  regDate date Null Comment 'Fecha create',
  regDatetime datetime Null Comment 'Fecha Hora create',
  regTimestamp bigint Null Comment 'Epoch create'
)
ENGINE = INNODB,
CHARACTER SET utf8mb4,
COLLATE utf8mb4_general_ci,
COMMENT = 'Enlace de la Cabecera del chat Gemini con el SIJ (sedes/instancias)';
-- Indexacion
ALTER TABLE JURISDB_METRICS.SIJCabGeminiChat
    ADD INDEX sijcgc_userIdIDX (userId),
    ADD INDEX sijcgc_codSedeIDX (codSede),
    ADD INDEX sijcgc_codInstanciaIDX (codInstancia),
    ADD INDEX sijcgc_sessionUIDIDX (sessionUID),
    ADD INDEX sijcgc_regDateIDX (regDate);


CREATE TABLE If Not Exists JURISDB_METRICS.SIJDetailGeminiChat (
  id bigint unsigned primary key not null auto_increment,
  userId bigint DEFAULT NULL Comment 'ID de Usuario',
  detailGeminiChatId bigint DEFAULT NULL Comment 'ID de Detalle del chat Gemini',
  codSede char(20) DEFAULT NULL Comment 'Codigo de Sede en el SIJ',
  codInstancia char(20) DEFAULT NULL Comment 'Codigo de Instancia en el SIJ',
  sede char(200) DEFAULT NULL Comment 'Nombre de Sede en el SIJ',
  instancia char(200) DEFAULT NULL Comment 'Nombre de Instancia en el SIJ',
  sessionUID char(50) DEFAULT NULL Comment 'UUID de la conversación con Gemini',
  regDate date Null Comment 'Fecha create',
  regDatetime datetime Null Comment 'Fecha Hora create',
  regTimestamp bigint Null Comment 'Epoch create'
)
ENGINE = INNODB,
CHARACTER SET utf8mb4,
COLLATE utf8mb4_general_ci,
COMMENT = 'Enlace del Detalle del chat Gemini con el SIJ (sedes/instancias)';
-- Indexacion
ALTER TABLE JURISDB_METRICS.SIJDetailGeminiChat
    ADD INDEX sijdgc_userIdIDX (userId),
    ADD INDEX sijdgc_detailGeminiChatIdIDX (detailGeminiChatId),
    ADD INDEX sijdgc_codSedeIDX (codSede),
    ADD INDEX sijdgc_codInstanciaIDX (codInstancia),
    ADD INDEX sijdgc_sessionUIDIDX (sessionUID),
    ADD INDEX sijdgc_regDateIDX (regDate);
