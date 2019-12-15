package com.adenon.sp.kernel.utils.codegen.test;

public class Snippet {

    private static Class<?> getTypeOf(final Class<?> clazz) {
        final Class<?> type = clazz.getComponentType();
        if (type == null) {
            return clazz;
        }
        return getTypeOf(type);
    }

    public static void main(final String[] args) {
        System.out.println(getTypeOf(int.class));
        System.out.println(getTypeOf(int[].class));
        System.out.println(getTypeOf(int[][].class));
        System.out.println(getTypeOf(String[][].class));
        System.out.println(getTypeOf(String[].class));
        System.out.println(getTypeOf(String.class));
    }

}
