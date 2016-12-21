package com.mlibrary.patch.framework;

class BundleException extends Exception {
    BundleException(String detailMessage) {
        super(detailMessage);
    }

    BundleException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
