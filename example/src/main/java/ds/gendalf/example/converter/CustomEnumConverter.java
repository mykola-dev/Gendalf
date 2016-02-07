package ds.gendalf.example.converter;

import java.util.*;

import ds.gendalf.Converter;
import ds.gendalf.example.data.Direction;

public class CustomEnumConverter implements Converter<List<Direction>, Set<String>> {

    @Override
    public Set<String> serialize(List<Direction> customItem) {
        Set<String> result = new HashSet<>();
        for (Direction d : customItem) {
            result.add(d.name());
        }
        return result;
    }

    @Override
    public List<Direction> deserialize(Set<String> prefValue) {
        List<Direction> result = new ArrayList<>();
        for (String v : prefValue) {
            try {
                result.add(Direction.valueOf(v));
            } catch (IllegalArgumentException e) {
                result.add(Direction.UNKNOWN);
            }
        }
        return result;
    }
}
