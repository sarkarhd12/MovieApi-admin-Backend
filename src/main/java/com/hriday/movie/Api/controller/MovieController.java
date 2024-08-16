package com.hriday.movie.Api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hriday.movie.Api.Utils.AppContrants;
import com.hriday.movie.Api.dto.MovieDto;
import com.hriday.movie.Api.dto.MoviepageResponse;
import com.hriday.movie.Api.exception.EmptyFileException;
import com.hriday.movie.Api.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/movie")
public class MovieController {
    @Autowired
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovieController(
           @RequestPart MultipartFile file,
           @RequestPart String movieDto
           ) throws IOException {
        if(file.isEmpty()){
            throw new EmptyFileException("Please throw another file");
        }
        MovieDto dto = convertMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto,file), HttpStatus.CREATED);
    }

    @GetMapping("/get-movie/{movieId}")
    public ResponseEntity<MovieDto> getMovieController(
            @PathVariable Long movieId
    ) throws IOException {
        return new ResponseEntity<>(movieService.getMovie(movieId), HttpStatus.OK);
    }

    @GetMapping("/allmovies")
    public ResponseEntity<List<MovieDto>> getAllMovieConroller(){
        List<MovieDto> movieDtoList = movieService.getAllMovies();
        return new ResponseEntity<>(movieDtoList,HttpStatus.OK);
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<MovieDto> updateMovieController(
            @PathVariable Long movieId,
            @RequestPart MultipartFile file,
            @RequestPart String movieDto
    ) throws IOException {
        if(file.isEmpty()) file=null;
        MovieDto dto = convertMovieDto(movieDto);
        return new ResponseEntity<>(movieService.updateMovie(movieId,dto,file), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovieConroller(
            @PathVariable Long movieId
    ) throws IOException {
        return new ResponseEntity<>(movieService.deletMovie(movieId),HttpStatus.OK);
    }

    @GetMapping("/allmovie-pages")
    public ResponseEntity<MoviepageResponse> getMoviesWithPagination(
            @RequestParam(defaultValue = AppContrants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppContrants.PAGE_SIZE, required = false) Integer pageSize
            ){
   return ResponseEntity.ok(movieService.getAllMovieWithPagination(pageNumber,pageSize));
    }

    @GetMapping("/allmovie-pages-sorted")
    public ResponseEntity<MoviepageResponse> getMoviesWithPaginationAndSorted(
            @RequestParam(defaultValue = AppContrants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = AppContrants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppContrants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppContrants.SORT_DIR, required = false) String dir
            ){
        return ResponseEntity.ok(movieService.getAllMovieWithPaginationAndSorting(pageNumber,pageSize,sortBy,dir));
    }


    private MovieDto convertMovieDto(String movieDtoObj) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(movieDtoObj, MovieDto.class);
    }

}
