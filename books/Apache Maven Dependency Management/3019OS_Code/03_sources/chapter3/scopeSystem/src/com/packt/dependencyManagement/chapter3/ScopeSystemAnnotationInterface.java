package com.packt.dependencyManagement.chapter3;

import java.lang.annotation.*;
// Unneeded import
import com.sun.tools.apt.Main;

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface ScopeSystemAnnotationInterface {
    String aStringField() default "hello";

    int anIntField() default 123456;
}

