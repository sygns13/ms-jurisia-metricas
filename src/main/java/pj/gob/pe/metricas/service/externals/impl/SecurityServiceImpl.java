package pj.gob.pe.metricas.service.externals.impl;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pj.gob.pe.metricas.configuration.ConfigProperties;
import pj.gob.pe.metricas.exception.AuthOpenAIException;
import pj.gob.pe.metricas.service.externals.SecurityService;
import pj.gob.pe.metricas.utils.inputs.consultaia.InputConsultaIAExternal;
import pj.gob.pe.metricas.utils.inputs.security.UserIdsRequest;
import pj.gob.pe.metricas.utils.responses.consultaia.OutputConsultaIAExternal;
import pj.gob.pe.metricas.utils.responses.security.ResponseLogin;
import pj.gob.pe.metricas.utils.responses.security.User;

import java.util.List;


@Service
public class SecurityServiceImpl implements SecurityService {

    private final RestClient restClient;
    private final ConfigProperties properties;

    public SecurityServiceImpl(RestClient.Builder builder, ConfigProperties properties) {
        this.restClient = builder.baseUrl(properties.getUrlSecurityAPI()).build();
        this.properties = properties;
    }

    @Override
    public ResponseLogin GetSessionData(String SessionId) {

        String pathSession = properties.getPathGetSession();

        pathSession = pathSession.replace(":sessionId", SessionId);

        return restClient.get()
                .uri(pathSession)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new AuthOpenAIException("Credenciales de Sessión Expirada");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Error del servidor, Comunicarse con el administrador");
                })
                .body(ResponseLogin.class); // Se convierte automáticamente el JSON a la clase Java
    }

    @Override
    public List<OutputConsultaIAExternal> Getusers(InputConsultaIAExternal inputConsultaIAExternal) {

        String pathGetUsers = properties.getPathGetUsers();

        return restClient.post()
                .uri(pathGetUsers)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(inputConsultaIAExternal) // Enviar objeto como JSON
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new AuthOpenAIException("Credenciales de Sessión Expirada");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Error del servidor, Comunicarse con el administrador");
                })
                .body(new ParameterizedTypeReference<List<OutputConsultaIAExternal>>() {});
    }

    @Override
    public List<User> GetListUsers(UserIdsRequest userIdsRequest) {

        String pathGetListUsers = properties.getPathGetListUsers();

        return restClient.post()
                .uri(pathGetListUsers)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(userIdsRequest) // Enviar objeto como JSON
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new AuthOpenAIException("Credenciales de Sessión Expirada");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Error del servidor, Comunicarse con el administrador");
                })
                .body(new ParameterizedTypeReference<List<User>>() {});
    }
}
