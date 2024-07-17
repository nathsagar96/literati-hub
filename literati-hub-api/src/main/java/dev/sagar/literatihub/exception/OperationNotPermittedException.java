package dev.sagar.literatihub.exception;

public class OperationNotPermittedException extends RuntimeException {

  public OperationNotPermittedException(String message) {
    super(message);
  }
}
