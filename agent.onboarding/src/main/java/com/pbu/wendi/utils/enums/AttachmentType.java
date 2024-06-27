package com.pbu.wendi.utils.enums;

public enum AttachmentType {
    UNKNOWN(0),
    DOCX(1),
    XLXS(2),
    PDF(3),
    PNG(4),
    JPG(5),
    TXT(6),
    EPUB(0);

    private final int value;

    AttachmentType(int value) {
        this.value = value;
    }

    public int getInteger() {
        return value;
    }
    public static AttachmentType convertToEnum(int value) {
        for (AttachmentType type : AttachmentType.values()) {
            if (type.getInteger() == value) {
                return type;
            }
        }
        return AttachmentType.UNKNOWN;
    }
}