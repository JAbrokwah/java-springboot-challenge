package challenge.api;

import challenge.exceptions.InvalidAvailableFilterException;
import challenge.exceptions.InvalidInventoryException;
import challenge.exceptions.InvalidPriceException;
import challenge.exceptions.InvalidProductIdException;
import challenge.exceptions.InvalidProductNameException;
import challenge.exceptions.InvalidProductPriceException;
import challenge.exceptions.InvalidQuantityChosenException;
import challenge.models.Cart;
import challenge.models.Product;
import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.web.bind.annotation.RequestBody;

@Path("/marketplace")
@Produces(MediaType.APPLICATION_JSON)
public class MarketplaceService {
  private static HashMap<String, Product> products = new HashMap<>();
  private int numOfProducts() {
    return products.size();
  }
  private Cart cart = new Cart();

  public MarketplaceService() {
    // hardcoding product objects
    Product product1 = new Product("Raptors Tickets (Lower Bowl)", new BigDecimal(79.99), 150);
    Product product2 = new Product("Raptors Tickets (Upper Bowl)", new BigDecimal(149.99), 100);
    Product product3 = new Product("Raptors Foam Finger", new BigDecimal(19.99), 200);
    Product product4 = new Product("Raptors Championship Ring", new BigDecimal(1.99), 2);

    // placing products into map
    products.put("1", product1);
    products.put("2", product2);
    products.put("3", product3);
    products.put("4", product4);
  }

  @POST
  @Path("/add")
  public Response addProductToCatalogue(@RequestBody Product product)
      throws InvalidInventoryException, InvalidPriceException, InvalidProductNameException {
    int inventory = product.getInventory();
    BigDecimal price = product.getPrice();
    String title = product.getTitle();

    if (inventory < 0) {
      throw new InvalidInventoryException("Inventory value is invalid. Submit an inventory >= 0");
    }
    if (price.floatValue() < 0) {
      throw new InvalidPriceException("Price value is invalid. Submit a price >= $0");
    }
    if (title.length() == 0) {
      throw new InvalidProductNameException("Name of product is empty. Please input a name.");
    }

    JsonObject reason = new JsonObject();
    if (products.containsValue(product)) {
      reason.addProperty("Message", "This Product already exists!");
      return Response.status(409, "This Product already exists!").entity(reason.toString()).build();
    }
    reason.addProperty("Message", "Product successfully added!");
    products.put(String.valueOf(this.numOfProducts() + 1), product);

    return Response.status(201, "Product successfully added!").entity(reason.toString()).build();
  }

  @PUT
  @Path("/restock")
  public Response restockProduct(@RequestBody Product product)
      throws InvalidInventoryException, InvalidPriceException, InvalidProductNameException {
    int inventory = product.getInventory();
    BigDecimal price = product.getPrice();
    String title = product.getTitle();

    if (inventory < 0) {
      throw new InvalidInventoryException("Inventory value is invalid. Submit an inventory >= 0");
    }
    if (price.floatValue() < 0) {
      throw new InvalidPriceException("Price value is invalid. Submit a price >= $0");
    }
    if (title.length() == 0) {
      throw new InvalidProductNameException("Name of product is empty. Please input a name.");
    }

    JsonObject reason = new JsonObject();

    if (products.containsValue(product)) {
      String key = null;

      for (Entry<String, Product> entry : products.entrySet()) {
        if (entry.getValue().equals(product)) {
          key = entry.getKey();
        }
      }

      Product match = products.get(key);
      match.setInventory(match.getInventory() + inventory);

      reason.addProperty("Message", inventory + " units of " + title + " were added.");

      return Response.ok().entity(reason.toString()).build();
    } else {
      reason.addProperty("Message", "This Product does not exist!");
      return Response.status(404, "This Product does not exist!").entity(reason.toString()).build();
    }
  }

  @GET
  @Path("/catalogue")
  public HashMap<String, Product> displayCatalogue() {
    return products;
  }

