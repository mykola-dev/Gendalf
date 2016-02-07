package ds.gendalf;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.VariableElement;

public class ClassData {

    public static final String DEFAULT_CLASS = "Gendalf";
    public static final String DEFAULT_PREFS = "gendalf_prefs";
    public static final String DEFAULT_PACKAGE = "ds.gendalf.generated";

    private String className;
    private String fileName;


    public String getFileName() {
        return fileName != null ? fileName : DEFAULT_PREFS;
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
            final String generatedClassName,
            final String filename,
            final List<VariableElement> elements) {
        this.className = generatedClassName;
        this.fileName = filename;
        this.elements = elements;
    }
}
