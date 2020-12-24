package ru.ivmiit.web.model;

import lombok.Getter;

import java.util.Date;

public enum ModelColumnType {
    String(java.lang.String.class), Base64(java.lang.String.class),
    Number(Double.class), Date(Date.class);

    @Getter
    private Class<?> javaClass;

    ModelColumnType(Class<?> javaClass) {
        this.javaClass = javaClass;
    }
}
