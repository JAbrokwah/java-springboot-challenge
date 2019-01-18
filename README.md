# Shopify Summer 2019 Developer Intern Challenge
My name is Justin Abrokwah and I'm applying to be a Backend Developer Intern at Shopify!

This project is my take on the challenge you provided. I used Java as my programming language, and Apache CXF and Spring Boot to get the server running. This is a RESTful server that works through URL's. For best use, one can use an HTTP too like [Postman](https://www.getpostman.com/ "Postman").

To start the server, you can open the project in your IDE and run the main method in [ChallengeApplication.java](https://github.com/JAbrokwah/shopify-s19-challenge/blob/master/src/main/java/challenge/ChallengeApplication.java). Another way to start it up, (if Maven is installed) is to open the terminal in the top level project folder, and enter: `mvn spring-boot:run`.

Once starting the server using either method, you should see some output on whichever console is opened for you. The last line should read:
`Started ChallengeApplication in _ seconds (JVM running for _)` where the underscores signify the number of seconds for each time.

This signifies that the server started successfully, then you are ready to try it out! If not, then you may have to do some troubleshooting first (Check error messages and act accordingly). Common issues are dependency conflicts or something already running on port 8080.

The marketplace is set up with a few hardcoded Products. They are set initially every time the server is started up.

The marketplace can now be used. The base url for it is: `localhost:8080/services/marketplace`.
The functions that can be used are:

| Function URL | Function Description | Request Type |
| -------- | ------ | :------------: |
| `/catalogue` | Displays all products available in the store, and their details (title, price, quantity available). This is represented as a map from and integer (product ID) to product.| GET|
| `/cart` | Displays details about user's cart (price & quantity of products in cart, total cost) | GET |
| `/info/{productId}` | Displays details about product with associated ID _(productId)_ in URL, throws error if _productID_ does not match an ID in the product list. | GET |
| `/search?productId=_&priceBelow=_&available=_` | Queries the objects based on 3 filters: product ID, maximum price, and if the product is available. The underscores represent values for each filters or empty strings (simply remove the empty string). Leaving a parameter blank does not apply it to the search. Errors are thrown if invalid values are given for any parameter. | GET |
| `/addToCart?productId=_&quantity=_` | Adds the product (associated to productId query parameter) to the cart with the given quantity (iff that quantity is available in stock). The underscores represent values for each filters or empty strings (simply remove the empty string). Throws an error if the productId is invalid or the quantity desired is invalid (too high).| POST | 
| `/checkout` | Checks out all items from the cart, therefore removing the carted quantity from the catalogue, then resetting the cart to empty.| POST |
| `/purchase/{productId}` | This is a bonus feature I added, sort of like a "Quick Purchase" action. It instantly 'buys' the given product, decrementing the products quantity by 1 in the catalogue. An error is thrown if the productId given is invalid, or the product is out of stock. | POST |

These are the core actions of the marketplace service. Shop away! 