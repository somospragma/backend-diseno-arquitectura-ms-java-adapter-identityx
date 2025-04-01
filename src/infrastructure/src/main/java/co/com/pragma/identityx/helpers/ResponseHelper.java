package co.com.pragma.identityx.helpers;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseHelper {

    public static <T> ResponseEntity<T> successResponse(T body) { return new ResponseEntity<>(body, HttpStatus.OK);}
}
