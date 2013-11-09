package gh.funthomas424242.annotations.numbers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.jdt.apt.core.internal.declaration.FieldDeclarationImpl;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.util.SourcePosition;

//@SupportedAnnotationTypes(value = { "gh.funthomas424242.annotations.numbers.Cardinal" })
//@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class CardinalEclipseAnnotationProcessor implements AnnotationProcessor {

    private static final String GENERATED_BASE_PACKAGE = "gh.funthomas424242.checked.numbers";
    private static final String GENERATED_BASE_TYPE = "Cardinal";
    private static final String PARAMETER_NAME_MAX = "max";
    private static final String PARAMETER_NAME_MIN = "min";

    private final static Logger LOG = Logger
	    .getLogger(CardinalEclipseAnnotationProcessor.class.getName());

    private final static Set<String> VALID_PARAMETERS = new HashSet<String>();

    private AnnotationProcessorEnvironment environment;

    public CardinalEclipseAnnotationProcessor(AnnotationProcessorEnvironment env) {
	environment = env;
	VALID_PARAMETERS.add(PARAMETER_NAME_MIN);
	VALID_PARAMETERS.add(PARAMETER_NAME_MAX);
    }

    public AnnotationProcessorEnvironment getEnvironment() {
	return environment;
    }

    private void logInfo(final String message) {
	LOG.info(message);
    }

    public void process() {
	logInfo("CardinalEclipseAnnotationProcessor entry to process");
	generateMarkerAndCode();

    }

    private void generateMarkerAndCode() {

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
			.get(PARAMETER_NAME_MIN);
		final AnnotationValue valueMax = parameterMap
			.get(PARAMETER_NAME_MAX);

		checkMinValue(messager, mirror, valueMin);
		checkMaxValue(messager, mirror, valueMax);
		checkMinMaxRelation(messager, mirror, valueMin, valueMax);

		if (valueMin != null && valueMax != null) {
		    final int validMinValue = getValidIntegerValue(valueMin);
		    final int validMaxValue = getValidIntegerValue(valueMax);

		    checkTypeName(messager, decl, mirror, validMinValue,
			    validMaxValue);
		    writeFile(validMinValue, validMaxValue);

		}
	    }// next annotation

	}

    }

    private void writeFile(final int validMinValue, final int validMaxValue) {

	try {
	    Filer f = getEnvironment().getFiler();

	    final String fileName = createGeneratedFileName(
		    GENERATED_BASE_PACKAGE, validMinValue, validMaxValue);

	    PrintWriter pw = f.createSourceFile(fileName);
	    final String className = createGeneratedClassName(validMinValue,
		    validMaxValue);

	    pw.print(getCode(GENERATED_BASE_PACKAGE, className, validMinValue,
		    validMaxValue));
	    pw.flush();
	    pw.close();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
    }

    protected String getCode(final String packageName, final String typeName,
	    final int minValue, final int maxValue) {

	return "package "

		+ packageName
		+ ";"
		+ "\n\n"
		+ "public class "
		+ typeName
		+ "\n {\n\n"
		+ "	private Integer cardinalValue;\n\n"
		+ "   public "
		+ typeName
		+ "(final Integer cardinalValue)\n"
		+ "    {"
		+ "\n"
		+ "	if (cardinalValue==null ) { \n"
		+ " 		throw new IllegalArgumentException(\"No cardinalValue defined - means null was applied.\");\n"
		+ "	}else if (cardinalValue.intValue()<0 ) {\n"
		+ "  		throw new IllegalArgumentException(\"A cardinal must be >= 0 !\");\n"
		+ "	}else if (cardinalValue.intValue()<"
		+ minValue
		+ "|| cardinalValue.intValue()>"
		+ maxValue
		+ ") {\n"
		+ "		throw new IllegalArgumentException(\"The cardinal must be pass the condition "
		+ minValue + " >= x <=" + maxValue + " !\");\n" + "	}\n"
		+ " 	  this.cardinalValue=cardinalValue; \n" + "   }\n\n"
		+ "public Integer getValue(){\n" + "" + "" + "" + ""
		+ "\n	return this.cardinalValue;\n" + "}\n" + "\n" + "}";

    }

    private void checkTypeName(Messager messager, Declaration decl,
	    AnnotationMirror mirror, final int valueMin, final int valueMax) {

	final String generatedTypeName = createGeneratedClassName(valueMin,
		valueMax);
	final SourcePosition mirrorPosition = mirror.getPosition();

	if (decl instanceof FieldDeclarationImpl) {
	    final FieldDeclarationImpl fieldDecl = (FieldDeclarationImpl) decl;
	    final TypeMirror typeMirror = fieldDecl.getType();

	    if (!generatedTypeName.endsWith(typeMirror.toString())) {

		messager.printWarning(
			mirrorPosition,
			"You should be change the type from "
				+ typeMirror.toString() + " to "
				+ generatedTypeName + " !");
	    }

	}

    }

    private String createGeneratedClassName(final int valueMin,
	    final int valueMax) {
	return GENERATED_BASE_TYPE + valueMin + "Bis" + valueMax;
    }

    private String createGeneratedFileName(final String packageName,
	    final int valueMin, final int valueMax) {
	return packageName + "." + createGeneratedClassName(valueMin, valueMax);
    }

    private void checkMinMaxRelation(Messager messager,
	    final AnnotationMirror mirror, final AnnotationValue valueMin,
	    final AnnotationValue valueMax) {

	if (valueMin != null && valueMax != null) {
	    final SourcePosition srcPosition = mirror.getPosition();
	    final Integer validMinValue = getValidIntegerValue(valueMin);
	    final Integer validMaxValue = getValidIntegerValue(valueMax);

	    if (validMinValue != null && validMaxValue != null
		    && validMinValue > validMaxValue) {
		messager.printError(srcPosition, "max must be greater then min");
	    }
	}
    }

    private void checkMaxValue(final Messager messager,
	    final AnnotationMirror mirror, final AnnotationValue valueMax) {
	if (valueMax == null) {
	    messager.printError(mirror.getPosition(), "'max' must be specified");
	} else {
	    final SourcePosition srcPosition = valueMax.getPosition();
	    final Integer validMaxValue = getValidIntegerValue(valueMax);

	    if (validMaxValue != null) {

		checkGtEqZero(messager, srcPosition, validMaxValue);
		checkLtEqMaxCard(messager, srcPosition, validMaxValue);

	    } // else not needed is handled from framework

	}
    }

    private void checkMinValue(final Messager messager,
	    final AnnotationMirror mirror, final AnnotationValue valueMin) {
	if (valueMin == null) {
	    messager.printError(mirror.getPosition(), "'min' must be specified");
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

}
