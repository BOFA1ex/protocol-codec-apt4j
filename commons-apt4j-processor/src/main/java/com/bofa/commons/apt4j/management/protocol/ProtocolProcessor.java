package com.bofa.commons.apt4j.management.protocol;

import com.bofa.commons.apt4j.annotate.protocol.*;
import com.bofa.commons.apt4j.annotate.cache.CacheMapping;
import com.bofa.commons.apt4j.management.internal.extension.FreeMarkerModelGenerator;
import com.bofa.commons.apt4j.management.internal.model.*;
import com.bofa.commons.apt4j.management.internal.utils.ElementUtils;
import com.bofa.commons.apt4j.management.internal.utils.TypeUtils;
import com.bofa.commons.apt4j.management.protocol.constant.ProtocolGenerateConstant;
import com.bofa.commons.apt4j.management.protocol.model.ProtocolImpl;
import com.bofa.commons.apt4j.management.protocol.model.common.*;
import com.bofa.commons.apt4j.management.protocol.model.decode.*;
import com.bofa.commons.apt4j.management.protocol.model.encode.*;
import com.google.common.base.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import lombok.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author bofa1ex
 * @since 2020/3/1
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.bofa.commons.apt4j.annotate.protocol.Protocol"})
@SupportedOptions({"UseLogback", "UseComponent"})
public class ProtocolProcessor extends AbstractProcessor {
    private static final String LOGBACK_ENV_VARIABLE = "UseLogback";
    private static final String COMPONENT_ENV_VARIABLE = "UseComponent";

    private static final FreeMarkerModelGenerator MODEL_GENERATOR = new FreeMarkerModelGenerator();

    private boolean useLogback = true;
    private boolean useComponent = true;

