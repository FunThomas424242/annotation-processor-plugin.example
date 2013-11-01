package gh.funthomas424242.annotations.numbers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import com.sun.mirror.util.SourcePosition;

//@SupportedAnnotationTypes(value = { "gh.funthomas424242.annotations.numbers.Cardinal" })
//@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class CardinalEclipseAnnotationProcessor implements AnnotationProcessor {

    private static final String PARAMETER_MAX = "max";
    private static final String PARAMETER_MIN = "min";

    private final static Logger LOG = Logger
	    .getLogger(CardinalEclipseAnnotationProcessor.class.getName());

    private final static Set<String> VALID_PARAMETERS = new HashSet<String>();

    private AnnotationProcessorEnvironment environment;

    public CardinalEclipseAnnotationProcessor(AnnotationProcessorEnvironment env) {
	environment = env;
	VALID_PARAMETERS.add(PARAMETER_MIN);
	VALID_PARAMETERS.add(PARAMETER_MAX);
    }

    public AnnotationProcessorEnvironment getEnvironment() {
	return environment;
    }

    public void process() {
	logInfo("CardinalEclipseAnnotationProcessor entry to process");
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

		final Map<String, AnnotationValue> parameterMap = new HashMap<String, AnnotationValue>();

		for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> annoKeyValue : valueSet) {
		    final String key = annoKeyValue.getKey().getSimpleName();
		    final AnnotationValue value = annoKeyValue.getValue();
		    parameterMap.put(key, value);

		}

		final AnnotationValue valueMin = parameterMap
			.get(PARAMETER_MIN);
		final AnnotationValue valueMax = parameterMap
			.get(PARAMETER_MAX);

		checkMinValue(messager, valueMin);
		checkMaxValue(messager, valueMax);
		checkMinMaxRelation(messager, valueMin, valueMax);

	    }// next annotation
	}

    }

    private void checkMinMaxRelation(Messager messager,
	    final AnnotationValue valueMin, final AnnotationValue valueMax) {
	if (valueMin != null && valueMax != null) {

	    final SourcePosition minSrcPosition = valueMin
		    .getPosition();
	    final SourcePosition maxSrcPosition = valueMax
		    .getPosition();
	    final Integer validMinValue = getValidIntegerValue(valueMin);
	    final Integer validMaxValue = getValidIntegerValue(valueMax);

	    if (validMinValue != null && validMaxValue != null
		    && validMinValue > validMaxValue) {

		messager.printError(minSrcPosition,
			"min must be less then max");
		messager.printError(maxSrcPosition,
			"max must be greater then min");
	    }

	}
    }

    private void checkMaxValue(Messager messager, final AnnotationValue valueMax) {
	if (valueMax == null) {
	    messager.printError("'max' must be specified");
	} else {
	    final SourcePosition srcPosition = valueMax.getPosition();
	    final Integer validMaxValue = getValidIntegerValue(valueMax);

	    if (validMaxValue != null) {

		checkGtEqZero(messager, srcPosition, validMaxValue);
		checkLtEqMaxCard(messager, srcPosition, validMaxValue);

	    } // else not needed is handled from framework

	}
    }

    private void checkMinValue(Messager messager, final AnnotationValue valueMin) {
	if (valueMin == null) {
	    messager.printError("'min' must be specified");
	} else {
	    final SourcePosition srcPosition = valueMin.getPosition();
	    final Integer validMinValue = getValidIntegerValue(valueMin);

	    if (validMinValue != null) {

		checkGtEqZero(messager, srcPosition, validMinValue);
		checkLtEqMaxCard(messager, srcPosition, validMinValue);
	    } // else not needed is handled from framework

	}
    }

    private Integer getValidIntegerValue(final AnnotationValue value) {
	Integer validIntValue = null;
	final Object obj = value.getValue();
	if (obj instanceof Integer) {
	    validIntValue = ((Integer) obj).intValue();
	}
	return validIntValue;
    }

    private void checkLtEqMaxCard(final Messager messager,
	    final SourcePosition position, final int value) {
	if (value > 65535) {
	    messager.printWarning(position,
		    "Maybe the value should be <= 65535");
	}
    }

    private void checkGtEqZero(final Messager messager,
	    final SourcePosition position, final int value) {
	if (value < 0) {
	    messager.printError(position, "A Cardinal must be >=0 ");
	}
    }

    private void logInfo(final String message) {
	LOG.info(message);
    }

}
