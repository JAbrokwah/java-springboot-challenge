package challenge.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
  private String title;
  private BigDecimal price;
  private int inventory;

  public Product() {

  }

  public Product(String title, BigDecimal price, int inventory) {
    this.title = title;
    this.price = price;
    this.price = this.price.setScale(2, BigDecimal.ROUND_HALF_UP);
    this.inventory = inventory;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public int getInventory() {
    return inventory;
  }

  public void setInventory(int inventory) {
    this.inventory = inventory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Product)) {
      return false;
    }
    Product product = (Product) o;
    return getTitle().equals(product.getTitle()) &&
        getPrice().equals(product.getPrice());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTitle(), getPrice());
  }
}
