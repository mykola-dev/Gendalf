package ds.gendalf.example.converter;

import android.text.TextUtils;

import java.util.*;

import ds.gendalf.Converter;

public class StringListConverter implements Converter<List<String>, String> {
    @Override
    public String serialize(List<String> customItem) {
        return TextUtils.join(";", customItem);
    }

    @Override
    public List<String> deserialize(String prefValue) {
        if (prefValue == null)
            return null;

        return new ArrayList<>(Arrays.asList(prefValue.split(";")));
    }
}
