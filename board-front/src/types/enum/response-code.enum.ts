enum ResponseCode {
    // status 200
    SUCCESS = "SU",
    
    // status 400
    VALIDATION_FAILED = "VF",
    DUPLICATE_EMAIL = "DE",
    DUPLICATE_NICKNAME = "DN",
    DUPLICATE_TEL_NUMBER = "DT",
    NOT_EXISTED_USER = "NU",
    NOT_EXISTED_BOARD = "NB",
 
    // status 401
    SIGN_IN_FAIL = "SF",
    AUTHORIZATION_FALI = "AF",
 
    // status 403
    NO_PERMISSION = "NP",
 
    // status 500
    DATABASE_ERROR = "DBE",
}

export default ResponseCode;