package com.bofa.commons.apt4j.management.protocol.model;

import com.bofa.commons.apt4j.management.internal.model.TypeHeadModel;
import com.bofa.commons.apt4j.management.internal.writable.JavaModelWritable;
import com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolOverrideDecodeMethod;
import com.bofa.commons.apt4j.management.protocol.model.decode.ProtocolDecodeInternalMethod;
import com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolOverrideEncodeMethod;
import com.bofa.commons.apt4j.management.protocol.model.encode.ProtocolEncodeInternalMethod;
import com.google.common.base.Strings;
import lombok.*;

import java.util.*;

import static com.bofa.commons.apt4j.management.protocol.constant.ProtocolGenerateConstant.CHANNEL_UTILS_QUALIFIER_NAME;


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
    private List<ProtocolOverrideDecodeMethod> decode_root_elements;
    private List<ProtocolDecodeInternalMethod> decode_elements;
    private List<ProtocolOverrideEncodeMethod> encode_root_elements;
    private List<ProtocolEncodeInternalMethod> encode_elements;
    private List<JavaModelWritable> common_methods;
    private Set<String> autowires;

    public Set<String> getStatic_import_stats() {
        return null;
    }

    public Set<String> getImport_stats() {
        if (autowires.size() != 0) {
            super.import_stats.add("org.springframework.beans.factory.annotation.Autowired");
        }
        super.import_stats.add(CHANNEL_UTILS_QUALIFIER_NAME);
        // 移除null值
        super.import_stats.remove(null);
        return import_stats;
    }

    public void addDecode_root_element(ProtocolOverrideDecodeMethod decode_root_element) {
        this.decode_root_elements.add(decode_root_element);
        super.import_stats.addAll(decode_root_element.getImport_stats());
    }

    public void addEncode_root_element(ProtocolOverrideEncodeMethod encode_root_element) {
        this.encode_root_elements.add(encode_root_element);
        super.import_stats.addAll(encode_root_element.getImport_stats());
    }

    public synchronized void addAutoWire(String autowireName) {
        // convertMethod为void的情况, 返回的autowireName返回null
        if (Strings.isNullOrEmpty(autowireName)) {
            return;
        }
        autowires.add(autowireName);
    }

    public synchronized void addDecodeElement(ProtocolDecodeInternalMethod ele) {
        decode_elements.add(ele);
        import_stats.addAll(ele.getImport_stats());
    }

    public synchronized void addEncodeElement(ProtocolEncodeInternalMethod ele) {
        encode_elements.add(ele);
        import_stats.addAll(ele.getImport_stats());
    }

    public synchronized void addCommonElement(JavaModelWritable ele) {
        common_methods.add(ele);
        import_stats.addAll(ele.getImport_stats());
    }
}
