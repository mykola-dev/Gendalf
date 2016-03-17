package ds.gendalf;

import com.squareup.javapoet.*;

import java.util.*;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;

import static javax.lang.model.element.Modifier.*;

final class CodeGenerator {

    private ClassData data;
    private Map<VariableElement, FieldSpec> keys;

    private ClassName context = ClassName.get("android.content", "Context");
    private ClassName prefs = ClassName.get("android.content", "SharedPreferences");
    private ClassName editor = ClassName.get("android.content", "SharedPreferences.Editor");
    private Map<String, FieldSpec> converters = new HashMap<>();

    CodeGenerator(ClassData data) {
        this.data = data;
    }

    TypeSpec generateClass() {
        List<MethodSpec> methods = new ArrayList<>();
        keys = new HashMap<>();
        for (VariableElement e : data.elements) {
            final String fieldName = e.getSimpleName().toString();
            //final String keyName = e.getSimpleName().toString();
            keys.put(e, FieldSpec.builder(Utils.STRING, Utils.toKey(fieldName), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                 .initializer("$S", fieldName)
                                 .build()
            );

            ConverterData converter = Utils.createConverterData(e);
            if (converter != null) {
                if (!converters.containsKey(converter.field.name))
                    converters.put(converter.field.name, converter.field);
                methods.add(customGetter(e, converter));
                methods.add(customSetter(e, converter));
            } else {
                methods.add(getter(e));
                methods.add(setter(e));
            }

            methods.add(contains(e));
        }

        return TypeSpec.classBuilder(data.getClassName())
                       .addField(prefs, "prefs", Modifier.PRIVATE)
                       .addField(editor, "edit", Modifier.PRIVATE)
                       .addField(allKeysField())
                       .addFields(keys.values())
                       .addStaticBlock(allKeysInit())
                       .addFields(converters.values())
                       .addMethod(constructor())
                       .addMethod(with())
                       .addModifiers(PUBLIC, FINAL)
                       .addMethods(methods)
                       .addMethod(commit())
                       .addMethod(getAll())
                       .addMethod(clear())
                       .addMethod(getPrefs())
                       .addMethod(getFileName())
                       .build();
    }

    private MethodSpec getFileName() {
        return MethodSpec.methodBuilder("getFileName")
                         .addJavadoc("Returns prefs file name")
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("return $S", data.getFileName())
                         .returns(ClassName.get(String.class))
                         .build();
    }

    private MethodSpec getPrefs() {
        return MethodSpec.methodBuilder("getPrefs")
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("return prefs")
                         .returns(prefs)
                         .build();
    }

    private MethodSpec with() {
        return MethodSpec.methodBuilder("with")
                         .addModifiers(PUBLIC, STATIC)
                         .addParameter(context, "ctx")
                         .addStatement("return new $L(ctx)", data.getClassName())
                         .returns(ClassName.bestGuess(data.getClassName()))
                         .build();
    }

    private MethodSpec constructor() {
        return MethodSpec.constructorBuilder()
                         .addModifiers(Modifier.PRIVATE)
                         .addParameter(context, "ctx")
                         .addStatement("prefs = ctx.getSharedPreferences($S, Context.MODE_PRIVATE)", data.getFileName())
                         .addStatement("edit = prefs.edit()")
                         .build();
    }

    private MethodSpec setter(VariableElement e) {
        final String fieldName = e.getSimpleName().toString();
        final String keyName = keys.get(e).name;
        final String setterName = Utils.appendPrefixTo(Utils.toCamelCase(fieldName), "set");
        final TypeName type = ClassName.get(e.asType());
        final String prefsSetterName = Utils.getPrefsSetter(e);
        return MethodSpec.methodBuilder(setterName)
                         .addModifiers(PUBLIC, FINAL)
                         .addParameter(type, fieldName)
                         .addStatement("edit.$L($L, $L)", prefsSetterName, keyName, fieldName)
                         .addStatement("edit.apply()")
                         .addStatement("return this")
                         .returns(ClassName.bestGuess(data.getClassName()))
                         .build();
    }

    private MethodSpec getter(VariableElement e) {
        final String fieldName = e.getSimpleName().toString();
        final String keyName = keys.get(e).name;
        final TypeName type = ClassName.get(e.asType());
        final String prefix = (type == TypeName.BOOLEAN) ? "is" : "get";
        final String getterName = Utils.appendPrefixTo(Utils.toCamelCase(fieldName), prefix);
        final String prefsGetterName = Utils.getPrefsGetter(e);
        return MethodSpec.methodBuilder(getterName)
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("return prefs.$L($L, $L)", prefsGetterName, keyName, Utils.provideDefaultValue(type, e))
                         .returns(type)
                         .build();
    }

    private MethodSpec customGetter(VariableElement e, ConverterData cd) {
        final String fieldName = e.getSimpleName().toString();
        final String keyName = keys.get(e).name;
        TypeMirror fieldType = e.asType();
        if (!fieldType.toString().equals(cd.typeAMirror.toString())) {
            throw new ClassCastException(String.format("Field type %s and converter type %s don't match", fieldType.toString(), cd.typeAMirror.toString()));
        }
        final TypeName returnType = TypeName.get(cd.typeAMirror);
        final ClassName prefType = ClassName.get((TypeElement) Utils.typeUtils.asElement(cd.typeBMirror));
        final String prefix = "fetch";
        final String getterName = Utils.appendPrefixTo(Utils.toCamelCase(fieldName), prefix);
        final String prefsGetterName = "get" + Utils.getPrefsMethodSuffix(prefType.simpleName());
        return MethodSpec.methodBuilder(getterName)
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("return $L.deserialize(prefs.$L($L, $L))", cd.field.name, prefsGetterName, keyName, Utils.provideDefaultValue(prefType, e))
                         .returns(returnType)
                         .addJavadoc("You need to cache this value manually to avoid performance hit during deserialization")
                         .build();
    }

    private MethodSpec customSetter(VariableElement e, ConverterData cd) {
        final String fieldName = e.getSimpleName().toString();
        final String keyName = keys.get(e).name;
        final TypeName paramType = TypeName.get(cd.typeAMirror);
        final ClassName prefType = ClassName.get((TypeElement) Utils.typeUtils.asElement(cd.typeBMirror));
        final String setterName = Utils.appendPrefixTo(Utils.toCamelCase(fieldName), "set");
        //final TypeName type = ClassName.get(e.asType());
        final String prefsSetterName = "put" + Utils.getPrefsMethodSuffix(prefType.simpleName());
        return MethodSpec.methodBuilder(setterName)
                         .addModifiers(PUBLIC, FINAL)
                         .addParameter(paramType, fieldName)
                         .addStatement("edit.$L($L, $L.serialize($L))", prefsSetterName, keyName, cd.field.name, fieldName)
                         .addStatement("edit.apply()")
                         .addStatement("return this")
                         .returns(ClassName.bestGuess(data.getClassName()))
                         .build();
    }

    private MethodSpec apply() {
        return MethodSpec.methodBuilder("apply")
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("edit.apply()")
                         .build();
    }

    private MethodSpec commit() {
        return MethodSpec.methodBuilder("commit")
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("edit.commit()")
                         .build();
    }

    private MethodSpec clear() {
        return MethodSpec.methodBuilder("clearAll")
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("edit.clear()")
                         .addStatement("return this")
                         .returns(ClassName.bestGuess(data.getClassName()))
                         .build();
    }

    private MethodSpec contains(final VariableElement e) {
        final String fieldName = e.getSimpleName().toString();
        final String keyName = keys.get(e).name;
        final String methodName = Utils.appendPrefixTo(Utils.toCamelCase(fieldName), "contains");
        return MethodSpec.methodBuilder(methodName)
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("return prefs.contains($L)", keyName)
                         .returns(TypeName.BOOLEAN)
                         .build();
    }

    private MethodSpec getAll() {
        final ParameterizedTypeName map = ParameterizedTypeName.get(
                ClassName.get("java.util", "Map"),
                Utils.STRING,
                WildcardTypeName.subtypeOf(Object.class));
        return MethodSpec.methodBuilder("getAll")
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("return prefs.getAll()")
                         .returns(map)
                         .build();

    }

    private FieldSpec allKeysField() {
        return FieldSpec.builder(ParameterizedTypeName.get(ClassName.get("java.util", "Map"), Utils.STRING, ClassName.get("java.lang", "Class")),
                "KEYS", PUBLIC, STATIC, FINAL)
                        .initializer("new $T()", ClassName.get("java.util", "HashMap"))
                        .addJavadoc("This field can be used for automatic PreferenceScreen generation")
                        .build();
    }

    private CodeBlock allKeysInit() {
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        for (VariableElement e : data.elements) {
            final String keyName = keys.get(e).name;
            codeBlock.addStatement("KEYS.put($L, $L.class)", keyName, Utils.getFieldSimpleType(e));
        }
        return codeBlock.build();
    }
}
