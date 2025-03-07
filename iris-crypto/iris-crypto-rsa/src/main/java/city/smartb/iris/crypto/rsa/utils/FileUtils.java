package city.smartb.iris.crypto.rsa.utils;

import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtils {

    public static final String FILE = "file:";

    public static File getFile(String filename) throws MalformedURLException {
        URL url = getUrl(filename);
        return new File(url.getFile());
    }

    public static URL getUrl(String filename) throws MalformedURLException {
        if(filename.startsWith(FILE)) {
            return new URL(filename);
        }
        return Resources.getResource(filename);
    }

    public static Reader getReader(String filename) throws IOException {
        URL url = getUrl(filename);
        return new InputStreamReader(url.openStream());
    }
}
