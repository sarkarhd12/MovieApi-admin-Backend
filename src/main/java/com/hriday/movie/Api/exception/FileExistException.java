package com.hriday.movie.Api.exception;

public class FileExistException extends RuntimeException{
    public FileExistException(String message){
        super(message);
    }
}
