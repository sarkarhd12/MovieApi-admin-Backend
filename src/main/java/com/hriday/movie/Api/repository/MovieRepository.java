package com.hriday.movie.Api.repository;


import com.hriday.movie.Api.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long>{
}
