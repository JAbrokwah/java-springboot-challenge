package challenge.api;

import challenge.exceptions.InvalidAvailableFilterException;
import challenge.exceptions.InvalidProductIdException;
import challenge.exceptions.InvalidProductPriceException;
import challenge.models.Product;
import java.math.BigDecimal;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

@Path("/marketplace")
@Produces(MediaType.APPLICATION_JSON)
public class MarketplaceService {
  private static HashMap<String, Product> hashMap = new HashMap<>();

  public MarketplaceService() {
    // hardcoding product objects
    Product product1 = new Product("Raptors Tickets (Lower Bowl)", new BigDecimal(79.99), 150);
    Product product2 = new Product("Raptors Tickets (Upper Bowl)", new BigDecimal(149.99), 100);
    Product product3 = new Product("Raptors Foam Finger", new BigDecimal(19.99), 200);
    Product product4 = new Product("Raptors Championship Ring", new BigDecimal(1.99), 2);

    // placing products into map
    hashMap.put("1", product1);
    hashMap.put("2", product2);
    hashMap.put("3", product3);
    hashMap.put("4", product4);
  }

  @GET
  @Path("/catalogue")
  public HashMap<String, Product> displayCatalogue() {
    return hashMap;
  }

  @GET
  @Path("/info/{productId}")
  public Product getProductInfoById(@PathParam("productId") int productId) throws InvalidProductIdException {
    // retrieve Product using given ID and hashmap function
    Product result = hashMap.get(String.valueOf(productId));
    // verify a valid Product was retrieved
    if (result == null) {
      // throw exception if no Person was retrieved
      throw new InvalidProductIdException("ID provided is not associated with a Registered Product");
    }
    // return person if retrieved
    return result;
  }

  @GET
  @Path("/purchase/{productId}")
  public Response purchaseProductById(@PathParam("productId") int productId) throws InvalidProductIdException {
    // retrieve Product using given ID and hashmap function
    Product result = hashMap.get(String.valueOf(productId));
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
  public List<Product> findProduct(@QueryParam("title") String title,
      @QueryParam("priceBelow") String price,
      @QueryParam("available") String status)
      throws InvalidProductPriceException, InvalidAvailableFilterException {
    boolean checkTitle = !title.equals("");
    boolean checkPriceLimit = !price.equals("");
    boolean checkAvailableInventory = false;
    if (status.equalsIgnoreCase("true")) {
      checkAvailableInventory = true;
    } else if (!status.equalsIgnoreCase("false") && !status.equalsIgnoreCase("")) {
      throw new InvalidAvailableFilterException("Available status must be: TRUE or FALSE");
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
    for (Map.Entry<String, Product> entry : hashMap.entrySet()) {
      // check if current product should be added to query results
      boolean addCurrentProduct = true;
      Product currentProduct = entry.getValue();
      // check each parameter if given in query
      if (checkTitle) {
        addCurrentProduct = currentProduct.getTitle().equals(title);
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

}
