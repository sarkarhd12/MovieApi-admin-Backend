package com.hriday.movie.Api.dto;

import java.util.List;

public record MoviepageResponse(List<MovieDto> movieDtoList,
                                Integer pageNumber,
                                Integer pageSize,
                                long totalElements,
                                int totalPages,
                                 boolean lasePage) {

}
