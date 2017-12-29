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
package com.bstek.urule.console.servlet;

import com.bstek.urule.Configure;
import com.bstek.urule.RuleException;
import com.bstek.urule.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;


/**
 * @author Jacky.gao
 * @since 2016年6月3日
 */
public abstract class BaseServletHandler implements ServletHandler, ApplicationContextAware {
    protected VelocityEngine ve;
    protected ApplicationContext applicationContext;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = retrieveMethod(req);
        if (method != null) {
            invokeMethod(method, req, resp);
        } else {
            handleRequest(req, resp);
        }
    }

    /**
     * 处理访问
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected abstract void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    protected void invokeMethod(String methodName, HttpServletRequest req, HttpServletResponse resp) {
        Method method;
        try {
            method = this.getClass().getMethod(methodName, new Class<?>[]{HttpServletRequest.class, HttpServletResponse.class});
            method.invoke(this, new Object[]{req, resp});
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuleException(e);
        }
    }

    protected String retrieveMethod(HttpServletRequest req) {
        String path = req.getContextPath() + URuleServlet.URL;
        String uri = req.getRequestURI();
        String targetUrl = uri.substring(path.length());
        int slashPos = targetUrl.indexOf("/", 1);
        if (slashPos > -1) {
            String methodName = targetUrl.substring(slashPos + 1).trim();
            return methodName.length() > 0 ? methodName : null;
        }
        return null;
    }

    protected void writeObjectToJson(HttpServletResponse resp, Object obj) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat(Configure.getDateFormat()));
        OutputStream out = resp.getOutputStream();
        try {
            mapper.writeValue(out, obj);
        } finally {
            out.flush();
            out.close();
        }
    }

    protected String buildProjectNameFromFile(String file) {
        String project = null;
        if (StringUtils.isNotBlank(file)) {
            file = Utils.decodeURL(file);
            if (file.startsWith("/")) {
                file = file.substring(1, file.length());
                int pos = file.indexOf("/");
                project = file.substring(0, pos);
            }
        }
        return project;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ve = new VelocityEngine();
        ve.setProperty(Velocity.RESOURCE_LOADER, "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM, new NullLogChute());
        ve.init();
    }
}
