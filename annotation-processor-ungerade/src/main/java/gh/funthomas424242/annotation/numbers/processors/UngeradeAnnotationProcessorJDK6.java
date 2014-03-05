package gh.funthomas424242.annotation.numbers.processors;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(value = { "gh.funthomas424242.annotation.numbers.Ungerade" })
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class UngeradeAnnotationProcessorJDK6 extends AbstractProcessor
        implements Processor {

    private static final String GENERATED_BASE_PACKAGE = "gh.funthomas424242.checked.numbers";
    private static final String GENERATED_BASE_TYPE = "UngeradeZahl";
    private static final String TXT_TYPE = "You should be change the type";

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

        logInfo("UngeradeAnnotationProcessorJDK6 entry to process");

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

   

}
