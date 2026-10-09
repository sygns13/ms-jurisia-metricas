-- =====================================================================
-- Sede SIJ en DemandasCalificadas / DemandasSentencias (métricas).
-- Llega en el payload Kafka (judicial-metrics-califications /
-- judicial-metrics-sentencias) desde ms-jurisia-consultaia y alimenta los
-- reportes admin agrupados por instancia y por expediente.
-- =====================================================================

ALTER TABLE JURISDB_METRICS.DemandasCalificadas
    ADD COLUMN codSede char(20) DEFAULT NULL COMMENT 'Código de sede SIJ de la instancia' AFTER xdescDemandante,
    ADD COLUMN sede char(200) DEFAULT NULL COMMENT 'Descripción de sede SIJ de la instancia' AFTER codSede,
    ADD INDEX IdxCodSede (codSede);

ALTER TABLE JURISDB_METRICS.DemandasSentencias
    ADD COLUMN codSede char(20) DEFAULT NULL COMMENT 'Código de sede SIJ de la instancia' AFTER xdescDemandante,
    ADD COLUMN sede char(200) DEFAULT NULL COMMENT 'Descripción de sede SIJ de la instancia' AFTER codSede,
    ADD INDEX IdxCodSede (codSede);

-- Backfill de históricos (registros previos quedan con sede NULL).
-- Mapeo cinstancia -> sede tomado del SIJ (ver CabDocumentoPlantillaGenerado).
-- VALIDAR el mapeo antes de ejecutar en cada entorno.
UPDATE JURISDB_METRICS.DemandasCalificadas
   SET codSede = '0201', sede = 'Sede Central de Corte'
 WHERE codSede IS NULL AND TRIM(cinstancia) IN ('301', '302');

UPDATE JURISDB_METRICS.DemandasSentencias
   SET codSede = '0201', sede = 'Sede Central de Corte'
 WHERE codSede IS NULL AND TRIM(cinstancia) IN ('301', '302');
