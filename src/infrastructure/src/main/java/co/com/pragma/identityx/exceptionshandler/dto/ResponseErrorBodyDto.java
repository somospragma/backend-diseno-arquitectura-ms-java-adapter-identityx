package co.com.pragma.identityx.exceptionshandler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseErrorBodyDto {
    private Integer code;
    private String message;
}