    /* 打印编译日志工具 */
    private Messager messager;
    /* 处理节点工具 */
    private Elements elementUtils;
    /* 处理节点类型工具 */
    private Types typeUtils;
    /* options */
    private Map<String, String> options;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.elementUtils = processingEnv.getElementUtils();
        this.typeUtils = processingEnv.getTypeUtils();
        this.options = processingEnv.getOptions();
        this.useLogback = Boolean.parseBoolean(options.getOrDefault(LOGBACK_ENV_VARIABLE, "true"));
        this.useComponent = Boolean.parseBoolean(options.getOrDefault(COMPONENT_ENV_VARIABLE, "true"));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 如果此轮注解编译还需要处理
        if (!roundEnv.processingOver()) {
            roundEnv.getElementsAnnotatedWith(Protocol.class).stream()
                    .map(ele -> (Symbol.ClassSymbol) ele)
                    .filter(ele -> {
                        if (ele.getKind().isInterface() || ElementUtils.isAbstractClass(ele)) {
                            return true;
                        }
                        messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "Protocol注解修饰的类不为接口/抽象类, 不处理");
                        return false;
                    })
                    .filter(ele -> {
                        if (notExistImplElement(ele, roundEnv)) {
                            return true;
                        }
                        messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "已存在实现类, 跳过编译");
                        return false;
                    })
                    .forEach(ele -> writeAndFlushModel(ele, preWriteAndFlush(ele)));
        }
        return false;
    }


    /**
     * exist impl element
     *
     * @param element element which kind is interface/abstract class
     */
    boolean notExistImplElement(Element element, RoundEnvironment roundEnv) {
        final Protocol protocolAnon = element.getAnnotation(Protocol.class);
        final String interfaceImplName = protocolAnon.implName();
        return roundEnv.getRootElements().stream().noneMatch(ele -> ele.getSimpleName().toString().equals(interfaceImplName));
    }

    /**
     * prepare to generate freeMarker'models before writeAndFlush it.
     */
    ProtocolImpl preWriteAndFlush(Symbol.ClassSymbol element) {
        final String implName = element.getAnnotation(Protocol.class).implName();
        final String packageName = ElementUtils.getPackageName(elementUtils.getPackageOf(element));
        final boolean needExtends = ElementUtils.isAbstractClass(element);
        final boolean needImplement = element.getKind().isInterface();
        final ProtocolImpl protocolImplModel = ProtocolImpl.builder()
                .package_name(packageName)
                // 类首部
                .type_head(TypeHeadModel.builder()
                        .modifier(TypeHeadModel.DEFAULT_MODIFIER)
                        .impl_type(TypeModel.builder()
                                .type_simple_name(implName)
                                .type_qualifier_name(packageName + "." + implName)
                                .build())
                        // 实现的接口类型
                        .interface_type(needImplement ? new TypeModel(element) : null)
                        // 继承的父类类型
                        .super_type(needExtends ? new TypeModel(element) : null)
                        .needExtends(needExtends)
                        .needImplement(needImplement)
                        .build())
                // 初始化后续需要decode/encode的内部元素以及commons方法
                .decode_elements(Lists.newArrayList())
                .encode_elements(Lists.newArrayList())
                .common_methods(Lists.newArrayList())
                // 需要注入的对象(convertMethods, validationMethods, resolveException)
                .autowires(Sets.newHashSet())
                .build();
        // process logback&component options
        processLogbackOption(protocolImplModel);
        processComponentOption(protocolImplModel);
        // process common methods
        ProtocolCommonProcessor.processCommonMethods(protocolImplModel, element, useLogback);
        element.getEnclosedElements().stream()
                .filter(ele -> ele instanceof ExecutableElement)
                .map(ele -> (Symbol.MethodSymbol) ele)
                // filter interface default method
                .filter(ele -> !ele.isDefault())
                // filter @ByteBufDecode/@ByteBufEncode
                .filter(ele -> ele.getAnnotation(ByteBufDecode.class) != null || ele.getAnnotation(ByteBufEncode.class) != null)
                .forEach(ele -> {
                    final ByteBufDecode byteBufDecodeAnon = ele.getAnnotation(ByteBufDecode.class);
                    final ByteBufEncode byteBufEncodeAnon = ele.getAnnotation(ByteBufEncode.class);
                    if (byteBufEncodeAnon != null) {
                        ProtocolEncodeProcessor.step1_injectContext(elementUtils, typeUtils)
                                .step2_prepareCache(ele, protocolImplModel)
                                .step3_overrideEncode()
                                .step__over();
                    }
                    if (byteBufDecodeAnon != null) {
                        ProtocolDecodeProcessor.step1_injectContext(elementUtils, typeUtils)
                                .step2_prepareCache(ele, protocolImplModel)
                                .step3_overrideDecode()
                                .step__over();
                    }
                });
        return protocolImplModel;
    }

    /** 处理logback 依赖 */
    void processLogbackOption(ProtocolImpl protocolImplModel) {
        final String LOGGER_IMPL_TYPE_QUALIFIER_NAME = "org.slf4j.impl.StaticLoggerBinder";
        if (useLogback) {
            Preconditions.checkNotNull(elementUtils.getTypeElement(LOGGER_IMPL_TYPE_QUALIFIER_NAME), "未找到logback-classic依赖包");
            protocolImplModel.getImport_stats().add("org.slf4j.*");
        }
        FreeMarkerModelGenerator.initConfigurationLogbackEnv(useLogback);
    }

    /** 处理spring-context 依赖 */
    void processComponentOption(ProtocolImpl protocolImplModel) {
        final String COMPONENT_TYPE_QUALIFIER_NAME = "org.springframework.stereotype.Component";
        if (useComponent) {
            Preconditions.checkNotNull(elementUtils.getTypeElement(COMPONENT_TYPE_QUALIFIER_NAME), "未找到spring-context依赖包");
            protocolImplModel.getImport_stats().add(COMPONENT_TYPE_QUALIFIER_NAME);
        }
        FreeMarkerModelGenerator.initConfigurationSpringComponentEnv(useComponent);
    }

    /** write freeMarker models in sourceFile */
    void writeAndFlushModel(Symbol.ClassSymbol ele, ProtocolImpl protocolImpl) {
        final String javaFileSourceName = ele.getAnnotation(Protocol.class).implName();
        try {
            final JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(javaFileSourceName);
            MODEL_GENERATOR.generateModel(sourceFile, protocolImpl);
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, Arrays.toString(e.getStackTrace()));
        }
    }


    static class ProtocolCommonProcessor implements ProtocolGenerateConstant {

        static void processCommonMethods(ProtocolImpl protocolImpl, Symbol.ClassSymbol element, boolean useLogback) {
            // build readSlice method.
            processInitReadSliceMethod(protocolImpl, useLogback);
            // build initChannelCodecContextUtils method
            processInitChannelCodecContextMethod(protocolImpl, element);
        }

        static void processInitReadSliceMethod(ProtocolImpl protocolImpl, boolean useLogback) {
            final boolean isOverride = false;
            final InitReadSliceMethod initMarkAndReadSliceMethod = InitReadSliceMethod.builder()
                    .method_head(MethodHeadModel.builder()
                            .modifier(MethodHeadModel.PRIVATE)
                            .return_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                            .method_name(_READ_SLICE_METHOD_NAME)
                            .method_parameters(Arrays.asList(
                                    new MethodParameterModel(ProtocolGenerateConstant.BYTEBUF_TYPE, _READ_SLICE_METHOD_PARAMETER_BUFFER),
                                    new MethodParameterModel(ProtocolGenerateConstant.INTEGER_TYPE, _READ_SLICE_METHOD_PARAMETER_INDEX),
                                    new MethodParameterModel(ProtocolGenerateConstant.INTEGER_TYPE, _READ_SLICE_METHOD_PARAMETER_LENGTH)
                            ))
                            .is_override(isOverride)
                            .build())
                    .log_message(useLogback ? InitReadSliceMethod.LOGBACK_LOG_MESSAGE : InitReadSliceMethod.SYSTEM_LOG_MESSAGE)
                    .buffer_parameter(_READ_SLICE_METHOD_PARAMETER_BUFFER)
                    .reader_index_parameter(_READ_SLICE_METHOD_PARAMETER_INDEX)
                    .length_parameter(_READ_SLICE_METHOD_PARAMETER_LENGTH)
                    .build();
            protocolImpl.addCommonElement(initMarkAndReadSliceMethod);
        }

        static void processInitChannelCodecContextMethod(ProtocolImpl protocolImpl, Symbol.ClassSymbol element) {
            final Protocol anon = element.getAnnotation(Protocol.class);
            final boolean isOverride = false;
            protocolImpl.addCommonElement(InitChannelCodecContextMethod.builder()
                    .method_head(MethodHeadModel.builder()
                            .modifier(MethodHeadModel.PRIVATE)
                            .return_type(ProtocolGenerateConstant.VOID_TYPE)
                            .method_name(_INIT_METHOD_NAME)
                            .method_parameters(Arrays.asList(
                                    MethodParameterModel.builder()
                                            .param_type(STRING_TYPE)
                                            .param_name(_INIT_METHOD_PARAMETER_OBJECT_NAME)
                                            .build(),
                                    MethodParameterModel.builder()
                                            .param_type(OBJECT_TYPE)
                                            .param_name(_INIT_METHOD_PARAMETER_OBJECT)
                                            .build(),
                                    MethodParameterModel.builder()
                                            .param_type(CHANNEL_TYPE)
                                            .param_name(_INIT_METHOD_PARAMETER_CHANNEL)
                                            .build()
                            ))
                            .is_override(isOverride)
                            .build())
                    .cache_vars(anon.variables())
                    .build()
            );
        }
    }

    @Setter
    static abstract class AbstractProtocolProcessor implements ProtocolGenerateConstant {

        // element typeEnum
        protected static final int PRIMITIVE = 0;
        protected static final int CACHE_OBJ = 1;
        protected static final int COLLECTION = 2;

        /* 处理节点工具 */
        protected Elements elementUtils;
        /* 处理节点类型工具 */
        protected Types typeUtils;

        protected ProtocolImpl protocolImplModel;

        /**
         * 混淆字符串
         */
        protected static String generateObfuscatedName(String original) {
            return "_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5) + original;
        }

        /**
         * 检查element的typeEnum
         */
        protected int checkVariableElementTypeEnum(Symbol symbol) {
            // 需要将varSymbol转classSymbol再获取@CacheMapping
            // fix#issue3 基本类型强转包装类型
            final Symbol.TypeSymbol classTypeElement = TypeUtils.getTypeSymbol(typeUtils, symbol.asType());
            final CacheMapping cacheMapping = classTypeElement.getAnnotation(CacheMapping.class);
            if (cacheMapping != null) {
                return CACHE_OBJ;
            }
            final List<Type> typeArguments = symbol.asType().getTypeArguments();
            return typeArguments == null || typeArguments.size() == 0 ? PRIMITIVE : COLLECTION;
        }

        /**
         * 校验decode返参/encode入参
         * 是否合法
         */
        protected boolean requireElementModifiedByCacheMapping(Symbol element) {
            final int typeEnum = checkVariableElementTypeEnum(element);
            if (typeEnum == PRIMITIVE) {
                throw new RuntimeException("Decode方法返参必须被@CacheMapping修饰");
            }
            if (typeEnum == COLLECTION) {
                final Type genericType = element.asType().getTypeArguments().get(0);
                return requireElementModifiedByCacheMapping((Symbol) typeUtils.asElement(genericType));
            }
            return true;
        }

        /**
         * 递归cacheMapping对象链
         */
        protected List<Symbol.VarSymbol> getTotalVarSymbols(TypeElement typeElement) {
            List<Symbol.VarSymbol> totals = new ArrayList<>();
            // 如果父类不为Object, 继续递归获取被ByteBufConvert注解修饰的内部元素
            if (!typeElement.getSuperclass().toString().equals(ProtocolGenerateConstant.OBJECT_QUALIFIER_NAME)) {
                totals.addAll(getTotalVarSymbols((TypeElement) typeUtils.asElement(typeElement.getSuperclass())));
            }
            totals.addAll(typeElement.getEnclosedElements().stream()
                    .filter(ele -> ele instanceof Symbol.VarSymbol)
                    .map(ele -> ((Symbol.VarSymbol) ele))
                    .filter(ele -> ele.getAnnotation(ByteBufConvert.class) != null)
                    .collect(Collectors.toList()));
            return totals;
        }

        /**
         * 内部元素异步递归, 父级分发完任务后需要同步等待子元素完成任务.
         *
         * @param symbol 父type元素
         *
         * @return 内部元素集合上下文
         */
        protected List<InternalModelContext> step_processInternalRecursive(Symbol.TypeSymbol symbol) {
            final List<Symbol.VarSymbol> internalElements = getTotalVarSymbols((TypeElement) symbol);
            CompletableFuture<?>[] totalTasks = new CompletableFuture[internalElements.size()];
            final List<InternalModelContext> internalModelContexts = IntStream.range(0, internalElements.size())
                    .parallel()
                    .mapToObj(index -> {
                        Symbol.VarSymbol element = internalElements.get(index);
                        final String memberName = element.getSimpleName().toString();
                        final String memberMethodName = generateObfuscatedName(memberName);
                        final boolean unRoot = true;
                        // 异步递归
                        totalTasks[index] = CompletableFuture.runAsync(() -> step_processInternalElements(element, memberMethodName, unRoot));
                        return InternalModelContext.builder()
                                .member_method_name(memberMethodName)
                                .member_name(memberName)
                                .member_type(new TypeModel(element))
                                .build();
                    })
                    .collect(Collectors.toList());
            // 等待欢乐时光结束
            CompletableFuture.allOf(totalTasks).join();
            return internalModelContexts;
        }

        protected abstract void step_processInternalElements(Symbol typeElement, String memberMethodName, boolean unRoot);
    }

    @Setter
    @Getter
    static class ProtocolEncodeProcessor extends AbstractProtocolProcessor {

        private static final String DEFAULT_BUFFER_PARAMETER_NAME = "buffer";

        private MethodParameterModel encodeTypeParameter;
        private String encodeTypeParameterName;
        private MethodParameterModel encodeChannelParameter;
        private String encodeChannelParameterName;

        private ExecutableElement methodElement;
        private Symbol encodeSymbol;

        private ProtocolOverrideEncodeMethod overrideEncode;

        /** 引入节点操作工具 */
        public static ProtocolEncodeProcessor step1_injectContext(Elements elementUtils, Types typeUtils) {
            final ProtocolEncodeProcessor protocolEncodeProcessor = new ProtocolEncodeProcessor();
            protocolEncodeProcessor.setElementUtils(elementUtils);
            protocolEncodeProcessor.setTypeUtils(typeUtils);
            return protocolEncodeProcessor;
        }

        /** 必要参数预先缓存 */
        public ProtocolEncodeProcessor step2_prepareCache(ExecutableElement element, ProtocolImpl protocolImplModel) {
            this.setMethodElement(element);
            this.setProtocolImplModel(protocolImplModel);
            this.setEncodeChannelParameter(element.getParameters().stream()
                    .map(variableEle -> (Symbol) variableEle)
                    // 过滤Channel参数类型
                    .filter(variableEle -> ElementUtils.getSymbolTypeSimpleName(variableEle).equals(ProtocolGenerateConstant.CHANNEL_TYPE_NAME))
                    // inject encodeChannelParameterName
                    .peek(variableEle -> this.setEncodeChannelParameterName(variableEle.getSimpleName().toString()))
                    .map(variableEle -> MethodParameterModel.builder()
                            .param_name(variableEle.getSimpleName().toString())
                            .param_type(new TypeModel(variableEle))
                            .build())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("接口方法定义找不到Channel参数)")));
            this.setEncodeTypeParameter(element.getParameters().stream()
                    .map(variableEle -> (Symbol) variableEle)
                    // 过滤非Channel参数类型, 即<T>
                    .filter(variableEle -> !ElementUtils.getSymbolTypeSimpleName(variableEle).equals(ProtocolGenerateConstant.CHANNEL_TYPE_NAME))
                    // 注入encodeSymbol
                    .peek(variableEle -> this.setEncodeSymbol((Symbol) typeUtils.asElement(variableEle.asType())))
                    // 校验encodeSymbol 必须由@CacheMapping修饰, 或者其泛型类型由@CacheMapping修饰
                    .peek(variableEle -> requireElementModifiedByCacheMapping(encodeSymbol))
                    // 如果encodeSymbol 是集合类型, 拿encode接口定义的parameterName, 反之拿CacheMapping的value
                    .peek(variableEle -> {
                        final CacheMapping cacheAnon = encodeSymbol.getAnnotation(CacheMapping.class);
                        this.setEncodeTypeParameterName(cacheAnon == null ? encodeSymbol.getSimpleName().toString() : cacheAnon.value());
                    })
                    .map(variableEle -> MethodParameterModel.builder()
                            .param_name(encodeTypeParameterName)
                            .param_type(new TypeModel(variableEle))
                            .build())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("接口方法定义找不到<T>参数)")));
            return this;
        }


        /** 实现接口定义的encode方法 */
        public ProtocolEncodeProcessor step3_overrideEncode() {
            // ByteBufEncode Anon properties
            final int initialCapacity = methodElement.getAnnotation(ByteBufEncode.class).initialCapacity();
            final int maxCapacity = methodElement.getAnnotation(ByteBufEncode.class).maxCapacity();
            final TypeModel encodeType = new TypeModel(encodeSymbol);
            final String modifier = methodElement.getModifiers().stream()
                    // 接口定义的方法 modifier默认是public abstract, 需要手动过滤abstract修饰符
                    .filter(mod -> mod != Modifier.ABSTRACT)
                    .map(Modifier::toString)
                    .collect(Collectors.joining(" "));
            // 接口encode方法名
            final String methodName = methodElement.getSimpleName().toString();
            // 混淆 方法名
            final String encodeMethodName = generateObfuscatedName(encodeTypeParameterName);
            // 重写接口定义的encode方法
            this.setOverrideEncode(ProtocolOverrideEncodeMethod.builder()
                    .encode_type(encodeType)
                    .encode_element_name(encodeTypeParameterName)
                    .encode_method_name(encodeMethodName)
                    .encode_parameter(encodeTypeParameterName)
                    .channel_parameter(encodeChannelParameterName)
                    // 校验组需要有执行顺序, 这里用链表
                    .init_validations(Lists.newLinkedList())
                    // 方法首部
                    .method_head(MethodHeadModel.builder()
                            .modifier(modifier)
                            .return_type(BYTEBUF_TYPE)
                            .method_name(methodName)
                            .method_parameters(Arrays.asList(encodeTypeParameter, encodeChannelParameter))
                            .build())
                    // 初始化缓冲区
                    .init_buffer(InitBuffer.builder()
                            .initial_capacity(initialCapacity + "")
                            .max_capacity(maxCapacity + "")
                            .build())
                    .build()
            );
            // inject validation condition
            injectValidateInOverrideEncodeMethod((TypeElement) encodeSymbol);
            // inject ProtocolOverrideEncodeMethod
            this.protocolImplModel.setEncode_root_element(overrideEncode);
            return this;
        }


        /** finally stage */
        public void step__over() {
            final boolean isUnRoot = false;
            final String encodeMethodName = overrideEncode.getEncode_method_name();
            step_processInternalElements(encodeSymbol, encodeMethodName, isUnRoot);
        }

        @Override
        protected void step_processInternalElements(Symbol element, String encodeMethodName, boolean unRoot) {
            // 检查element的类型(primitive/cache_obj/collection)
            final int typeEnum = checkVariableElementTypeEnum(element);
            final ByteBufConvert convertAnon = element.getAnnotation(ByteBufConvert.class);
            boolean isCacheObj = (typeEnum == CACHE_OBJ);
            boolean isCollection = (typeEnum == COLLECTION);
            boolean isNotPrimitive = typeEnum != PRIMITIVE;
            if (isCollection){
                if (unRoot && convertAnon.parameters().length == 0){
                    throw new IllegalArgumentException("集合的泛型类型必须在ByteBufConvert的parameter中指定, 例如parameters = {\"java.util.LinkedList\"}");
                }
                if (!unRoot && methodElement.getAnnotation(ByteBufDecode.class).parameters().length == 0){
                    throw new IllegalArgumentException("集合的泛型类型必须在ByteBufDecode的parameter中指定, 例如parameters = {\"java.util.LinkedList\"}");
                }
            }
            /*
             * Note:
             * varSymbol转classSymbol, 否则@CacheMapping注解获取不到
             * fix#issue3 基本类型强转包装类型
             */
            final Symbol.TypeSymbol typeSymbol = TypeUtils.getTypeSymbol(typeUtils, element.asType());
            final String obfuscateBufferName = unRoot ? generateObfuscatedName(DEFAULT_BUFFER_PARAMETER_NAME) : DEFAULT_BUFFER_PARAMETER_NAME;
            // 如果该元素是cacheObj, 则获取@CacheMapping的value()作为encode_element_name
            final String encodeElementName = isCacheObj ? typeSymbol.getAnnotation(CacheMapping.class).value() : element.getSimpleName().toString();
            /*
             * Note:
             * fix#issue3 这里不能放基本类型作为入参, 必须是包装类型
             * 如果是基本类型就放包装类型, 由于集合类型考虑到泛型擦除, 不能使用typeSymbol, 只能用varSymbol
             */
            final TypeModel encodeTypeModel = new TypeModel(element.asType().getKind().isPrimitive() ? typeSymbol : element);
            final ProtocolEncodeInternalMethod protocolEncodeInternalMethod = ProtocolEncodeInternalMethod.builder()
                    // 方法首部
                    .method_head(MethodHeadModel.builder()
                            .modifier(MethodHeadModel.PRIVATE)
                            // 返参
                            .return_type(ProtocolGenerateConstant.VOID_TYPE)
                            .method_name(encodeMethodName)
                            .is_override(false)
                            // 入参
                            .method_parameters(Arrays.asList(
                                    // ByteBuf
                                    MethodParameterModel.builder()
                                            .param_name(DEFAULT_BUFFER_PARAMETER_NAME)
                                            .param_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                                            .build(),
                                    // <T>
                                    MethodParameterModel.builder()
                                            .param_name(encodeElementName)
                                            .param_type(encodeTypeModel)
                                            .build(),
                                    // channel
                                    encodeChannelParameter
                            ))
                            .build())
                    // index, condition, length 获取缓冲区的解析部分
                    .part0(InitConditionIndexLength.builder()
                            .convert_anon(convertAnon)
                            .buffer_parameter(DEFAULT_BUFFER_PARAMETER_NAME)
                            .channel_parameter(encodeChannelParameterName)
                            .is_decode(false)
                            .build()
                             // 处理ByteBufConvert#convertMethod的import和autowire, 当前前提不为空
                            .processImportAndAutoWire(protocolImplModel))
                    // convertMethod 不为空的解析部分
                    .part1(ProtocolEncodeConvertMethod.builder()
                            .convert_anon(convertAnon)
                            .encode_type(encodeTypeModel)
                            .channel_parameter(encodeChannelParameterName)
                            .encode_element_name(encodeElementName)
                            .confused_buffer_name(obfuscateBufferName)
                            .build())
                    // parameters
                    .buffer_parameter(DEFAULT_BUFFER_PARAMETER_NAME)
                    .channel_parameter(encodeChannelParameterName)
                    // member mappings
                    .member_mappings(Lists.newLinkedList())
                    .encode_type(encodeTypeModel)
                    .encode_element_name(encodeElementName)
                    // judgement
                    .is_not_primitive(isNotPrimitive)
                    .is_cache_obj(isCacheObj)
                    .is_collection(isCollection)
                    .is_un_root(unRoot)
                    // confused part
                    .confused_buffer_name(obfuscateBufferName)
                    .build();
            if (typeEnum == CACHE_OBJ) {
                protocolEncodeInternalMethod.addAllInternalModelContext(step_processInternalRecursive(typeSymbol));
            }
            if (typeEnum == COLLECTION) {
                // 获取集合元素的泛型元素
                final Element genericElement = ElementUtils.getGenericElement(typeUtils, element);
                final Symbol genericClassElement = TypeUtils.getTypeSymbol(typeUtils, genericElement.asType());
                // 生成泛型encode的混淆名
                final String genericEncodeMethodName = generateObfuscatedName(genericElement.getSimpleName().toString());
                // 把泛型注入到集合元素的memberMapping中
                protocolEncodeInternalMethod.addInternalModelContext(InternalModelContext.builder()
                        .member_method_name(genericEncodeMethodName)
                        .member_name(ElementUtils.getSymbolTypeSimpleName(genericClassElement))
                        .member_type(new TypeModel(genericClassElement))
                        .build()
                );
                step_processInternalElements(genericClassElement, genericEncodeMethodName, false);
            }
            protocolImplModel.addEncodeElement(protocolEncodeInternalMethod);
        }

        /** inject validate operations before encode */
        private void injectValidateInOverrideEncodeMethod(TypeElement typeElement) {
            final ByteBufValidationGroup validationGroup = typeElement.getAnnotation(ByteBufValidationGroup.class);
            final ByteBufValidation validation = typeElement.getAnnotation(ByteBufValidation.class);
            if (validation == null && validationGroup == null){
                final Element superElement = typeUtils.asElement(typeElement.getSuperclass());
                // 如果父类不为Object, 递归父类是否有@ByteBufValidation
                if (superElement.toString().equals(ProtocolGenerateConstant.OBJECT_QUALIFIER_NAME)){
                    return;
                }
                injectValidateInOverrideEncodeMethod((TypeElement) superElement);
                return;
            }
            ByteBufValidation[] validations = null;
            if (validationGroup != null){
                validations = validationGroup.value();
                if (validations.length == 0) {
                    return;
                }
            }
            if (validation != null){
                validations = new ByteBufValidation[]{validation};
            }
            // 根据优先级order排序执行校验
            Arrays.stream(validations).sorted(Comparator.comparing(ByteBufValidation::order)).forEach(byteBufValidationAnon ->
                this.overrideEncode.addInit_validation(InitValidation.builder()
                        .validation_anon(byteBufValidationAnon)
                        .buffer_parameter(DEFAULT_BUFFER_PARAMETER_NAME)
                        .channel_parameter(encodeChannelParameterName)
                        // validate/mapper flag
                        .is_validate(false)
                        .build()
                        // 处理import和autowire
                        .processImportAndAutoWire(protocolImplModel))
            );
        }
    }

    @Setter
    static class ProtocolDecodeProcessor extends AbstractProtocolProcessor {

        private MethodParameterModel decodeByteBufParameter;
        private String decodeByteBufParameterName;
        private MethodParameterModel decodeChannelParameter;
        private String decodeChannelParameterName;

        private ExecutableElement methodElement;
        private Symbol methodReturnTypeElement;

        private ProtocolOverrideDecodeMethod overrideDecode;

        /* 引入节点操作工具 */
        public static ProtocolDecodeProcessor step1_injectContext(Elements elementUtils, Types typeUtils) {
            final ProtocolDecodeProcessor protocolDecodeProcessor = new ProtocolDecodeProcessor();
            protocolDecodeProcessor.setElementUtils(elementUtils);
            protocolDecodeProcessor.setTypeUtils(typeUtils);
            return protocolDecodeProcessor;
        }

        /* 必要参数预先缓存 */
        public ProtocolDecodeProcessor step2_prepareCache(ExecutableElement element, ProtocolImpl protocolImplModel) {
            this.setMethodElement(element);
            this.setProtocolImplModel(protocolImplModel);
            this.setMethodReturnTypeElement(TypeUtils.getTypeSymbol(typeUtils, element.getReturnType()));
            // 校验returnType必须由@CacheMapping, 或者其泛型类型由@CacheMapping修饰
            requireElementModifiedByCacheMapping(methodReturnTypeElement);
            this.setDecodeChannelParameter(element.getParameters().stream()
                    // VariableElement 强转 Symbol
                    .map(variableEle -> (Symbol) variableEle)
                    // variable元素是否为channel类型
                    .filter(variableEle -> ElementUtils.getSymbolTypeSimpleName(variableEle).equals(ProtocolGenerateConstant.CHANNEL_TYPE_NAME))
                    // inject decodeChannelParameterName
                    .peek(variableEle -> this.setDecodeChannelParameterName(variableEle.getSimpleName().toString()))
                    // generate MethodParameterModel
                    .map(variableEle -> MethodParameterModel.builder()
                            .param_name(variableEle.getSimpleName().toString())
                            .param_type(new TypeModel(variableEle))
                            .build())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("接口方法定义找不到Channel参数)")));
            this.setDecodeByteBufParameter(element.getParameters().stream()
                    .filter(variableEle -> ElementUtils.getSymbolTypeSimpleName((Symbol) variableEle).equals(ProtocolGenerateConstant.BYTEBUF_TYPE_NAME))
                    .map(variableEle -> (Symbol) variableEle)
                    // inject decodeByteBufParameterName
                    .peek(variableEle -> this.setDecodeByteBufParameterName(variableEle.getSimpleName().toString()))
                    .map(variableEle -> MethodParameterModel.builder()
                            .param_name(variableEle.getSimpleName().toString())
                            .param_type(new TypeModel(variableEle))
                            .build())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("接口方法定义找不到ByteBuf参数)")));
            return this;
        }

        /* 实现接口定义的decode方法 */
        public ProtocolDecodeProcessor step3_overrideDecode() {
            final ByteBufDecode decodeAnon = methodElement.getAnnotation(ByteBufDecode.class);
            final TypeModel decodeType = new TypeModel(methodReturnTypeElement);
            final String decodeElementName = methodReturnTypeElement.getAnnotation(CacheMapping.class).value();
            final String decodeMethodName = methodElement.getSimpleName().toString();
            final String obfuscatedDecodeMethodName = generateObfuscatedName(decodeElementName);
            final String modifier = methodElement.getModifiers().stream()
                    // 接口定义的方法 modifier默认是public abstract, 需要手动过滤abstract修饰符
                    .filter(mod -> mod != Modifier.ABSTRACT)
                    .map(Modifier::toString)
                    .collect(Collectors.joining(" "));
            // 重写接口定义的decode方法
            this.setOverrideDecode(ProtocolOverrideDecodeMethod.builder()
                    .decode_type(decodeType)
                    .decode_anon(decodeAnon)
                    .decode_element_name(decodeElementName)
                    .decode_method_name(obfuscatedDecodeMethodName)
                    .buffer_parameter(decodeByteBufParameterName)
                    .channel_parameter(decodeChannelParameterName)
                    // 校验组需要有执行顺序, 这里用链表
                    .init_validations(Lists.newLinkedList())
                    .method_head(MethodHeadModel.builder()
                            .modifier(modifier)
                            .return_type(decodeType)
                            .method_name(decodeMethodName)
                            .method_parameters(Arrays.asList(decodeByteBufParameter, decodeChannelParameter))
                            .build())
                    .build()
                    // 处理resolveExceptionMethod的import和autowire
                    .processImportAndAutoWire(protocolImplModel)
            );
            // inject validation condition
            injectValidateInOverrideDecodeMethod((TypeElement) methodReturnTypeElement);
            // inject ProtocolOverrideDecodeMethod
            this.protocolImplModel.setDecode_root_element(overrideDecode);
            return this;
        }


        /** finally stage */
        public void step__over() {
            final String decodeMethodName = overrideDecode.getDecode_method_name();
            final boolean isUnRoot = false;
            step_processInternalElements(methodReturnTypeElement, decodeMethodName, isUnRoot);
        }

        @Override
        protected void step_processInternalElements(Symbol element, String decodeMethodName, boolean unRoot) {
            // 检查element的类型(primitive/cache_obj/collection)
            final int typeEnum = checkVariableElementTypeEnum(element);
            boolean isCacheObj = (typeEnum == CACHE_OBJ);
            boolean isCollection = (typeEnum == COLLECTION);
            boolean isNotPrimitive = typeEnum != PRIMITIVE;
            final ByteBufConvert convertAnon = element.getAnnotation(ByteBufConvert.class);
            final String obfuscateBufferName = unRoot ? generateObfuscatedName(decodeByteBufParameterName) : decodeByteBufParameterName;
            if (isCollection){
                if (unRoot && convertAnon.parameters().length == 0){
                    throw new IllegalArgumentException("集合的泛型类型必须在ByteBufConvert的parameter中指定, 例如parameters = {\"java.util.LinkedList\"}");
                }
                if (!unRoot && methodElement.getAnnotation(ByteBufDecode.class).parameters().length == 0){
                    throw new IllegalArgumentException("集合的泛型类型必须在ByteBufDecode的parameter中指定, 例如parameters = {\"java.util.LinkedList\"}");
                }
            }
            /*
             * Note:
             * varSymbol转classSymbol, 否则@CacheMapping获取不到
             * fix#issue3 基本类型强转包装类型
             */
            final Symbol.TypeSymbol typeSymbol = TypeUtils.getTypeSymbol(typeUtils, element.asType());
            // 获取@CacheMapping的value作为decode_element_name
            final String originalDecodeElementName = isCacheObj ? typeSymbol.getAnnotation(CacheMapping.class).value() : element.getSimpleName().toString();
            /*
             * Note:
             * fix#issue3 这里不能放基本类型作为decode_type, 必须是包装类型
             * 如果是基本类型就放包装类型, 由于集合类型考虑到泛型擦除, 不能使用typeSymbol, 只能用varSymbol
             */
            final TypeModel decodeTypeModel = new TypeModel(element.asType().getKind().isPrimitive() ? typeSymbol : element);
            final ProtocolDecodeInternalMethod protocolDecodeInternalMethod = ProtocolDecodeInternalMethod.builder()
                    .method_head(MethodHeadModel.builder()
                            .modifier(MethodHeadModel.PRIVATE)
                            .return_type(decodeTypeModel)
                            .method_name(decodeMethodName)
                            .is_override(false)
                            .method_parameters(Arrays.asList(
                                    decodeByteBufParameter,
                                    decodeChannelParameter
                            ))
                            .build())
                    // index, condition, length 获取缓冲区的解析部分
                    .part0(InitConditionIndexLength.builder()
                            .convert_anon(convertAnon)
                            .buffer_parameter(decodeByteBufParameterName)
                            .channel_parameter(decodeChannelParameterName)
                            .is_decode(true)
                            .build()
                            // 处理ByteBufConvert#convertMethod的import和autowire, 当前前提不为空
                            .processImportAndAutoWire(protocolImplModel))
                    .part1(ProtocolDecodeConvertMethod.builder()
                            .convert_anon(convertAnon)
                            .decode_type(decodeTypeModel)
                            .channel_parameter(decodeChannelParameterName)
                            .decode_type_name(originalDecodeElementName)
                            .confused_buffer_name(obfuscateBufferName)
                            .build())
                    // parameters
                    .buffer_parameter(decodeByteBufParameterName)
                    .channel_parameter(decodeChannelParameterName)
                    // member mappings
                    .member_mappings(Lists.newLinkedList())
                    .decode_type(decodeTypeModel)
                    .decode_element_name(originalDecodeElementName)
                    // judgement
                    .is_not_primitive(isNotPrimitive)
                    .is_cache_obj(isCacheObj)
                    .is_collection(isCollection)
                    .is_un_root(unRoot)
                    // confused part
                    .confused_buffer_name(obfuscateBufferName)
                    .build();
            // cacheObj元素或者集合元素内部元素遍历递归
            if (typeEnum == CACHE_OBJ) {
                protocolDecodeInternalMethod.addAllInternalModelContext(step_processInternalRecursive(typeSymbol));
            }
            if (typeEnum == COLLECTION) {
                // 获取集合元素的泛型元素
                final Element genericElement = ElementUtils.getGenericElement(typeUtils, element);
                final Symbol genericClassElement = TypeUtils.getTypeSymbol(typeUtils, genericElement.asType());
                // 生成泛型decode的混淆名
                final String genericDecodeMethodName = generateObfuscatedName(genericElement.getSimpleName().toString());
                // 把泛型注入到集合元素的memberMapping中
                protocolDecodeInternalMethod.addInternalModelContext(InternalModelContext.builder()
                        .member_name(ElementUtils.getSymbolTypeSimpleName(genericClassElement))
                        .member_method_name(genericDecodeMethodName)
                        .member_type(new TypeModel(genericClassElement))
                        .build()
                );
                step_processInternalElements(genericClassElement, genericDecodeMethodName, false);
            }
            protocolImplModel.addDecodeElement(protocolDecodeInternalMethod);
        }

        /** inject validate operations before decode */
        private void injectValidateInOverrideDecodeMethod(TypeElement typeElement) {
            final ByteBufValidationGroup validationGroup = typeElement.getAnnotation(ByteBufValidationGroup.class);
            final ByteBufValidation validation = typeElement.getAnnotation(ByteBufValidation.class);
            if (validation == null && validationGroup == null){
                final Element superElement = typeUtils.asElement(typeElement.getSuperclass());
                // 如果父类不为Object, 递归父类是否有@ByteBufValidation
                if (superElement.toString().equals(ProtocolGenerateConstant.OBJECT_QUALIFIER_NAME)){
                    return;
                }
                injectValidateInOverrideDecodeMethod((TypeElement) superElement);
                return;
            }
            ByteBufValidation[] validations = null;
            if (validationGroup != null){
                validations = validationGroup.value();
                if (validations.length == 0) {
                    return;
                }
            }
            if (validation != null){
                validations = new ByteBufValidation[]{validation};
            }
            // 根据优先级order执行校验
            Arrays.stream(validations).sorted(Comparator.comparing(ByteBufValidation::order)).forEach(byteBufValidationAnon ->
                this.overrideDecode.addInit_validation(InitValidation.builder()
                        .validation_anon(byteBufValidationAnon)
                        .buffer_parameter(decodeByteBufParameterName)
                        .channel_parameter(decodeChannelParameterName)
                        // validate/mapper flag
                        .is_validate(true)
                        .build()
                        // 处理import和autowire
                        .processImportAndAutoWire(protocolImplModel))
            );
        }
    }
}
