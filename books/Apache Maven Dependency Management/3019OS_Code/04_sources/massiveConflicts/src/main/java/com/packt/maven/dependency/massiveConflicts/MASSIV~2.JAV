package com.packt.maven.dependency.massiveConflicts;

import org.apache.camel.component.exec.ExecCommand;
import org.apache.camel.impl.DefaultCamelContext;
import org.grep4j.core.GrepExpression;

public class MassiveConflictsFoo {
    private ExecCommand execCommand;
    // from camel-core-2.9.7, that is induced by camel-exec
    private DefaultCamelContext defaultCamelContext;
    private GrepExpression grepExpression;
}
