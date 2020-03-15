/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.bofa.commons.apt4j.management.internal.extension;

import com.bofa.commons.apt4j.management.internal.writable.Writable;
import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.*;
import com.bofa.commons.apt4j.management.internal.extension.FreeMarkerModelGenerator.DefaultModelElementWriterContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link freemarker.template.TemplateDirectiveModel} which allows to recursively write a graph of
 * {@link com.bofa.commons.apt4j.management.internal.writable.Writable}s, with each element using its own template. Elements are
 * imported into the parent template by using this directive like so:
 * {@code <@includeModel object=myProperty/>}.
 *
 * @author Gunnar Morling
 */
public class FreeMarkerModelIncludeDirective implements TemplateDirectiveModel {

    private final Configuration configuration;

    public FreeMarkerModelIncludeDirective(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
                        TemplateDirectiveBody body)
            throws TemplateException, IOException {

        Writable modelElement = getModelElement(params);
        DefaultModelElementWriterContext context = createContext(params);

        try {
            if (modelElement != null) {
                modelElement.write(context, env.getOut());
            }
        } catch (TemplateException | RuntimeException | IOException te) {
            throw te;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    private Writable getModelElement(Map params) {
        if (!params.containsKey("object")) {
            throw new IllegalArgumentException(
                    "Object to be included must be passed to this directive via the 'object' parameter"
            );
        }

        BeanModel objectModel = (BeanModel) params.get("object");

        if (objectModel == null) {
            return null;
        }

        if (!(objectModel.getWrappedObject() instanceof Writable)) {
            throw new IllegalArgumentException("Given object isn't a Writable:" + objectModel.getWrappedObject());
        }

        return (Writable) objectModel.getWrappedObject();
    }

    /**
     * Creates a writer context providing access to the FreeMarker
     * {@link freemarker.template.Configuration} and a map with any additional parameters passed to
     * the directive.
     *
     * @param params The parameter map passed to this directive.
     *
     * @return A writer context.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private DefaultModelElementWriterContext createContext(Map params) {
        Map<String, Object> ext = new HashMap<String, Object>(params);
        ext.remove("object");

        Map<Class<?>, Object> values = new HashMap<>();
        values.put(Configuration.class, configuration);
        values.put(Map.class, ext);

        return new DefaultModelElementWriterContext(values);
    }
}
