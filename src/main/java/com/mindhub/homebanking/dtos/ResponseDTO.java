package com.mindhub.homebanking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {

    private Integer code;

    private String message;

    private T data;

    public static <T> ResponseDTO<T> success(T data){
        return new ResponseDTO<>(200, "success", data);
    }

    public static <T> ResponseDTO<T> fail(T data){
        return new ResponseDTO<>(0, "fail", data);
    }


}