  @GET
  @Path("/info/{productId}")
  public Product getProductInfoById(@PathParam("productId") int productId) throws InvalidProductIdException {
    // retrieve Product using given ID and hashmap function
    Product result = products.get(String.valueOf(productId));
    // verify a valid Product was retrieved
    if (result == null) {
      // throw exception if no Person was retrieved
      throw new InvalidProductIdException("ID provided is not associated with a Registered Product");
    }
    // return product if retrieved
    return new Product(result.getTitle(), result.getPrice(), result.getInventory());
  }

  @POST
  @Path("/purchase/{productId}")
  public Response purchaseProductById(@PathParam("productId") int productId) throws InvalidProductIdException {
    // retrieve Product using given ID and hashmap function
    Product result = products.get(String.valueOf(productId));
    // verify a valid Product was retrieved
    if (result == null) {
      // throw exception if no Person was retrieved
      throw new InvalidProductIdException("ID provided is not associated with a Registered Product");
    }
    if (result.getInventory() == 0) {
      return Response.status(404, "This product is out of stock!").entity(result).build();
    }
    result.setInventory(result.getInventory() - 1);
    return Response.status(200, "Successful Purchase!").entity(result).build();
  }

  @GET
  @Path("/search")
  public List<Product> findProduct(@QueryParam("productId") String id,
      @QueryParam("priceBelow") String price,
      @QueryParam("available") String status)
      throws InvalidProductPriceException, InvalidAvailableFilterException {
    boolean checkId = !id.equals("");
    boolean checkPriceLimit = !price.equals("");
    boolean checkAvailableInventory = !(status == null);
    if (checkAvailableInventory)  {
      if (!status.equalsIgnoreCase("true") && !status.equalsIgnoreCase("")) {
        throw new InvalidAvailableFilterException("Available status must be: TRUE or FALSE");
      } else {
        checkAvailableInventory = status.equalsIgnoreCase("true");
      }
    }

    BigDecimal priceLimit = null;
    if (checkPriceLimit) {
      try {
        priceLimit = new BigDecimal(price);
      } catch (NumberFormatException e) {
        throw new InvalidProductPriceException("Price limit given is invalid!");
      }
    }


    List<Product> results = new ArrayList<>();
    for (Map.Entry<String, Product> entry : products.entrySet()) {
      // check if current product should be added to query results
      boolean addCurrentProduct = true;
      Product currentProduct = entry.getValue();
      // check each parameter if given in query
      if (checkId) {
        addCurrentProduct = entry.getKey().equals(id);
      }
      if (checkPriceLimit) {
        addCurrentProduct = currentProduct.getPrice().doubleValue() < priceLimit.doubleValue();
      }
      if (checkAvailableInventory) {
        addCurrentProduct = currentProduct.getInventory() > 0;
      }
      if (addCurrentProduct) {
        results.add(currentProduct);
      }
    }
    return results;
  }

  @POST
  @Path("/addToCart")
  public Response addToCart(@QueryParam("productId") String productId,
                        @QueryParam("quantity")  String quantity)
      throws InvalidProductIdException, InvalidQuantityChosenException {
    if (productId.isEmpty()) {
      throw new InvalidProductIdException("No Product ID was provided!");
    }
    // retrieve Product using given ID and hashmap function
    Product product = products.get(productId);
    // verify a valid Product was retrieved
    if (product == null) {
      // throw exception if no Person was retrieved
      throw new InvalidProductIdException("ID provided is not associated with a Registered Product");
    }

    int number;
    if (quantity.isEmpty()) {
      number = 1;
    } else {
      try {
        number = Integer.parseInt(quantity);
      } catch (NumberFormatException e) {
        throw new InvalidQuantityChosenException("Value given for desired quantity is invalid!");
      }
    }
    boolean result = this.cart.addToCart(product, number);

    if (!result) {
      throw new InvalidQuantityChosenException("Quantity of this product is too high");
    }

    product.setInventory(product.getInventory() - number);
    return Response.ok(product).build();
  }

  @GET
  @Path("/cart")
  public Response displayCart() {
    return Response.ok(this.cart.display().toString()).build();
  }

  @POST
  @Path("/checkout")
  public Response checkOut() {
    String receipt = this.cart.checkout();
    return Response.ok(receipt).build();
  }

}
