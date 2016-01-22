package ds.gendalf;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

final class Utils {

    public static final TypeName STRING = ClassName.get("java.lang", "String");

    public static Types typeUtils;
    public static Elements elementUtils;
    public static Filer filer;


    public static void init(final ProcessingEnvironment processingEnv) {
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }


    public static String getPackageName(TypeElement type) {
        PackageElement pkg = elementUtils.getPackageOf(type);
        return pkg.getQualifiedName().toString();
    }


    public static String getClassName(TypeElement type) {
        return type.getSimpleName().toString();
    }


    public static String getFieldSimpleType(final VariableElement e) {
        final TypeMirror tm = e.asType();
        TypeElement te = (TypeElement) typeUtils.asElement(tm);
        if (te == null)   // primitive
            return TypeName.get(tm).toString();

        final ClassName cn = ClassName.get(te);
        return cn.simpleName();
    }


    public static String appendPrefixTo(String s, String prefix) {
        String result = s.substring(0, 1).toUpperCase() + s.substring(1);
        return (prefix != null ? prefix : "") + result;
    }


    public static String provideGetSetName(String fieldName) {
        if (fieldName.contains("_")) {
            String[] parts = fieldName.split("_");
            StringBuilder b = new StringBuilder();
            for (String p : parts) {
                if (p.equals("KEY") && parts.length > 1)
                    continue;

                b.append(Utils.appendPrefixTo(p.toLowerCase(), null));

            }
            return b.toString();
        }

        return fieldName;
    }


    public static String getPrefsMethodSuffix(final String type) {
        if (type.equals("Set"))
            return "StringSet";

        return type;
    }


    public static Object provideDefaultValue(final TypeName type, final VariableElement e) {
        final Object constValue = e.getConstantValue();
        if (constValue == null) {
            if (type == TypeName.BOOLEAN)
                return false;
            else if (type.equals(TypeName.INT) || type.equals(TypeName.LONG))
                return 0;
            else if (type.equals(TypeName.FLOAT))
                return 0f;
            else if (type.equals(STRING))
                return null;
        }

        if (type.equals(TypeName.FLOAT))
            return String.format("%sf", constValue);
        else if (type.equals(STRING))
            return String.format("\"%s\"", constValue);

        return constValue;
    }


    public static String getPrefsGetter(final VariableElement e) {return appendPrefixTo(getPrefsMethodSuffix(getFieldSimpleType(e)), "get");}


    public static String getPrefsSetter(final VariableElement e) {return appendPrefixTo(getPrefsMethodSuffix(getFieldSimpleType(e)), "put");}
}
