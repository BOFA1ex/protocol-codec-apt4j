package com.bofa.commons.apt4j.management.internal.extension;

import com.bofa.commons.apt4j.management.internal.writable.Writable;
import com.bofa.commons.apt4j.management.internal.writer.IndentationCorrectingWriter;
import freemarker.cache.StrongCacheStorage;
import freemarker.cache.TemplateLoader;
import freemarker.log.Logger;
import freemarker.template.*;

import javax.tools.FileObject;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import com.bofa.commons.apt4j.management.internal.writable.Writable.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bofa1ex
 * @description Writes Java source files based on given mapper models, using a FreeMarker template.
 * copy by {@code org.mapstruct.ap.internal.writer.ModelWriter}
 * @since 2020/3/1
 */
public class FreeMarkerModelGenerator {

    /**
     * FreeMarker configuration. As per the documentation, thread-safe if not
     * altered after original initialization
     */
    private static final Configuration CONFIGURATION;

    static {
        try {
            Logger.selectLoggerLibrary(Logger.LIBRARY_NONE);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        CONFIGURATION = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        CONFIGURATION.setTemplateLoader(new SimpleClasspathLoader());
        CONFIGURATION.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        CONFIGURATION.setSharedVariable(
                "includeModel",
                new FreeMarkerModelIncludeDirective(CONFIGURATION)
        );
        try {
            // logback 全局环境参数
            CONFIGURATION.setSharedVariable("useLogback", false);
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }
        // do not refresh/gc the cached templates, as we never change them at runtime
        CONFIGURATION.setCacheStorage(new StrongCacheStorage());
        CONFIGURATION.setTemplateUpdateDelay(Integer.MAX_VALUE);
        CONFIGURATION.setLocalizedLookup(false);
    }

    /**
     * for test
     */
    public void generateModel(Writable model) {
        try (BufferedWriter writer = new BufferedWriter(
                new IndentationCorrectingWriter(
                        new OutputStreamWriter(System.out) {
                            @Override
                            public void close() {
                                System.out.println();
                            }
                        }
                )
        )) {
            Map<Class<?>, Object> values = new HashMap<>();
            values.put(Configuration.class, CONFIGURATION);
            // 这里会调FreeMarkerModelElementWriter#write方法
            model.write(new DefaultModelElementWriterContext(values), writer);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * apt编译环境正式入口
     */
    public void generateModel(FileObject sourceFile, Writable model) {
        try (BufferedWriter writer = new BufferedWriter(
                new IndentationCorrectingWriter(sourceFile.openWriter())
        )) {
            Map<Class<?>, Object> values = new HashMap<>();
            values.put(Configuration.class, CONFIGURATION);
            // 这里会调FreeMarkerModelElementWriter#write方法
            model.write(new DefaultModelElementWriterContext(values), writer);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Simplified template loader that avoids reading modification timestamps and disables the jar-file caching.
     *
     * @author Andreas Gudian
     */
    private static final class SimpleClasspathLoader implements TemplateLoader {
        @Override
        public Reader getReader(Object name, String encoding) throws IOException {
            URL url = getClass().getClassLoader().getResource(String.valueOf(name));
            if (url == null) {
                throw new IllegalStateException(name + " not found on classpath");
            }
            URLConnection connection = url.openConnection();

            // don't use jar-file caching, as it caused occasionally closed input streams [at least under JDK 1.8.0_25]
            connection.setUseCaches(false);

            InputStream is = connection.getInputStream();

            return new InputStreamReader(is, StandardCharsets.UTF_8);
        }

        @Override
        public long getLastModified(Object templateSource) {
            return 0;
        }

        @Override
        public Object findTemplateSource(String name) throws IOException {
            return name;
        }

        @Override
        public void closeTemplateSource(Object templateSource) throws IOException {
        }
    }

    /**
     * {@link com.bofa.commons.apt4j.management.internal.writable.Writable.Context} implementation which provides access to the current FreeMarker {@link freemarker.template.Configuration}.
     *
     * @author Gunnar Morling
     */
    static class DefaultModelElementWriterContext implements Context {

        private final Map<Class<?>, Object> values;

        DefaultModelElementWriterContext(Map<Class<?>, Object> values) {
            this.values = new HashMap<>(values);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(Class<T> type) {
            return (T) values.get(type);
        }
    }
}
