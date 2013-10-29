package gh.funthomas424242.annotations;

import gh.funthomas424242.annotations.numbers.CardinalEclipseAnnotationProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class AnnotationFactory implements AnnotationProcessorFactory {

    public Collection<String> supportedOptions() {
	return Collections.emptyList();
    }

    public Collection<String> supportedAnnotationTypes() {
	return annotations;
    }

    public AnnotationProcessor getProcessorFor(
	    Set<AnnotationTypeDeclaration> atds,
	    AnnotationProcessorEnvironment env) {
	return new CardinalEclipseAnnotationProcessor(env);
    }

    private static ArrayList<String> annotations = new ArrayList<String>();

    {
	annotations.add(CardinalEclipseAnnotationProcessor.class.getName());
    }

}
