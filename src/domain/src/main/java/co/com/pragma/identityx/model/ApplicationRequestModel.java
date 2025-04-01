package co.com.pragma.identityx.model;

import com.daon.identityx.rest.model.pojo.Application;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationRequestModel {
    private String code;
    private String message;
    private Application data;
}
