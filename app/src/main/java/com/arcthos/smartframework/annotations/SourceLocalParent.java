package com.arcthos.smartframework.annotations;

import com.arcthos.smartframework.smartorm.SmartObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Vinicius Damiati on 10-Mar-18.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SourceLocalParent {
    Class<? extends SmartObject> model();
}
