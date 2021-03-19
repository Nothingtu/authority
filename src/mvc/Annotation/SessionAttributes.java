package mvc.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//放在类上，用来把参数装入session
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionAttributes {
    String[] value();
}
