package co.com.pragma.identityx.exceptionshandler;

import co.com.pragma.identityx.exceptionshandler.dto.ResponseErrorBodyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception exception) {
        ResponseErrorBodyDto responseErrorBodyDto = new ResponseErrorBodyDto();
        responseErrorBodyDto.setCode(INTERNAL_SERVER_ERROR.value());
        responseErrorBodyDto.setMessage("Internal Server Error| INT MS "+exception.getMessage());
        log.error("Exception: {}",exception.getMessage());
        return new ResponseEntity<>(responseErrorBodyDto, INTERNAL_SERVER_ERROR);
    }

}
