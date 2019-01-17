package challenge.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map.Entry;

public class Cart {
  private HashMap<Product, Integer> productsInCart;
  private int numItems;
  private BigDecimal subtotal;

  public Cart () {
    this.productsInCart = new HashMap<>();
    this.numItems = 0;
    this.subtotal = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  public boolean addToCart(Product product, int quantity) {
    if (quantity > product.getInventory()) {
      return false;
    } else {
      // add to cart
      if (this.productsInCart.containsKey(product)) {
        int oldVal = this.productsInCart.get(product);
        if (quantity > product.getInventory() - oldVal) {
          return false;
        }
        this.productsInCart.put(product, oldVal + quantity);
      } else {
        this.productsInCart.put(product, quantity);
      }
    }
    this.subtotal = this.subtotal.add(new BigDecimal((product.getPrice().doubleValue() * quantity)).setScale(2, BigDecimal.ROUND_HALF_UP));
    this.numItems += quantity;
    return true;
  }

  public void checkout() {
    for (Object o : this.productsInCart.entrySet()) {
      Entry pair = (Entry) o;
      Product current = (Product)(pair.getKey());
      current.setInventory(current.getInventory() - ((Integer) pair.getValue()));
    }

    // clear cart
    this.productsInCart.clear();
    // reset stats
    this.numItems = 0;
    this.subtotal = BigDecimal.ZERO;
  }

  @Override
  public String toString() {
    return "CART DETAILS:\n"
        + "Num Items - " + this.numItems
        + "\n\n"
        + "Products:\n"
        + this.listProducts()
        + "\nSubtotal: $" + this.subtotal
        + "\nTotal with Tax (13% HST): $" + this.getTotalWithTax();
  }

  private String getTotalWithTax() {
    return String.valueOf(this.subtotal.multiply(BigDecimal.valueOf(1.13)).setScale(2, BigDecimal.ROUND_HALF_UP));
  }

  private String listProducts() {
    StringBuilder message = new StringBuilder();
    for (Object o : this.productsInCart.entrySet()) {
      Entry pair = (Entry) o;
      Product current = (Product)(pair.getKey());
      Integer quantity = (Integer) pair.getValue();

      message.append(current.getTitle())
          .append("\t")
          .append(quantity)
          .append(" * $")
          .append(current.getPrice())
          .append("\n");
    }
    return message.toString();
  }

}
