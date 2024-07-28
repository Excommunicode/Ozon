package kz.ozon.enums;

import lombok.Getter;

@Getter
public enum ClothingSize {
    XS("Extra Small"),
    S("Small"),
    M("Medium"),
    L("Large"),
    XL("Extra Large"),
    XXL("Double Extra Large");
    private final String description;

    ClothingSize(String description) {
        this.description = description;
    }

    public static ClothingSize fromDescription(String description) {
        for (ClothingSize size : ClothingSize.values()) {
            if (size.getDescription().equalsIgnoreCase(description)) {
                return size;
            }
        }
        throw new IllegalArgumentException("No enum constant with description " + description);
    }
}