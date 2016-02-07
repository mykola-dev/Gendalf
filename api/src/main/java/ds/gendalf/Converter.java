package ds.gendalf;

public interface Converter<T, K> {
    K serialize(T customItem);
    T deserialize(K prefValue);
}
