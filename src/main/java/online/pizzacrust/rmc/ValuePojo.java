package online.pizzacrust.rmc;

import com.google.gson.annotations.SerializedName;

public class ValuePojo {

    public Type type; // number, string, etc
    public String name;
    public String value;

    public enum Type {
        @SerializedName("number")
        NUMBER("double", "NumberValue"),
        @SerializedName("string")
        STRING("string", "StringValue");

        private final String primitiveType;
        private final String className;

        Type(String primitiveType, String className) {
            this.primitiveType = primitiveType;
            this.className = className;
        }

        public String getClassName() {
            return className;
        }

        public String getPrimitiveType() {
            return primitiveType;
        }
    }

}
