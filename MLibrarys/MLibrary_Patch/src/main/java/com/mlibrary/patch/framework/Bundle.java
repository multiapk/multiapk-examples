package com.mlibrary.patch.framework;

import java.io.InputStream;

public interface Bundle {

    long getBundleId();

    String getLocation();

    void update(InputStream inputStream) throws BundleException;
}
