<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.ProtocolImpl" -->
<#import "../../internal/lib/ModelBuilder.ftl" as builder/>
<#import "../../internal/lib/Log.ftl" as logger/>
package ${package_name};

<#list import_stats as import_stat>
import ${import_stat};
</#list>

${javadoc()}
<#if useComponent>@Component</#if>
<@builder.build_type_head type_head>
    <#if useLogback>
        <@logger.build_logger type_head.class_type.type_name/>
    </#if>
    <#if useComponent>
        <#lt><#list autowires as autowire>
            @Autowired
            private ${autowire} ${autowire?uncap_first};
        </#list>
    </#if>
    <#if encode_root_element??>
        <@includeModel object=encode_root_element/>
    </#if>
    <#if encode_elements?? && encode_elements?has_content>
        <#list encode_elements as encode_element>
            <@includeModel object=encode_element/>
        </#list>
    </#if>
    <#if decode_root_element??>
        <@includeModel object=decode_root_element/>
    </#if>
    <#if decode_elements?? && decode_elements?has_content>
        <#list decode_elements as decode_element>
            <@includeModel object=decode_element/>
        </#list>
    </#if>
    <#if common_methods?? && common_methods?has_content>
        <#list common_methods as common_method>
            <@includeModel object=common_method/>
        </#list>
    </#if>
</@builder.build_type_head>
