package gh.funthomas424242.annotations.numbers;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

public class CardinalCLIAnnotationProcessor extends AbstractProcessor {

    /**
     * simple write out the annotations of TypeElement
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations,
	    RoundEnvironment roundEnv) {
	for (TypeElement element : annotations) {
	    System.out.println(element.getQualifiedName());
	}
	return true;
    }

}
