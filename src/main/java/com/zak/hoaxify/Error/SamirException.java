package com.zak.hoaxify.Error;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@Data
@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SamirException extends  RuntimeException {
    private Map<String , String > validationErrors;

    public SamirException(Map<String,String> errors){
        validationErrors = errors;
    }

}
