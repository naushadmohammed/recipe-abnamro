# Recipe Management Service

Recipe management API allows users to create and manage their own recipes. 
And search for recipes based on various parameters across all recipes.

## Getting Started
If you have docker setup already working, you can just run `docker-compose up` command to start the application from the root folder. It will start postgres database and application.
Once it's up and running you can go to `http://localhost:8080` to access swagger documentation.

### Technologies Used
* Java 25 as the programming language.
* Spring boot 4.0.0 as the application framework.
* Postgres to store data.
* Flyway Migration to manage database schema changes.
* Spring Data JPA with QueryDSL for search query generation.
* Spring Security & JWT to create tokens and authorize requests.
* MapStruct for mapping between domain objects and DTOs.
* GIN index for full text search.


## API Design Approach
There are two main components:

### User
- User can be created using username and password.
- User can sign in using username and password to get JWT token.
- Creating and login is straightforward. Provide username and password in the request body and you will get JWT token in the response.

### Recipe
- Logged in user can fetch, create, update and delete their own recipes. User will get error if they try to manage other user's recipes.
- They can also search for recipes based on various parameters across all recipes including one's they don't own.

### Create Recipe
POST `localhost:8080/recipe` with `Authorization: Bearer <token>` header and response body as in the example below.
I will describe only fields which are not self explanatory.

- type : It's an enum containing various recipe types. The values are : NON_VEGETARIAN, VEGETARIAN, VEGAN
- ingredients : List of ingredients which are part of the recipe. It's an object containing name, amount and unit which are all mandatory fields.
- instructions : List of instructions which are part of the recipe. If you see, there is no step number. The application automatically adds step number based on the order of instructions in the list.
- I have kept adding ingredients and instructions non mandatory while creating recipe. So, a user can create recipe without it and later they can add ingredients and instruction. This is a future improvement suggestion.
```json
{
  "name": "Chicken Tikka",
  "description": "Chicken Tikka Masala",
  "servings": 4,
  "type": "NON_VEGETARIAN",
  "ingredients": [
    {
      "name": "Chicken",
      "amount": 1.0,
      "unit": "kg"
    },
    {
      "name": "Onion",
      "amount": 1.0,
      "unit": "kg"
    },
    {
      "name": "Garlic",
      "amount": 100.0,
      "unit": "gram"
    },
    {
      "name": "Ginger",
      "amount": 100.0,
      "unit": "gram"
    }
  ],
  "instructions": [
    {
      "description": "Cut chicken and Boil"
    },
    {
      "description": "Fry onion, garlic and ginger"
    },
    {
      "description": "Add chicken"
    }
  ]
}
```
Once the recipe is successfully created, you will get a response with recipe id, createdBy and other timestamps.
```json
{
    "id": "ddb6f9bd-b231-4ea6-924a-66ce19658fbc",
    "name": "chicken1",
    "description": "string",
    "servings": 1,
    "type": "NON_VEGETARIAN",
    "ingredients": [
        {
            "name": "string",
            "amount": 1.0,
            "unit": "string"
        }
    ],
    "instructions": [
        {
            "step": 1,
            "description": "chicken"
        },
        {
            "step": 2,
            "description": "string1"
        },
        {
            "step": 3,
            "description": "string2"
        }
    ],
    "createdBy": "naushad",
    "createdAt": "2026-01-06 00:12:02",
    "updatedAt": "2026-01-06 00:12:02"
}
```

### Get Recipe
GET `localhost:8080/recipe/` with `Authorization: Bearer <token>` header. You can also add page query parameter `?page=0&size=5` to get pageable response.
- It fetches all the recipes created by the logged in user in a pageable format. Below is an example: 
```json
{
    "content": [
        {
            "id": "ddb6f9bd-b231-4ea6-924a-66ce19658fbc",
            "name": "chicken1",
            "description": "string",
            "servings": 1,
            "type": "NON_VEGETARIAN",
            "ingredients": [
                {
                    "name": "string",
                    "amount": 1.0,
                    "unit": "string"
                }
            ],
            "instructions": [
                {
                    "step": 1,
                    "description": "chicken"
                }
              ]
        }
      ],
    "page": {
        "size": 5,
        "number": 0,
        "totalElements": 2,
        "totalPages": 1
    }
}
```
### Update & Delete Recipe
PUT `localhost:8080/recipe/{recipeId}` with `Authorization: Bearer <token>` header.

DELETE `localhost:8080/recipe/{recipeId}` with `Authorization: Bearer <token>` header.
- You can update and delete only your own recipes. If you try to update or delete other user's recipe, you will get 403 forbidden error.
- If the recipe doesn't exist, you will get 404 not found error.
- The request body for update is same as create recipe request. You can also pass the created recipe response body as request body for update.
- For delete request, no body is required.

### Search Recipe
POST `localhost:8080/recipe/search` with `Authorization: Bearer <token>` header. You can find the example below. You can also add page query parameter `?page=0&size=5` to get pageable response.
```json
{
  "isVegetarian": true,
  "noOfServings": 4,
  "includingIngredients": [
    "tomato",
    "chicken"
  ],
  "excludingIngredients": [
    "potato"
  ],
  "includingInstruction": "oven"
}
```
The reason why I went ahead with `POST` is because the search parameters can become complex, if we search with alot of parameters. It is more flexible and allows for easier expansion in the future.

Below are the parameters which can be used for search:
- isVegetarian : boolean value to filter recipes based on vegetarian or non vegetarian. In vegetarian, vegan is also included
- noOfServings : number of servings the recipe serves.
- includingIngredients : comma separated list of ingredients to include in search results. It performs an exact case sensitive match. 
- excludingIngredients : comma separated list of ingredients to exclude from search results. It performs an exact case sensitive match.
- includingInstruction : text which should be present in instructions. It is not case sensitive and will match partial instruction text.

I have created indexes on all the fields which are searchable. For instruction field, I have used GIN index of postgresql.
It helps in indexing and provides faster search in full text search queries.

### Future Improvements
- I have kept adding ingredients and instructions non mandatory while creating recipe. So, a user can create recipe without it and later they can add ingredients and instruction. I have not implemented the endpoints yet.
- Implement CREATE, UPDATE, DELETE endpoints for instructions and ingredients
- For instructions DELETE endpoint, automatically move up the below instruction list by recalculating steps. We can also implement Move endpoint for instructions which we can use to reorder
- Implement basic get recipe first, without ingredients and instructions, then you can fetch details later, by calling GET recipe/{recipeId} to improve user experience.


## Swagger and health check endpoints
Once the application is up and running you can access:

Swagger UI: [http://localhost:8080](http://localhost:8080)

Health Check: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)


### Authors
* **Naushad Mohammed** 