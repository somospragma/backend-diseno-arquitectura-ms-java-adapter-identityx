package co.com.pragma.identityx.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceConstants {

    public static final String OPERATION_GENERATE_OTP = "Endpoint for generate an OTP auth request";
    public static final String OPERATION_VALIDATE_OTP = "Endpoint to validate an OTP auth request";
    public static final String OPERATION_GET_USER = "Endpoint to get an user of the system";
    public static final String OPERATION_CREATE_USER = "Endpoint to create an user in the system";
    public static final String OPERATION_CREATE_REGISTRATION = "Endpoint to register a created user with an application";
    public static final String OPERATION_CREATE_REGISTRATION_CHALLENGE = "Endpoint to link a registered user in an application with a policy";
    public static final String OPERATION_UPDATE_REGISTRATION_CHALLENGE = "Endpoint to update a registration challenge";
    public static final String OPERATION_GET_APPLICATION = "Endpoint to get new data of an application";
    public static final String OPERATION_GET_POLICY = "Endpoint to get new data of a policy";
    public static final String OPERATION_CREATE_AUTHENTICATION_REQUEST = "Endpoint to create an auth request";
    public static final String OPERATION_UPDATE_AUTHENTICATION_REQUEST = "Endpoint to update an auth request";
    public static final String OPERATION_LIST_AUTHENTICATORS_REQUEST = "Endpoint to list authenticators associated to an user and application";
    public static final String OPERATION_DELETE_AUTHENTICATORS_REQUEST = "Endpoint to delete an authenticator";

    public static final String RESPONSE_STATUS_CODE_OK = "200";
    public static final String RESPONSE_STATUS_CODE_BAD_REQUEST = "400";
    public static final String RESPONSE_STATUS_CODE_BUSINESS = "422";
    public static final String RESPONSE_STATUS_CODE_SERVER = "500";

    public static final String RESPONSE_STATUS_DESC_OK = "OK";
    public static final String RESPONSE_STATUS_DESC_BAD_REQUEST = "BAD REQUEST ERROR";
    public static final String RESPONSE_STATUS_DESC_BUSINESS = "BUSINESS ERROR";
    public static final String RESPONSE_STATUS_DESC_SERVER = "INTERNAL SERVER ERROR";

    public static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";
    public static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";
    public static final String EMPTY_STRING = "";
    public static final String UNDER_SCOPE = "\\s";
    public static final String JWT = "JWT";
    public static final String RSA = "RSA";
    public static final String RS256 = "RS256";
    public static final String TYP = "typ";
    public static final String ALG = "alg";

    //exceptions
    public static final String POLICYID_NOT_FOUND = "Could not find a policy with the policyId: ";
    public static final String DESCRIPTION_OTP = "OTP generation";
    public static final String TYPE_OTP = "OP";
    public static final String SUBJECT = "1234567890";
    public static final String CLAIM_KEY = "roles";
    public static final String CLAIM_VALUE = "JwtRole";
    public static final String USER_ID = "userId";
    public static final String REG_CHALLENGE_ID = "regChallengeId";
    public static final String FIDO_REGISTRATION_RESPONSE = "fidoRegistrationResponse";
    public static final String APPLICATION_ID = "appId";
    public static final String POLICY_ID = "policyId";
    public static final String AUTH_REQUEST_ID = "authenticationRequestId";
    public static final String AUTH_REQUEST = "authenticationRequest";
    public static final String SUBMIT_AUTH_REQUEST_CODE = "submittedAuthenticationCode";
    public static final boolean OTP_BOOLEAN = true;
    public static final String AUTHENTICATOR_ID = "authenticatorId";

    //Tenant url and paths
    public static final String IDENTITYX_HOST = "${fido.url.host}";
    public static final String IDENTITYX_TENANT = "${fido.url.tenant}";
    public static final String FIND_POLICY_PATH = "${fido.url.host.policy}";
    public static final String FIND_APPLICATION_PATH = "${fido.url.host.application}";
    public static final String FIND_AUTH_REQUEST_ID_PATH = "${fido.url.host.auth-request-id}";
    public static final String FIND_USER_PATH = "${fido.url.host.user-id}";
    public static final String USER_PATH = "${fido.url.host.user}";
    public static final String FIND_AUTH_REQUEST_PATH = "${fido.url.host.auth-request}";
    public static final String ARCHIVE_AUTH_REQUEST_PATH = "${fido.url.host.auth-request.archive}";
    public static final String FIND_REGISTRATION_REQUEST_PATH = "${fido.url.host.registration}";
    public static final String FIND_REGISTRATION_CHALLENGE_REQUEST_PATH = "${fido.url.host.registrationChallenges}";
    public static final String FIND_AUTHENTICATORS_PATH = "${fido.url.host.get.authenticators}";
    public static final String DELETE_AUTHENTICATORS_PATH = "${fido.url.host.delete.authenticators}";

    public static final String HEADER_CHANNEL_ID_REGIONAL = "channel-id";
    public static final String HEADER_APPLICATION_REGIONAL = "application";
    public static final String HEADER_TIMESTAMP_REGIONAL = "timestamp";
    public static final String HEADER_TRANSACTION_ID_REGIONAL = "transaction_id";
    public static final String HEADER_TERMINAL_ID_REGIONAL = "terminal_id";
    public static final String HEADER_TOKEN_CLIENT_ID_REGIONAL = "token-client-id";
    public static final String HEADER_TOKEN_USER_ID_REGIONAL = "token-user-id";
    public static final String HEADER_SESSION_ID_REGIONAL = "session_id";

    public static final String IDENTITYX_SECRET_NAME = "${identityx.key.secret.name}";
}
