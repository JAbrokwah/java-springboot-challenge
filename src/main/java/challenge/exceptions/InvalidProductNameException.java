package challenge.exceptions;

public class InvalidProductNameException extends Throwable {
  public InvalidProductNameException() { super(); }
  public InvalidProductNameException(String s) { super(s);
  }
}
