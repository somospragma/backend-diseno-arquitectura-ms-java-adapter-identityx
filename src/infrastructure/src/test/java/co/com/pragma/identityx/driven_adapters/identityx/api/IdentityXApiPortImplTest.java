package co.com.pragma.identityx.driven_adapters.identityx.api;

import co.com.pragma.identityx.driven_adapters.identityx.api.feign.IdentityXFeign;
import co.com.pragma.identityx.driven_adapters.identityx.mapper.IdentityXMapper;
import co.com.pragma.identityx.entry_points.rest.dto.AuthRequestOTPDto;
import co.com.pragma.identityx.model.*;
import com.daon.identityx.rest.model.def.AuthenticationRequestStatusEnum;
import com.daon.identityx.rest.model.pojo.*;
import com.identityx.clientSDK.collections.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdentityXApiPortImplTest {

    @Mock
    private IdentityXFeign identityXFeign;

    @Mock
    private IdentityXMapper identityXMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IdentityXApiPortImpl identityXApiPort;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(identityXApiPort, "token", "Token");

    }

    @Test
    void When_Generate_Token_Return_String() throws Exception {
        Date date = Date.from(Instant.now());
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // Tamaño de la clave
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        String testPrivateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getMimeEncoder().encodeToString(privateKey.getEncoded()) +
                "\n-----END PRIVATE KEY-----";
        String token = identityXApiPort.generateToken(date,testPrivateKeyPem);
        assertTrue(token.matches("[\\w-]*\\.[\\w-]*\\.[\\w-]*"));

    }

    @Test
    void When_Policy_Id_Only_Has_One_Policy_Return_Success() {
        String policyId = "testPolicyId";
        Policy policy = new Policy();
        policy.setPolicyId(policyId);

        Policy[] policyList = new Policy[1];
        policyList[0] = policy;

        PolicyCollection policyCollection = new PolicyCollection();
        policyCollection.setItems(policyList);

        when(identityXFeign.findPolicy(any(), any())).thenReturn(policyCollection);
        PolicyRequestModel result = identityXApiPort.findPolicy(policyId);
        assertEquals(policyCollection.getItems()[0], result.getData());

    }

    @Test
    void When_Policy_Id_Has_None_Policy_Return_Exception() {
        String policyId = "testPolicyId";
        Policy[] policyList = new Policy[0];

        PolicyCollection policyCollection = new PolicyCollection();
        policyCollection.setItems(policyList);

        when(identityXFeign.findPolicy(any(), any())).thenReturn(policyCollection);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->{
                    identityXApiPort.findPolicy(policyId);
                });

        assertTrue(exception.getMessage().contains("Could not find a policy with the policyId: " + policyId));
    }

    @Test
    void When_Policy_Id_Only_Has_More_Than_One_Policy_Return_Exception() {
        String policyId = "testPolicyId";
        Policy policy = new Policy();
        policy.setPolicyId(policyId);

        Policy[] policyList = new Policy[2];
        policyList[0] = policy;
        policyList[1] = policy;

        PolicyCollection policyCollection = new PolicyCollection();
        policyCollection.setItems(policyList);

        when(identityXFeign.findPolicy(any(), any())).thenReturn(policyCollection);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->{
                    identityXApiPort.findPolicy(policyId);
                });

        assertTrue(exception.getMessage().contains("More than one policy with the same policyId!!!!"));
    }

    @Test
    void When_Application_Id_Only_Has_One_Application_Return_Success() {
        String applicationId = "testApplicationId";
        Application application = new Application();
        application.setApplicationId("testApplicationId");

        Application[] applicationList = new Application[1];
        applicationList[0] = application;

        ApplicationCollection applicationCollection = new ApplicationCollection();
        applicationCollection.setItems(applicationList);

        when(identityXFeign.findApplication(any(), any())).thenReturn(applicationCollection);

        ApplicationRequestModel result = identityXApiPort.findApplication(applicationId);

        assertEquals(applicationCollection.getItems()[0], result.getData());

    }

    @Test
    void When_Application_Id_Only_Has_None_Application_Return_Exception() {
        String applicationId = "testApplicationId";
        Application[] applicationList = new Application[0];

        ApplicationCollection applicationCollection = new ApplicationCollection();
        applicationCollection.setItems(applicationList);

        when(identityXFeign.findApplication(any(), any())).thenReturn(applicationCollection);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->{
                    identityXApiPort.findApplication(applicationId);
                });

        assertTrue(exception.getMessage().contains("Could not find an application"));
    }

    @Test
    void When_Application_Id_Has_More_Than_One_Application_Return_Exception() {
        String applicationId = "testApplicationId";
        Application application = new Application();
        application.setApplicationId("testApplicationId");

        Application[] applicationList = new Application[2];
        applicationList[0] = application;
        applicationList[1] = application;

        ApplicationCollection applicationCollection = new ApplicationCollection();
        applicationCollection.setItems(applicationList);

        when(identityXFeign.findApplication(any(), any())).thenReturn(applicationCollection);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->{
                    identityXApiPort.findApplication(applicationId);
                });

        assertTrue(exception.getMessage().contains("More than one application with the same ApplicationId!!!!"));
    }

    @Test
    void When_User_Id_Only_Has_One_User_Return_Success() {
        String userId = "testUserId";
        User user = new User();
        user.setUserId("testUserId");

        User[] userList = new User[1];
        userList[0] = user;

        UserCollection userCollection = new UserCollection();
        userCollection.setItems(userList);
        UserRequestModel userRequestModel = new UserRequestModel();
        userRequestModel.setData(userCollection.getItems()[0]);
        userRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
        userRequestModel.setMessage(HttpStatus.OK.getReasonPhrase());

        when(identityXFeign.getUser(any(), any())).thenReturn(userCollection);

        UserRequestModel result = identityXApiPort.getUser(userId);

        assertEquals(userRequestModel, result);
    }

    @Test
    void When_User_Id_Has_None_User_Return_Exception() {
        String userId = "testUserId";
        User[] userList = new User[0];

        UserCollection userCollection = new UserCollection();
        userCollection.setItems(userList);
        UserRequestModel userRequestModel = new UserRequestModel();
        userRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
        userRequestModel.setMessage("no User with this userId: "+userId);

        when(identityXFeign.getUser(any(), any())).thenReturn(userCollection);

        UserRequestModel result = identityXApiPort.getUser(userId);

        assertEquals(userRequestModel, result);
    }

    @Test
    void When_User_Id_Has_More_Than_One_User_Return_Exception() {
        String userId = "testUserId";
        User user = new User();
        user.setUserId("testUserId");

        User[] userList = new User[2];
        userList[0] = user;
        userList[1] = user;

        UserCollection userCollection = new UserCollection();
        userCollection.setItems(userList);
        UserRequestModel userRequestModel = new UserRequestModel();
        userRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
        userRequestModel.setMessage("More than one user with the same userId: "+userId);

        when(identityXFeign.getUser(any(), any())).thenReturn(userCollection);

        UserRequestModel result = identityXApiPort.getUser(userId);

        assertEquals(userRequestModel, result);
    }

    @Test
    void When_User_Id_Is_Available_Create_New_User() {

        String userId = "testUserId";
        User user = new User();
        user.setUserId(userId);

        when(identityXFeign.createUser(any(), any())).thenReturn(user);

        UserRequestModel result = identityXApiPort.createUser(userId);

        assertEquals(HttpStatus.OK.value(), Integer.parseInt(result.getCode()));
        assertEquals(HttpStatus.OK.getReasonPhrase(), result.getMessage());
        assertEquals(user, result.getData());
    }

    @Test
    void When_Auth_Request_Id_Only_Has_One_Value_Return_Success() {

        String authenticationRequestId = "testRequestId";
        String token = "testToken";

        AuthenticationRequest expectedAuthRequest = new AuthenticationRequest();
        AuthenticationRequestCollection authColl = new AuthenticationRequestCollection();
        authColl.setItems(new AuthenticationRequest[]{expectedAuthRequest});

        when(identityXFeign.getAuthRequest(any(), any())).thenReturn(authColl);

        AuthenticationRequest result = identityXApiPort.getAuthRequest(authenticationRequestId, token);

        assertEquals(expectedAuthRequest, result);
    }

    @Test
    void When_Auth_Request_Id_Has_None_Value_Return_Exception() {
        String authenticationRequestId = "testRequestId";
        String token = "testToken";
        AuthenticationRequest[] authenticationRequests = new AuthenticationRequest[0];

        AuthenticationRequestCollection authenticationRequestCollection = new AuthenticationRequestCollection();
        authenticationRequestCollection.setItems(authenticationRequests);

        when(identityXFeign.getAuthRequest(any(), any())).thenReturn(authenticationRequestCollection);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->{
                    identityXApiPort.getAuthRequest(authenticationRequestId, token);
                });

        assertTrue(exception.getMessage().contains("Could not find an authentication request with the authenticationRequestId: "+ authenticationRequestId));
    }

    @Test
    void When_Auth_Request_Id_Has_More_Than_One_Value_Return_Exception() {
        String authenticationRequestId = "testRequestId";
        String token = "testToken";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setAuthenticationRequestId("testRequestId");
        AuthenticationRequest[] authenticationRequests = new AuthenticationRequest[2];
        authenticationRequests[0] = authenticationRequest;
        authenticationRequests[1] = authenticationRequest;

        AuthenticationRequestCollection authenticationRequestCollection = new AuthenticationRequestCollection();
        authenticationRequestCollection.setItems(authenticationRequests);

        when(identityXFeign.getAuthRequest(any(), any())).thenReturn(authenticationRequestCollection);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->{
                    identityXApiPort.getAuthRequest(authenticationRequestId, token);
                });

        assertTrue(exception.getMessage().contains("More than one authentication request with the same authenticationRequestId!!!!"));
    }

    @Test
    void When_Request_Auth_Request_OTP_Then_Return_OTP_Success(){

        String userId = "testUserId";
        Long otpTimeAlive  = 90L;

        String policyId = "OTPPN";
        String applicationId = "itaupn";
        User user = new User();
        user.setUserId(userId);

        User[] userList = new User[1];
        userList[0] = user;

        UserCollection userCollection = new UserCollection();
        userCollection.setItems(userList);

        Application application = new Application();
        application.setApplicationId(applicationId);

        Policy policy = new Policy();
        policy.setPolicyId(policyId);

        AuthenticationRequest authRequest = new AuthenticationRequest();
        Registration registration  = new Registration();
        registration.setRegistrationId("65451321");
        registration.setUser(user);
        registration.setApplication(application);
        authRequest.setRegistration(registration);
        authRequest.setAuthenticationRequestId("1234");
        authRequest.setFidoAuthenticationRequest("123456");
        authRequest.setAuthenticationCode("123456");
        authRequest.setAvailableRetries(1L);
        authRequest.setTotalRetriesAllowed(1L);
        authRequest.setCreated(new Date());
        authRequest.setExpiration(Date.from(Instant.now()));
        authRequest.setStatus(AuthenticationRequestStatusEnum.COMPLETED_SUCCESSFUL);
        AuthRequestOTPDto authRequestOTPDto = getAuthRequestOTPDto(authRequest);

        AuthRequestOTPModel authRequestOTPModel = new AuthRequestOTPModel("authenticationRequestId","authenticationRequestId",new Date(),2L,3L,null,new Date(),"Pending",null);

        OtpRequestModel expectedOtpRequestModel = new OtpRequestModel();
        expectedOtpRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
        expectedOtpRequestModel.setMessage(HttpStatus.OK.getReasonPhrase());
        expectedOtpRequestModel.setData(authRequestOTPModel);

        when(identityXFeign.createAuthenticationRequest(any(), any())).thenReturn(authRequest);
        when(identityXFeign.getUser(any(),any())).thenReturn(userCollection);
        when(identityXMapper.mapToModel(authRequestOTPDto)).thenReturn(expectedOtpRequestModel.getData());

        OtpRequestModel result = identityXApiPort.createAuthRequestOTP(userId, application, policy, otpTimeAlive);

        assertEquals(expectedOtpRequestModel, result);
    }

    @Test
    void When_Validate_Auth_Request_OTP_Then_Return_Success() {

        AuthenticationRequest expectedAuthRequest = new AuthenticationRequest();
        AuthenticationRequestCollection authColl = new AuthenticationRequestCollection();
        authColl.setItems(new AuthenticationRequest[]{expectedAuthRequest});

        String authenticationRequestId = "testRequestId";
        String submittedAuthenticationCode = "123456";
        String userId = "userId";

        User user = new User();
        user.setUserId(userId);
        user.setDirectOtpFailedAttempts(0L);

        User[] userList = new User[1];
        userList[0] = user;

        UserCollection userCollection = new UserCollection();
        userCollection.setItems(userList);

        AuthenticationRequest authRequest = new AuthenticationRequest();
        authRequest.setAuthenticationRequestId("1234");
        authRequest.setFidoAuthenticationRequest("123456");
        authRequest.setAuthenticationCode("123456");
        authRequest.setAvailableRetries(1L);
        authRequest.setTotalRetriesAllowed(1L);
        authRequest.setCreated(new Date());
        authRequest.setExpiration(new Date());
        authRequest.setStatus(AuthenticationRequestStatusEnum.COMPLETED_SUCCESSFUL);
        authRequest.setId("requestId");
        AuthRequestOTPDto authRequestOTPDto = getAuthRequestOTPDto(authRequest);

        AuthRequestOTPModel authRequestOTPModel = new AuthRequestOTPModel(authenticationRequestId,authenticationRequestId,new Date(),2L,3L,submittedAuthenticationCode,new Date(),"Pending",null);

        OtpRequestModel expectedOtpRequestModel = new OtpRequestModel();
        expectedOtpRequestModel.setCode(String.valueOf(HttpStatus.OK.value()));
        expectedOtpRequestModel.setMessage(HttpStatus.OK.getReasonPhrase());
        expectedOtpRequestModel.setData(authRequestOTPModel);

        when(identityXFeign.getAuthRequest(any(), any())).thenReturn(authColl);
        when(identityXFeign.getUser(any(),any())).thenReturn(userCollection);
        when(identityXFeign.updateAuthenticationRequest(any(), any(), any())).thenReturn(authRequest);
        when(identityXMapper.mapToModel(authRequestOTPDto)).thenReturn(authRequestOTPModel);

        OtpRequestModel result = identityXApiPort.validateAuthRequestOTP(authenticationRequestId, submittedAuthenticationCode,userId);

        assertEquals(expectedOtpRequestModel.getData().getAuthenticationRequestId(), result.getData().getAuthenticationRequestId());
    }

    @Test
    void When_Auth_Request_Id_Is_Invalid_Validate_Auth_Request_Not_Works() {
        String authenticationRequestId = "";
        String submittedAuthenticationCode = "123456";
        String userId = "564654";

        OtpRequestModel result = identityXApiPort.validateAuthRequestOTP(authenticationRequestId, submittedAuthenticationCode,userId);

        assertEquals(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(result.getCode()));
    }

    @Test
    void When_User_Id_And_Application_Are_Valid_Create_Registration() {

        Application application = new Application();
        application.setApplicationId("applicationId");

        User user = new User();
        user.setUserId("email@email.com");

        Registration registration = new Registration();
        registration.setApplication(application);
        registration.setUser(user);

        Registration registrationCreated = new Registration();
        registrationCreated.setApplication(application);
        registrationCreated.setUser(user);
        registrationCreated.setRegistrationId("registrationId");

        User[] userList = new User[1];
        userList[0] = user;

        UserCollection userCollection = new UserCollection();
        userCollection.setItems(userList);

        when(identityXFeign.getUser(any(),any())).thenReturn(userCollection);
        when(identityXFeign.createRegistration(any(),any())).thenReturn(registrationCreated);

        RegistrationModel result = identityXApiPort.registration("email@email.com",application);

        assertEquals(registrationCreated, result.getData());
    }

    @Test
    void When_User_Id_Application_And_Policy_Are_Valid_Create_Registration_Challenge()  {

        Application application = new Application();
        application.setApplicationId("applicationId");

        User user = new User();
        user.setUserId("email@email.com");

        String policyId = "testPolicyId";
        Policy policy = new Policy();
        policy.setPolicyId(policyId);

        Registration registration = new Registration();
        registration.setApplication(application);
        registration.setUser(user);

        RegistrationChallenge registrationChallenge = new RegistrationChallenge();
        registrationChallenge.setPolicy(policy);
        registrationChallenge.setRegistration(registration);

        User[] userList = new User[1];
        userList[0] = user;

        UserCollection userCollection = new UserCollection();
        userCollection.setItems(userList);

        when(identityXFeign.getUser(any(),any())).thenReturn(userCollection);
        when(identityXFeign.getRegistrationsByUser(any(),any())).thenReturn(new RegistrationCollection());
        when(identityXFeign.createRegistrationChallenge(any(),any())).thenReturn(registrationChallenge);

        RegistrationChallengeModel result = identityXApiPort.registrationChallenge("email@email.com",application,policy);

        assertEquals(registrationChallenge, result.getData());
    }

    @Test
    void Create_Registration_Challenge_When_Registration_Is_New()  {

        Application application = new Application();
        application.setApplicationId("applicationId");
        application.setHref("href");

        User user = new User();
        user.setUserId("email@email.com");

        String policyId = "testPolicyId";
        Policy policy = new Policy();
        policy.setPolicyId(policyId);

        Registration registration = new Registration();
        registration.setApplication(application);
        registration.setUser(user);
        Registration[] registrations = {registration};

        RegistrationCollection registrationCollection = new RegistrationCollection();
        registrationCollection.setItems(registrations);

        RegistrationChallenge registrationChallenge = new RegistrationChallenge();
        registrationChallenge.setPolicy(policy);
        registrationChallenge.setRegistration(registration);

        User[] userList = new User[1];
        userList[0] = user;

        UserCollection userCollection = new UserCollection();
        userCollection.setItems(userList);

        when(identityXFeign.getUser(any(),any())).thenReturn(userCollection);
        when(identityXFeign.getRegistrationsByUser(any(),any())).thenReturn(registrationCollection);
        when(restTemplate.exchange(eq("href"),eq(HttpMethod.GET),any(),any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(application,HttpStatus.OK));
        when(identityXFeign.createRegistrationChallenge(any(),any())).thenReturn(registrationChallenge);

        RegistrationChallengeModel result = identityXApiPort.registrationChallenge("email@email.com",application,policy);

        assertEquals(registrationChallenge, result.getData());
    }

    @Test
    void When_fidoRegistrationResponse_Is_Valid_Update_Registration_Challenge() {

        RegistrationChallenge registrationChallenge = new RegistrationChallenge();
        registrationChallenge.setFidoRegistrationResponse("fido");

        when(identityXFeign.getRegistrationChallenge(any(), any())).thenReturn(registrationChallenge);
        when(identityXFeign.updateRegistrationChallenge(any(), any(), any())).thenReturn(registrationChallenge);

        RegistrationChallengeModel result = identityXApiPort.registrationChallengeUpdate("email@email.com", "fido");

        assertEquals(registrationChallenge, result.getData());
    }


    @Test
    void When_Request_Auth_Request_Then_Return_Success() {

        String userId = "testUserId";

        String policyId = "OTPPN";
        String applicationId = "itaupn";
        User user = new User();
        user.setUserId(userId);

        User[] userList = new User[1];
        userList[0] = user;

        UserCollection userCollection = new UserCollection();
        userCollection.setItems(userList);

        AuthenticationRequest authRequest = new AuthenticationRequest();
        authRequest.setAuthenticationRequestId("1234");
        authRequest.setFidoAuthenticationRequest("123456");
        authRequest.setAuthenticationCode("123456");
        authRequest.setAvailableRetries(1L);
        authRequest.setTotalRetriesAllowed(1L);
        authRequest.setCreated(new Date());
        authRequest.setExpiration(new Date());
        authRequest.setStatus(AuthenticationRequestStatusEnum.COMPLETED_SUCCESSFUL);

        Application application = new Application();
        application.setApplicationId(applicationId);

        Policy policy = new Policy();
        policy.setPolicyId(policyId);

        when(identityXFeign.createAuthenticationRequest(any(), any())).thenReturn(authRequest);
        when(identityXFeign.getUser(any(),any())).thenReturn(userCollection);

        AuthenticationRequestModel result = identityXApiPort.createAuthRequest(userId, application, policy,"desc","type");

        assertEquals(authRequest, result.getData());
    }

    @Test
    void When_Request_Update_Auth_Request_Then_Return_Success() {

        String authRequestId = "authRequestId";
        AuthenticationRequest authRequest = new AuthenticationRequest();
        authRequest.setAuthenticationRequestId("1234");
        authRequest.setFidoAuthenticationResponse("response");

        AuthenticationRequest authRequestResponse = new AuthenticationRequest();
        authRequestResponse.setAuthenticationRequestId("1234");
        authRequestResponse.setFidoAuthenticationResponse("response");
        authRequestResponse.setAuthenticationCode("123456");
        authRequestResponse.setAvailableRetries(1L);
        authRequestResponse.setTotalRetriesAllowed(1L);
        authRequestResponse.setCreated(new Date());
        authRequestResponse.setExpiration(new Date());
        authRequestResponse.setStatus(AuthenticationRequestStatusEnum.COMPLETED_SUCCESSFUL);

        AuthenticationRequest[] authenticationRequestList = new AuthenticationRequest[1];
        authenticationRequestList[0] = authRequestResponse;

        AuthenticationRequestCollection authColl  = new AuthenticationRequestCollection();
        authColl.setItems(authenticationRequestList);

        when(identityXFeign.getAuthRequest(any(),any())).thenReturn(authColl);
        when(identityXFeign.updateAuthenticationRequest(any(), any(),any())).thenReturn(authRequestResponse);

        AuthenticationRequestModel result = identityXApiPort.updateAuthRequest(authRequestId, authRequest);

        assertEquals(authRequestResponse, result.getData());
    }

    public static AuthRequestOTPDto getAuthRequestOTPDto(AuthenticationRequest request) {
        AuthRequestOTPDto response = new AuthRequestOTPDto();
        response.setAuthenticationRequestId(request.getAuthenticationRequestId());
        response.setFidoAuthenticationRequest(request.getFidoAuthenticationRequest());
        response.setAuthenticationCode(request.getAuthenticationCode());
        response.setAvailableRetries(request.getAvailableRetries());
        response.setTotalRetriesAllowed(request.getTotalRetriesAllowed());
        response.setCreated(request.getCreated());
        response.setExpiration(request.getExpiration());
        response.setStatus(request.getStatus().name());
        return response;
    }

    @Test
    void testListAuthenticators() {
        String userId = "testUserId";
        String applicationId = "testApplicationId";

        User user = new User();
        user.setUserId(userId);
        user.setId("QWERTY");

        User[] userList = new User[1];
        userList[0] = user;

        UserCollection userCollection = new UserCollection();
        userCollection.setItems(userList);

        RegistrationChallenge registrationChallengeAuth = new RegistrationChallenge();
        registrationChallengeAuth.setHref("href");

        Application applicationAuth = new Application();
        applicationAuth.setHref("hrefApp");

        Authenticator authenticator = new Authenticator();
        authenticator.setId("testId");
        authenticator.setAuthenticatorId("testAuthenticatorId");
        authenticator.setDeviceCorrelationId("deviceId");
        authenticator.setRegistrationChallenge(registrationChallengeAuth);
        authenticator.setApplication(applicationAuth);

        Application applicationAuthInOtherApplication = new Application();
        applicationAuthInOtherApplication.setHref("hrefApp2");

        Authenticator authenticatorInOtherApplication = new Authenticator();
        authenticatorInOtherApplication.setId("testId2");
        authenticatorInOtherApplication.setAuthenticatorId("testAuthenticatorId2");
        authenticatorInOtherApplication.setDeviceCorrelationId("deviceId2");
        authenticatorInOtherApplication.setRegistrationChallenge(registrationChallengeAuth);
        authenticatorInOtherApplication.setApplication(applicationAuthInOtherApplication);

        Authenticator[] authenticatorList = {authenticator,authenticatorInOtherApplication};

        AuthenticatorCollection authenticatorCollection = new AuthenticatorCollection();
        authenticatorCollection.setItems(authenticatorList);

        RegistrationChallenge registrationChallenge = new RegistrationChallenge();
        registrationChallenge.setId("regChallengeId");

        Application application = new Application();
        application.setApplicationId(applicationId);
        Application application2 = new Application();
        application2.setApplicationId("otherApp");

        when(identityXFeign.getUser(any(), any())).thenReturn(userCollection);
        when(identityXFeign.findAuthenticatorsByUserId(any(), any())).thenReturn(authenticatorCollection);
        when(restTemplate.exchange(eq("href"),eq(HttpMethod.GET),any(),any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(registrationChallenge,HttpStatus.OK));
        when(restTemplate.exchange(eq("hrefApp"),eq(HttpMethod.GET),any(),any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(application,HttpStatus.OK));
        when(restTemplate.exchange(eq("hrefApp2"),eq(HttpMethod.GET),any(),any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(application2,HttpStatus.OK));
        List<AuthenticatorModel> result = identityXApiPort.getListAuthenticators(userId,applicationId);

        assertEquals("testId", result.get(0).getAuthenticatorId());
        assertEquals("deviceId", result.get(0).getDeviceCorrelationId());
        assertEquals("regChallengeId", result.get(0).getRegChallengeId());

    }

    @Test
    void testDeleteAuthenticators() {
        // Arrange
        String authenticatorId = "authenticatorId";
        RegistrationChallenge registrationChallengeAuth = new RegistrationChallenge();
        registrationChallengeAuth.setHref("href");

        Application applicationAuth = new Application();
        applicationAuth.setHref("hrefApp");

        Authenticator authenticator = new Authenticator();
        authenticator.setId("ID-TEST");
        authenticator.setAuthenticatorId("testAuthenticatorId");
        authenticator.setDeviceCorrelationId("deviceId");
        authenticator.setFidoDeregistrationRequest("fidoderequest");
        authenticator.setRegistrationChallenge(registrationChallengeAuth);
        authenticator.setApplication(applicationAuth);

        RegistrationChallenge registrationChallenge = new RegistrationChallenge();
        registrationChallenge.setId("regChallengeId");


        when(identityXFeign.archiveAuthenticator("Token",authenticatorId)).thenReturn(authenticator);
        when(restTemplate.exchange(eq("href"),eq(HttpMethod.GET),any(),any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(registrationChallenge,HttpStatus.OK));
        // Act
        identityXApiPort.deleteAuthenticator(authenticatorId);

        verify(identityXFeign, times(1)).archiveAuthenticator("Token",authenticatorId);

    }

}