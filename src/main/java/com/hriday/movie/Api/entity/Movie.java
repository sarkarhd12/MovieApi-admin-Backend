package com.hriday.movie.Api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "movie")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "pleaase provide movie name")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "pleaase provide director name")
    private String director;

    @Column(nullable = false)
    @NotBlank(message = "pleaase provide studio name")
    private String studio;

    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    @Column(nullable = false, name = "release_year")
    @NotNull(message = "pleaase provide release year")
    private Integer releaseYear;

    @Column(nullable = false, name = "poster")
    @NotBlank(message = "pleaase provide movie's poster")
    private String poster;

}
