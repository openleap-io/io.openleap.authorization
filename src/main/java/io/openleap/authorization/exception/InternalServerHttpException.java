package io.openleap.authorization.exception;

public class InternalServerHttpException extends BaseHttpException {

  public InternalServerHttpException(int statusCode, String message) {
    super(statusCode, message);
  }
}
