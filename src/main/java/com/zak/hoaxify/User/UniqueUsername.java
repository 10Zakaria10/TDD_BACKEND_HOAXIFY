package com.zak.hoaxify.User;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {UniqueUsernameValidation.class}
)
public @interface UniqueUsername {
    String message() default "{custom.validation.constraints.username.unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
