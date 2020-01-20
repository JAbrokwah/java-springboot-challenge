package challenge.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
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
        if (quantity > product.getInventory()) {
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

  public String checkout() {
    JsonObject receipt = new JsonObject();
    receipt.addProperty("Products Sold", this.numItems);
    receipt.addProperty("Subtotal", this.subtotal);
    receipt.addProperty("Total Paid", this.getTotalWithTax());

    // clear cart
    this.productsInCart.clear();
    // reset stats
    this.numItems = 0;
    this.subtotal = BigDecimal.ZERO;
    return receipt.toString();
  }

  public JsonObject display() {
    JsonObject cart = new JsonObject();
    cart.addProperty("Subtotal", this.subtotal);
    cart.addProperty("Total with Tax (13% HST)", this.getTotalWithTax());

    JsonArray products = new JsonArray();

    for (Map.Entry<Product, Integer> entry : this.productsInCart.entrySet()) {
      Product prod = entry.getKey();
      Integer count = entry.getValue();

      JsonObject current = new JsonObject();
      current.addProperty("Product", prod.getTitle());
      current.addProperty("Price", prod.getPrice());
      current.addProperty("Count", count);

      products.add(current);
    }

    cart.add("Products", products);

    return cart;
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
