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
package com.bstek.urule.console.servlet.action;

import com.bstek.urule.Utils;
import com.bstek.urule.console.servlet.BaseServletHandler;
import com.bstek.urule.model.ExposeAction;
import com.bstek.urule.model.ExposeActionClass;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.bstek.urule.Utils.getDatatypeFromClass;
import static com.bstek.urule.console.helper.SpringObjectHelper.getTargetObject;

public class ActionServletHandler extends BaseServletHandler {
    @Override
    protected void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        VelocityContext context = new VelocityContext();
        context.put("contextPath", req.getContextPath());
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        Template template = ve.getTemplate("html/action-editor.html", "utf-8");
        PrintWriter writer = resp.getWriter();
        template.merge(context, writer);
        writer.close();
    }

    public void loadActionBeans(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ApplicationContext context = Utils.getApplicationContext();
        Map<String, Object> beanMap = context.getBeansWithAnnotation(ExposeActionClass.class);
        List<Map<String, Object>> list = new ArrayList<>();
        Set<String> keySet = beanMap.keySet();
        for (String key : keySet) {
            Object proxy = beanMap.get(key);
            Object object = getTargetObject(proxy);
            Class<?> clazz = object.getClass();

            ExposeActionClass actionBean = clazz.getAnnotation(ExposeActionClass.class);
            Map<String, Object> map = new HashMap<>(16);
            map.put("id", actionBean.value());
            map.put("name", actionBean.name());
            map.put("type", "Custom");

            List<Method> methods = parseClassMethods(object.getClass());

            map.put("methods", methods);
            list.add(map);
        }
        writeObjectToJson(response, list);
    }

    public void loadMethods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String beanId = req.getParameter("beanId");
        Object o = applicationContext.getBean(beanId);
        Object bean = getTargetObject(o);
        List<Method> list = parseClassMethods(bean.getClass());
        writeObjectToJson(resp, list);
    }

    private List<Method> parseClassMethods(Class<?> cls) {
        List<Method> list = new ArrayList<>();
        java.lang.reflect.Method[] methods = cls.getDeclaredMethods();
        for (java.lang.reflect.Method m : methods) {
            ExposeAction action = m.getAnnotation(ExposeAction.class);
            if (action == null) {
                continue;
            }
            String name = m.getName();
            Method method = new Method();
            method.setMethodName(name);
            method.setName(action.value());
            method.setParameters(buildParameters(m));
            list.add(method);
        }
        return list;
    }

    private List<Parameter> buildParameters(java.lang.reflect.Method m) {
        List<Parameter> parameters = new ArrayList<>();
        Class<?>[] classes = m.getParameterTypes();
        for (int i = 0; i < classes.length; i++) {
            Class<?> c = classes[i];
            Parameter p = new Parameter();
            p.setName("参数" + i);
            p.setType(getDatatypeFromClass(c));
            parameters.add(p);
        }
        return parameters;
    }


    @Override
    public String url() {
        return "/actioneditor";
    }

}
