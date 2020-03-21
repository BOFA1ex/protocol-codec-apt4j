package com.bofa.commons.apt4j.management.protocol.model.decode;

import com.bofa.commons.apt4j.management.internal.model.MethodHeadModel;
import com.bofa.commons.apt4j.management.internal.model.TypeModel;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.common.*;
import lombok.*;

import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.protocol.model
 * @date 2020/3/3
 */
@Getter
@Setter
@Builder
public class ProtocolDecode$1 extends JavaModelWritable {

    /* method-head */
    private MethodHeadModel method_head;
    /* parameters */
    private String buffer_parameter;
    private String channel_parameter;
    private String standard_reader_index_parameter;
    /* anon model */
    private ByteBufConvertAnonModel convert_model;
    /* common-structure model */
    private ProtocolSpelProcessExpr part0;
    private ProtocolDecodePart$0 part1;
    /* 待解析对象类型名 */
    private TypeModel decode_type;
    private String decode_element_name;
    /** 待解析对象类型 */
    private boolean is_not_primitive;
    private boolean is_spel_object;
    private boolean is_collection;
    private boolean is_un_root;
    /** 待解析内部元素集合 */
    private volatile List<InternalModelContext> member_mappings;
    /* 缓冲区和基准readerIndex的混淆名 */
    private String confused_buffer_name;
    private String confused_standard_reader_index_name;

    public void addAllInternalModelContext(List<InternalModelContext> internalModelContexts){
        this.member_mappings.addAll(internalModelContexts);
        internalModelContexts.forEach(internalModelContext -> super.import_stats.addAll(internalModelContext.getImport_stats()));
    }

    public synchronized void addInternalModelContext(InternalModelContext internalModelContext){
        this.member_mappings.add(internalModelContext);
        super.import_stats.addAll(internalModelContext.getImport_stats());
    }

    public Set<String> getStatic_import_stats() {
        return static_import_stats;
    }

    public Set<String> getImport_stats() {
        super.import_stats.addAll(method_head.getImport_stats());
        super.import_stats.addAll(part0.getImport_stats());
        super.import_stats.addAll(part1.getImport_stats());
        super.import_stats.addAll(decode_type.getImport_stats());
        super.import_stats.addAll(convert_model.getImport_stats());
        return import_stats;
    }
}
