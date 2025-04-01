package co.com.pragma.identityx.driven_adapters.identityx.api;

import co.com.pragma.identityx.constants.ServiceConstants;
import co.com.pragma.identityx.driven_adapters.identityx.api.feign.IdentityXFeign;
import co.com.pragma.identityx.driven_adapters.identityx.mapper.IdentityXMapper;
import co.com.pragma.identityx.driven_adapters.secret_manager.service.SecretManagerService;
import co.com.pragma.identityx.driven_port.api.IdentityXApiPort;
import co.com.pragma.identityx.entry_points.rest.dto.AuthRequestOTPDto;
import co.com.pragma.identityx.model.*;
import com.daon.identityx.rest.model.pojo.*;
import com.identityx.clientSDK.collections.*;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Component
public class IdentityXApiPortImpl implements IdentityXApiPort {

    private final Logger logger = LoggerFactory.getLogger(IdentityXApiPortImpl.class);

    private final IdentityXMapper identityXMapper;
    private final IdentityXFeign identityXFeign;
    private final RestTemplate restTemplate;
    private final SecretManagerService secretManagerService;
    private String token;

    @Value(ServiceConstants.IDENTITYX_SECRET_NAME)
    private String secretName;

    private static final Long OTP_TIME_DELAY = 5L;

    @Scheduled(fixedRate = 3600000)
    public void generateToken() throws Exception {
        String privateKeyPem = secretManagerService.getSecret(secretName).getPrivateKey();
        token = generateToken( new Date(System.currentTimeMillis() + 3600000 ), privateKeyPem);
    }

    public String generateToken(Date expirationDate,String privateKeyPem) throws Exception {

        PrivateKey privateKey = getPrivateKeyFromPem(privateKeyPem);

        return Jwts.builder()
                .header()
                .add(ServiceConstants.TYP, ServiceConstants.JWT)
                .add(ServiceConstants.ALG, ServiceConstants.RS256)
                .and()
                .subject(ServiceConstants.SUBJECT)
                .claim(ServiceConstants.CLAIM_KEY, ServiceConstants.CLAIM_VALUE)
                .expiration(expirationDate) // 1 hour expiration
                .signWith(privateKey)
                .compact();
    }

