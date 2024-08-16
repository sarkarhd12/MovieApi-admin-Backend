package com.hriday.movie.Api.service;

import com.hriday.movie.Api.dto.MovieDto;
import com.hriday.movie.Api.dto.MoviepageResponse;
import com.hriday.movie.Api.entity.Movie;
import com.hriday.movie.Api.exception.MovieNotFoundException;
import com.hriday.movie.Api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private final MovieRepository movieRepository;

    private String basUrl = "http://localhost:8080";
    @Autowired
    private FileService fileService;

    @Value("${file.upload-dir}")
    private String path;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        // Upload the file
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileAlreadyExistsException("File already exist");
        }
        String uploadedFileName = fileService.uploadFile(path, file);
        movieDto.setPoster(uploadedFileName);

        //map dto to mivie;
        Movie movie = new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        //save the movie obbject
        Movie saveMovie = movieRepository.save(movie);

        //generate the postal url
        String postUrl = basUrl + "/file/" + uploadedFileName;

        MovieDto response = new MovieDto(
                null,
                saveMovie.getTitle(),
                saveMovie.getDirector(),
                saveMovie.getStudio(),
                saveMovie.getMovieCast(),
                saveMovie.getReleaseYear(),
                saveMovie.getPoster(),
                postUrl
        );
        return response;
    }

    @Override
    public MovieDto getMovie(Long movieId) {
        //find movie by movie id
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        //generat the poter url
        String postUrl = basUrl + "/file/" + movie.getPoster();

        //map to movieDto and return it
        MovieDto movieDto = new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),

                movie.getMovieCast(),

                movie.getReleaseYear(),
                movie.getPoster(),
                postUrl
        );

        return movieDto;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        //get All data from db
        List<Movie> movies = movieRepository.findAll();

        List<MovieDto> movieDtoList = new ArrayList<>();
        //iterate through list and generate posterUrl then map to MovieDto
        for (Movie movie : movies) {
            String postUrl = basUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    postUrl
            );
            movieDtoList.add(movieDto);
        }
        return movieDtoList;
    }

    @Override
    public MovieDto updateMovie(Long movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        Movie mv = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));


        String existingFile = mv.getPoster();

        if (file != null && !file.isEmpty()) {
            // Ensure the path is correctly formatted
            Path existingFilePath = Paths.get(path + File.separator + existingFile);
            System.out.println(existingFilePath.toString());
            if (Files.exists(existingFilePath)) {
                // Attempt to delete the existing file
                try {
                    Files.delete(existingFilePath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete the existing file: " + existingFile, e);
                }
            }
            // Upload the new file
            existingFile = fileService.uploadFile(path, file);
        }
//        if(file!=null && !file.isEmpty()){
//            Files.deleteIfExists(Paths.get(path+File.separator+existingFile));
//             existingFile =  fileService.uploadFile(path,file);
//        }
        movieDto.setPoster(existingFile);

        Movie movie = new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        Movie saveMovie = movieRepository.save(movie);

        //generate the postal url
        String postUrl = basUrl + "/file/" + existingFile;

        MovieDto response = new MovieDto(
                saveMovie.getMovieId(),
                saveMovie.getTitle(),
                saveMovie.getDirector(),
                saveMovie.getStudio(),
                saveMovie.getMovieCast(),
                saveMovie.getReleaseYear(),
                saveMovie.getPoster(),
                postUrl
        );

        return response;
    }

    @Override
    public String deletMovie(Long movieId) throws IOException {
        Movie mv = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));

        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));

        movieRepository.delete(mv);

        return "Movie dletetd with id " + mv.getMovieId();
    }

    @Override
    public MoviepageResponse getAllMovieWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Movie> moviePage = movieRepository.findAll(pageable);
        List<Movie> movies = moviePage.getContent();
        List<MovieDto> movieDtoList = new ArrayList<>();
        //iterate through list and generate posterUrl then map to MovieDto
        for (Movie movie : movies) {
            String postUrl = basUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    postUrl
            );
            movieDtoList.add(movieDto);
        }

        return new MoviepageResponse(movieDtoList, pageNumber, pageSize, moviePage.getTotalElements(), moviePage.getTotalPages(),
                moviePage.isLast());
    }

    @Override
    public MoviepageResponse getAllMovieWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {

        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Movie> moviePage = movieRepository.findAll(pageable);
        List<Movie> movies = moviePage.getContent();
        List<MovieDto> movieDtoList = new ArrayList<>();
        //iterate through list and generate posterUrl then map to MovieDto
        for (Movie movie : movies) {
            String postUrl = basUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    postUrl
            );
            movieDtoList.add(movieDto);
        }

        return new MoviepageResponse(movieDtoList, pageNumber, pageSize, moviePage.getTotalElements(), moviePage.getTotalPages(),
                moviePage.isLast());
    }


}
