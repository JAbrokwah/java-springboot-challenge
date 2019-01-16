package challenge.exceptions;

public class InvalidProductPriceException extends Exception {
  public InvalidProductPriceException() {
    super();
  }
  public InvalidProductPriceException(String s) {
    super(s);
  }
}
