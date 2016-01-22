package ds.gendalf;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.VariableElement;

public class ClassData {

    public static final String DEFAULT_CLASS = "Gendalf";
    public static final String DEFAULT_PREFS = "gendalf_prefs";
    public static final String DEFAULT_PACKAGE = "ds.gendalf.generated";

    private String configuratorClassName;
    private String packageName;
    private String className;
    private String fileName;


    public String getFileName() {
        return fileName != null ? fileName : DEFAULT_PREFS;
    }


    public String getConfiguratorClassName() {
        return configuratorClassName;
    }


    public String getPackageName() {
        return packageName != null ? packageName : DEFAULT_PACKAGE;
    }


    public String getClassName() {
        return className != null ? className : DEFAULT_CLASS;
    }


    public List<VariableElement> elements = new ArrayList<>();

    public ClassData(final String className, final String fileName) {
        this.className = className;
        this.fileName = fileName;
        elements = new ArrayList<>();
    }

    public ClassData(
            final String configuratorClassName,
            final String generatedClassName,
            final String packageName,
            final String filename,
            final List<VariableElement> elements) {
        this.configuratorClassName = configuratorClassName;
        this.className = generatedClassName;
        this.packageName = packageName;
        this.fileName = filename;
        this.elements = elements;
    }
}
