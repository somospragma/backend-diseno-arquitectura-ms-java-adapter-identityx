package co.com.pragma.identityx.helpers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ResponseHelperTest {

    @Test
    void testSuccessResponse() {
        // Arrange
        String expectedBody = "Success";
        // Act
        ResponseEntity<String> response = ResponseHelper.successResponse(expectedBody);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    void testSuccessResponseWithNullBody() {
        // Act
        ResponseEntity<Object> response = ResponseHelper.successResponse(null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testSuccessResponseWithObjectBody() {
        // Arrange
        TestObject expectedBody = new TestObject("test", 42);

        // Act
        ResponseEntity<TestObject> response = ResponseHelper.successResponse(expectedBody);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    private static class TestObject {
        private final String name;
        private final int value;

        TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            TestObject other = (TestObject) obj;
            return name.equals(other.name) && value == other.value;
        }
    }
}
