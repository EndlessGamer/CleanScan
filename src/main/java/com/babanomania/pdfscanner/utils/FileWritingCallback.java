package com.pandasdroid.scanner.utils;

import java.io.FileOutputStream;
import java.io.IOException;

public interface FileWritingCallback {

    public void write(FileOutputStream out) throws IOException;
}
