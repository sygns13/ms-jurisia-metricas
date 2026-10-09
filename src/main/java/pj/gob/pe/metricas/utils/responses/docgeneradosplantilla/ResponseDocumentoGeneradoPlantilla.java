package pj.gob.pe.metricas.utils.responses.docgeneradosplantilla;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Fila de /v1/documento-generado-ia/get-data construida desde CabDocumentoPlantillaGenerado.
 * Conserva TODAS las propiedades JSON que devolvía la entidad CabDocumentoGenerado (contrato del
 * front) y agrega los datos propios del flujo por plantilla.
 */
@Schema(description = "Documento generado (flujo por plantilla) con el contrato de CabDocumentoGenerado")
@Data
@NoArgsConstructor
public class ResponseDocumentoGeneradoPlantilla {

    // ===== Contrato anterior (CabDocumentoGenerado) =====
    private Long id;
    private Long userId;
    private String typedoc;
    private String codSede;
    private String codInstancia;
    private String codEspecialidad;
    private String codMateria;
    private String sede;
    private String instancia;
    private String especialidad;
    private String materia;
    private String codNumero;
    private String codYear;
    private Long idDocumento;
    private Long idTipoDocumento;
    private String tipoDocumento;
    private String documento;
    private Long nUnico;
    private String xFormato;
    private String ubicacion;
    private String juez;
    private String estado;
    private String dniDemandante;
    private String demandante;
    private String dniDemandado;
    private String demandado;

    @Schema(description = "Código de la plantilla (codigoPlantilla)")
    private String templateCode;

    @Schema(description = "ID de la plantilla (idPlantilla); null en registros migrados del flujo por secciones")
    private Long templateID;

    @Schema(description = "Nombre de salida de la plantilla (nombreOutPlantilla)")
    private String templateNombreOut;

    private String model;
    private String roleSystem;
    private BigDecimal temperature;

    @Schema(description = "Sin equivalente en el flujo por plantilla (siempre null)")
    private String object;

    @Schema(description = "Modelo de IA (mismo valor que model)")
    private String modelResponse;

    @Schema(description = "Sin equivalente en el flujo por plantilla (siempre null)")
    private String roleResponse;

    @Schema(description = "Sin equivalente en el flujo por plantilla (siempre null)")
    private Integer logprobs;

    private String finishReason;
    private Integer promptTokens;

    @Schema(description = "Tokens de salida (candidatesTokens de Gemini)")
    private Integer completionTokens;

    private Integer totalTokens;
    private Integer cachedTokens;

    @Schema(description = "Sin equivalente en el flujo por plantilla (siempre null)")
    private Integer audioTokens;

    @Schema(description = "Tokens de razonamiento (thoughtsTokens de Gemini)")
    private Integer completionReasoningTokens;

    @Schema(description = "Sin equivalente en el flujo por plantilla (siempre null)")
    private Integer completionAudioTokens;

    @Schema(description = "Sin equivalente en el flujo por plantilla (siempre null)")
    private Integer completionAceptedTokens;

    @Schema(description = "Sin equivalente en el flujo por plantilla (siempre null)")
    private Integer completionRejectedTokens;

    @Schema(description = "Sin equivalente en el flujo por plantilla (siempre null)")
    private String serviceTier;

    private Integer configurationsId;
    private String sessionUID;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate regDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDatetime;

    private Long regTimestamp;

    // ===== Datos adicionales del flujo por plantilla =====
    @Schema(description = "0 iniciado, 1 exitoso, 2 error")
    private Integer status;
    private String mensajeError;
    private String username;
    private String nombreUsuario;
    private String cargo;
    private Long idDependencia;
    private String dependencia;
    private String numIncidente;
    private String especialista;
    private Integer cantidadDemandantes;
    private Integer cantidadDemandados;
    private Integer versionPlantilla;
    private Integer corregirIA;
    private Long tamanioPlantillaBytes;
    private Long tamanioSalidaBytes;
    private Integer totalVariables;
    private Integer variablesSij;
    private Integer variablesCalculadas;
    private Integer variablesManuales;
    private Integer variablesIA;
    private Integer variablesNoDefinidas;
    private Integer variablesSinValor;
    private Integer reemplazosRealizados;

    @Schema(description = "NO_APLICA, EXITOSO o ERROR")
    private String estadoIA;
    private String mensajeIA;
    private Integer parrafosEnviadosIA;
    private Integer parrafosCorregidosIA;
    private Integer bloquesSolicitadosIA;
    private Integer bloquesGeneradosIA;
    private Integer candidatesTokens;
    private Integer thoughtsTokens;
    private Long tiempoSijMs;
    private Long tiempoIAMs;
    private Long tiempoTotalMs;

