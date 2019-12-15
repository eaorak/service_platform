package com.adenon.sp.executer.task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used for differentiate tasks that are being scheduled more than once. <br>
 * Thread manager uses given id with the method which has this annotation to seperate <br>
 * tasks that has same name.<br>
 * <br>
 * The method that is annotated with this annotation should take no parameters and return <br>
 * a String value that is unique among the other instances of the same task.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TaskId {
}
