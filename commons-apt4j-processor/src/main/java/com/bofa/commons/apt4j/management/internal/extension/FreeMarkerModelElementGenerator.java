/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.bofa.commons.apt4j.management.internal.extension;

import com.bofa.commons.apt4j.management.internal.writable.FreeMarkerWritable;
import freemarker.ext.beans.*;
import freemarker.template.*;
import com.bofa.commons.apt4j.management.internal.writable.Writable.Context;

import java.io.Writer;
import java.util.Map;

/**
 * Delegate for writing given {@link com.bofa.commons.apt4j.management.internal.writable.Writable}s into a {@link java.io.Writer} using
 * FreeMarker templates. Any parameters passed to the
 * {@link FreeMarkerModelIncludeDirective} in addition to element itself can be accessed
 * from within the template using the {@code ext} pseudo-element.
 *
 * @author Gunnar Morling
 */
public class FreeMarkerModelElementGenerator {

    public void generate(FreeMarkerWritable writable, Context context, Writer writer) throws Exception {
        Configuration configuration = context.get(Configuration.class);
        Template template = configuration.getTemplate(writable.getTemplateName());
        BeansWrapperBuilder beansWrapperBuilder = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        beansWrapperBuilder.setUseModelCache(true);
        beansWrapperBuilder.setExposeFields(true);
        BeansWrapper beansWrapper = beansWrapperBuilder.build();
        template.process(
                new ExternalParamsTemplateModel(
                        new BeanModel(writable, beansWrapper),
                        new SimpleMapModel(context.get(Map.class), beansWrapper)
                ),
                writer
        );
    }


    private static class ExternalParamsTemplateModel implements TemplateHashModel {

        private final BeanModel object;
        private final SimpleMapModel extParams;

        ExternalParamsTemplateModel(BeanModel object, SimpleMapModel extParams) {
            this.object = object;
            this.extParams = extParams;
        }

        @Override
        public TemplateModel get(String key) throws TemplateModelException {
            if (key.equals("ext")) {
                return extParams;
            } else {
                return object.get(key);
            }
        }

        @Override
        public boolean isEmpty() throws TemplateModelException {
            return object.isEmpty() && extParams.isEmpty();
        }
    }
}