    public static ResponseDocumentoGeneradoPlantilla from(CabDocumentoPlantillaGenerado d) {
        ResponseDocumentoGeneradoPlantilla r = new ResponseDocumentoGeneradoPlantilla();
        r.setId(d.getId());
        r.setUserId(d.getUserId());
        r.setTypedoc(d.getTypedoc());
        r.setCodSede(d.getCodSede());
        r.setCodInstancia(d.getCodInstancia());
        r.setCodEspecialidad(d.getCodEspecialidad());
        r.setCodMateria(d.getCodMateria());
        r.setSede(d.getSede());
        r.setInstancia(d.getInstancia());
        r.setEspecialidad(d.getEspecialidad());
        r.setMateria(d.getMateria());
        r.setCodNumero(d.getCodNumero());
        r.setCodYear(d.getCodYear());
        r.setIdDocumento(d.getIdDocumento());
        r.setIdTipoDocumento(d.getIdTipoDocumento());
        r.setTipoDocumento(d.getTipoDocumento());
        r.setDocumento(d.getDocumento());
        r.setNUnico(d.getNUnico());
        r.setXFormato(d.getXFormato());
        r.setUbicacion(d.getUbicacion());
        r.setJuez(d.getJuez());
        r.setEstado(d.getEstado());
        r.setDniDemandante(d.getDniDemandante());
        r.setDemandante(d.getDemandante());
        r.setDniDemandado(d.getDniDemandado());
        r.setDemandado(d.getDemandado());
        r.setTemplateCode(d.getCodigoPlantilla());
        r.setTemplateID(d.getIdPlantilla());
        r.setTemplateNombreOut(d.getNombreOutPlantilla());
        r.setModel(d.getModel());
        r.setRoleSystem(d.getRoleSystem());
        r.setTemperature(d.getTemperature());
        r.setModelResponse(d.getModel());
        r.setFinishReason(d.getFinishReason());
        r.setPromptTokens(d.getPromptTokens());
        r.setCompletionTokens(d.getCandidatesTokens());
        r.setTotalTokens(d.getTotalTokens());
        r.setCachedTokens(d.getCachedTokens());
        r.setCompletionReasoningTokens(d.getThoughtsTokens());
        r.setConfigurationsId(d.getConfigurationsId());
        r.setSessionUID(d.getSessionUID());
        r.setRegDate(d.getRegDate());
        r.setRegDatetime(d.getRegDatetime());
        r.setRegTimestamp(d.getRegTimestamp());

        r.setStatus(d.getStatus());
        r.setMensajeError(d.getMensajeError());
        r.setUsername(d.getUsername());
        r.setNombreUsuario(d.getNombreUsuario());
        r.setCargo(d.getCargo());
        r.setIdDependencia(d.getIdDependencia());
        r.setDependencia(d.getDependencia());
        r.setNumIncidente(d.getNumIncidente());
        r.setEspecialista(d.getEspecialista());
        r.setCantidadDemandantes(d.getCantidadDemandantes());
        r.setCantidadDemandados(d.getCantidadDemandados());
        r.setVersionPlantilla(d.getVersionPlantilla());
        r.setCorregirIA(d.getCorregirIA());
        r.setTamanioPlantillaBytes(d.getTamanioPlantillaBytes());
        r.setTamanioSalidaBytes(d.getTamanioSalidaBytes());
        r.setTotalVariables(d.getTotalVariables());
        r.setVariablesSij(d.getVariablesSij());
        r.setVariablesCalculadas(d.getVariablesCalculadas());
        r.setVariablesManuales(d.getVariablesManuales());
        r.setVariablesIA(d.getVariablesIA());
        r.setVariablesNoDefinidas(d.getVariablesNoDefinidas());
        r.setVariablesSinValor(d.getVariablesSinValor());
        r.setReemplazosRealizados(d.getReemplazosRealizados());
        r.setEstadoIA(d.getEstadoIA());
        r.setMensajeIA(d.getMensajeIA());
        r.setParrafosEnviadosIA(d.getParrafosEnviadosIA());
        r.setParrafosCorregidosIA(d.getParrafosCorregidosIA());
        r.setBloquesSolicitadosIA(d.getBloquesSolicitadosIA());
        r.setBloquesGeneradosIA(d.getBloquesGeneradosIA());
        r.setCandidatesTokens(d.getCandidatesTokens());
        r.setThoughtsTokens(d.getThoughtsTokens());
        r.setTiempoSijMs(d.getTiempoSijMs());
        r.setTiempoIAMs(d.getTiempoIAMs());
        r.setTiempoTotalMs(d.getTiempoTotalMs());
        return r;
    }
}
