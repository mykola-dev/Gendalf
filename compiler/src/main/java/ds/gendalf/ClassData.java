package ds.gendalf;

import javax.lang.model.element.VariableElement;
import java.util.List;

public class ClassData {

	public static final String DEFAULT_CLASS = "Gendalf";
	public static final String DEFAULT_PREFS = "gendalf_prefs";
	public static final String DEFAULT_PACKAGE = "ds.gendalf";

	private String configuratorClassName;
	private String packageName;
	private String generatedClassName;
	private String prefsName;


	public String getPrefsName() {
		return prefsName != null ? prefsName : DEFAULT_PREFS;
	}


	public String getConfiguratorClassName() {
		return configuratorClassName;
	}


	public String getPackageName() {
		return packageName != null ? packageName : DEFAULT_PACKAGE;
	}


	public String getGeneratedClassName() {
		return generatedClassName != null ? generatedClassName : DEFAULT_CLASS;
	}


	public List<VariableElement> elements;


	public ClassData(
			final String configuratorClassName,
			final String generatedClassName,
			final String packageName,
			final String prefsName,
			final List<VariableElement> elements) {
		this.configuratorClassName = configuratorClassName;
		this.generatedClassName = generatedClassName;
		this.packageName = packageName;
		this.prefsName = prefsName;
		this.elements = elements;
	}
}
