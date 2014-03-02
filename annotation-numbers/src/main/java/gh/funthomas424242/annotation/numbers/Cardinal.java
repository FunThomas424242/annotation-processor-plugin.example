package gh.funthomas424242.annotation.numbers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


@Target(value = {ElementType.METHOD,ElementType.PARAMETER,ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface Cardinal {
    
    public int min() default 0;
    public int max() default 65535;

}
