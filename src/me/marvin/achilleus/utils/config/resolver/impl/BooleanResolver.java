package me.marvin.achilleus.utils.config.resolver.impl;

import me.marvin.achilleus.utils.config.resolver.ConfigResolver;

public class BooleanResolver implements ConfigResolver<Boolean> {
    @Override
    public Boolean resolve(Object value) {
        return Boolean.valueOf(value.toString());
    }
}
