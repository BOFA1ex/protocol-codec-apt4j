package com.bofa.commons.apt4j.management.internal.directive;

import com.bofa.commons.apt4j.annotate.protocol.*;
import com.bofa.commons.apt4j.annotate.protocol.internal.ByteBufInternalModel;
import com.bofa.commons.apt4j.management.internal.utils.TypeUtils;
import com.google.common.base.Strings;
import freemarker.ext.beans.StringModel;
import freemarker.template.*;

import java.util.List;

/**
 * @author bofa1ex
 * @since 2020/3/26
 */
public class MirrorTypeExceptionResolveMethod implements TemplateMethodModelEx {

    @Override
    public Object exec(List arguments) {
        if (arguments == null) {
            return null;
        }
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("resolveMirror方法有且仅有1个参数");
        }
        if (arguments.get(0) == null) {
            return null;
        }
        Object wrappedObject = ((StringModel) arguments.get(0)).getWrappedObject();
        String simpleName = null;
        if (wrappedObject instanceof ByteBufInternalModel) {
            simpleName = TypeUtils.qualifierTypeName2SimpleName(
                    TypeUtils.resolveClassTypeMirrorException(
                            ((ByteBufInternalModel) wrappedObject)::keyClazz)
            );
        }
        if (wrappedObject instanceof ByteBufValidation) {
            simpleName = TypeUtils.qualifierTypeName2SimpleName(
                    TypeUtils.resolveClassTypeMirrorException(
                            ((ByteBufValidation) wrappedObject)::validateMethod)
            );
        }
        if (wrappedObject instanceof ByteBufConvert) {
            simpleName = TypeUtils.qualifierTypeName2SimpleName(
                    TypeUtils.resolveClassTypeMirrorException(
                            ((ByteBufConvert) wrappedObject)::convertMethod)
            );
        }
        if (wrappedObject instanceof ByteBufDecode) {
            simpleName = TypeUtils.qualifierTypeName2SimpleName(
                    TypeUtils.resolveClassTypeMirrorException(
                            ((ByteBufDecode) wrappedObject)::resolveException)
            );
        }
        return simpleName;
    }

}
