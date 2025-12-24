package notification.common;

public class EnumParser {
    public static <T extends Enum<T>> T parse(
            Class<T> enumType,
            String value,
            String fieldName
    ) {
        try {
            return Enum.valueOf(enumType, value);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid value for field '" + fieldName + "': " + value
            );
        }
    }
}
