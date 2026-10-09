-- =====================================================================
-- Migración del histórico de documentos generados (flujo por secciones,
-- CabDocumentoGenerado) a CabDocumentoPlantillaGenerado, que pasa a ser la
-- única fuente de /v1/documento-generado-ia.
--
-- - CabDocumentoGenerado NO se modifica ni se borra (queda como respaldo).
-- - Idempotente: solo inserta los sessionUID que aún no existen en destino.
-- - Todas las filas antiguas eran generaciones exitosas: status = 1.
-- - idPlantilla queda NULL: templateID del flujo anterior es otro catálogo y
--   colisiona con los IDs de PlantillaDocumento. Se conservan templateCode y
--   templateNombreOut en codigoPlantilla / nombreOutPlantilla.
-- - Tokens: completionTokens -> candidatesTokens, CompletionReasoningTokens ->
--   thoughtsTokens. Los campos propios de OpenAI (object, modelResponse,
--   roleResponse, logprobs, audio/aceptados/rechazados, serviceTier) no tienen
--   columna destino.
-- - estadoIA y las columnas de variables/IA/tiempos del flujo nuevo quedan NULL.
-- =====================================================================

INSERT INTO JURISDB_METRICS.CabDocumentoPlantillaGenerado
    (sessionUID, typedoc, status, userId,
     nUnico, codSede, sede, codInstancia, instancia, codEspecialidad, especialidad,
     codMateria, materia, codNumero, codYear, xFormato, ubicacion, juez, estado,
     dniDemandante, demandante, dniDemandado, demandado,
     idTipoDocumento, tipoDocumento, idDocumento, documento,
     codigoPlantilla, nombreOutPlantilla,
     model, roleSystem, temperature, configurationsId, finishReason,
     promptTokens, candidatesTokens, thoughtsTokens, cachedTokens, totalTokens,
     regDate, regDatetime, regTimestamp)
SELECT o.sessionUID, o.typedoc, 1, o.userId,
       o.nUnico, o.codSede, o.sede, o.codInstancia, o.instancia, o.codEspecialidad, o.especialidad,
       o.codMateria, o.materia, o.codNumero, o.codYear, o.xFormato, o.ubicacion, o.juez, o.estado,
       o.dniDemandante, o.demandante, o.dniDemandado, o.demandado,
       o.idTipoDocumento, o.tipoDocumento, o.idDocumento, o.documento,
       o.templateCode, o.templateNombreOut,
       o.model, o.roleSystem, o.temperature, o.ConfigurationsId, o.finishReason,
       o.promptTokens, o.completionTokens, o.CompletionReasoningTokens, o.cachedTokens, o.totalTokens,
       o.regDate, o.regDatetime, o.regTimestamp
  FROM JURISDB_METRICS.CabDocumentoGenerado o
 WHERE NOT EXISTS (SELECT 1 FROM JURISDB_METRICS.CabDocumentoPlantillaGenerado n WHERE n.sessionUID = o.sessionUID)
 ORDER BY o.id;

-- Opcional: completa los datos del usuario de las filas migradas desde JURISDB_USERS.
-- Ejecutar SOLO si JURISDB_USERS está en el mismo servidor MySQL que JURISDB_METRICS.
UPDATE JURISDB_METRICS.CabDocumentoPlantillaGenerado n
  JOIN JURISDB_METRICS.CabDocumentoGenerado o ON o.sessionUID = n.sessionUID
  JOIN JURISDB_USERS.Users u ON u.id = n.userId
  LEFT JOIN JURISDB_USERS.Dependencias dep ON dep.id = u.dependenciaId
   SET n.username = u.username,
       n.nombreUsuario = TRIM(CONCAT(COALESCE(u.nombres, ''), ' ', COALESCE(u.apellidos, ''))),
       n.cargo = u.cargo,
       n.idDependencia = u.dependenciaId,
       n.dependencia = dep.nombre
 WHERE n.username IS NULL;
