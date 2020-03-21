package com.bofa.commons.apt4j.management.protocol.model;

import com.bofa.commons.apt4j.management.internal.model.TypeHeadModel;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecode$0;
import com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecode$1;
import com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncode$0;
import com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncode$1;
import com.google.common.base.Strings;
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
    private TypeHeadModel type_head;
    private ProtocolDecode$0 decode_root_element;
    private List<ProtocolDecode$1> decode_elements;
    private ProtocolEncode$0 encode_root_element;
    private List<ProtocolEncode$1> encode_elements;
    private List<JavaModelWritable> common_methods;
    private Set<String> autowires;

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        if (autowires.size() != 0) {
            import_stats.add("org.springframework.beans.factory.annotation.Autowired");
        }
        return import_stats;
    }

    public void setDecode_root_element(ProtocolDecode$0 decode_root_element) {
        this.decode_root_element = decode_root_element;
        super.import_stats.addAll(decode_root_element.getImport_stats());
    }

    public void setEncode_root_element(ProtocolEncode$0 encode_root_element) {
        this.encode_root_element = encode_root_element;
        super.import_stats.addAll(encode_root_element.getImport_stats());
    }

    public synchronized void addAutoWire(String autowireName) {
        // convertMethod为void的情况, 返回的autowireName返回null
        if (Strings.isNullOrEmpty(autowireName)) {
            return;
        }
        autowires.add(autowireName);
    }

    public synchronized void addDecodeElement(ProtocolDecode$1 ele) {
        decode_elements.add(ele);
        import_stats.addAll(ele.getImport_stats());
    }

    public synchronized void addEncodeElement(ProtocolEncode$1 ele) {
        encode_elements.add(ele);
        import_stats.addAll(ele.getImport_stats());
    }

    public synchronized void addCommonElement(JavaModelWritable ele) {
        common_methods.add(ele);
        import_stats.addAll(ele.getImport_stats());
    }
}
