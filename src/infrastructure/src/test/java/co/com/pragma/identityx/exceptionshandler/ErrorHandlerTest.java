package co.com.pragma.identityx.exceptionshandler;

import co.com.pragma.identityx.driven_adapters.identityx.api.IdentityXApiPortImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {

    @InjectMocks
    ErrorHandler errorHandler;

    private static final Logger log = LoggerFactory.getLogger(IdentityXApiPortImpl.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testServerExceptionException() throws JsonProcessingException {
        int expectedCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String expectedBody = "{\"code\":500,\"message\":\"Internal Server Error| INT MS Failed Action between ins processing\"}";

        String message = "Failed Action between ins processing";
        Exception exception = new Exception(message);

        ObjectMapper objectsMapper = new ObjectMapper();

        ResponseEntity<Object> responseEntity = errorHandler.handleException(exception);
        System.out.println(objectsMapper.writeValueAsString(responseEntity.getBody()));
        assertEquals(expectedCode, responseEntity.getStatusCode().value());
        assertEquals(expectedBody, objectsMapper.writeValueAsString(responseEntity.getBody()));
    }
}
