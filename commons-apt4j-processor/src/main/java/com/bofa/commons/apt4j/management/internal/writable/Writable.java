package com.bofa.commons.apt4j.management.internal.writable;

import java.io.Writer;

/**
 * @author bofa1ex
 * @since 2020/3/1
 * @description An element with the ability to write itself into a given {@link java.io.Writer}.
 * copy by {@code org.mapstruct.ap.internal.writer.Writable}
 */
public interface Writable {

    /**
     * Passed to {@link com.bofa.commons.apt4j.management.internal.writable.Writable}, providing access to additional data specific to a given implementation of the model
     * serialization mechanism.
     *
     * @author Gunnar Morling
     */
    interface Context {

        /**
         * Retrieves the object with the given type from this context.
         *
         * @param type The type of the object to retrieve from this context.
         * @param <T> the type
         * @return The object with the given type from this context.
         */
        <T> T get(Class<T> type);
    }

    /**
     * Writes this element to the given writer.
     *
     * @param context Provides additional data specific to the used implementation of the serialization mechanism.
     * @param writer The writer to write this element to. Must not be closed by implementations.
     * @throws Exception in case of an error
     */
    void write(Context context, Writer writer) throws Exception;
}
