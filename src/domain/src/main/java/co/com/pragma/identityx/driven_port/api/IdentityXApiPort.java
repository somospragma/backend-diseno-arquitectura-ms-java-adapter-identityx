package co.com.pragma.identityx.driven_port.api;

import co.com.pragma.identityx.model.*;
import com.daon.identityx.rest.model.pojo.Application;
import com.daon.identityx.rest.model.pojo.AuthenticationRequest;
import com.daon.identityx.rest.model.pojo.Policy;

import java.util.List;

public interface IdentityXApiPort {
    UserRequestModel getUser(String userId);

    UserRequestModel createUser(String userId);

    OtpRequestModel createAuthRequestOTP(String userId, Application application, Policy policy, Long otpTimeAlive);

    OtpRequestModel validateAuthRequestOTP(String authenticationRequestId,
                                           String submittedAuthenticationCode, String userId);

    RegistrationModel registration(String userId, Application application);

    RegistrationChallengeModel registrationChallenge(String userId, Application application, Policy policy);

    RegistrationChallengeModel registrationChallengeUpdate(String regChallengeId, String fidoRegistrationResponse);

    ApplicationRequestModel findApplication(String appId);

    PolicyRequestModel findPolicy(String policyId);

    AuthenticationRequestModel createAuthRequest(String userId,
                                                 Application application,
                                                 Policy policy,
                                                 String description,
                                                 String type);

    AuthenticationRequestModel updateAuthRequest(String authenticationRequestId,
                                                 AuthenticationRequest authenticationRequest);

    List<AuthenticatorModel> getListAuthenticators(String userId, String appId);

    AuthenticatorModel deleteAuthenticator(String authenticatorId);
}
