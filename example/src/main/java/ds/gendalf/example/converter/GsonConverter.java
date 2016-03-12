package ds.gendalf.example.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ds.gendalf.Converter;

public class GsonConverter<T> implements Converter<T, String> {

    Gson gson = new Gson();

    @Override
    public String serialize(T obj) {
        return gson.toJson(obj);
    }

    @Override
    public T deserialize(String json) {
        Type t = new TypeToken<T>() {}.getType();
        return gson.fromJson(json, t);
    }
}
