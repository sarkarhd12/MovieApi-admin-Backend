package com.hriday.movie.Api.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    private Long movieId;


    @NotBlank(message = "pleaase provide movie name")
    private String title;


    @NotBlank(message = "pleaase provide director name")
    private String director;

    @Column(nullable = false)
    @NotBlank(message = "pleaase provide studio name")
    private String studio;

    private Set<String> movieCast;


    private Integer releaseYear;


    @NotBlank(message = "pleaase provide movie's poster")
    private String poster;

    @NotBlank(message = "pleaase provide poster url")
    private String posterurl;

}