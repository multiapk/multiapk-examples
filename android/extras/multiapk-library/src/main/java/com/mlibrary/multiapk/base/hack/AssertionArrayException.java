package com.mlibrary.multiapk.base.hack;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AssertionArrayException extends Exception {
    private static final String SYMBOL_SEMICOLON = ";";
    private static final long serialVersionUID = 1;
    private List<Hack.HackDeclaration.HackAssertionException> mAssertionErr;

    AssertionArrayException(String str) {
        super(str);
        this.mAssertionErr = new ArrayList<>();
    }

    public static AssertionArrayException mergeException(AssertionArrayException assertionArrayException, AssertionArrayException assertionArrayException2) {
        if (assertionArrayException == null) {
            return assertionArrayException2;
        }
        if (assertionArrayException2 == null) {
            return assertionArrayException;
        }
        AssertionArrayException assertionArrayException3 = new AssertionArrayException(assertionArrayException.getMessage() + SYMBOL_SEMICOLON + assertionArrayException2.getMessage());
        assertionArrayException3.addException(assertionArrayException.getExceptions());
        assertionArrayException3.addException(assertionArrayException2.getExceptions());
        return assertionArrayException3;
    }

    void addException(Hack.HackDeclaration.HackAssertionException hackAssertionException) {
        this.mAssertionErr.add(hackAssertionException);
    }

    private void addException(List<Hack.HackDeclaration.HackAssertionException> list) {
        this.mAssertionErr.addAll(list);
    }

    private List<Hack.HackDeclaration.HackAssertionException> getExceptions() {
        return this.mAssertionErr;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Hack.HackDeclaration.HackAssertionException hackAssertionException : this.mAssertionErr) {
            stringBuilder.append(hackAssertionException.toString()).append(SYMBOL_SEMICOLON);
            try {
                if (hackAssertionException.getCause() instanceof NoSuchFieldException) {
                    Field[] declaredFields = hackAssertionException.getHackedClass().getDeclaredFields();
                    stringBuilder.append(hackAssertionException.getHackedClass().getName()).append(".").append(hackAssertionException.getHackedFieldName()).append(SYMBOL_SEMICOLON);
                    for (Field field : declaredFields) {
                        stringBuilder.append(field.getName()).append(File.separator);
                    }
                } else if (hackAssertionException.getCause() instanceof NoSuchMethodException) {
                    Method[] declaredMethods = hackAssertionException.getHackedClass().getDeclaredMethods();
                    stringBuilder.append(hackAssertionException.getHackedClass().getName()).append("->").append(hackAssertionException.getHackedMethodName()).append(SYMBOL_SEMICOLON);
                    for (Method declaredMethod : declaredMethods) {
                        if (hackAssertionException.getHackedMethodName().equals(declaredMethod.getName())) {
                            stringBuilder.append(declaredMethod.toGenericString()).append(File.separator);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuilder.append("@@@@");
        }
        return stringBuilder.toString();
    }
}
