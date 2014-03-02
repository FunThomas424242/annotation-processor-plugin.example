package gh.funthomas424242.annotation.numbers.processors;

import gh.funthomas424242.annotation.numbers.Cardinal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class AnnotationFactory implements AnnotationProcessorFactory {

    private static ArrayList<String> annotations = new ArrayList<String>();

    {
        annotations.add(Cardinal.class.getName());
    }

    @Override
    public Collection<String> supportedOptions() {
        return Collections.emptyList();
    }

    @Override
    public Collection<String> supportedAnnotationTypes() {
        return annotations;
    }

    @Override
    public AnnotationProcessor getProcessorFor(
            final Set<AnnotationTypeDeclaration> atds,
            final AnnotationProcessorEnvironment env) {
        return new CardinalAnnotationProcessorJDK5(env);
    }

}
