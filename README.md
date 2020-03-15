# commons-apt4j

目前就集成了protocol解/编码的processor, 已完成单元测试
protocol-manager项目需要配合protocol-processor实现其功能

protocol-processor 其内部部分实现, 参考mapstruct

```java
@SpelMapping("flvFile")
public class FlvFile {
    @ByteBufConvert(index = "0", length = "3", convertMethod = AsciiConvertMethod.class)
    private String signature;
    @ByteBufConvert(index = "3", length = "1", convertMethod = IntegerConvertMethod.class)
    private Integer version;
    @ByteBufConvert(index = "4", length = "1", convertMethod = BinaryIntegerConvertMethod.class, parameters = {"0", "4"})
    private Integer typeFlagsReserved;
    @ByteBufConvert(index = "4", length = "1", convertMethod = BinaryIntegerConvertMethod.class, parameters = {"4", "5"})
    private Integer typeFlagsAudio;
    @ByteBufConvert(index = "4", length = "1", convertMethod = BinaryIntegerConvertMethod.class, parameters = {"5", "6"})
    private Integer typeFlagsReserved2;
    @ByteBufConvert(index = "4", length = "1", convertMethod = BinaryIntegerConvertMethod.class, parameters = {"6", "7"})
    private Integer typeFlagsVideo;
    @ByteBufConvert(index = "5", length = "4", convertMethod = IntegerConvertMethod.class)
    private Integer dataOffset;
    @ByteBufConvert(index = "9", length = "#flvFile_buffer.readableBytes()", parameters = {"java.util.LinkedList"})
    private List<FlvTag> flvTags;
}
```

```java
@Protocol(implName = "FlvParserImpl")
public interface FlvParser {

    @ByteBufDecode(resolveException = FlvDecodeResolveExceptionMethod.class)
    FlvFile decode(ByteBuf buffer, Channel channel);

    @ByteBufEncode
    ByteBuf encode(FlvFile flvFile, Channel channel);
}
```

apt 内部编译后, 可通过spring容器 autowired注入实现类bean或者通过Parsers.getParser(xxx.class)获取

整体实现跟mapstruct类似.

后续会更进validation优先级校验, 包括encode阶段密钥策略方面对数据域的aes/hmac-sha 256加密混淆.

后续在protocol-manager项目中会接入更多的项目, mqtt, coap, zigbee, quic等协议保证其兼容性.
