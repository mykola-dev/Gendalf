package ds.gendalf.example.converter;

import ds.gendalf.Converter;
import ds.gendalf.example.data.Guitar;

public class GuitarToStringConverter implements Converter<Guitar, String> {
    @Override
    public String serialize(Guitar g) {
        return String.format("%s~%s~%s", g.type, g.price, g.color);
    }

    @Override
    public Guitar deserialize(String prefValue) {
        String[] splits = prefValue.split("~");
        return new Guitar(splits[0], Float.valueOf(splits[1]), Integer.valueOf(splits[2]));
    }
}
