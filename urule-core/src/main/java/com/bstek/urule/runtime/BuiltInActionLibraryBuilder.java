/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.urule.runtime;

import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.bstek.urule.Utils.getDatatypeFromClass;

/**
 * @author Jacky.gao
 * @since 2015年11月26日
 */
public class BuiltInActionLibraryBuilder implements ApplicationContextAware {
    private List<SpringBean> builtInActions = new ArrayList<SpringBean>();

    public List<SpringBean> getBuiltInActions() {
        return builtInActions;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("Load built in actions...");
        String[] names = applicationContext.getBeanDefinitionNames();
        if (names == null || names.length == 0) return;
        for (String name : names) {
            Object obj = null;
            try {
                obj = applicationContext.getBean(name);
            } catch (Exception ex) {
                continue;
            }
            if (obj == null) {
                continue;
            }
            ActionBean aa = obj.getClass().getAnnotation(ActionBean.class);
            if (aa == null) {
                continue;
            }
            SpringBean bean = new SpringBean();
            bean.setId(name);
            bean.setName(aa.name());
            bean.setMethods(buildMethod(obj.getClass().getMethods()));
            builtInActions.add(bean);
        }
    }

    private List<com.bstek.urule.model.library.action.Method> buildMethod(Method[] methods) {
        List<com.bstek.urule.model.library.action.Method> list = new ArrayList<com.bstek.urule.model.library.action.Method>();
        for (Method m : methods) {
            ActionMethod methodAnnotation = m.getAnnotation(ActionMethod.class);
            if (methodAnnotation == null) continue;
            String name = methodAnnotation.name();
            String methodName = m.getName();
            com.bstek.urule.model.library.action.Method libMethod = new com.bstek.urule.model.library.action.Method();
            libMethod.setMethodName(methodName);
            libMethod.setName(name);
            list.add(libMethod);
            ActionMethodParameter mp = m.getAnnotation(ActionMethodParameter.class);
            List<String> parameterNames = new ArrayList<String>();
            if (mp != null) {
                String pnames[] = mp.names();
                for (String pname : pnames) {
                    parameterNames.add(pname);
                }
            }
            libMethod.setParameters(buildParameters(m, parameterNames));
        }
        return list;
    }

    private List<Parameter> buildParameters(Method m, List<String> parameterNames) {
        List<Parameter> list = new ArrayList<Parameter>();
        Class<?> pclasses[] = m.getParameterTypes();
        for (int i = 0; i < pclasses.length; i++) {
            Class<?> c = pclasses[i];
            String name = "";
            if (parameterNames.size() > i) {
                name = parameterNames.get(i);
            }
            Parameter p = new Parameter();
            p.setName(name);
            p.setType(getDatatypeFromClass(c));
            list.add(p);
        }
        return list;
    }
}
