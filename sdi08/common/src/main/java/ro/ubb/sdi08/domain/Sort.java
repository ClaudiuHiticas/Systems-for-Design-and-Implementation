package ro.ubb.sdi08.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by radu.
 */
public class Sort implements Serializable {
    private static final long serialVersionUID = 2651817750549974262L;
    final private List<FieldOrder> fieldsOrder = new ArrayList<>();

    public Sort(final String... fieldNames) {
        Stream.of(fieldNames)
                .forEach(fieldName ->
                        fieldsOrder
                                .add(
                                        new FieldOrder(fieldName)));
    }

    public Sort(final Direction direction,
                final String... fieldNames) {
        Stream.of(fieldNames)
                .forEach(fieldName ->
                        fieldsOrder
                                .add(
                                        new FieldOrder(
                                                fieldName,
                                                direction)));
    }

    public Sort and(final Sort sort) {
        sort.getFieldsOrder().stream()
                .forEach(fieldOrder ->
                        fieldsOrder
                                .add(fieldOrder));
        return this;
    }

    public List<FieldOrder> getFieldsOrder() {
        return fieldsOrder;
    }

    @Override
    public String toString() {
        return "Sort{" +
                "fieldsOrder=" + fieldsOrder +
                '}';
    }

    public enum Direction implements Serializable {
        ASC, DESC
    }

    public class FieldOrder implements Serializable {
        private static final long serialVersionUID = 6842007428288395090L;
        private final String fieldName;
        private final Direction direction;

        public FieldOrder(final String fieldName) {
            this.fieldName = fieldName;
            this.direction = Direction.ASC;
        }

        public FieldOrder(final String name,
                          final Direction direction) {
            this.fieldName = name;
            this.direction = direction;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Direction getDirection() {
            return direction;
        }

        @Override
        public String toString() {
            return "FieldOrder{" +
                    "fieldName='" + fieldName + '\'' +
                    ", direction=" + direction +
                    '}';
        }
    }
}
