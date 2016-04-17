package ds.gendalf;

import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
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


    public static String getFieldSimpleType(final Element e) {
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


    public static String toCamelCase(String fieldName) {
        if (isUpperCase(fieldName) || fieldName.contains("_")) {
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

    public static String toUnderScore(String s) {
        return s.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static String toKey(String s) {
        return "KEY_" + s.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }

    public static boolean isUpperCase(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLowerCase(s.charAt(i))) {
                return false;
            }
        }
        return true;
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
        else if (type.equals(TypeName.LONG))
            return String.format("%sL", constValue);
        else if (type.equals(STRING))
            return String.format("\"%s\"", constValue);

        return constValue;
    }


    public static String getPrefsGetter(final VariableElement e) {return appendPrefixTo(getPrefsMethodSuffix(getFieldSimpleType(e)), "get");}


    public static String getPrefsSetter(final VariableElement e) {return appendPrefixTo(getPrefsMethodSuffix(getFieldSimpleType(e)), "put");}

    public static ConverterData createConverterData(VariableElement e) {
        CustomPref customPref = e.getAnnotation(CustomPref.class);
        if (customPref != null) {
            try {
                customPref.value(); // always fails
            } catch (MirroredTypeException mte) {
                TypeMirror mirror = mte.getTypeMirror();
                TypeElement converterClassElement = (TypeElement) Utils.typeUtils.asElement(mirror);
                TypeMirror declaredType;
                if (converterClassElement.getInterfaces().size() != 0)
                    declaredType = converterClassElement.getInterfaces().get(0);
                else {
                    declaredType = converterClassElement.getSuperclass();
                }
                ClassName converterClass = ClassName.get(converterClassElement);
                ClassName iConverter = ClassName.get(elementUtils.getTypeElement(Converter.class.getCanonicalName()));
                final TypeMirror typeAMirror = e.asType();
                final TypeMirror typeBMirror = ((DeclaredType) declaredType).getTypeArguments().get(1);
                TypeName typeAName = ClassName.get(e.asType());
                TypeName typeBName = TypeName.get(typeBMirror);

                TypeName parametrizedType = ParameterizedTypeName.get(iConverter, typeAName, typeBName);
                String fName = e.getSimpleName().toString();
                String name = fName + converterClass.simpleName();
                final FieldSpec.Builder builder = FieldSpec.builder(parametrizedType, name, Modifier.PRIVATE);

                boolean isTypedConverter = Utils.typeUtils.asElement(converterClassElement.getSuperclass()).getSimpleName().contentEquals("TypedConverter");
                if (isTypedConverter)
                    builder.initializer(
                            "new $T($L.class, $L.class)",
                            converterClass,
                            getFieldSimpleType(typeUtils.asElement(typeAMirror)),
                            getFieldSimpleType(typeUtils.asElement(typeBMirror))
                    );
                else
                    builder.initializer("new $T()", converterClass);
                return new ConverterData(builder.build(), typeAMirror, typeBMirror);
            }
        }
        return null;
    }
}