<#--

    Copyright bofa1ex.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="com.bofa.commons.apt4j.management.protocol.model.common.InitBuffer" -->
<@compress single_line=true>
    final ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(${initial_capacity}, ${max_capacity});
    // 由于复用缓冲池中的缓冲区对象, 里面的指针虽然已经clear了, 但是里面的数据还没有清空. 需要在encode之前, 手动显式置0.
    buffer.setZero(0, buffer.capacity());
</@compress>

