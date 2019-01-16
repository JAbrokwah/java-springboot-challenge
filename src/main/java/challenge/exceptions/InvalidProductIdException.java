package challenge.exceptions;

public class InvalidProductIdException extends Exception {
  public InvalidProductIdException() {
    super();
  }
  public InvalidProductIdException(String m) {
    super(m);
  }
}
