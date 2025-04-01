package co.com.pragma.identityx.use_case;

import co.com.pragma.identityx.driven_port.api.IdentityXApiPort;
import co.com.pragma.identityx.model.*;
import com.daon.identityx.rest.model.pojo.Application;
import com.daon.identityx.rest.model.pojo.AuthenticationRequest;
import com.daon.identityx.rest.model.pojo.Policy;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class IdentityxUseCase {
    private IdentityXApiPort identityXApiPort;

    public OtpRequestModel getOtp(String userId, Application application, Policy policy, Long otpTimeAlive) {
        return identityXApiPort.createAuthRequestOTP(userId,application,policy, otpTimeAlive);
    }

    public OtpRequestModel validateOtp(String authenticationRequestId, String submittedAuthenticationCode, String userId)  {
        return identityXApiPort.validateAuthRequestOTP(authenticationRequestId,submittedAuthenticationCode, userId);
    }

    public UserRequestModel getUser(String userId) {
        return identityXApiPort.getUser(userId);
    }

    public UserRequestModel createUser(String userId) {
        return identityXApiPort.createUser(userId);
    }

    public RegistrationModel createRegistration(String userId, Application application) {
        return identityXApiPort.registration(userId,application);
    }

    public RegistrationChallengeModel createRegistrationChallenge(String userId, Application application, Policy policy) {
        return identityXApiPort.registrationChallenge(userId,application,policy);
    }

    public RegistrationChallengeModel updateRegistrationChallenge(String regChallengeId, String fidoRegistrationResponse) {
        return identityXApiPort.registrationChallengeUpdate(regChallengeId,fidoRegistrationResponse);
    }

    public ApplicationRequestModel getApplicationById(String appId) {
        return identityXApiPort.findApplication(appId);
    }

    public PolicyRequestModel getPolicyById(String policyId) {
        return identityXApiPort.findPolicy(policyId);
    }

    public AuthenticationRequestModel createAuthRequest(String userId,
                                                        Application application,
                                                        Policy policy,
                                                        String description,
                                                        String type) {
        return identityXApiPort.createAuthRequest(userId, application, policy, description, type);
    }

    public AuthenticationRequestModel updateAuthRequest(String authenticationRequestId,
                                                        AuthenticationRequest authenticationRequest){
        return identityXApiPort.updateAuthRequest(authenticationRequestId,authenticationRequest);
    }

    public List<AuthenticatorModel> getListAuthenticators(String userId, String appId) {
        return identityXApiPort.getListAuthenticators(userId,appId);
    }

    public AuthenticatorModel deleteAuthenticator(String authenticatorId){
        return identityXApiPort.deleteAuthenticator(authenticatorId);
    }
}
