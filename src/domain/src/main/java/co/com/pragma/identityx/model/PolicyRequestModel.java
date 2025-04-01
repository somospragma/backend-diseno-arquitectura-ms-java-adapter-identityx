package co.com.pragma.identityx.model;

import com.daon.identityx.rest.model.pojo.Policy;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyRequestModel {
    private String code;
    private String message;
    private Policy data;
}
