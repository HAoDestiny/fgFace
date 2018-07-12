package net.sh.rgface.annotations;

import java.lang.annotation.*;

/**
 * Created by DESTINY on 2018/5/23.
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotAspect {

    String value() default ""; //@NotAspect(value = "")
    String desc() default ""; //切面传参  @NotAspect(desc = "")
}