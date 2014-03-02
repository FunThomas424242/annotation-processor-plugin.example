package gh.funthomas424242.annotations.numbers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value = { ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD,
        ElementType.LOCAL_VARIABLE })
public @interface UngeradeZahl {

}
