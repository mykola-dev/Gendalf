package ds.gendalf.example.converter;

import com.google.gson.Gson;

import ds.gendalf.TypedConverter;

public class GsonConverter<T> extends TypedConverter<T, String> {

    Gson gson = new Gson();

    public GsonConverter(Class<T> srcClass, Class<String> dstClass) {
        super(srcClass, dstClass);
    }

    @Override
    public String serialize(T obj) {
        return gson.toJson(obj,getSrcClass());
    }

    @Override
    public T deserialize(String json) {
        return gson.fromJson(json, getSrcClass());
    }
}
