package gh.funthomas424242.annotations.numbers;

import gh.funthomas424242.annotations.AnnotationPlugin;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

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

    private AnnotationProcessorEnvironment environment;

    public CardinalEclipseAnnotationProcessor(AnnotationProcessorEnvironment env) {
	environment = env;
    }

    public AnnotationProcessorEnvironment getEnvironment() {
	return environment;
    }

   
    public void process() {
	addMarker();

    }

    private void addMarker() {
	    
	Messager messager = environment.getMessager();
	messager.printError("stop it");
	
	// obtain the declaration of the annotation we want to process
	AnnotationTypeDeclaration annoDecl = (AnnotationTypeDeclaration) environment
		.getTypeDeclaration(Cardinal.class.getName());
	    log("break2");
	// get the annotated types
	Collection<Declaration> annotatedTypes = environment
		.getDeclarationsAnnotatedWith(annoDecl);
	    log("break3");
	for (Declaration decl : annotatedTypes) {
	    Collection<AnnotationMirror> mirrors = decl.getAnnotationMirrors();
	    log("break4");
	    // for each annotation found, get a map of element name/value pairs
	    for (AnnotationMirror mirror : mirrors) {
		Map<AnnotationTypeElementDeclaration, AnnotationValue> valueMap = mirror
			.getElementValues();
		Set<Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue>> valueSet = valueMap
			.entrySet();
		    log("break5");
		// the annotation processor understands two elements: "min" and
		// "max"
		for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> annoKeyValue : valueSet) {
		    log("break6");
		    AnnotationValue annoValue = annoKeyValue.getValue(); // get
									 // the
									 // name
		    log("break7");
		   
		    if (annoKeyValue.getKey().getSimpleName().equals("min")) {
			    log("break8");
			Object minValue = annoValue.getValue(); // get the value
			if (minValue instanceof Integer) {
			    if (!((Integer) minValue).equals("2")) { 
				    log("break9");
				messager.printError(annoValue.getPosition(),
					"But I don't like " + minValue);
			    }
			    log("break10");
			}
			    log("break11");
		    } else if (annoKeyValue.getKey().getSimpleName()
			    .equals("max")) {
			    log("break12");
			    
			Object maxValue = annoValue.getValue();
			    log("break13");
			if (maxValue instanceof Integer) {
			    log("break14");
			    if (((Integer) maxValue) > 6) {
				    log("break15");
				messager.printError(annoValue.getPosition(),
					maxValue + " is too many");
				    log("break16");
			    }
			}
		    }
		}
	    }
	}
    }

    private void log(final String message) {
	final ILog log=AnnotationPlugin.getDefault().getLog();
	IStatus status = new Status(Status.INFO,"annotation.plugin" , message);
	log.log(status);
	System.out.println(message);

    }
    
    
}
