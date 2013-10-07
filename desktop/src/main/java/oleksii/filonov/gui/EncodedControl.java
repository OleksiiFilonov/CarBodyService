package oleksii.filonov.gui;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class EncodedControl extends ResourceBundle.Control {

    @Override
    public List<String> getFormats(String basename) {

        if (basename == null) {
            throw new NullPointerException();
        }
        return Arrays.asList("properties");

    }

    @Override
    public ResourceBundle newBundle(final String baseName, final Locale locale, final String format,
                                    final ClassLoader loader, final boolean reload) {

        if (baseName == null || locale == null || format == null || loader == null) {
            throw new NullPointerException();
        }

        ResourceBundle bundle = null;

        if (format.equals("properties")) {

            final String bundleName = toBundleName(baseName, locale);
            final String resourceName = toResourceName(bundleName, format);
            InputStream stream = null;

            try {
                if (reload) {
                    URL url = loader.getResource(resourceName);
                    if (url != null) {
                        final URLConnection connection = url.openConnection();
                        if (connection != null) {
                            connection.setUseCaches(false);
                            stream = connection.getInputStream();
                        }
                    }
                } else {
                    stream = loader.getResourceAsStream(resourceName);
                }

                if (stream != null) {
                    final InputStreamReader is = new InputStreamReader(stream, "UTF-8");
                    bundle = new PropertyResourceBundle(is);
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bundle;

    }
}
