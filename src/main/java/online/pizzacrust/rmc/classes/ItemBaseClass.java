package online.pizzacrust.rmc.classes;

import java.util.ArrayList;
import java.util.List;

public class ItemBaseClass implements ModelClass {

    private final List<PropertyValue> values = new ArrayList<>();
    private final String className;
    private final String content; // protectedstring
    private final List<ItemBaseClass> childrenClasses = new ArrayList<>();

    public List<ItemBaseClass> getChildrenClasses() {
        return childrenClasses;
    }

    public List<PropertyValue> getValues() {
        return values;
    }

    public String getClassName() {
        return className;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<Item class=\"").append(className).append("\">");
        stringBuilder.append("<Properties>");
        values.forEach((propertyValue -> stringBuilder.append(propertyValue.toString())));
        if (content != null) {
            stringBuilder.append("<ProtectedString name=\"Source\">").append(content).append
                    ("</ProtectedString>");
        }
        stringBuilder.append("</Properties>");
        childrenClasses.forEach((i) -> stringBuilder.append(i.getXML()));
        stringBuilder.append("</Item>");
        return stringBuilder.toString();
    }

    public static class PropertyValue {
        private final String className; // double, string etc
        private final String propertyName;
        private String propertyValue;

        public PropertyValue(String className, String propertyName, String propertyValue) {
            this.className = className;
            this.propertyName = propertyName;
            this.propertyValue = propertyValue;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<").append(className).append(" name=\"").append(propertyName)
                    .append("\">");
            if (propertyValue != null) {
                stringBuilder.append(propertyValue);
            }
            stringBuilder.append("<").append("/").append(className).append(">");
            return stringBuilder.toString();
        }
    }

    public ItemBaseClass(List<PropertyValue> values, String className, String elementName,
                         List<ItemBaseClass> children, String content) {
        values.add(new PropertyValue("string", "Name", elementName));
        this.values.addAll(values);
        this.className = className;
        this.childrenClasses.addAll(children);
        this.content = content;
    }

}
