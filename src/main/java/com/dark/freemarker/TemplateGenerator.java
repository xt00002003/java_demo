package com.dark.freemarker;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class TemplateGenerator {
    static Configuration configuration = null;
    private static final Logger logger = LoggerFactory.getLogger(TemplateGenerator.class);
    static {
        configuration = new Configuration();
        configuration.setTemplateLoader(new ClassTemplateLoader(TemplateGenerator.class, "/"));
    }

    public static String generate(String templatePath, Map<String, Object> dataMap) {
        Template template;
        try {
            template = configuration.getTemplate(templatePath);
            StringWriter result = new StringWriter();
            template.process(dataMap, result);
            return result.toString();
        } catch (IOException | TemplateException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
