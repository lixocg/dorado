package com.experian.core.utils;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
public class PropUtil implements EmbeddedValueResolverAware {
    
    private static StringValueResolver stringValueResolver;

    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        stringValueResolver = resolver;
    }

    public static String getValue(String name){
    	name="${"+name+"}";
        return stringValueResolver.resolveStringValue(name);
    }
}