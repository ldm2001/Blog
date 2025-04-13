package com.blog.board_back.common;

public interface ResponseMessage {
  
  // status 200
  String SUCCESS = "Success.";
    
  // status 400
  String VALIDATION_FAILED = "Validation failed.";
  String DUPLICATE_EMAIL = "Duplicate email.";
  String DUPLICATE_NICKNAME = "Duplicate nickname.";
  String DUPLICATE_TEL_NUMBER = "Duplicate tel number."; 
  String NOT_EXISTED_USER = "This user does not exist.";
  String NOT_EXISTED_BOARD = "This board does not exist.";

  // status 401
  String  SIGN_IN_FAIL = "Login information mismatch.";
  String AUTHORIZATION_FALI = "Authorization Failed.";

  // status 403
  String NO_PERMISSION = "Do not have permission.";

  // status 500
  String DATABASE_ERROR = "Database error.";
}
