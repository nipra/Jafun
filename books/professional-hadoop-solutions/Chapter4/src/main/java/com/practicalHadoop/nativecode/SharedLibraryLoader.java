package com.practicalHadoop.nativecode;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import com.google.common.io.Files;

public class SharedLibraryLoader {
    private File tempPath;
    private File dataConversionLibraryPath; 

    /**
     * Constructor
     */
    public SharedLibraryLoader() {
        tempPath = Files.createTempDir();
    }

    public void cleanup() throws IOException {
        Files.deleteRecursively(tempPath);
    }

    private void loadLibrary(String libraryName) throws IOException {
        URL url = this.getClass().getClassLoader().getResource(libraryName);
        File library = new File(tempPath, libraryName);
        FileUtils.copyURLToFile(url, library);
        library.setExecutable(true);
        System.load(library.getAbsolutePath());
    }
}