package by.daw.api.utils;

import java.io.*;

public class FileIO {

    public static String read(File file, int bufferSize) {
        FileInputStream stream;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

        return read(stream, bufferSize);
    }
    public static String read(InputStream inputStream, int bufferSize) {
        BufferedReader reader;
        StringBuilder fileContent = new StringBuilder();

        char[] buffer = new char[bufferSize];
        int charsReaded;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while((charsReaded = reader.read(buffer)) != -1) {
                fileContent.append(buffer, 0, charsReaded);
            }
            reader.close();

        } catch( IOException e) {
            throw new RuntimeException(e.getMessage());

        }
        return fileContent.toString();
    }




}
