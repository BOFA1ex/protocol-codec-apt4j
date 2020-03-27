# commons-apt4j

ProtocolProcessor

配合<code>@ByteBufValidation</code>, <code>@ByteBufConvert</code>, <code>@Protocol</code>, <code>@CacheMapping</code>等注解

基于模板注解设计模式以及Javac提供的Annotation-processor-util技术(编译前类文件的语法树处理), 
实现协议的<code>Decode/Encode</code>


以下是引用<code>protocol-codec-manager</code>项目的<code>mqtt3.0#connect</code>报文示例
```java
@Data
@ByteBufValidation(
        validate = @ByteBufValidation.Validate(
                index = @ByteBufInternalPoint(step = "1"),
                length = @ByteBufInternalPoint(step = "4")
        ),
        mapper = @ByteBufValidation.Mapper(
                index = @ByteBufInternalPoint(step = "1"),
                length = @ByteBufInternalPoint(step = "4")
        ),
        validateMethod = MqttPacketLengthValidateMethod.class
)
public abstract class AbstractMqttCommand {

    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(step = "1"),
            convertMethod = BinaryIntegerConvertMethod.class,
            parameters = {"0", "4"}
    )
    private Integer packetType;

    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "-1"),
            length = @ByteBufInternalPoint(step = "1"),
            convertMethod = BinaryIntegerConvertMethod.class,
            parameters = {"4", "7"}
    )
    private Integer packetTypeSign;

    /** 这里直接传默认最大长度4字节 */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(step = "4"),
            convertMethod = MqttPacketLengthConvertMethod.class
    )
    private Integer packetLength = 0xffffff7f;
}
---------------------------------- ----------------------------------
@Data
@EqualsAndHashCode(callSuper = true)
@CacheMapping("mqttConnectCommand")
public class MqttConnectCommand extends AbstractMqttCommand {

    /* ******************************** 可变报文头部 固定10字节 ********************************/

    /** 协议名不正确的情况下, 不处理connect报文, 防火墙可以根据该字段来识别mqtt流量 */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "2"),
            length = @ByteBufInternalPoint(step = "4"),
            convertMethod = Utf8ConvertMethod.class
    )
    private String protocolName = "MQTT";

    /** 如果不支持该协议级别, 在connAck报文中响应返回码0x01, 并断开客户端连接 */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(step = "1"),
            convertMethod = IntegerConvertMethod.class
    )
    private Integer protocolLevel = 4;

    /** 如果connect控制报文的保留标志位不为0, 直接断开客户端连接 */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(step = "1"),
            convertMethod = BinaryIntegerConvertMethod.class,
            parameters = {"7", "8"}
    )
    private Integer reserved = 0;

    /** 清理会话, 指定了会话状态的处理方式, 这个标志位控制会话状态的生存时间 */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "-1"),
            length = @ByteBufInternalPoint(step = "1"),
            convertMethod = BinaryIntegerConvertMethod.class,
            parameters = {"6", "7"}
    )
    private Integer cleanSession;

    /**
     * 遗嘱标志, 如果设置为1， 表示如果连接请求被接受了，willQoS消息必须存储在服务端并且与这个网络连接关联.
     * 并且在网络连接关闭时, 服务端必须发布这个遗嘱信息, 除非服务端收到disconnect报文并删除了这个遗嘱信息
     */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "-1"),
            length = @ByteBufInternalPoint(step = "1"),
            convertMethod = BinaryIntegerConvertMethod.class,
            parameters = {"5", "6"}
    )
    private Integer willFlag;

    /** 遗嘱QoS */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "-1"),
            length = @ByteBufInternalPoint(step = "1"),
            convertMethod = BinaryIntegerConvertMethod.class,
            parameters = {"3", "5"}
    )
    private Integer willQoS;

    /** 遗嘱保留 */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "-1"),
            length = @ByteBufInternalPoint(step = "1"),
            convertMethod = BinaryIntegerConvertMethod.class,
            parameters = {"2", "3"}
    )
    private Integer willRetain;

    /** 密码标志 */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "-1"),
            length = @ByteBufInternalPoint(step = "1"),
            convertMethod = BinaryIntegerConvertMethod.class,
            parameters = {"1", "2"}
    )
    private Integer passwordFlag;

    /** 用户名标志 */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "-1"),
            length = @ByteBufInternalPoint(step = "1"),
            convertMethod = BinaryIntegerConvertMethod.class,
            parameters = {"0", "1"}
    )
    private Integer userNameFlag;

    /** 保持连接时间 */
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(step = "2"),
            convertMethod = IntegerConvertMethod.class
    )
    private Integer keepAliveTime;

    /* ******************************** PayLoad ********************************/
    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(step = "2"),
            convertMethod = IntegerConvertMethod.class
    )
    private Integer clientIdLength;

    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(model = @ByteBufInternalModel(
                    key = "mqttConnectCommand", prop = "clientIdLength", keyClazz = MqttConnectCommand.class),
                    type = ByteBufInternalPoint.StepType.MODEL
            ),
            convertMethod = Utf8ConvertMethod.class
    )
    private String clientId;

    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(step = "2"),
            convertMethod = IntegerConvertMethod.class,
            condition = @ByteBufInternalCondition(model = @ByteBufInternalModel(
                    key = "mqttConnectCommand", prop = "willFlag", keyClazz = MqttConnectCommand.class),
                    compareValue = "1"
            )
    )
    private Integer willTopicLength;

    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(model = @ByteBufInternalModel(
                    key = "mqttConnectCommand", prop = "willTopicLength", keyClazz = MqttConnectCommand.class),
                    type = ByteBufInternalPoint.StepType.MODEL
            ),
            convertMethod = Utf8ConvertMethod.class,
            condition = @ByteBufInternalCondition(model = @ByteBufInternalModel(
                    key = "mqttConnectCommand", prop = "willFlag", keyClazz = MqttConnectCommand.class),
                    compareValue = "1"
            )
    )
    private String willTopic;

    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(step = "2"),
            convertMethod = IntegerConvertMethod.class,
            condition = @ByteBufInternalCondition(model = @ByteBufInternalModel(
                    key = "mqttConnectCommand", prop = "userNameFlag", keyClazz = MqttConnectCommand.class),
                    compareValue = "1"
            )
    )
    private Integer useNameLength;

    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(model = @ByteBufInternalModel(
                    key = "mqttConnectCommand", prop = "useNameLength", keyClazz = MqttConnectCommand.class),
                    type = ByteBufInternalPoint.StepType.MODEL
            ),
            convertMethod = Utf8ConvertMethod.class,
            condition = @ByteBufInternalCondition(model = @ByteBufInternalModel(
                    key = "mqttConnectCommand", prop = "userNameFlag", keyClazz = MqttConnectCommand.class),
                    compareValue = "1"
            )
    )
    private String userName;

    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(step = "2"),
            convertMethod = IntegerConvertMethod.class,
            condition = @ByteBufInternalCondition(model = @ByteBufInternalModel(
                    key = "mqttConnectCommand", prop = "passwordFlag", keyClazz = MqttConnectCommand.class),
                    compareValue = "1"
            )
    )
    private Integer passwordLength;

    @ByteBufConvert(
            index = @ByteBufInternalPoint(step = "0"),
            length = @ByteBufInternalPoint(model = @ByteBufInternalModel(
                    key = "mqttConnectCommand", prop = "passwordLength", keyClazz = MqttConnectCommand.class),
                    type = ByteBufInternalPoint.StepType.MODEL
            ),
            convertMethod = Utf8ConvertMethod.class,
            condition = @ByteBufInternalCondition(model = @ByteBufInternalModel(
                    key = "mqttConnectCommand", prop = "passwordFlag", keyClazz = MqttConnectCommand.class),
                    compareValue = "1"
            )
    )
    private String password;
}
```

```java
@Protocol(implName = "MqttParserImpl")
public interface MqttParser {

    @ByteBufEncode(initialCapacity = 2 << 7)
    ByteBuf encode(MqttConnectCommand command, Channel channel);

    @ByteBufDecode()
    MqttConnectCommand decode(ByteBuf buffer, Channel channel);
}

```

这里说明以下不使用JavaPoet依赖的原因, 当模板内容过于复杂(嵌套, 多重策略等), 建议还是模板与代码分离, 便于阅读和维护.
这里内部使用freemarker作为模板依赖, 参考了mapStruct的源码.
更多详情参考protocol-codec-manager项目...

> 之前有想法对guava包的eventbus做apt改造的, 但经思考本地订阅策略并不实用, 还需要维护对应的topic, 就没继续开发了.
> 
> 有好的想法或者建议, 可以提issue, 多谢.
  
  

