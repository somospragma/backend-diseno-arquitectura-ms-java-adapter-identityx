package co.com.pragma.identityx.model;

import com.daon.identityx.rest.model.pojo.User;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestModel {
    private String code;
    private String message;
    private User data;
}
