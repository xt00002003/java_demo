package com.dark.util;


import net.sf.cglib.beans.BeanCopier;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanUtils {
	private final static String DEFAULT_CHARSET = "UTF-8";

	private final static Map<Class<?>, Map<Class<?>, BeanCopier>> beanCopiers = new ConcurrentHashMap<Class<?>, Map<Class<?>, BeanCopier>>(
			128);

	public static void quickCopy(Object source, Object target) {
		Assert.notNull(source);
		Assert.notNull(target);
		BeanCopier beanCopier = getBeanCopier(source.getClass(), target.getClass());
		beanCopier.copy(source, target, null);
	}


	private static BeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass) {
		Map<Class<?>, BeanCopier> mapInner = beanCopiers.get(sourceClass);
		BeanCopier beanCopier = null;
		if (mapInner == null) {
			mapInner = new ConcurrentHashMap<Class<?>, BeanCopier>(128);
			beanCopier = BeanCopier.create(sourceClass, targetClass, false);
			mapInner.put(targetClass, beanCopier);
			beanCopiers.put(sourceClass, mapInner);
		} else {
			beanCopier = mapInner.get(targetClass);
			if (beanCopier == null) {
				beanCopier = BeanCopier.create(sourceClass, targetClass, false);
				mapInner.put(targetClass, beanCopier);
			}
		}
		return beanCopier;
	}

}
