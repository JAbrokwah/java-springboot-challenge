# Java SpringBoot Example
This repository contains the backend implementation of a store where the user can act as a customer and as an admin.
 
Java was used as the programming language, and Apache CXF and Spring Boot to get the server running. This is a RESTful server that works through URL's. For best use, one can use an HTTP tool like [Postman](https://www.getpostman.com/ "Postman"). Different HTTP methods (GET, POST, PUT) are used to showcase when to use each method, and different status codes for the possible scenarios in each resource.

To start the server, you can open the project in your IDE and run the main method in [ChallengeApplication.java](https://github.com/JAbrokwah/java-springboot-challenge/blob/master/src/main/java/challenge/ChallengeApplication.java). Another way to start it up, (if Maven is installed) is to open the terminal in the top level project folder, and enter: `mvn spring-boot:run`.

Once starting the server using either method, you should see some output on whichever console is opened for you. The last line should read:
`Started ChallengeApplication in _ seconds (JVM running for _)` where the underscores signify the number of seconds for each time.

This signifies that the server started successfully, then you are ready to try it out! If not, then you may have to do some troubleshooting first (Check error messages and act accordingly). Common issues are dependency conflicts or something already running on port 8080.

The marketplace is set up with a few hardcoded Products. They are set initially every time the server is started up with a specified inventory. Check the Service constructor for the initial products: [Constructor](https://github.com/JAbrokwah/java-springboot-challenge/blob/74f3c28d5d3f544c3eeefcdb37e1538c244a5aa1/src/main/java/challenge/api/MarketplaceService.java#L30)

The marketplace can now be used. The base url for it is: `localhost:8080/services/marketplace`.
The functions that can be used are:

| Function URL | Function Description | Request Type |
| -------- | ------ | :------------: |
| `/catalogue` | Displays all products available in the store, and their details (title, price, quantity available). This is represented as a map from and integer (product ID) to product.| GET|
| `/cart` | Displays details about user's cart (price & quantity of products in cart, total cost) | GET |
| `/info/{productId}` | Displays details about product with associated ID _(productId)_ in URL, throws error if _productID_ does not match an ID in the product list. | GET |
| `/search?productId=_&priceBelow=_&available=_` | Queries the objects based on 3 filters: product ID, maximum price, and if the product is available. The underscores represent values for each filters or empty strings (simply remove the empty string). Leaving a parameter blank does not apply it to the search. Errors are thrown if invalid values are given for any parameter. | GET |
| `/addToCart?productId=_&quantity=_` | Adds the product (associated to productId query parameter) to the cart with the given quantity (iff that quantity is available in stock). The underscores represent values for each filters or empty strings (simply remove the empty string). Throws an error if the productId is invalid or the quantity desired is invalid (too high). A response shows the user what is left of that product after moving their items to the cart.| POST | 
| `/checkout` | Checks out all items from the cart, therefore removing the carted quantity from the catalogue, then resetting the cart to empty. The user's receipt is returned in the response.| POST |
| `/purchase/{productId}` | This is a bonus feature I added, sort of like a "Quick Purchase" action. It instantly 'buys' the given product, decrementing the products quantity by 1 in the catalogue. An error is thrown if the productId given is invalid, or the product is out of stock. | POST |
|`/add`| Adds new items to catalogue by including price, title, inventory using JSON in the request body. Doesn't work if item provided is in catalogue. This feature returns a **201** if creation is successful, or a **409** if the item is already in the catalogue.|POST|
|`/restock`| Restocks item in catalogue by including price, title, inventory using JSON in the request body. Increases inventory by the inventory provided. Doesn't work if item provided is not in catalogue. A **200** is returned if the update is successful and a **404** if the product isn't in the catalogue.|PUT|

To use Postman to test, the following collection of requests may be used: [![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/8a64d629a28c16c8e6ba)

Happy Shopping!

### Possible Future Work
- Include authentication & authorization to differentiate between customer and admin
    - Allow specific roles to access API's specific to them
- Develop a Front End for UX aspect
- Implement a 'Remove from Cart' feature