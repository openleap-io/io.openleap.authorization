package io.openleap.authorization.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.openleap.authorization.exception.BadRequestHttpException;
import io.openleap.authorization.exception.BaseHttpException;
import io.openleap.authorization.exception.InternalServerHttpException;
import io.openleap.authorization.util.CheckedExceptionHandler;
import org.springframework.http.HttpStatus;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;


public class BaseHttpClient {
    protected final HttpClient client;
    protected final String baseUrl;
    protected final ObjectMapper objectMapper;

    protected static class HttpUtil {
        public static final String ACCEPT_HEADER_NAME = "Accept";
        public static final String ACCEPT_HEADER_VALUE = "application/json";
        public static final String CONTENT_TYPE_HEADER_NAME = "Content-type";
        public static final String CONTENT_TYPE_HEADER_VALUE = "application/json";

        private HttpUtil() {
        }
    }

    protected BaseHttpClient(String baseUrl) {
        this(baseUrl, HttpClient.newBuilder().build());
    }

    protected BaseHttpClient(String baseUrl, HttpClient httpClient) {
        this.client = httpClient;
        this.baseUrl = baseUrl;
        this.objectMapper =
                JsonMapper.builder()
                        .addModule(new JavaTimeModule())
                        .build()
                        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    protected <T> T handleResponse(HttpResponse<String> response, Class<T> responseType) {

        if (response.statusCode() >= HttpStatus.OK.value()
                && response.statusCode() < HttpStatus.MULTIPLE_CHOICES.value()) {
            if (responseType.equals(Void.class)) {
                return null;
            } else {
                return CheckedExceptionHandler.handleCheckedException(objectMapper::readValue, response.body(), responseType);
            }
        } else if (response.statusCode() >= HttpStatus.BAD_REQUEST.value()
                && response.statusCode() < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            throw new BadRequestHttpException(response.statusCode(), response.body());
        } else if (response.statusCode() >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            throw new InternalServerHttpException(response.statusCode(), response.body());
        } else {
            throw new BaseHttpException(response.statusCode(), response.body());
        }
    }

}
