package ds.gendalf;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;

import static com.squareup.javapoet.JavaFile.builder;

@AutoService(Processor.class)
public class GendalfProcessor extends AbstractProcessor {

    private final Messager messager = new Messager();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager.init(processingEnv);
        Utils.init(processingEnv);

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        //set.add(PrefKey.class.getCanonicalName());
        set.add(PrefsConfig.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @SuppressWarnings("Convert2streamapi")
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // todo some validation here
        for (Element e : roundEnv.getElementsAnnotatedWith(PrefsConfig.class)) {
            if (e instanceof TypeElement) {
                String cls = e.getAnnotation(PrefsConfig.class).value();
                List<? extends Element> childs = e.getEnclosedElements();
                List<VariableElement> vars = new ArrayList<>();
                for (Element f : childs) {
                    if (f instanceof VariableElement) {
                        vars.add((VariableElement) f);
                    }
                }
                String filename = Utils.toUnderScore(cls);
                String packageName = Utils.getPackageName((TypeElement) e);
                ClassData data = new ClassData(cls, filename, (vars));
                processPrefKeys(data, packageName);
            } else
                messager.error(e, "Annotation should be assigned to class");
        }

        return true;
    }


    private void processPrefKeys(ClassData data, String packageName) {
        try {
            if (data.elements.size() == 0)
                return;

            CodeGenerator codeGenerator = new CodeGenerator(data);
            TypeSpec generatedClass = codeGenerator.generateClass();

            JavaFile javaFile = builder(packageName, generatedClass).build();
            javaFile.writeTo(Utils.filer);
        } catch (IOException e) {
            messager.error("Generator error: %s", e.getMessage());
        }

    }


}

