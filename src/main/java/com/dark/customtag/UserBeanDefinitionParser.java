package com.dark.customtag;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Created by dark on 2017/6/20.
 */
public class UserBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return User.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String userNamae=element.getAttribute("userName");
        String email=element.getAttribute("email");
        if(StringUtils.hasText(userNamae)){
            builder.addPropertyValue("userName",userNamae);
        }
        if (StringUtils.hasText("email")){
            builder.addPropertyValue("email",email);
        }
    }

    
}
