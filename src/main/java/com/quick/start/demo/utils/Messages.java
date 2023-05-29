package com.quick.start.demo.utils;


import io.jsonwebtoken.lang.Assert;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class Messages {

    private static final Map<Integer, Messages> messagesCache = new ConcurrentHashMap<>();
    private final String placeholdPrefix;
    private final String placeholdSuffix;
    private final String simplePrefix;
    private final ResourceBundle bundle;

    public Messages( ResourceBundle bundle) {
        this.placeholdPrefix = "{";
        this.placeholdSuffix = "}";
        this.simplePrefix = placeholdPrefix;
        this.bundle = bundle;
    }

    public static Messages load(String baseName){
        return load(baseName, Locale.getDefault());
    }


    public static Messages load(String baseName, Locale locale){
        return getOrResolve(Objects.hash(baseName, locale.getLanguage()), () -> new Messages((ResourceBundle.getBundle(baseName, locale))));
    }

    private static Messages getOrResolve(int hashCode, Supplier<Messages> supplier) {
        Messages messages;
        if((messages = messagesCache.get(hashCode)) == null){
            synchronized ((Messages.class)) {
                if((messages = messagesCache.get(hashCode)) == null) {
                    messagesCache.put(hashCode, messages = supplier.get());
                }
            }
        }
        return messages;
    }

    public String getProperty(String key){
        if(key == null || key.trim().isEmpty()){
            return "";
        }
        return getBundle().getString(key);
    }


    public String getProperty(String key,Object... params){
        String property = getProperty(key);
        return replacePlaceholders(property,placeholder -> {
            try {
                return params[Integer.parseInt(placeholder)].toString();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });
    }

    private ResourceBundle getBundle() {
        return bundle;
    }

    private String replacePlaceholders(String value, PlaceholderResolver placeholderResolver){
        Assert.notNull(value, "'value' must not be null");
        return parseStringValue(value, placeholderResolver, null);
    }

    private String parseStringValue(String value, PlaceholderResolver placeholderResolver, @Nullable Set<String> visitedPlaceholders) {
        int startIndex = value.indexOf(this.placeholdPrefix);
        if(startIndex== -1){
            return value;
        }
        StringBuilder result = new StringBuilder(value);

        while (startIndex!=-1){
            int endIndex = findPlaceholderEndIndex(result, startIndex);
            if(endIndex!=-1){
                String placeholder = result.substring(startIndex + this.placeholdPrefix.length(), endIndex);
                String originalPlaceholder = placeholder;
                if(visitedPlaceholders == null){
                    visitedPlaceholders = new HashSet<>(4);
                }
                if(!visitedPlaceholders.add(originalPlaceholder)){
                    throw new IllegalArgumentException("Circular placeholder reference '" + originalPlaceholder + "'in property definitions");
                }
                placeholder = parseStringValue(placeholder, placeholderResolver, visitedPlaceholders);
                String propVal = placeholderResolver.resolvePlaceholder(placeholder);
                if(propVal!=null){
                    propVal = parseStringValue(propVal, placeholderResolver, visitedPlaceholders);
                    result.replace(startIndex, endIndex + this.placeholdSuffix.length(), propVal);
                    startIndex = result.indexOf(this.placeholdPrefix, startIndex + propVal.length());
                }else {
                    startIndex = result.indexOf(this.placeholdPrefix, endIndex + this.placeholdSuffix.length());
                }
                visitedPlaceholders.remove(originalPlaceholder);
            }else {
                startIndex =-1;
            }
        }
        return result.toString();
    }

    private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
        int index = startIndex + this.placeholdPrefix.length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if(StringUtils.substringMatch(buf,index,this.placeholdSuffix)){
                if(withinNestedPlaceholder>0){
                    withinNestedPlaceholder--;
                    index = index + this.placeholdSuffix.length();
                }else {
                    return index;
                }

            }else if (StringUtils.substringMatch(buf,index,this.simplePrefix)){
                withinNestedPlaceholder++;
                index = index + this.simplePrefix.length();
            }else {
                index++;
            }
        }
        return -1;
    }

    @FunctionalInterface
    public interface PlaceholderResolver{
        @Nullable
        String resolvePlaceholder(String placeholder);
    }


}
