package ds.gendalf;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

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
        set.add(PrefKey.class.getCanonicalName());
        set.add(PrefsConfig.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, ClassData> data = new HashMap<>();

        // todo some validation here

        for (Element e : roundEnv.getElementsAnnotatedWith(PrefKey.class)) {
            if (e instanceof VariableElement) {
                final TypeElement parent = (TypeElement) e.getEnclosingElement();
                String cls = parent.getAnnotation(PrefsConfig.class).cls();
                if (!data.containsKey(cls)) {
                    String filename = parent.getAnnotation(PrefsConfig.class).filename();
                    //String packageName = Utils.getPackageName(parent);
                    data.put(cls, new ClassData(cls, filename));
                }
                data.get(cls).elements.add((VariableElement) e);
            } else
                messager.error(e, "Annotation should be assigned to field");

        }

        if (data.size() != 0) {
            data.values().forEach(this::processPrefKeys);
        }

        return true;
    }


    private void processPrefKeys(ClassData data) {
        try {
            if (data.elements.size() == 0)
                return;

            final VariableElement e = data.elements.get(0);
            final TypeElement parent = (TypeElement) e.getEnclosingElement();
            String packageName = Utils.getPackageName(parent);
            //final String className = Utils.getClassName(parent);

            CodeGenerator codeGenerator = new CodeGenerator(data);
            TypeSpec generatedClass = codeGenerator.generateClass();

            JavaFile javaFile = builder(packageName, generatedClass).build();
            javaFile.writeTo(Utils.filer);
        } catch (IOException e) {
            messager.error("Generator error: %s", e.getMessage());
        }

    }


}

