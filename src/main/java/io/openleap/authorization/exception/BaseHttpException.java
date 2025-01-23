package io.openleap.authorization.exception;

public class BaseHttpException extends RuntimeException {
  private final int statusCode;
  private final String message;

  public BaseHttpException(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
