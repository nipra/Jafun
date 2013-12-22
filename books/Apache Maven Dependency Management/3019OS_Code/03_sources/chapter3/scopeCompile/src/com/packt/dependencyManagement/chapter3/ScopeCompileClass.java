package com.packt.dependencyManagement.chapter3;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

public class ScopeCompileClass {
    public void scopeCompileMethod() {
        Assert.isNull(null);
        Logger.getLogger(ScopeCompileClass.class).info("hello world");
    }
}
