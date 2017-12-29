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
package com.bstek.urule;

import com.bstek.urule.model.Const;
import com.bstek.urule.model.Label;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import com.bstek.urule.model.library.constant.Constant;
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.Variable;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.bstek.urule.Utils.getDatatypeFromClass;

/**
 * @author Jacky.gao
 * @since 2016年6月2日
 */
public class ClassUtils {
    public static void classToXml(Class<?> cls, File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            List<Variable> variables = classToVariables(cls);
            StringBuffer sb = new StringBuffer();
            sb.append("<variables clazz=\"" + cls.getName() + "\">");
            for (Variable var : variables) {
                sb.append("<variable ");
                sb.append("name=\"" + var.getName() + "\" ");
                if (var.getLabel() != null) {
                    sb.append("label=\"" + var.getLabel() + "\" ");
                }
                if (var.getDefaultValue() != null) {
                    sb.append("defaultValue=\"" + var.getDefaultValue() + "\" ");
                }
                if (var.getType() != null) {
                    sb.append("type=\"" + var.getType() + "\" ");
                }
                if (var.getAct() != null) {
                    sb.append("act=\"" + var.getAct() + "\" ");
                }
                sb.append(">");
                sb.append("</variable>");
            }
            sb.append("</variables>");
            Document doc = DocumentHelper.parseText(sb.toString());
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(doc);
            writer.close();
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static List<Method> classToMethods(Class<?> cls) {
        try {
            List<Method> list = new ArrayList<>();
            java.lang.reflect.Method[] methods = cls.getDeclaredMethods();

            for (java.lang.reflect.Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                ActionMethod actionMethod = method.getAnnotation(ActionMethod.class);
                ActionMethodParameter actionMethodParameter = method.getAnnotation(ActionMethodParameter.class);
                if (actionMethod != null && actionMethodParameter != null) {
                    Method method1 = new Method();
                    method1.setName(actionMethod.name());
                    method1.setMethodName(method.getName());

                    String[] names = actionMethodParameter.names();

                    if (names.length != parameterTypes.length) {
                        throw new RuntimeException("ActionMethodParameter注解参数个数不匹配");
                    }

                    for (int i = 0; i < names.length; i++) {
                        String name = names[i];
                        Class<?> type = parameterTypes[i];
                        Parameter parameter = new Parameter();
                        parameter.setName(name);
                        parameter.setType(getDatatypeFromClass(type));
                        method1.addParameter(parameter);
                    }

                    list.add(method1);
                }
            }

            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Constant> classToConstant(Class<?> cls) {
        try {
            Object instance = cls.newInstance();

            List<Constant> list = new ArrayList<>();
            Field[] fields = cls.getDeclaredFields();

            for (Field field : fields) {
                Const c = field.getAnnotation(Const.class);
                if (c != null) {
                    Constant constant = new Constant();
                    Datatype datatype = getDatatypeFromClass(field.getType());
                    Object value = field.get(instance);
                    constant.setValue(datatype.convertObjectToString(value));
                    constant.setLabel(c.value());
                    constant.setType(datatype);
                    list.add(constant);
                }
            }

            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Variable> classToVariables(Class<?> cls) {
        try {
            List<Variable> result = parseClass("", cls, new ArrayList<Class<?>>());
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Variable> parseClass(String path, Class<?> cls, Collection<Class<?>> parsed) throws Exception {
        List<Variable> variables = new ArrayList<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(cls, Object.class);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

        if (pds != null && !parsed.contains(cls)) {
            parsed.add(cls);
            for (PropertyDescriptor pd : pds) {
                Variable variable = new Variable();
                Class<?> type = pd.getPropertyType();
                Datatype dataType = getDatatypeFromClass(type);
                String propertyName = pd.getName();
                String label = getPropertyAnnotationLabel(cls, propertyName);
                String name = path + pd.getName();
                variable.setName(name);
                variable.setLabel(label == null ? name : label);
                variable.setType(dataType);
                variable.setAct(Act.InOut);
                if (Datatype.Object.equals(dataType)) {
                    variables.addAll(parseClass(path + pd.getName() + ".", type, parsed));
                } else {
                    variables.add(variable);
                }
            }
        }
        return variables;
    }

    private static String getPropertyAnnotationLabel(Class<?> cls, String fieldName) throws Exception {
        Field field = null;
        while (field == null) {
            try {
                field = cls.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {
                if (cls == Object.class) {
                    throw ex;
                }
                cls = cls.getSuperclass();
            }
        }
        Label pd = field.getAnnotation(Label.class);
        if (pd != null) {
            return pd.value();
        }
        return null;
    }

}
