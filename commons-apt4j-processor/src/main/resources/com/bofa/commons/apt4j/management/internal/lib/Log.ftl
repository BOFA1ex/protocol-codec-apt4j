<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#macro build_logger clazz>
    private static final Logger logger = LoggerFactory.getLogger(${clazz}.class);
</#macro>
<#-- @see FreeMarkerModelGenerator shareVariables -->
<#macro info ext>
    <#if useLogback>
        logger.info(<#nested>);
    <#else>
        <@compress single_line=true>System.out.println(String.format(<#lt><#nested>), <#list ext as item>${item}<#if item_has_next>, </#if></#list>);</@compress>
    </#if>
</#macro>
<#macro debug ext>
    <#if useLogback>
        logger.debug(<#nested>);
    <#else>
       <@compress single_line=true>System.out.println(String.format(<#lt><#nested >), <#list ext as item>${item}<#if item_has_next>, </#if></#list>);</@compress>
    </#if>
</#macro>
<#macro error ext>
    <#if useLogback>
        logger.error(<#nested>);
    <#else>
        <@compress single_line=true>System.err.println(String.format(<#nested >, <#list ext as item>${item}<#if item_has_next>, </#if></#list>));</@compress>
    </#if>
</#macro>