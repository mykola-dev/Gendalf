package ds.gendalf;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import static javax.lang.model.element.Modifier.*;

final class CodeGenerator {


    private ClassData data;
    private ClassName context = ClassName.get("android.content", "Context");
    private ClassName prefs = ClassName.get("android.content", "SharedPreferences");
    private ClassName editor = ClassName.get("android.content", "SharedPreferences.Editor");


    CodeGenerator(ClassData data) {
        this.data = data;
    }


    TypeSpec generateClass() {
        List<MethodSpec> methods = new ArrayList<>();
        for (VariableElement e : data.elements) {
            methods.add(getter(e));
            methods.add(setter(e));
            methods.add(contains(e));
        }

        return TypeSpec.classBuilder(data.getClassName())
                       .addField(prefs, "prefs", Modifier.PRIVATE)
                       .addField(editor, "edit", Modifier.PRIVATE)
                       .addMethod(constructor())
                       .addMethod(with())
                       .addModifiers(PUBLIC)
                .addMethods(methods)
                        //.addMethod(apply())
                .addMethod(commit())
                .addMethod(getAll())
                .addMethod(clear())
                .addMethod(getPrefs())
                .build();
    }

    private MethodSpec getPrefs() {
        return MethodSpec.methodBuilder("with")
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
        final String setterName = Utils.appendPrefixTo(Utils.provideGetSetName(fieldName), "set");
        final TypeName type = ClassName.get(e.asType());
        final String prefsSetterName = Utils.getPrefsSetter(e);
        return MethodSpec.methodBuilder(setterName)
                         .addModifiers(PUBLIC, FINAL)
                         .addParameter(type, fieldName)
                         .addStatement("edit.$L($S, $L)", prefsSetterName, fieldName, fieldName)
                         .addStatement("edit.apply()")
                         .addStatement("return this")
                         .returns(ClassName.bestGuess(data.getClassName()))
                         .build();
    }


    private MethodSpec getter(VariableElement e) {
        final String fieldName = e.getSimpleName().toString();
        final TypeName type = ClassName.get(e.asType());
        final String prefix = (type == TypeName.BOOLEAN) ? "is" : "get";
        final String getterName = Utils.appendPrefixTo(Utils.provideGetSetName(fieldName), prefix);
        final String prefsGetterName = Utils.getPrefsGetter(e);
        return MethodSpec.methodBuilder(getterName)
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("return prefs.$L($S, $L)", prefsGetterName, fieldName, Utils.provideDefaultValue(type, e))
                         .returns(type)
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
        final String methodName = Utils.appendPrefixTo(Utils.provideGetSetName(fieldName), "contains");
        return MethodSpec.methodBuilder(methodName)
                         .addModifiers(PUBLIC, FINAL)
                         .addStatement("return prefs.contains($S)", fieldName)
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


}
