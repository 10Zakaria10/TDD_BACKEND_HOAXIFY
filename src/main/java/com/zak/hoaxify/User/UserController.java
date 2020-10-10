package com.zak.hoaxify.User;

import com.zak.hoaxify.Error.ApiError;
import com.zak.hoaxify.Error.SamirException;
import com.zak.hoaxify.shared.GenericResponse;
import com.zak.hoaxify.shared.MyExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController implements   MyExceptionHandler {

    @Autowired
    UserService userService;

    public void simulateSamirException(){
        Map<String , String > validationErrors = new HashMap<>();
        validationErrors.put("username","fih samir");
        validationErrors.put("password","fih samir");
        throw new SamirException(validationErrors);
    }

    @PostMapping("/api/1.0/users")
    public GenericResponse createUser(@Valid  @RequestBody User user){
        //simulateSamirException();
        userService.save(user);
        return new GenericResponse("user Saved");
    }



}
