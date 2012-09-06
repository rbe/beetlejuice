/*
 * beetlejuice
 * beetlejuice-spring
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 06.09.12 09:38
 */

package eu.artofcoding.beetlejuice.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class Slf4jPostProcessor implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(Slf4jPostProcessor.class);

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @SuppressWarnings("unchecked")
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(field);
                // Check if the field is annoted with @Slf4j
                if (field.getAnnotation(Slf4j.class) != null) {
                    logger.debug("Injecting Logger into " + beanName + "/" + bean.getClass());
                    //Slf4j slf4jAnnotation = field.getAnnotation(Slf4j.class);
                    Logger logger = LoggerFactory.getLogger(bean.getClass());
                    field.set(bean, logger);
                }
            }
        });
        return bean;
    }

}
