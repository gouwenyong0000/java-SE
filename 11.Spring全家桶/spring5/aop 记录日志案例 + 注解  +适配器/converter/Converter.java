package com.example.springboot.aop.converter;

public interface Converter<T> {
    public String converterToId(T o);
}
