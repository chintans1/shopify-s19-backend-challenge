# Summer 2019 Shopify Developer Challenge
#### This project is deployed here: [add later]()

## API Descriptions
### Products (`/products`)
Endpoints for fetching all products, fetching in-stock products or fetching specific products using title or ID.
#### View all products
Returns all the products that are currently present in the marketplace. 
If the query param `showInStockOnly` is specified and is true, only in-stock products are returned.

**Endpoint:** `GET: /products`  
**Query Params:**
- `showInStockOnly`
    - type: `boolean`
    - required: `false`
    - default: `false`

**Sample Response:**
```json
[
  {
    "productId": 0,
    "title": "string",
    "price": 0.00,
    "inventoryCount": 0
  }
]
```
#### View products by title
Returns all products present in the marketplace that match the product title you specify.

**Endpoint:** `GET: /products?productTitle=`  
**Query Params:**
- `productTitle`
    - type: `string`
    - required: `false`
    - default: `N/A`

**Sample Response:**
```json
[
  {
    "productId": 0,
    "title": "string",
    "price": 0.00,
    "inventoryCount": 0
  }
]
```
#### View single product
Return details for a single product using the given product ID. 
An exception is thrown if the product is not present in the marketplace.

**Endpoint:** `GET: /products/{productId}`

**Sample Response:**
```json
{
  "productId": 0,
  "title": "string",
  "price": 0.00,
  "inventoryCount": 0
}
```

### Cart (`/carts`)
Endpoints for creating new carts, adding products to a cart and completing purchases.
#### View existing cart
Returns the cart details that includes a list of products and totalCost for the given cart.
An exception is thrown if the cart is not found in the database.

**Endpoint:** `GET: /carts/{cartId}`

**Sample Response:**
```json
{
  "cartId": 0,
  "products": [
    {
      "productId": 0,
      "title": "string",
      "price": 0.00,
      "inventoryCount": 0
    }
  ],
  "totalCost": 0.00
}
```
#### Create a new cart
Returns a newly created cart. If the list of productIDs were passed in, the cart returned will reflect that.

**NOTE**: The request body can be left empty. An empty body will just return a new empty cart.

**Endpoint:** `POST: /carts`  
**Sample Request Body:**
```json 
{
  "productIds": [0, 1]
}
```
**Sample Response:**
```json
{
  "cartId": 0,
  "products": [
    {
      "productId": 0,
      "title": "string",
      "price": 0.00,
      "inventoryCount": 0
    },
    {
      "productId": 1,
      "title": "string",
      "price": 0.00,
      "inventoryCount": 0
     },
  ],
  "totalCost": 0.00
}
```
#### Add product to existing cart
Returns the updated cart details where the product you specified to be added is present and the total cost reflects those changes. 
An exception is thrown if the cart is not present or if the product is not present or the product is already present in the cart.

**Endpoint:** `PUT: /carts/{cartId}/products/{productId}`  

**Sample Response:**
```json
{
  "cartId": 0,
  "products": [
    {
      "productId": 0,
      "title": "string",
      "price": 0.00,
      "inventoryCount": 0
    }
  ],
  "totalCost": 0.00
}
```
#### Complete a purchase for a cart
This will complete the purchase of the products that are in the cart. The product inventory will be updated and the cart will get subsequently deleted. 
An exception is thrown if the cart is not present in the database or one of the products is out of stock.

**Endpoint**: `PUT: /carts/{cartId}/complete`

**Sample Response:**
No response is sent if it was success. A status code of `200` denotes success.

## Local Development
#### Requirements
- Docker
- Lombok

#### Instructions
With Docker, you can get this application running very easily locally.
Run the following commands in the root directory of this project to build the Docker image that you can run after locally.

`docker build -t marketplace:dev .`  
`docker run -p 8080:8080 marketplace:dev`

This will start the application and make it accessible at [localhost:8080](http://localhost:8080).

If you wanted to import this project into an IDE like IntelliJ, I suggest to install the Lombok plugin beforehand to avoid 
compilation failures. Read more about Lombok [here](https://projectlombok.org/).

## Project Background
For this project, I used Java and the framework Spring Boot as I felt the development will go faster this way. I am also using an in-memory database for lower complexity but this can easily be changed to use a database like Postgres by changing the dependencies in `build.gradle` and configuring a couple application properties.

You may see that in-code comments may be missing but I try not to explain all my code as it should be readable on its own but I will try to clarify anything that seems a bit complex so everyone can understand it easily.

I am using Spring to preload the database on application start with products so it can be added to new carts and/or queried. You can find the script where I load the database [here](/src/main/resources/data.sql).

## Points of Improvement
- Allow the creation of products instead of solely relying on the initial load of products
- Introduce an authentication system where each user gets one shopping cart to add their products to and complete their purchase
- Allow for the removal of products from a cart once added
- Integrate the project with [Swagger UI](http://springfox.github.io/springfox/) for easy access to the endpoints to help in the development process