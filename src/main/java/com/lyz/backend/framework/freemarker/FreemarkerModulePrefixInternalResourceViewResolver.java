package com.lyz.backend.framework.freemarker;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * 路径规则定义：<br />
 * ApplicationContextPath/child system path/ module path/function path/args or
 * query string
 *
 * @author leo
 */
public class FreemarkerModulePrefixInternalResourceViewResolver extends
        org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver {
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        AbstractUrlBasedView view = (AbstractUrlBasedView) BeanUtils.instantiateClass(getViewClass());
        if (viewName.startsWith("/")) {
            view.setUrl(super.getPrefix() + viewName + getSuffix());
        } else {
            view.setUrl(getPrefix() + viewName + getSuffix());
        }
        String contentType = getContentType();
        if (contentType != null) {
            view.setContentType(contentType);
        }
        view.setRequestContextAttribute(getRequestContextAttribute());
        view.setAttributesMap(getAttributesMap());
        return view;
    }

    @Override
    protected Object getCacheKey(String viewName, Locale locale) {
        if (viewName != null && viewName.startsWith("/")) {
            return viewName;
        } else {
            return getPrefix() + viewName;
        }
    }

    @Override
    protected String getPrefix() {

        StringBuilder prefix = new StringBuilder();
        prefix.append(super.getPrefix());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        String ctxPath = request.getContextPath();
        String uri = request.getRequestURI();

        if (StringUtils.isNotBlank(ctxPath)) {// 应用名称不空
            uri = uri.substring(ctxPath.length());
        }

        StringTokenizer st = new StringTokenizer(uri, "/");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (!st.hasMoreTokens()) {
                break;
            } else {
                prefix.append(token).append("/");
            }
        }

        return prefix.toString();
    }
}
