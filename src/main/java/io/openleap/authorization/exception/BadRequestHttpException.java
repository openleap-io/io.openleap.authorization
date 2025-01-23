package io.openleap.authorization.exception;

public class BadRequestHttpException extends BaseHttpException {

  public BadRequestHttpException(int statusCode, String message) {
    super(statusCode, message);
  }
}
