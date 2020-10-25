package com.zak.hoaxify.Error;

import com.zak.hoaxify.Error.ApiError;
import com.zak.hoaxify.Error.SamirException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public interface MyExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    default ApiError handleValidationException(MethodArgumentNotValidException expection , HttpServletRequest request){
        BindingResult bindingResult = expection.getBindingResult();
        Map<String , String > validationErrors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()
        ) {
            validationErrors.put(error.getField() , error.getDefaultMessage());
        }
        return  constructApiError(validationErrors,request.getServletPath());
    }

    @ExceptionHandler({SamirException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    default ApiError handleValidationException(SamirException expection , HttpServletRequest request){
        return  constructApiError(expection.getValidationErrors(),request.getServletPath());
    }

    default ApiError constructApiError(Map<String,String> errors, String path){
        ApiError apiError= new ApiError(400 ,"Validation Error",path);
        apiError.setValidationErrors(errors);
        return  apiError;
    }
}
