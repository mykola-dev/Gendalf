package ds.gendalf;

abstract public class TypedConverter<T, K> implements Converter<T, K> {
    private Class<T> srcClass;
    private Class<K> dstClass;

    public TypedConverter(Class<T> srcClass, Class<K> dstClass) {
        this.srcClass = srcClass;
        this.dstClass = dstClass;
    }

    public Class<T> getSrcClass() {
        return srcClass;
    }

    public Class<K> getDstClass() {
        return dstClass;
    }
}
