package com.blog.board_back.common;

public interface ResponseCode {
  
    // status 200
    String SUCCESS = "SU";
    
    // status 400
    String VALIDATION_FAILED = "VF";
    String DUPLICATE_EMAIL = "DE";
    String DUPLICATE_TEL_NUMBER = "DT"; 
    String DUPLICATE_NICKNAME = "DN";
    String NOT_EXISTED_USER = "NU";
    String NOT_EXISTED_BOARD = "NB";

    // status 401
    String  SIGN_IN_FAIL = "SF";
    String AUTHORIZATION_FALI = "AF";

    // status 403
    String NO_PERMISSION = "NP";

    // status 500
    String DATABASE_ERROR = "DBE";
    
}
