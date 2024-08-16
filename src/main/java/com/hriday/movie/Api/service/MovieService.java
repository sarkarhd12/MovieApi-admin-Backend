package com.hriday.movie.Api.service;

import com.hriday.movie.Api.dto.MovieDto;
import com.hriday.movie.Api.dto.MoviepageResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface MovieService {
    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;

    MovieDto getMovie(Long movieId);

    List<MovieDto> getAllMovies();

    MovieDto updateMovie(Long movieId,MovieDto movieDto, MultipartFile file) throws IOException;

    String deletMovie(Long movieId) throws IOException;

    MoviepageResponse getAllMovieWithPagination(Integer pageNumber, Integer pageSize);

    //SortBy means the field we have to provide and the dir means the direction we want to be sorted ascending or decending
    MoviepageResponse getAllMovieWithPaginationAndSorting(Integer pageNumber, Integer pageSize,String sortBy, String dir);

}
