package com.packt.dependencyManagement.chapter3;

import javax.jms.TextMessage;
import javax.ejb.EJBContext;


public class ScopeProvidedClass {
    public void scopeProvidedMethod() {
        final TextMessage textMessage = null;
        final EJBContext ejbContext = null;
    }
}
