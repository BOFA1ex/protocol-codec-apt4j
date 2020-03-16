package com.bofa.commons.apt4j.management.protocol;

import com.bofa.commons.apt4j.annotate.protocol.*;
import com.bofa.commons.apt4j.annotate.spel.SpelMapping;
import com.bofa.commons.apt4j.management.internal.extension.FreeMarkerModelGenerator;
import com.bofa.commons.apt4j.management.internal.model.*;
import com.bofa.commons.apt4j.management.internal.utils.ElementUtils;
import com.bofa.commons.apt4j.management.internal.utils.TypeUtils;
import com.bofa.commons.apt4j.management.protocol.constant.ProtocolGenerateConstant;
import com.bofa.commons.apt4j.management.protocol.model.ProtocolImpl;
import com.bofa.commons.apt4j.management.protocol.model.common.*;
import com.bofa.commons.apt4j.management.protocol.model.decode.*;
import com.bofa.commons.apt4j.management.protocol.model.encode.*;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import lombok.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author bofa1ex
 * @since 2020/3/1
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.bofa.commons.apt4j.annotate.protocol.Protocol"})
@SupportedOptions({"test"})
public class ProtocolProcessor extends AbstractProcessor {

    /* 语法树 */
    private Trees trees;
    /* 树节点创建工具类 */
    private TreeMaker treeMaker;
    /* 命名工具类 */
    private Names names;
    /* 打印编译日志工具 */
    private Messager messager;
    /* 处理节点工具 */
    private Elements elementUtils;
    /* 处理节点类型工具 */
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.trees = Trees.instance(processingEnv);
        Context ctx = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(ctx);
        this.names = Names.instance(ctx);
        this.messager = processingEnv.getMessager();
        this.elementUtils = processingEnv.getElementUtils();
        this.typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 如果此轮注解编译还需要处理
        if (!roundEnv.processingOver()) {
            roundEnv.getElementsAnnotatedWith(Protocol.class).stream()
                    .map(ele -> (Symbol.ClassSymbol) ele)
                    // Protocol注解修饰的类不为接口, 不处理
                    .filter(ele -> ele.getKind().isInterface())
                    // 已存在实现类, 跳过编译
                    .filter(ele -> notExistImplElement(ele, roundEnv))
                    .forEach(ele -> writeAndFlushModel(ele, preWriteAndFlush(ele)));
        }
        return false;
    }

    /**
     * 是否已存在实现类
     *
     * @param interfaceElement 接口element
     * @param roundEnv         环境上下文
     *
     * @return 存在实现类返回false, 反之返回true
     */
    protected static boolean notExistImplElement(Element interfaceElement, RoundEnvironment roundEnv) {
        final Protocol protocolAnon = interfaceElement.getAnnotation(Protocol.class);
        final String interfaceImplName = protocolAnon.implName();
        return roundEnv.getRootElements().stream().noneMatch(ele -> ele.getSimpleName().toString().equals(interfaceImplName));
    }

    /**
     * prepare to generate freeMarker'models before writeAndFlush it.
     */
    public ProtocolImpl preWriteAndFlush(Symbol.ClassSymbol element) {
        final String implName = element.getAnnotation(Protocol.class).implName();
        final String packageName = ElementUtils.getPackageName(elementUtils.getPackageOf(element));
        final boolean isCollection = false, isInterface = true;
        // generic types in interface type.
        final List<Type> typeParameters = element.getTypeParameters().stream().map(typeVariableSymbol -> {
            final String symbolTypeSimpleName = ElementUtils.getSymbolTypeSimpleName(typeVariableSymbol);
            final String symbolTypeQualifierName = ElementUtils.getSymbolTypeQualifierName(typeVariableSymbol);
            return new Type(symbolTypeSimpleName, symbolTypeQualifierName);
        }).collect(Collectors.toList());
        final ProtocolImpl protocolImplModel = new ProtocolImpl(packageName, TypeHead.buildImplTypeHead(
                new Type(implName, packageName + "." + implName, isCollection, isInterface),
                new Type(elementUtils, typeUtils, element), typeParameters));
        // process common methods
        ProtocolCommonProcessor.processCommonMethods(protocolImplModel, element);
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

    public void writeAndFlushModel(Symbol.ClassSymbol ele, ProtocolImpl protocolImpl) {
        final String javaFileSourceName = ele.getAnnotation(Protocol.class).implName();
        try {
            final JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(javaFileSourceName);
            new FreeMarkerModelGenerator().generateModel(sourceFile, protocolImpl);
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, Arrays.toString(e.getStackTrace()));
        }
    }


    static class ProtocolCommonProcessor {

        static void processCommonMethods(ProtocolImpl protocolImpl, Symbol.ClassSymbol element) {
            // build markAndReadSlice method.
            processInitMarkAndReadSliceMethod(protocolImpl);
            // build initChannelSpelContext method
            processInitChannelSpelContextMethod(protocolImpl, element);
        }

        static void processInitMarkAndReadSliceMethod(ProtocolImpl protocolImpl) {
            final String METHOD_NAME = "markAndReadSlice";
            final String BUFFER_METHOD_PARAMETER_NAME = "buffer";
            final String INDEX_METHOD_PARAMETER_NAME = "index";
            final String LENGTH_METHOD_PARAMETER_NAME = "length";
            final boolean isOverride = false;
            final InitMarkAndReadSliceMethod initMarkAndReadSliceMethod = InitMarkAndReadSliceMethod.builder()
                    .method_head(MethodHead.builder()
                            .modifier(MethodHead.PRIVATE_FINAL)
                            .return_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                            .method_name(METHOD_NAME)
                            .method_parameters(Arrays.asList(
                                    new MethodParameter(ProtocolGenerateConstant.BYTEBUF_TYPE, BUFFER_METHOD_PARAMETER_NAME),
                                    new MethodParameter(ProtocolGenerateConstant.INTEGER_TYPE, INDEX_METHOD_PARAMETER_NAME),
                                    new MethodParameter(ProtocolGenerateConstant.INTEGER_TYPE, LENGTH_METHOD_PARAMETER_NAME)
                            ))
                            .is_override(isOverride)
                            .build())
                    .log_message(InitMarkAndReadSliceMethod.DEFAULT_LOG_MESSAGE)
                    .buffer_parameter(BUFFER_METHOD_PARAMETER_NAME)
                    .reader_index_parameter(INDEX_METHOD_PARAMETER_NAME)
                    .length_parameter(LENGTH_METHOD_PARAMETER_NAME)
                    .build();
            protocolImpl.addCommonElement(initMarkAndReadSliceMethod);
        }

        static void processInitChannelSpelContextMethod(ProtocolImpl protocolImpl, Symbol.ClassSymbol element) {
            final Protocol anon = element.getAnnotation(Protocol.class);
            final String METHOD_NAME = "_init";
            final String OBJ_NAME_METHOD_PARAMETER_NAME = "objName";
            final String OBJ_METHOD_PARAMETER_NAME = "obj";
            final String BUFFER_METHOD_PARAMETER_NAME = "buffer";
            final String CHANNEL_METHOD_PARAMETER_NAME = "channel";
            final boolean isOverride = false;
            final InitChannelSpelContextMethod initChannelSpelContextMethod = InitChannelSpelContextMethod.builder()
                    .method_head(MethodHead.builder()
                            .modifier(MethodHead.PRIVATE_FINAL)
                            .return_type(ProtocolGenerateConstant.VOID_TYPE)
                            .method_name(METHOD_NAME)
                            .method_parameters(Arrays.asList(
                                    new MethodParameter(ProtocolGenerateConstant.STRING_TYPE, OBJ_NAME_METHOD_PARAMETER_NAME),
                                    new MethodParameter(ProtocolGenerateConstant.OBJECT_TYPE, OBJ_METHOD_PARAMETER_NAME),
                                    new MethodParameter(ProtocolGenerateConstant.BYTEBUF_TYPE, BUFFER_METHOD_PARAMETER_NAME),
                                    new MethodParameter(ProtocolGenerateConstant.CHANNEL_TYPE, CHANNEL_METHOD_PARAMETER_NAME)
                            ))
                            .is_override(isOverride)
                            .build())
                    .spel_vars(Arrays.stream(anon.variables())
                            .map(spelVar -> new InitChannelSpelContextMethod.SpelVarModel(spelVar.key(), spelVar.value()))
                            .collect(Collectors.toList()))
                    .build();
            // 手动注入channelSpelContextUtils的依赖到imports
            initChannelSpelContextMethod.getImport_stats().add(ProtocolGenerateConstant.CHANNEL_UTILS_QUALIFIER_NAME);
            protocolImpl.addCommonElement(initChannelSpelContextMethod);
        }

    }

    @Setter
    static abstract class AbstractProtocolProcessor {

        protected static final String DEFAULT_STANDARD_READER_INDEX = "standardReaderIndex";
        protected static final String DEFAULT_STANDARD_WRITER_INDEX = "standardWriterIndex";

        // internal element typeEnum
        protected static final int PRIMITIVE = 0;
        protected static final int SPEL_OBJ = 1;
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

        protected static String resolveEncodeByteBufConvertAnonLength(String byteBufConvertLength) {
            String findStr = "readableBytes";
            String replaceStr = "writableBytes";
            if (byteBufConvertLength.endsWith("readableBytes()")) {
                return byteBufConvertLength.replace(findStr, replaceStr);
            }
            return byteBufConvertLength;
        }

        /**
         * 检查element的typeEnum
         */
        protected int checkVariableElementTypeEnum(Element element) {
            // 需要将varSymbol转classSymbol再获取SpelMapping注解
            // fix#issue3 基本类型强转包装类型
            final Symbol.TypeSymbol classTypeElement = TypeUtils.resolvePrimitiveType(typeUtils, element.asType());
            final SpelMapping spelMappingAnon = classTypeElement.getAnnotation(SpelMapping.class);
            if (spelMappingAnon != null) {
                return SPEL_OBJ;
            }
            final List<com.sun.tools.javac.code.Type> typeArguments = ((Symbol.VarSymbol) element).asType().getTypeArguments();
            return typeArguments == null || typeArguments.size() == 0 ? PRIMITIVE : COLLECTION;
        }

        /**
         * 校验decode返参/encode入参
         * 是否合法
         */
        protected void requireElementModifiedBySpelMapping(Element element) {
            final int typeEnum = checkVariableElementTypeEnum(element);
            if (typeEnum == PRIMITIVE) {
                throw new RuntimeException("Decode方法返参必须被@SpelMapping修饰");
            }
            if (typeEnum == COLLECTION) {
                final com.sun.tools.javac.code.Type genericType = ((Symbol.VarSymbol) element).asType().getTypeArguments().get(0);
                requireElementModifiedBySpelMapping(typeUtils.asElement(genericType));
            }
        }

        /**
         * 递归spelMapping对象链
         */
        protected List<Symbol.VarSymbol> getTotalVarSymbols(TypeElement typeElement) {
            List<Symbol.VarSymbol> totals = new ArrayList<>();
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
         * ProtocolEncode$1 内部元素异步递归, 父级分发完任务后需要同步等待子元素完成任务.
         *
         * @param typeElement 父type元素
         * @param parent      父级model, 需要注入子级memberMapping
         */
        protected void step_processInternalRecursive(TypeElement typeElement, ProtocolEncode$1 parent) {
            final List<Symbol.VarSymbol> internalElements = getTotalVarSymbols(typeElement);
            CompletableFuture<?>[] totalTasks = new CompletableFuture[internalElements.size()];
            for (int index = 0; index < internalElements.size(); index++) {
                Symbol.VarSymbol element = internalElements.get(index);
                final String memberName = element.getSimpleName().toString();
                final String memberMethodName = generateObfuscatedName(memberName);
                // 父级需要先填充子级的上下文
                parent.addInternalModelContext(new InternalModelContext(new Type(element), memberName, memberMethodName));
                // 异步递归
                totalTasks[index] = CompletableFuture.runAsync(() -> step_processInternalElements(element, memberMethodName, true));
            }
            // 等待欢乐时光结束
            CompletableFuture.allOf(totalTasks).join();
        }

        /**
         * ProtocolDecode$1 内部元素异步递归, 父级分发完任务后需要同步等待子元素完成任务.
         *
         * @param typeElement 父type元素
         * @param parent      父级model, 需要注入子级memberMapping
         */
        protected void step_processInternalRecursive(TypeElement typeElement, ProtocolDecode$1 parent) {
            final List<Symbol.VarSymbol> internalElements = getTotalVarSymbols(typeElement);
            CompletableFuture<?>[] totalTasks = new CompletableFuture[internalElements.size()];
            for (int index = 0; index < internalElements.size(); index++) {
                Symbol.VarSymbol element = internalElements.get(index);
                final String memberName = element.getSimpleName().toString();
                final String memberDecodeMethodName = generateObfuscatedName(memberName);
                // 父级需要先填充子级的上下文
                parent.addInternalModelContext(new InternalModelContext(new Type(element), memberName, memberDecodeMethodName));
                // 异步递归
                totalTasks[index] = CompletableFuture.runAsync(() -> step_processInternalElements(element, memberDecodeMethodName, true));
            }
            // 等待欢乐时光结束
            CompletableFuture.allOf(totalTasks).join();
        }

        protected abstract void step_processInternalElements(Symbol typeElement, String memberMethodName, boolean unRoot);
    }

    @Setter
    static class ProtocolEncodeProcessor extends AbstractProtocolProcessor {

        private static final String DEFAULT_BUFFER_PARAMETER_NAME = "buffer";

        private MethodParameter encodeTypeParameter;
        private String encodeTypeParameterName;
        private MethodParameter encodeChannelParameter;
        private String encodeChannelParameterName;

        private ExecutableElement methodElement;
        private TypeElement encodeTypeElement;

        private ProtocolEncode$0 overrideEncode;

        /* 引入节点操作工具 */
        public static ProtocolEncodeProcessor step1_injectContext(Elements elementUtils, Types typeUtils) {
            final ProtocolEncodeProcessor protocolEncodeProcessor = new ProtocolEncodeProcessor();
            protocolEncodeProcessor.setElementUtils(elementUtils);
            protocolEncodeProcessor.setTypeUtils(typeUtils);
            return protocolEncodeProcessor;
        }

        /* 必要参数预先缓存 */
        public ProtocolEncodeProcessor step2_prepareCache(ExecutableElement element, ProtocolImpl protocolImplModel) {
            this.setMethodElement(element);
            this.setProtocolImplModel(protocolImplModel);
            this.setEncodeChannelParameter(element.getParameters().stream()
                    .filter(variableEle -> ElementUtils.getSymbolTypeSimpleName((Symbol) variableEle).equals(ProtocolGenerateConstant.CHANNEL_TYPE_NAME))
                    .map(variableEle -> (Symbol) variableEle)
                    // inject encodeChannelParameterName
                    .peek(variableEle -> this.setEncodeChannelParameterName(variableEle.getSimpleName().toString()))
                    .map(variableEle -> new MethodParameter(new Type(variableEle), variableEle.getSimpleName().toString()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("接口方法定义找不到Channel参数)")));
            this.setEncodeTypeParameter(element.getParameters().stream()
                    .filter(variableEle -> !ElementUtils.getSymbolTypeSimpleName((Symbol) variableEle).equals(ProtocolGenerateConstant.CHANNEL_TYPE_NAME))
                    .map(variableEle -> (Symbol) variableEle)
                    // inject encodeTypeParameterName
                    .peek(variableEle -> this.setEncodeTypeParameterName(variableEle.getSimpleName().toString()))
                    .peek(variableEle -> this.setEncodeTypeElement((TypeElement) typeUtils.asElement(variableEle.asType())))
                    .map(variableEle -> new MethodParameter(new Type(variableEle), variableEle.getSimpleName().toString()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("接口方法定义找不到<T>参数)")));
            return this;
        }


        /* 实现接口定义的encode方法 */
        public ProtocolEncodeProcessor step3_overrideEncode() {
            // 校验encodeType必须由spelMapping修饰, 或者其泛型类型由spelMapping修饰
            requireElementModifiedBySpelMapping(encodeTypeElement);
            final String modifier = methodElement.getModifiers().stream()
                    // 接口定义的方法 modifier默认是public abstract, 需要手动过滤abstract修饰符
                    .filter(mod -> mod != Modifier.ABSTRACT)
                    .map(Modifier::toString)
                    .collect(Collectors.joining(" "));

            // ByteBufEncode Anon properties
            final int initialCapacity = methodElement.getAnnotation(ByteBufEncode.class).initialCapacity();
            final int maxCapacity = methodElement.getAnnotation(ByteBufEncode.class).maxCapacity();

            final Type encodeType = new Type((Symbol) encodeTypeElement);
            /* ******** 这里用spelMapping修饰的入参<T>给的value值, 直接覆盖掉encode方法入参<T>的参数名 ******** */
            this.setEncodeTypeParameterName(encodeTypeElement.getAnnotation(SpelMapping.class).value());
            // 接口encode方法名
            final String methodName = methodElement.getSimpleName().toString();
            // 混淆 方法名
            final String encodeMethodName = generateObfuscatedName(encodeTypeParameterName);
            // 重写接口定义的decode方法
            this.setOverrideEncode(ProtocolEncode$0.builder()
                    .method_head(MethodHead.builder()
                            .modifier(modifier)
                            .return_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                            .method_name(methodName)
                            .method_parameters(Arrays.asList(encodeTypeParameter, encodeChannelParameter))
                            .build())
                    .encode_type(encodeType)
                    .encode_element_name(encodeTypeParameterName)
                    .encode_method_name(encodeMethodName)
                    .encode_parameter(encodeTypeParameterName)
                    .channel_parameter(encodeChannelParameterName)
                    .init_buffer(InitBuffer.builder()
                            .initial_capacity(initialCapacity + "")
                            .max_capacity(maxCapacity + "")
                            .build())
                    .build()
            );
            // inject validation condition in ProtocolDecode$0
            injectValidateInOverrideEncodeMethod();
            // inject ProtocolDecode$0 in protocolImplModel
            this.protocolImplModel.setEncode_root_element(overrideEncode);
            return this;
        }


        /** finally stage */
        public void step__over() {
            final String encodeMethodName = overrideEncode.getEncode_method_name();
            final boolean isUnRoot = false;
            step_processInternalElements((Symbol) encodeTypeElement, encodeMethodName, isUnRoot);
        }

        @Override
        protected void step_processInternalElements(Symbol element, String encodeMethodName, boolean unRoot) {
            /* *********************** Common Part ************************ */
            // varSymbol转classSymbol, 否则SpelMapping注解获取不到
            // fix#issue3 基本类型强转包装类型
            final Symbol.TypeSymbol classTypeElement = TypeUtils.resolvePrimitiveType(typeUtils, element.asType());
            // 检查element的类型(primitive/spel_obj/collection)
            final int typeEnum = checkVariableElementTypeEnum(element);
            boolean isSpelObj = (typeEnum == SPEL_OBJ);
            boolean isCollection = (typeEnum == COLLECTION);
            boolean isNotPrimitive = typeEnum != PRIMITIVE;
            String byteBufConvertModelConvertMethod = null;
            String byteBufConvertModelIndex = null;
            String byteBufConvertModelLength = null;
            String byteBufConvertModelCondition = null;
            String[] byteBufConvertModelParameters = null;
            String obfuscateBufferName = null;
            String originalStandardWriterIndex = null;
            /* *********************** UnRoot Part ************************ */
            if (unRoot) {
                // ByteBufConvert Annotation#convertMethod
                final ByteBufConvert byteBufConvertAnon = element.getAnnotation(ByteBufConvert.class);
                if (isCollection & byteBufConvertAnon.parameters().length == 0) {
                    throw new IllegalArgumentException("集合的泛型类型必须在ByteBufConvert的parameter中指定, 例如parameters = {\"java.util.ArrayList\"}");
                }
                byteBufConvertModelIndex = byteBufConvertAnon.index();
                // 这里需要处理encode转换readableBytes=>writableBytes
                byteBufConvertModelLength = resolveEncodeByteBufConvertAnonLength(byteBufConvertAnon.length());
                byteBufConvertModelCondition = byteBufConvertAnon.condition();
                byteBufConvertModelParameters = byteBufConvertAnon.parameters();
                byteBufConvertModelConvertMethod = TypeUtils.resolveClassTypeMirrorException(byteBufConvertAnon::convertMethod);
                obfuscateBufferName = generateObfuscatedName(DEFAULT_BUFFER_PARAMETER_NAME);
                originalStandardWriterIndex = isNotPrimitive ? generateObfuscatedName(DEFAULT_STANDARD_WRITER_INDEX) : DEFAULT_STANDARD_WRITER_INDEX;
            }
            /* *********************** Common Part ************************ */
            final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder()
                    .index(byteBufConvertModelIndex)
                    .length(byteBufConvertModelLength)
                    .condition(byteBufConvertModelCondition)
                    .convert_method(byteBufConvertModelConvertMethod)
                    .parameters(byteBufConvertModelParameters)
                    .build();
            // 获取spelMapping注解的值作为encode_element_name
            String originalEncodeElementName = isSpelObj ? classTypeElement.getAnnotation(SpelMapping.class).value() : element.getSimpleName().toString();
            final ProtocolEncode$1 protocolEncode$1 = ProtocolEncode$1.builder()
                    .method_head(MethodHead.builder()
                            .modifier(MethodHead.PRIVATE_FINAL)
                            .return_type(ProtocolGenerateConstant.VOID_TYPE)
                            .method_name(encodeMethodName)
                            .method_parameters(Arrays.asList(
                                    MethodParameter.builder()
                                            .param_name(DEFAULT_BUFFER_PARAMETER_NAME)
                                            .param_type(ProtocolGenerateConstant.BYTEBUF_TYPE)
                                            .build(),
                                    MethodParameter.builder()
                                            .param_name(originalEncodeElementName)
                                            // fix#issue3 这里不能放基本类型作为入参, 必须是包装类型
                                            // 这里权衡下, 如果是基本类型就放包装类型, 如果是集合类型, 如果放classType, 泛型类型就被擦除了.
                                            .param_type(new Type(element.asType().getKind().isPrimitive() ? classTypeElement : element))
                                            .build(),
                                    encodeChannelParameter,
                                    MethodParameter.builder()
                                            .param_name(DEFAULT_STANDARD_WRITER_INDEX)
                                            .param_type(ProtocolGenerateConstant.INTEGER_TYPE)
                                            .build()
                            ))
                            .is_override(false)
                            .build())
                    // index, condition, length 获取缓冲区的解析部分
                    .part0(ProtocolSpelProcessExpr.builder()
                            .convert_anon_model(convertAnonModel)
                            .buffer_parameter(DEFAULT_BUFFER_PARAMETER_NAME)
                            .channel_parameter(encodeChannelParameterName)
                            .standard_index_parameter(DEFAULT_STANDARD_WRITER_INDEX)
                            .build())
                    // convertMethod 不为空的解析部分
                    .part1(ProtocolEncodePart$0.builder()
                            .convertAnonModel(convertAnonModel)
                            // fix#issue3 这里不能放基本类型作为encode_type, 必须是包装类型
                            // 这里权衡下, 如果是基本类型就放包装类型, 如果是集合类型, 如果放classType, 泛型类型就被擦除了.
                            .encode_type(new Type(element.asType().getKind().isPrimitive() ? classTypeElement : element))
                            .channel_parameter(encodeChannelParameterName)
                            .encode_type_name(originalEncodeElementName)
                            .confused_buffer_name(obfuscateBufferName)
                            .build())
                    // parameters
                    .buffer_parameter(DEFAULT_BUFFER_PARAMETER_NAME)
                    .channel_parameter(encodeChannelParameterName)
                    .standard_writer_index_parameter(DEFAULT_STANDARD_WRITER_INDEX)
                    // member mappings
                    .member_mappings(new LinkedList<>())
                    .convert_model(convertAnonModel)
                    //fix#issue3 这里不能放基本类型作为encode_type, 必须是包装类型
                    // 这里权衡下, 如果是基本类型就放包装类型, 如果是集合类型, 如果放classType, 泛型类型就被擦除了.
                    .encode_type(new Type(element.asType().getKind().isPrimitive() ? classTypeElement : element))
                    .encode_element_name(originalEncodeElementName)
                    // judgement
                    .is_not_primitive(isNotPrimitive)
                    .is_spel_object(isSpelObj)
                    .is_collection(isCollection)
                    .is_un_root(unRoot)
                    // confused part
                    .confused_buffer_name(obfuscateBufferName)
                    .confused_standard_writer_index_name(originalStandardWriterIndex)
                    .build();
            // spel元素或者集合元素内部元素遍历递归
            if (typeEnum == SPEL_OBJ) {
                step_processInternalRecursive((TypeElement) typeUtils.asElement(element.asType()), protocolEncode$1);
            }
            if (typeEnum == COLLECTION) {
                // 获取集合元素的泛型元素
                final Element genericElement = ElementUtils.getGenericElement(typeUtils, element);
                final Symbol genericClassElement = (Symbol) typeUtils.asElement(genericElement.asType());
                // 生成泛型encode的混淆名
                final String genericEncodeMethodName = generateObfuscatedName(genericElement.getSimpleName().toString());
                // 把泛型注入到集合元素的memberMapping中
                protocolEncode$1.addInternalModelContext(new InternalModelContext(
                        new Type(genericClassElement),
                        genericElement.getSimpleName().toString(), genericEncodeMethodName
                ));
                step_processInternalElements(genericClassElement, genericEncodeMethodName, false);
            }
            protocolImplModel.addEncodeElement(protocolEncode$1);
        }


        /** inject validate operations before encode */
        private void injectValidateInOverrideEncodeMethod() {
            final ByteBufValidation byteBufValidationAnon = encodeTypeElement.getAnnotation(ByteBufValidation.class);
            if (byteBufValidationAnon == null) {
                return;
            }
            final boolean isValidate = false;
            final ByteBufValidation.Validate validate = byteBufValidationAnon.validate();
            final String validateIndex = validate.index();
            final String validateLength = validate.length();
            final ByteBufValidation.Mapper mapper = byteBufValidationAnon.mapper();
            final String mapperIndex = mapper.index();
            final String mapperLength = mapper.length();
            final String[] parameters = byteBufValidationAnon.parameters();
            final String validateMethodSimpleName = TypeUtils.resolveClassTypeMirrorException(byteBufValidationAnon::validateMethod);
            final InitValidation initValidation = InitValidation.builder()
                    .validate_name(validateMethodSimpleName)
                    .validate_index(validateIndex)
                    .validate_length(validateLength)
                    .mapper_index(mapperIndex)
                    .mapper_length(mapperLength)
                    // ByteBufValidation#parameters
                    .anon_params(Arrays.asList(parameters))
                    .buffer_parameter(DEFAULT_BUFFER_PARAMETER_NAME)
                    .channel_parameter(encodeChannelParameterName)
                    // validate/mapper flag
                    .is_validate(isValidate)
                    .build();
            this.overrideEncode.setValidate_condition(initValidation);
        }


    }

    @Setter
    static class ProtocolDecodeProcessor extends AbstractProtocolProcessor {

        private MethodParameter decodeByteBufParameter;
        private String decodeByteBufParameterName;
        private MethodParameter decodeChannelParameter;
        private String decodeChannelParameterName;

        private ExecutableElement methodElement;
        private TypeElement methodReturnTypeElement;

        private ProtocolDecode$0 overrideDecode;

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
            this.setMethodReturnTypeElement((TypeElement) typeUtils.asElement(element.getReturnType()));
            this.setDecodeChannelParameter(element.getParameters().stream()
                    .filter(variableEle -> ElementUtils.getSymbolTypeSimpleName((Symbol) variableEle).equals(ProtocolGenerateConstant.CHANNEL_TYPE_NAME))
                    .map(variableEle -> (Symbol) variableEle)
                    // inject decodeChannelParameterName
                    .peek(variableEle -> this.setDecodeChannelParameterName(variableEle.getSimpleName().toString()))
                    .map(variableEle -> new MethodParameter(new Type(variableEle), variableEle.getSimpleName().toString()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("接口方法定义找不到Channel参数)")));
            this.setDecodeByteBufParameter(element.getParameters().stream()
                    .filter(variableEle -> ElementUtils.getSymbolTypeSimpleName((Symbol) variableEle).equals(ProtocolGenerateConstant.BYTEBUF_TYPE_NAME))
                    .map(variableEle -> (Symbol) variableEle)
                    // inject decodeByteBufParameterName
                    .peek(variableEle -> this.setDecodeByteBufParameterName(variableEle.getSimpleName().toString()))
                    .map(variableEle -> new MethodParameter(new Type(variableEle), variableEle.getSimpleName().toString()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("接口方法定义找不到ByteBuf参数)")));
            return this;
        }

        /* 实现接口定义的decode方法 */
        public ProtocolDecodeProcessor step3_overrideDecode() {
            // 校验returnType必须由spelMapping修饰, 或者其泛型类型由spelMapping修饰
            requireElementModifiedBySpelMapping(methodReturnTypeElement);
            // ByteBufDecode Anon properties
            // resolve exception class name
            final String resolveExceptionTypeSimpleName = TypeUtils.resolveClassTypeMirrorException(() -> methodElement.getAnnotation(ByteBufDecode.class).resolveException());
            final String modifier = methodElement.getModifiers().stream()
                    // 接口定义的方法 modifier默认是public abstract, 需要手动过滤abstract修饰符
                    .filter(mod -> mod != Modifier.ABSTRACT)
                    .map(Modifier::toString)
                    .collect(Collectors.joining(" "));
            final Type returnType = new Type((Symbol) methodReturnTypeElement);
            final String returnTypeElementSimpleName = methodReturnTypeElement.getAnnotation(SpelMapping.class).value();
            // 接口decode方法名
            final String methodName = methodElement.getSimpleName().toString();
            // 混淆 方法名
            final String decodeMethodName = generateObfuscatedName(returnTypeElementSimpleName);
            // 重写接口定义的decode方法
            this.setOverrideDecode(ProtocolDecode$0.builder()
                    .method_head(MethodHead.builder()
                            .modifier(modifier)
                            .return_type(returnType)
                            .method_name(methodName)
                            .method_parameters(Arrays.asList(decodeByteBufParameter, decodeChannelParameter))
                            .build())
                    .decode_type(returnType)
                    .decode_element_name(returnTypeElementSimpleName)
                    .decode_method_name(decodeMethodName)
                    .buffer_parameter(decodeByteBufParameterName)
                    .channel_parameter(decodeChannelParameterName)
                    .resolve_exception_name(resolveExceptionTypeSimpleName)
                    .build()
            );
            // inject validation condition in ProtocolDecode$0
            injectValidateInOverrideDecodeMethod();
            // inject ProtocolDecode$0 in protocolImplModel
            this.protocolImplModel.setDecode_root_element(overrideDecode);
            return this;
        }

        /** finally stage */
        public void step__over() {
            final String decodeMethodName = overrideDecode.getDecode_method_name();
            final boolean isUnRoot = false;
            step_processInternalElements((Symbol) methodReturnTypeElement, decodeMethodName, isUnRoot);
        }

        @Override
        protected void step_processInternalElements(Symbol element, String decodeMethodName, boolean unRoot) {
            /* *********************** Common Part ************************ */
            // varSymbol转classSymbol, 否则SpelMapping注解获取不到
            // fix#issue3 基本类型强转包装类型
            final Symbol.TypeSymbol classTypeElement = TypeUtils.resolvePrimitiveType(typeUtils, element.asType());
            // 检查element的类型(primitive/spel_obj/collection)
            final int typeEnum = checkVariableElementTypeEnum(element);
            boolean isSpelObj = (typeEnum == SPEL_OBJ);
            boolean isCollection = (typeEnum == COLLECTION);
            boolean isNotPrimitive = typeEnum != PRIMITIVE;
            String byteBufConvertModelConvertMethod = null;
            String byteBufConvertModelIndex = null;
            String byteBufConvertModelLength = null;
            String byteBufConvertModelCondition = null;
            String[] byteBufConvertModelParameters = null;
            String obfuscateBufferName = null;
            String originalStandardReaderIndex = null;
            /* *********************** UnRoot Part ************************ */
            if (unRoot) {
                // ByteBufConvert Annotation#convertMethod
                final ByteBufConvert byteBufConvertAnon = element.getAnnotation(ByteBufConvert.class);
                if (isCollection & byteBufConvertAnon.parameters().length == 0) {
                    throw new IllegalArgumentException("集合的泛型类型必须在ByteBufConvert的parameter中指定, 例如parameters = {\"java.util.ArrayList\"}");
                }
                byteBufConvertModelIndex = byteBufConvertAnon.index();
                byteBufConvertModelLength = byteBufConvertAnon.length();
                byteBufConvertModelCondition = byteBufConvertAnon.condition();
                byteBufConvertModelParameters = byteBufConvertAnon.parameters();
                byteBufConvertModelConvertMethod = TypeUtils.resolveClassTypeMirrorException(byteBufConvertAnon::convertMethod);
                obfuscateBufferName = generateObfuscatedName(decodeByteBufParameterName);
                originalStandardReaderIndex = isNotPrimitive ? generateObfuscatedName(DEFAULT_STANDARD_READER_INDEX) : DEFAULT_STANDARD_READER_INDEX;
            }
            /* *********************** Root Part ************************ */
            if (!unRoot) {
                if (isCollection && methodElement.getAnnotation(ByteBufDecode.class).parameters().length == 0) {
                    throw new IllegalArgumentException("集合的泛型类型必须在ByteBufDecode的parameter中指定, 例如parameters = {\"java.util.ArrayList\"}");
                }
            }
            /* *********************** Common Part ************************ */
            final ByteBufConvertAnonModel convertAnonModel = ByteBufConvertAnonModel.builder()
                    .index(byteBufConvertModelIndex)
                    .length(byteBufConvertModelLength)
                    .condition(byteBufConvertModelCondition)
                    .convert_method(byteBufConvertModelConvertMethod)
                    .parameters(byteBufConvertModelParameters)
                    .build();
            // 获取spelMapping注解的值作为decode_element_name
            String originalDecodeElementName = isSpelObj ? classTypeElement.getAnnotation(SpelMapping.class).value() : element.getSimpleName().toString();
            final ProtocolDecode$1 protocolDecode$1 = ProtocolDecode$1.builder()
                    .method_head(MethodHead.builder()
                            .modifier(MethodHead.PRIVATE_FINAL)
                            // fix#issue3 这里不能放基本类型作为返参, 必须是包装类型
                            // 这里权衡下, 如果是基本类型就放包装类型, 如果是集合类型, 如果放classType, 泛型类型就被擦除了.
                            .return_type(new Type(element.asType().getKind().isPrimitive() ? classTypeElement : element))
                            .method_name(decodeMethodName)
                            .method_parameters(Arrays.asList(
                                    decodeByteBufParameter,
                                    decodeChannelParameter,
                                    MethodParameter.builder()
                                            .param_name(DEFAULT_STANDARD_READER_INDEX)
                                            .param_type(ProtocolGenerateConstant.INTEGER_TYPE)
                                            .build()
                            ))
                            .is_override(false)
                            .build())
                    // index, condition, length 获取缓冲区的解析部分
                    .part0(ProtocolSpelProcessExpr.builder()
                            .convert_anon_model(convertAnonModel)
                            .buffer_parameter(decodeByteBufParameterName)
                            .channel_parameter(decodeChannelParameterName)
                            .standard_index_parameter(DEFAULT_STANDARD_READER_INDEX)
                            .build())
                    // convertMethod 不为空的解析部分
                    .part1(ProtocolDecodePart$0.builder()
                            .convertAnonModel(convertAnonModel)
                            // fix#issue3 这里不能放基本类型作为decode_type, 否则imports那边会出异常.
                            // 这里权衡下, 如果是基本类型就放包装类型, 如果是集合类型, 如果放classType, 泛型类型就被擦除了.
                            .decode_type(new Type(element.asType().getKind().isPrimitive() ? classTypeElement : element))
                            .channel_parameter(decodeChannelParameterName)
                            .decode_type_name(originalDecodeElementName)
                            .confused_buffer_name(obfuscateBufferName)
                            .build())
                    // parameters
                    .buffer_parameter(decodeByteBufParameterName)
                    .channel_parameter(decodeChannelParameterName)
                    .standard_reader_index_parameter(DEFAULT_STANDARD_READER_INDEX)
                    // member mappings
                    .member_mappings(new LinkedList<>())
                    .convert_model(convertAnonModel)
                    // fix#issue3 这里不能放基本类型作为decode_type, 否则imports那边会出异常.
                    // 这里权衡下, 如果是基本类型就放包装类型, 如果是集合类型, 如果放classType, 泛型类型就被擦除了.
                    .decode_type(new Type(element.asType().getKind().isPrimitive() ? classTypeElement : element))
                    .decode_element_name(originalDecodeElementName)
                    // judgement
                    .is_not_primitive(isNotPrimitive)
                    .is_spel_object(isSpelObj)
                    .is_collection(isCollection)
                    .is_un_root(unRoot)
                    // confused part
                    .confused_buffer_name(obfuscateBufferName)
                    .confused_standard_reader_index_name(originalStandardReaderIndex)
                    .build();
            // spel元素或者集合元素内部元素遍历递归
            if (typeEnum == SPEL_OBJ) {
                step_processInternalRecursive((TypeElement) typeUtils.asElement(element.asType()), protocolDecode$1);
            }
            if (typeEnum == COLLECTION) {
                // 获取集合元素的泛型元素
                final Element genericElement = ElementUtils.getGenericElement(typeUtils, element);
                final Symbol genericClassElement = (Symbol) typeUtils.asElement(genericElement.asType());
                // 生成泛型decode的混淆名
                final String genericDecodeMethodName = generateObfuscatedName(genericElement.getSimpleName().toString());
                // 把泛型注入到集合元素的memberMapping中
                protocolDecode$1.addInternalModelContext(new InternalModelContext(
                        new Type(genericClassElement),
                        genericElement.getSimpleName().toString(), genericDecodeMethodName
                ));
                step_processInternalElements(genericClassElement, genericDecodeMethodName, false);
            }
            protocolImplModel.addDecodeElement(protocolDecode$1);
        }

        /** inject validate operations before decode */
        private void injectValidateInOverrideDecodeMethod() {
            final ByteBufValidation byteBufValidationAnon = methodReturnTypeElement.getAnnotation(ByteBufValidation.class);
            if (byteBufValidationAnon == null) {
                return;
            }
            final boolean isValidate = true;
            final ByteBufValidation.Validate validate = byteBufValidationAnon.validate();
            final String validateIndex = validate.index();
            final String validateLength = validate.length();
            final ByteBufValidation.Mapper mapper = byteBufValidationAnon.mapper();
            final String mapperIndex = mapper.index();
            final String mapperLength = mapper.length();
            final String[] parameters = byteBufValidationAnon.parameters();
            final String validateMethodSimpleName = TypeUtils.resolveClassTypeMirrorException(byteBufValidationAnon::validateMethod);
            final InitValidation initValidation = InitValidation.builder()
                    .validate_name(validateMethodSimpleName)
                    .validate_index(validateIndex)
                    .validate_length(validateLength)
                    .mapper_index(mapperIndex)
                    .mapper_length(mapperLength)
                    // ByteBufValidation#parameters
                    .anon_params(Arrays.asList(parameters))
                    .buffer_parameter(decodeByteBufParameterName)
                    .channel_parameter(decodeChannelParameterName)
                    // validate/mapper flag
                    .is_validate(isValidate)
                    .build();
            this.overrideDecode.setValidate_condition(initValidation);
        }
    }
}
