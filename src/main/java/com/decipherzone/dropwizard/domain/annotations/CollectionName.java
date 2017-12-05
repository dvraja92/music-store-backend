package com.decipherzone.dropwizard.domain.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CollectionName {
    public String name() default "";
    public String[] indexes() default {};
    public String[] uniqueIndexes() default {};
}
