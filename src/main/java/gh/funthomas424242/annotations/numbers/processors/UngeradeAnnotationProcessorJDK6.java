package gh.funthomas424242.annotations.numbers.processors;

import gh.funthomas424242.annotations.numbers.Ungerade;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value = { "gh.funthomas424242.annotations.numbers.Ungerade" })
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class UngeradeAnnotationProcessorJDK6 extends AbstractProcessor
        implements Processor {

    private static final String GENERATED_BASE_PACKAGE = "gh.funthomas424242.checked.numbers";
    private static final String GENERATED_BASE_TYPE = "UngeradeZahl";
    // private static final String PARAMETER_NAME_MAX = "max";
    // private static final String PARAMETER_NAME_MIN = "min";
    private static final String TXT_TYPE = "You should be change the type";
    // private static final String TXT_RETURNTYPE =
    // "You should be change the returned type";
    //

    private final static Logger LOG = Logger
            .getLogger(UngeradeAnnotationProcessorJDK6.class.getName());

    private Messager messager;
    private Filer filer;

    public UngeradeAnnotationProcessorJDK6() {
        super();
    }

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
    }

    private void logInfo(final String message) {
        LOG.info(message);
    }

    /**
     * simple write out the annotations of TypeElement
     */
    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {

        for (final TypeElement annotation : annotations) {
            final Set<? extends Element> elements = roundEnv
                    .getElementsAnnotatedWith(annotation);

            for (final Element element : elements) {
                writeFile(element);
                checkTypeName(messager, element);
            }
        }

        return true;
    }

    private void checkTypeName(final Messager messager, final Element element) {

        final String generatedTypeName = createGeneratedFileName(GENERATED_BASE_PACKAGE);

        final TypeMirror typeMirror = element.asType();
        if (typeMirror != null) {

            final String varTypeName = typeMirror.toString();

            if (!generatedTypeName.equals(varTypeName)) {
                final String message = TXT_TYPE + " from " + varTypeName
                        + " to " + generatedTypeName + " !";
                messager.printMessage(Kind.WARNING, message, element);
            }
        }
    }

    private void writeFile(final Element element) {

        try {

            final String fileName = createGeneratedFileName(GENERATED_BASE_PACKAGE);
            final String className = createGeneratedClassName();

            final JavaFileObject fo = filer.createSourceFile(fileName, element);
            final Writer w = fo.openWriter();
            w.append(getCode(GENERATED_BASE_PACKAGE, className));
            w.close();
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }

    protected String getCode(final String packageName, final String typeName) {

        return "package "
                + packageName
                + ";\n\npublic class "
                + typeName
                + " { \n\n"
                + "  final private Integer value; \n\n"
                + "  public "
                + typeName
                + " (final int zahl) { \n"
                + "   if ((zahl % 2) == 0) { \n"
                + "     throw new IllegalArgumentException(\"Value\" + zahl + \" is not odd.\"); \n   } \n"
                + "   value = zahl;\n" + "}  \n"
                + "public Integer getValue() {\n" + "  return value;  \n"
                + "}\n" + "}\n";

    }

    private String createGeneratedFileName(final String packageName) {
        return packageName + "." + createGeneratedClassName();
    }

    private String createGeneratedClassName() {
        return GENERATED_BASE_TYPE;
    }

    //
    //
    // private final static Set<String> VALID_PARAMETERS = new
    // HashSet<String>();
    //
    // private final AnnotationProcessorEnvironment environment;
    //
    // public CardinalAnnotationProcessorJDK5(
    // final AnnotationProcessorEnvironment env) {
    // environment = env;
    // VALID_PARAMETERS.add(PARAMETER_NAME_MIN);
    // VALID_PARAMETERS.add(PARAMETER_NAME_MAX);
    // }
    //
    // public AnnotationProcessorEnvironment getEnvironment() {
    // return environment;
    // }
    //

    // // @Override comment out to compile with newer jdk
    // public void process() {
    // logInfo("CardinalEclipseAnnotationProcessor entry to process");
    // generateMarkerAndCode();
    //
    // }
    //
    // private void generateMarkerAndCode() {
    //
    // final Messager messager = environment.getMessager();
    //
    // final AnnotationTypeDeclaration annoDecl = (AnnotationTypeDeclaration)
    // environment
    // .getTypeDeclaration(Cardinal.class.getName());
    //
    // final Collection<Declaration> annotatedTypes = environment
    // .getDeclarationsAnnotatedWith(annoDecl);
    //
    // for (final Declaration decl : annotatedTypes) {
    // final Collection<AnnotationMirror> mirrors = decl
    // .getAnnotationMirrors();
    //
    // for (final AnnotationMirror mirror : mirrors) {
    // final Map<AnnotationTypeElementDeclaration, AnnotationValue> valueMap =
    // mirror
    // .getElementValues();
    // final Set<Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue>>
    // valueSet = valueMap
    // .entrySet();
    //
    // final Map<String, AnnotationValue> parameterMap = new HashMap<String,
    // AnnotationValue>();
    //
    // for (final Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue>
    // annoKeyValue : valueSet) {
    // final String key = annoKeyValue.getKey().getSimpleName();
    // final AnnotationValue value = annoKeyValue.getValue();
    // parameterMap.put(key, value);
    // }
    //
    // final AnnotationValue valueMin = parameterMap
    // .get(PARAMETER_NAME_MIN);
    // final AnnotationValue valueMax = parameterMap
    // .get(PARAMETER_NAME_MAX);
    //
    // checkMinValue(messager, mirror, valueMin);
    // checkMaxValue(messager, mirror, valueMax);
    // checkMinMaxRelation(messager, mirror, valueMin, valueMax);
    //
    // if (valueMin != null && valueMax != null) {
    // final int validMinValue = getValidIntegerValue(valueMin);
    // final int validMaxValue = getValidIntegerValue(valueMax);
    //
    // checkTypeName(messager, decl, mirror, validMinValue,
    // validMaxValue);
    // writeFile(validMinValue, validMaxValue);
    //
    // }
    // }// next annotation
    //
    // }
    //
    // }
    //

    //

    //
    // private TypeMirror getTypeMirrorOf(final Declaration decl) {
    // TypeMirror typeMirror = null;
    // if (decl instanceof FieldDeclaration) {
    // typeMirror = ((FieldDeclaration) decl).getType();
    // }
    // if (decl instanceof ParameterDeclaration) {
    // typeMirror = ((ParameterDeclaration) decl).getType();
    // }
    // if (decl instanceof MethodDeclaration) {
    // typeMirror = ((MethodDeclaration) decl).getReturnType();
    // }
    // return typeMirror;
    // }
    //

    //
    // private void checkMinMaxRelation(final Messager messager,
    // final AnnotationMirror mirror, final AnnotationValue valueMin,
    // final AnnotationValue valueMax) {
    //
    // if (valueMin != null && valueMax != null) {
    // final SourcePosition srcPosition = mirror.getPosition();
    // final Integer validMinValue = getValidIntegerValue(valueMin);
    // final Integer validMaxValue = getValidIntegerValue(valueMax);
    //
    // if (validMinValue != null && validMaxValue != null
    // && validMinValue > validMaxValue) {
    // messager.printError(srcPosition, "max must be greater then min");
    // }
    // }
    // }
    //
    // private void checkMaxValue(final Messager messager,
    // final AnnotationMirror mirror, final AnnotationValue valueMax) {
    // if (valueMax == null) {
    // messager.printError(mirror.getPosition(), "'max' must be specified");
    // } else {
    // final SourcePosition srcPosition = valueMax.getPosition();
    // final Integer validMaxValue = getValidIntegerValue(valueMax);
    //
    // if (validMaxValue != null) {
    //
    // checkGtEqZero(messager, srcPosition, validMaxValue);
    // checkLtEqMaxCard(messager, srcPosition, validMaxValue);
    //
    // } // else not needed is handled from framework
    //
    // }
    // }
    //
    // private void checkMinValue(final Messager messager,
    // final AnnotationMirror mirror, final AnnotationValue valueMin) {
    // if (valueMin == null) {
    // messager.printError(mirror.getPosition(), "'min' must be specified");
    // } else {
    // final SourcePosition srcPosition = valueMin.getPosition();
    // final Integer validMinValue = getValidIntegerValue(valueMin);
    //
    // if (validMinValue != null) {
    //
    // checkGtEqZero(messager, srcPosition, validMinValue);
    // checkLtEqMaxCard(messager, srcPosition, validMinValue);
    // } // else not needed is handled from framework
    //
    // }
    // }
    //
    // private Integer getValidIntegerValue(final AnnotationValue value) {
    // Integer validIntValue = null;
    // final Object obj = value.getValue();
    // if (obj instanceof Integer) {
    // validIntValue = ((Integer) obj).intValue();
    // }
    // return validIntValue;
    // }
    //
    // private void checkLtEqMaxCard(final Messager messager,
    // final SourcePosition position, final int value) {
    // if (value > 65535) {
    // messager.printWarning(position,
    // "Maybe the value should be <= 65535");
    // }
    // }
    //
    // private void checkGtEqZero(final Messager messager,
    // final SourcePosition position, final int value) {
    // if (value < 0) {
    // messager.printError(position, "A Cardinal must be >=0 ");
    // }
    // }
    //
    // }

}
