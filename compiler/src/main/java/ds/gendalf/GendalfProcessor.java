package ds.gendalf;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.squareup.javapoet.JavaFile.builder;

@AutoService(Processor.class)
public class GendalfProcessor extends AbstractProcessor {

	private final Messager messager = new Messager();
	private String prefsName;


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
		List<VariableElement> prefKeys = new ArrayList<>();

		// todo some validation here

		for (Element e : roundEnv.getElementsAnnotatedWith(PrefsConfig.class)) {
			if (e instanceof TypeElement) {
				prefsName = e.getAnnotation(PrefsConfig.class).value();
			} else
				messager.error(e, "Annotation should be assigned to class");
		}


		for (Element e : roundEnv.getElementsAnnotatedWith(PrefKey.class)) {
			if (e instanceof VariableElement)
				prefKeys.add((VariableElement) e);
			else
				messager.error(e, "Annotation should be assigned to field");

		}

		if (prefKeys.size() != 0)
			processPrefKeys(prefKeys);

		return true;
	}


	private void processPrefKeys(final List<VariableElement> prefKeys) {
		try {
			if (prefKeys.size() == 0)
				return;

			final VariableElement e = prefKeys.get(0);
			final TypeElement parent = (TypeElement) e.getEnclosingElement();
			String packageName = Utils.getPackageName(parent);
			final String className = Utils.getClassName(parent);

			ClassData data = new ClassData(className, null, packageName, prefsName, prefKeys);
			CodeGenerator codeGenerator = new CodeGenerator(data);
			TypeSpec generatedClass = codeGenerator.generateClass();

			JavaFile javaFile = builder(packageName, generatedClass).build();
			javaFile.writeTo(Utils.filer);
		} catch (IOException e) {
			messager.error("Generator error: %s", e.getMessage());
		}

	}


}

