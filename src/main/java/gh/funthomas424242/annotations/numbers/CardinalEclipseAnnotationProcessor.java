package gh.funthomas424242.annotations.numbers;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;

@SupportedAnnotationTypes(value = { "gh.funthomas424242.annotations.numbers.Cardinal" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class CardinalEclipseAnnotationProcessor  implements AnnotationProcessor{

    private AnnotationProcessorEnvironment environment;

    public CardinalEclipseAnnotationProcessor(AnnotationProcessorEnvironment env) {
	environment = env;
    }

    public AnnotationProcessorEnvironment getEnvironment() {
	return environment;
    }

   

    @Override
    public void process() {
	com.sun.mirror.apt.Messager messager = environment.getMessager();
	
	// obtain the declaration of the annotation we want to process
	AnnotationTypeDeclaration annoDecl = (AnnotationTypeDeclaration)environment.getTypeDeclaration(Cardinal.class.getName());
	
	// get the annotated types
	Collection<Declaration> annotatedTypes = environment.getDeclarationsAnnotatedWith(annoDecl);
	
	for (Declaration decl : annotatedTypes) {
		Collection<AnnotationMirror> mirrors = decl.getAnnotationMirrors();
		
		// for each annotation found, get a map of element name/value pairs
		for (AnnotationMirror mirror : mirrors) {
			Map<AnnotationTypeElementDeclaration, AnnotationValue> valueMap = mirror.getElementValues();
			Set<Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue>> valueSet = valueMap.entrySet();
			
			// the annotation processor understands two elements: "what" and "howMany"
			for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> annoKeyValue : valueSet) {					
				AnnotationValue annoValue = annoKeyValue.getValue();	// get the name
				if (annoKeyValue.getKey().getSimpleName().equals("what")) {
					Object whatValue = annoValue.getValue();			// get the value
					if (whatValue instanceof String) {
						if (!((String)whatValue).equals("fish")) {		// complain about invalid value
							messager.printError(annoValue.getPosition(), "But I don't like " + whatValue);
						}
					}
				}
				else if (annoKeyValue.getKey().getSimpleName().equals("howMany")) {
					Object howManyValue = annoValue.getValue();
					if (howManyValue instanceof Integer) {
						if (((Integer)howManyValue) > 6) {
							messager.printError(annoValue.getPosition(), howManyValue + " is too many");
						}
					}
				}
			}
		}
	}
	
    }

}
