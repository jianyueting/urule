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
package com.bstek.urule.console.servlet.constant;

import com.bstek.urule.ClassUtils;
import com.bstek.urule.Utils;
import com.bstek.urule.console.servlet.RenderPageServletHandler;
import com.bstek.urule.model.ConstantClass;
import com.bstek.urule.model.library.constant.Constant;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ConstantServletHandler extends RenderPageServletHandler {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = retrieveMethod(req);
        if (method != null) {
            invokeMethod(method, req, resp);
        } else {
            VelocityContext context = new VelocityContext();
            context.put("contextPath", req.getContextPath());
            resp.setContentType("text/html");
            resp.setCharacterEncoding("utf-8");
            Template template = ve.getTemplate("html/constant-editor.html", "utf-8");
            PrintWriter writer = resp.getWriter();
            template.merge(context, writer);
            writer.close();
        }
    }

    public void loadConstantClasses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ApplicationContext context = Utils.getApplicationContext();
        Map<String, Object> beanMap = context.getBeansWithAnnotation(ConstantClass.class);
        List<Map<String, Object>> list = new ArrayList<>();
        Set<String> keySet = beanMap.keySet();
        for (String key : keySet) {
            Object object = beanMap.get(key);
            ConstantClass constantClass = object.getClass().getAnnotation(ConstantClass.class);
            Map<String, Object> map = new HashMap<>(16);
            map.put("name", constantClass.name());
            map.put("label", constantClass.label());
            map.put("type", "Custom");

            List<Constant> constants = ClassUtils.classToConstant(object.getClass());

            map.put("constants", constants);
            list.add(map);
        }
        writeObjectToJson(response, list);
    }

    @Override
    public String url() {
        return "/constanteditor";
    }

}
