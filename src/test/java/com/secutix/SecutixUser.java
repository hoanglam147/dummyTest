package com.secutix;

import com.secutix.util.OperatorData;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SecutixUser {
    OperatorData value();
}
