package pj.gob.pe.metricas.service.externals.impl;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pj.gob.pe.metricas.configuration.ConfigProperties;
import pj.gob.pe.metricas.exception.AuthOpenAIException;
import pj.gob.pe.metricas.service.externals.JudicialService;
import pj.gob.pe.metricas.utils.responses.judicial.DataEspecialidadDTO;
import pj.gob.pe.metricas.utils.responses.judicial.DataInstanciaDTO;
import pj.gob.pe.metricas.utils.responses.judicial.Documento;
import pj.gob.pe.metricas.utils.responses.judicial.TipoDocumento;

import java.util.List;

@Service
public class JudicialServiceImpl implements JudicialService {

    private final RestClient restClient;
    private final ConfigProperties properties;

    public JudicialServiceImpl(RestClient.Builder builder, ConfigProperties properties) {
        this.restClient = builder.baseUrl(properties.getUrlJudicialAPI()).build();
        this.properties = properties;
    }

    @Override
    public List<DataInstanciaDTO> GetInstancias() {

        String path = properties.getPathInstancias();

        return restClient.get()
                .uri(path)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new AuthOpenAIException("Credenciales de Sessión Expirada");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Error del servidor, Comunicarse con el administrador");
                })
                .body(new ParameterizedTypeReference<List<DataInstanciaDTO>>() {});
    }

    @Override
    public List<DataEspecialidadDTO> GetEspecialidades() {
        String path = properties.getPathEspecialidades();

        return restClient.get()
                .uri(path)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new AuthOpenAIException("Credenciales de Sessión Expirada");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Error del servidor, Comunicarse con el administrador");
                })
                .body(new ParameterizedTypeReference<List<DataEspecialidadDTO>>() {});
    }

    @Override
    public List<TipoDocumento> GetTipoDocumento() {
        String path = properties.getPathTipoDocumentos();

        return restClient.get()
                .uri(path)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new AuthOpenAIException("Credenciales de Sessión Expirada");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Error del servidor, Comunicarse con el administrador");
                })
                .body(new ParameterizedTypeReference<List<TipoDocumento>>() {});
    }

    @Override
    public List<Documento> GetDocumento() {
        String path = properties.getPathDocumentos();

        return restClient.get()
                .uri(path)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new AuthOpenAIException("Credenciales de Sessión Expirada");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Error del servidor, Comunicarse con el administrador");
                })
                .body(new ParameterizedTypeReference<List<Documento>>() {});
    }
}
