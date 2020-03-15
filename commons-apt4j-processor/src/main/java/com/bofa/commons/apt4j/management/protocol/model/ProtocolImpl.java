package com.bofa.commons.apt4j.management.protocol.model;

import com.bofa.commons.apt4j.management.internal.model.TypeHead;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecode$0;
import com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecode$1;
import com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncode$0;
import com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncode$1;
import lombok.*;

import java.util.*;


/*
 * @author bofa1ex
 * @version 1.0
 * @package com.bofa.commons.apt4j.management.protocol.model
 * @date 2020/3/3
 */
@Getter
@Setter
@Builder
public class ProtocolImpl extends JavaModelWritable {

    private String package_name;
    private TypeHead type_head;
    private ProtocolDecode$0 decode_root_element;
    private List<ProtocolDecode$1> decode_elements;
    private ProtocolEncode$0 encode_root_element;
    private List<ProtocolEncode$1> encode_elements;
    private List<JavaModelWritable> common_methods;

    public ProtocolImpl(String package_name, TypeHead type_head, ProtocolDecode$0 decode_root_element, List<ProtocolDecode$1> decode_elements, ProtocolEncode$0 encode_root_element, List<ProtocolEncode$1> encode_elements, List<JavaModelWritable> common_methods) {
        this.package_name = package_name;
        this.type_head = type_head;
        this.decode_root_element = decode_root_element;
        this.decode_elements = decode_elements;
        this.encode_root_element = encode_root_element;
        this.encode_elements = encode_elements;
        this.common_methods = common_methods;
    }

    public ProtocolImpl(String package_name, TypeHead type_head) {
        this(package_name, type_head, null, null, null, null, null);
    }

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        return import_stats;
    }

    public synchronized void addDecodeElement(ProtocolDecode$1 ele) {
        if (this.decode_elements == null) {
            decode_elements = new LinkedList<>();
        }
        decode_elements.add(ele);
        import_stats.addAll(ele.getImport_stats());
    }

    public synchronized void addEncodeElement(ProtocolEncode$1 ele) {
        if (this.encode_elements == null) {
            encode_elements = new LinkedList<>();
        }
        encode_elements.add(ele);
        import_stats.addAll(ele.getImport_stats());
    }

    public synchronized void addCommonElement(JavaModelWritable ele) {
        if (this.common_methods == null) {
            common_methods = new LinkedList<>();
        }
        common_methods.add(ele);
        import_stats.addAll(ele.getImport_stats());
    }
}
