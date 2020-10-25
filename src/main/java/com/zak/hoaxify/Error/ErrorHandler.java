package com.zak.hoaxify.Error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
public class ErrorHandler implements ErrorController {

    @Autowired
    ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ApiError handlerError(WebRequest webRequest){
        Map<String,Object>  attributes = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
        String message = attributes.get("message").toString();
        String url = attributes.get("path").toString();
        int status = (Integer) attributes.get("status");
        return new ApiError(status,message,url);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
