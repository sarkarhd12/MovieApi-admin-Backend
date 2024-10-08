# MovieApi-admin-Backend
Movie API

This is a RESTful API built using Spring Boot to manage movies. The API allows for operations such as adding, updating, retrieving, and deleting movie information. Additionally, it provides endpoints for file uploads (like movie posters) and paginated movie listings.
Table of Contents

    Features
    Technologies Used
    Installation
    Usage
    API Endpoints
    Project Structure
    Contributing
    License

Features

    Add, update, retrieve, and delete movie information.
    Upload and retrieve movie poster images.
    Paginate and sort movie listings.
    Exception handling for empty files during uploads.

Technologies Used

    Java: The primary programming language.
    Spring Boot: For building the RESTful API.
    Maven: For project management and dependency management.
    Jakarta Servlet API: For handling HTTP responses.

Installation

    Clone the repository:

    bash

git clone git remote add origin https://github.com/sarkarhd12/MovieApi-admin-Backend.git
cd movie-api

Build the project using Maven:

bash

mvn clean install

Run the application:

bash

    mvn spring-boot:run

The application will start running at http://localhost:8080.
Usage

You can interact with the API endpoints using tools like Postman, Curl, or any HTTP client.
API Endpoints
FileController Endpoints

    Upload a File
        URL: /file/upload
        Method: POST
        Description: Uploads a file (e.g., movie poster).
        Request Part:
            file: The file to be uploaded.
        Response:
            Success: 200 OK with the uploaded file name.

    bash

curl -X POST -F "file=@path/to/your/image.png" http://localhost:8080/file/upload

Serve a File

    URL: /file/{fileName}
    Method: GET
    Description: Retrieves a file (e.g., movie poster) by its name.
    Path Variable: fileName: The name of the file to be retrieved.
    Response: The requested file as an image.

bash

    curl -X GET http://localhost:8080/file/sample.png --output sample.png

MovieController Endpoints

    Add a Movie
        URL: /api/movie/add-movie
        Method: POST
        Description: Adds a new movie along with its poster.
        Request Parts:
            file: The movie poster.
            movieDto: The movie details in JSON format.

    bash

curl -X POST -F "file=@path/to/poster.png" -F "movieDto={...json...}" http://localhost:8080/api/movie/add-movie

Get Movie by ID

    URL: /api/movie/get-movie/{movieId}
    Method: GET
    Description: Retrieves movie details by ID.
    Path Variable: movieId: The ID of the movie.

bash

curl -X GET http://localhost:8080/api/movie/get-movie/1

Get All Movies

    URL: /api/movie/allmovies
    Method: GET
    Description: Retrieves a list of all movies.

bash

curl -X GET http://localhost:8080/api/movie/allmovies

Update a Movie

    URL: /api/movie/update/{movieId}
    Method: PUT
    Description: Updates movie details along with its poster.
    Path Variable: movieId: The ID of the movie to be updated.
    Request Parts:
        file: The new movie poster (optional).
        movieDto: The updated movie details in JSON format.

bash

curl -X PUT -F "file=@path/to/new-poster.png" -F "movieDto={...json...}" http://localhost:8080/api/movie/update/1

Delete a Movie

    URL: /api/movie/delete/{movieId}
    Method: DELETE
    Description: Deletes a movie by ID.
    Path Variable: movieId: The ID of the movie to be deleted.

bash

curl -X DELETE http://localhost:8080/api/movie/delete/1

Get Movies with Pagination

    URL: /api/movie/allmovie-pages
    Method: GET
    Description: Retrieves movies with pagination.
    Query Parameters:
        pageNumber: The page number (default is 0).
        pageSize: The number of movies per page (default is 10).

bash

curl -X GET "http://localhost:8080/api/movie/allmovie-pages?pageNumber=0&pageSize=10"

Get Movies with Pagination and Sorting

    URL: /api/movie/allmovie-pages-sorted
    Method: GET
    Description: Retrieves movies with pagination and sorting.
    Query Parameters:
        pageNumber: The page number (default is 0).
        pageSize: The number of movies per page (default is 10).
        sortBy: The field to sort by (default is "id").
        dir: The direction of sorting, either "asc" or "desc" (default is "asc").

bash

curl -X GET "http://localhost:8080/api/movie/allmovie-pages-sorted?pageNumber=0&pageSize=10&sortBy=title&dir=asc"