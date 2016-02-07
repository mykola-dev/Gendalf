package ds.gendalf;

import com.squareup.javapoet.FieldSpec;

import javax.lang.model.type.TypeMirror;

public class ConverterData {
    public FieldSpec field;
    public TypeMirror typeAMirror;
    public TypeMirror typeBMirror;

    public ConverterData(FieldSpec field, TypeMirror typeAMirror, TypeMirror typeBMirror) {
        this.field = field;
        this.typeAMirror = typeAMirror;
        this.typeBMirror = typeBMirror;
    }
}