    public static PrivateKey getPrivateKeyFromPem(String privateKeyPem) throws Exception {

        // Strip off the first and last lines of the PEM file
        privateKeyPem = privateKeyPem.replace(ServiceConstants.BEGIN_PRIVATE_KEY, ServiceConstants.EMPTY_STRING)
                .replace(ServiceConstants.END_PRIVATE_KEY, ServiceConstants.EMPTY_STRING)
                .replaceAll(ServiceConstants.UNDER_SCOPE, ServiceConstants.EMPTY_STRING);

        byte[] encoded = Base64.getDecoder().decode(privateKeyPem);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance(ServiceConstants.RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    public PolicyRequestModel findPolicy(String policyId) {

        PolicyCollection policyCollection = identityXFeign.findPolicy(token, policyId);

        assert policyCollection != null;
        logger.info("Policy Collection :{}", policyCollection.getItems().length);

        return switch (policyCollection.getItems().length) {
            case 0 -> throw new RuntimeException(ServiceConstants.POLICYID_NOT_FOUND + policyId);
            case 1 -> PolicyRequestModel.builder()
                    .code(String.valueOf(HttpStatus.OK.value()))
                    .message(HttpStatus.OK.getReasonPhrase())
                    .data(policyCollection.getItems()[0]).build();
            default -> throw new RuntimeException("More than one policy with the same policyId!!!!");
        };
    }

    public ApplicationRequestModel findApplication(String appId) {

        ApplicationCollection applicationCollection = identityXFeign.findApplication(token,appId);

        assert applicationCollection != null;
        logger.info("App Collection :{}" , applicationCollection.getItems().length);

        return switch (applicationCollection.getItems().length) {
            case 0 -> throw new RuntimeException("Could not find an application");
            case 1 -> ApplicationRequestModel.builder()
                    .code(String.valueOf(HttpStatus.OK.value()))
                    .message(HttpStatus.OK.getReasonPhrase())
                    .data(applicationCollection.getItems()[0]).build();
            default -> throw new RuntimeException("More than one application with the same ApplicationId!!!!");
        };
    }

    @Override
    public UserRequestModel getUser(String userId) {

        UserCollection userCollection = identityXFeign.getUser(token,userId) ;
        logger.info("User Collection :{}" , userCollection.getItems().length);
        UserRequestModel userRequestModel = new UserRequestModel();

        switch (userCollection.getItems().length) {
            case 0:
                userRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
                userRequestModel.setMessage("no User with this userId: "+userId);
                return userRequestModel;
            case 1:
                userRequestModel.setData(userCollection.getItems()[0]);
                userRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
                userRequestModel.setMessage(HttpStatus.OK.getReasonPhrase());
                return userRequestModel;
            default:
                userRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
                userRequestModel.setMessage("More than one user with the same userId: "+userId);
                return userRequestModel;
        }
    }


    @Override
    public UserRequestModel createUser(String userId){

        User request = new User();
        request.setUserId(userId);

        User response = identityXFeign.createUser(token,request);

        logger.info("user response : {}",response);
        UserRequestModel userRequestModel = new UserRequestModel();
        userRequestModel.setData(response);
        userRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
        userRequestModel.setMessage(HttpStatus.OK.getReasonPhrase());

        return userRequestModel;

    }

    public AuthenticationRequest getAuthRequest(String authenticationRequestId, String token) {

        AuthenticationRequestCollection authColl  = identityXFeign.getAuthRequest(token,authenticationRequestId);

        logger.info("Authentication Request Collection :{}" , authColl.getItems().length);

        return switch (authColl.getItems().length) {
            case 0 -> throw new RuntimeException("Could not find an authentication request with the authenticationRequestId: " + authenticationRequestId);
            case 1 -> authColl.getItems()[0];
            default -> throw new RuntimeException("More than one authentication request with the same authenticationRequestId!!!!");
        };
    }

    @Override
    public OtpRequestModel createAuthRequestOTP(String userId, Application application, Policy policy, Long otpTimeAlive){

        UserRequestModel user = getUser(userId);

        AuthenticationRequest request = createAuthenticationRequest(application, user.getData(), policy, ServiceConstants.DESCRIPTION_OTP, ServiceConstants.TYPE_OTP);
        request.setExpiration(Date.from(Instant.now().plusSeconds(otpTimeAlive).plusSeconds(OTP_TIME_DELAY)));

        AuthenticationRequest response = identityXFeign.createAuthenticationRequest(token,request);

        AuthRequestOTPModel authRequestOTPModel = updateStatusAndRetriesAccordingUser(response, userId);

        OtpRequestModel otpRequestModel = new OtpRequestModel();
        otpRequestModel.setData(authRequestOTPModel);
        otpRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
        otpRequestModel.setMessage(HttpStatus.OK.getReasonPhrase());
        logger.info("otpRequestModel response : {} ", response);

        return otpRequestModel;
    }

    @Override
    public OtpRequestModel validateAuthRequestOTP(String authenticationRequestId,
                                                  String submittedAuthenticationCode, String userId) {

        AuthenticationRequest request;
        OtpRequestModel otpRequestModel = new OtpRequestModel();

        if (!"".equals(authenticationRequestId) && authenticationRequestId != null) {
            request = getAuthRequest(authenticationRequestId, token);
        } else {
            otpRequestModel.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            otpRequestModel.setMessage("Unable to find the Authentication Request: " + authenticationRequestId);
            return otpRequestModel;
        }

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setSubmittedAuthenticationCode(submittedAuthenticationCode);

        AuthenticationRequest response = identityXFeign.updateAuthenticationRequest(token,request.getId(),authenticationRequest);

        AuthRequestOTPModel authRequestOTPModel = updateStatusAndRetriesAccordingUser(response, userId);

        otpRequestModel.setData(authRequestOTPModel);
        otpRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
        otpRequestModel.setMessage(HttpStatus.OK.getReasonPhrase());
        logger.info("otpRequestModel response : {},",response);

        return otpRequestModel;
    }

    @Override
    public RegistrationModel registration(String userId, Application application ) {

        User user = getUserById(userId);

        Registration registration  = new Registration();
        registration.setRegistrationId(userId);
        registration.setUser(user);
        registration.setApplication(application);

        Registration response = identityXFeign.createRegistration(token,registration);

        RegistrationModel registrationModel = new RegistrationModel();
        registrationModel.setData(response);
        registrationModel.setCode(String.valueOf(HttpStatus.OK.value()));
        registrationModel.setMessage(HttpStatus.OK.getReasonPhrase());
        logger.info("registrationModel response: {}", response);

        return registrationModel;

    }

    @Override
    public RegistrationChallengeModel registrationChallenge(String userId, Application application, Policy policy) {

        RegistrationChallenge request = new RegistrationChallenge();

        User user = getUserById(userId);

        Registration registration;
        try {
            RegistrationCollection registrationCollection = identityXFeign.getRegistrationsByUser(token,user.getId());

            registration = Arrays.stream(registrationCollection.getItems()).filter(registrationStreamed -> {
                Application applicationStreamed = getHref(registrationStreamed.getApplication().getHref(), new ParameterizedTypeReference<Application>() {}).getBody();
                assert applicationStreamed != null;
                return  applicationStreamed.getApplicationId().equals(application.getApplicationId());
            }).toList().get(0);
            logger.info("Registration already exists, using this one");

        }catch (Exception e){
            registration  = new Registration();
            registration.setRegistrationId(userId);
            registration.setUser(user);
            registration.setApplication(application);
            logger.info("Registration not exists, creating one");
        }

        request.setRegistration(registration);
        request.setPolicy(policy);

        RegistrationChallenge response = identityXFeign.createRegistrationChallenge(token,request);

        RegistrationChallengeModel registrationChallengeModel = new RegistrationChallengeModel();
        registrationChallengeModel.setData(response);
        registrationChallengeModel.setCode(String.valueOf(HttpStatus.OK.value()));
        registrationChallengeModel.setMessage(HttpStatus.OK.getReasonPhrase());
        logger.info("registrationModel response : {}", response );

        return registrationChallengeModel;
    }

    @Override
    public RegistrationChallengeModel registrationChallengeUpdate(String regChallengeId, String fidoRegistrationResponse)  {

        RegistrationChallenge request = identityXFeign.getRegistrationChallenge(token,regChallengeId);
        request.setFidoRegistrationResponse(fidoRegistrationResponse);

        RegistrationChallenge response = identityXFeign.updateRegistrationChallenge(token,regChallengeId,request);

        RegistrationChallengeModel registrationChallengeModel = new RegistrationChallengeModel();
        registrationChallengeModel.setData(response);
        registrationChallengeModel.setCode(String.valueOf(HttpStatus.OK.value()));
        registrationChallengeModel.setMessage(HttpStatus.OK.getReasonPhrase());
        logger.info("registrationChallengeModel response : {}",response);

        return registrationChallengeModel;

    }

    public AuthenticationRequestModel createAuthRequest(String userId,
                                                        Application application,
                                                        Policy policy,
                                                        String description,
                                                        String type) {

        User user = getUserById(userId);

        AuthenticationRequest response =
                identityXFeign.createAuthenticationRequest(token,
                        createAuthenticationRequest(application,user,policy,
                                description,
                                type));

        AuthenticationRequestModel authenticationRequestModel = new AuthenticationRequestModel();
        authenticationRequestModel.setData(response);
        authenticationRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
        authenticationRequestModel.setMessage(HttpStatus.OK.getReasonPhrase());
        logger.info("Authentication Request Model Response : {}",authenticationRequestModel);

        return authenticationRequestModel;
    }

    public AuthenticationRequestModel updateAuthRequest(String authenticationRequestId,
                                                        AuthenticationRequest authenticationRequest) {

        AuthenticationRequest request;
        AuthenticationRequestModel authenticationRequestModel = new AuthenticationRequestModel();

        if (!"".equals(authenticationRequestId) && authenticationRequestId != null) {
            request = getAuthRequest(authenticationRequestId, token);
        } else {
            authenticationRequestModel.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
            authenticationRequestModel.setMessage("Unable to find the Authentication Request: " + authenticationRequestId);
            return authenticationRequestModel;
        }

        AuthenticationRequest response = identityXFeign.updateAuthenticationRequest(token,request.getId(),authenticationRequest);

        authenticationRequestModel.setData(response);
        authenticationRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
        authenticationRequestModel.setMessage(HttpStatus.OK.getReasonPhrase());
        logger.info("authenticationRequestModel response: {}",response);

        return authenticationRequestModel;
    }


    public static AuthRequestOTPDto getAuthRequestOTPDto(AuthenticationRequest request) {
        AuthRequestOTPDto response = new AuthRequestOTPDto();
        response.setAuthenticationRequestId(request.getAuthenticationRequestId());
        response.setFidoAuthenticationRequest(request.getFidoAuthenticationRequest());
        response.setAuthenticationCode(request.getAuthenticationCode());
        response.setAvailableRetries(request.getAvailableRetries());
        response.setTotalRetriesAllowed(request.getTotalRetriesAllowed());
        response.setCreated(request.getCreated());
        response.setStatus(request.getStatus().name());
        response.setExpiration(request.getExpiration());
        return response;
    }

    private AuthenticationRequest createAuthenticationRequest(Application application, User user, Policy policy, String description, String type) {
        AuthenticationRequest request = new AuthenticationRequest();
        Registration registration  = new Registration();
        registration.setRegistrationId(user.getUserId());
        registration.setUser(user);
        registration.setApplication(application);
        request.setRegistration(registration);
        request.setApplication(application);
        request.setUser(user);
        request.setPolicy(policy);
        request.setDescription(description);
        request.setType(type);
        request.setAuthenticationRequestId(UUID.randomUUID().toString());
        return request;
    }

    private User getUserById(String userId)
    {
        UserCollection userCollection = identityXFeign.getUser(token,userId) ;

        return switch (userCollection.getItems().length) {
            case 0 -> throw new RuntimeException("No exists active user with this Id ");
            case 1 -> userCollection.getItems()[0];
            default -> throw new RuntimeException("More than one user with the same UserId!!!!");
        };
    }

    public List<AuthenticatorModel> getListAuthenticators(String userId, String appId) {

        User user = getUserById(userId);

        AuthenticatorCollection response = identityXFeign.findAuthenticatorsByUserId(token,user.getId());
        logger.info("AuthenticatorCollection response: {}",response);
        List<AuthenticatorModel> authenticatorModelList = new ArrayList<>();

        for ( Authenticator authenticator : response.getItems()){
            Application application = getHref(authenticator.getApplication().getHref(), new ParameterizedTypeReference<Application>() {}).getBody();
            AuthenticatorModel authenticatorModel = getAuthenticatorModelFromAuthenticatorObject( authenticator);

            assert application != null;
            if(application.getApplicationId().equals(appId)){
                authenticatorModelList.add(authenticatorModel);
            }
        }

        return authenticatorModelList;
    }

    public AuthenticatorModel deleteAuthenticator(String authenticatorId) {
        Authenticator response = identityXFeign.archiveAuthenticator(token,authenticatorId);
        logger.info("Authenticator deleted: {}",response.getId());
        return  getAuthenticatorModelFromAuthenticatorObject(response);
    }

    private AuthenticatorModel getAuthenticatorModelFromAuthenticatorObject(Authenticator authenticator){
        RegistrationChallenge registrationChallenge = getHref(authenticator.getRegistrationChallenge().getHref(), new ParameterizedTypeReference<RegistrationChallenge>() {}).getBody();
        assert registrationChallenge != null;

        AuthenticatorModel authenticatorModel = new AuthenticatorModel();
        authenticatorModel.setAuthenticatorId(authenticator.getId());
        authenticatorModel.setDeviceCorrelationId(authenticator.getDeviceCorrelationId());
        authenticatorModel.setRegChallengeId(registrationChallenge.getId());
        authenticatorModel.setFidoDeregistrationRequest(authenticator.getFidoDeregistrationRequest());
        return authenticatorModel;
    }

    private <T> ResponseEntity<T> getHref(String url, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url,HttpMethod.GET, entity, responseType);
    }

    private AuthRequestOTPModel updateStatusAndRetriesAccordingUser(AuthenticationRequest response, String userId){

        UserRequestModel user = getUser(userId);

        Long userFails = (Objects.nonNull(user.getData().getDirectOtpFailedAttempts()))
                ? user.getData().getDirectOtpFailedAttempts()
                : 0L;
        AuthRequestOTPDto authRequestOTPDto = getAuthRequestOTPDto(response);
        long userAvailableRetries = response.getTotalRetriesAllowed()-userFails;
        AuthRequestOTPModel authRequestOTPModel = identityXMapper.mapToModel(authRequestOTPDto);
        authRequestOTPModel.setAvailableRetries(response.getTotalRetriesAllowed()-userFails);
        if(userAvailableRetries<=0){
            authRequestOTPModel.setStatus("COMPLETED_FAILURE");
        }
        return authRequestOTPModel;
    }

}