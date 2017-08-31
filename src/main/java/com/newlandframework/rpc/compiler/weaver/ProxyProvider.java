/**
 * Copyright (C) 2017 Newland Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newlandframework.rpc.compiler.weaver;

import com.newlandframework.rpc.compiler.intercept.Interceptor;
import com.newlandframework.rpc.compiler.invoke.InterceptorInvoker;
import com.newlandframework.rpc.compiler.invoke.ObjectInvoker;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:ProxyProvider.java
 * @description:ProxyProvider功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2017/8/30
 */
public class ProxyProvider extends AbstractProxyProvider {
    private static final ClassCache PROXY_CLASS_CACHE = new ClassCache(new ByteCodeClassTransformer());

    @Override
    public <T> T createProxy(ClassLoader classLoader, Object target, Interceptor interceptor, Class<?>... proxyClasses) {
        return createProxy(classLoader, new InterceptorInvoker(target, interceptor), proxyClasses);
    }

    private <T> T createProxy(ClassLoader classLoader, ObjectInvoker invoker, final Class<?>... proxyClasses) {
        Class<?> proxyClass = PROXY_CLASS_CACHE.getProxyClass(classLoader, proxyClasses);
        try {
            T result = (T) proxyClass.getConstructor(ObjectInvoker.class).newInstance(invoker);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
