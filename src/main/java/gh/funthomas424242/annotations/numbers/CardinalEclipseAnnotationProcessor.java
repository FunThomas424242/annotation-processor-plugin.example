package gh.funthomas424242.annotations.numbers;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

















import java.util.logging.Logger;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration; 

//@SupportedAnnotationTypes(value = { "gh.funthomas424242.annotations.numbers.Cardinal" })
//@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class CardinalEclipseAnnotationProcessor implements AnnotationProcessor {

  private Logger logger=Logger.getLogger(CardinalEclipseAnnotationProcessor.class.getName());
    
    //@Inject Logger logger;
    
    private AnnotationProcessorEnvironment environment;

    public CardinalEclipseAnnotationProcessor(AnnotationProcessorEnvironment env) {
	environment = env;
    }

    public AnnotationProcessorEnvironment getEnvironment() {
	return environment;
    }

    public void process() {
	log("testbreak"); 
	addMarker();

    }

    private void addMarker() {

	Messager messager = environment.getMessager();


	AnnotationTypeDeclaration annoDecl = (AnnotationTypeDeclaration) environment
		.getTypeDeclaration(Cardinal.class.getName());

	
	Collection<Declaration> annotatedTypes = environment
		.getDeclarationsAnnotatedWith(annoDecl);

	for (Declaration decl : annotatedTypes) {
	    Collection<AnnotationMirror> mirrors = decl.getAnnotationMirrors();

	   
	    for (AnnotationMirror mirror : mirrors) {
		Map<AnnotationTypeElementDeclaration, AnnotationValue> valueMap = mirror
			.getElementValues();
		Set<Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue>> valueSet = valueMap
			.entrySet();

		
		for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> annoKeyValue : valueSet) {

		    AnnotationValue annoValue = annoKeyValue.getValue();

		    if (annoKeyValue.getKey().getSimpleName().equals("min")) {

			Object minValue = annoValue.getValue(); // get the value
			if (minValue instanceof Integer) {
			    if (!((Integer) minValue).equals(2)) {

				messager.printError(annoValue.getPosition(),
					"But I don't like " + minValue);
			    }

			}

		    } else if (annoKeyValue.getKey().getSimpleName()
			    .equals("max")) {

			Object maxValue = annoValue.getValue();

			if (maxValue instanceof Integer) {

			    if (((Integer) maxValue) > 6) {

				messager.printError(annoValue.getPosition(),
					maxValue + " is too many");

			    }
			}
		    }
		}
	    }
	}
    }

    private void log(final String message) {
	// final ILog log=AnnotationPlugin.getDefault().getLog();
	// IStatus status = new Status(Status.INFO,"annotation.plugin" ,
	// message);
	// log.log(status);
	 logger.info( message);
	System.out.println(message);

    }

}
