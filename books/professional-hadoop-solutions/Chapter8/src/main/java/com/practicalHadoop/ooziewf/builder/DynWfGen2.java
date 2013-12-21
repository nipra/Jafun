package com.practicalHadoop.ooziewf.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;


import com.practicalHadoop.ooziewf.ACTION;
import com.practicalHadoop.ooziewf.ACTIONTRANSITION;
import com.practicalHadoop.ooziewf.CASE;
import com.practicalHadoop.ooziewf.CONFIGURATION;
import com.practicalHadoop.ooziewf.DECISION;
import com.practicalHadoop.ooziewf.END;
import com.practicalHadoop.ooziewf.FLAG;
import com.practicalHadoop.ooziewf.JAVA;
import com.practicalHadoop.ooziewf.KILL;
import com.practicalHadoop.ooziewf.ObjectFactory;
import com.practicalHadoop.ooziewf.START;
import com.practicalHadoop.ooziewf.SUBWORKFLOW;
import com.practicalHadoop.ooziewf.SWITCH;
import com.practicalHadoop.ooziewf.WORKFLOWAPP;
import com.practicalHadoop.ooziewf.DEFAULT;

public class DynWfGen2
{
    public static void main(String[] args) throws JAXBException
    {
        DynWfGen2 dynWfGen2 = new DynWfGen2("${jobTracker}", "${nameNode}"); 
        dynWfGen2.createWfApp();
        
        JAXBContext jc = JAXBContext.newInstance(WORKFLOWAPP.class, com.practicalHadoop.oozieEmail.ACTION.class);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        
        JAXBElement<WORKFLOWAPP> jaxbElem = new JAXBElement<WORKFLOWAPP>(
                new QName("local","workflow-app"),
                WORKFLOWAPP.class, 
                dynWfGen2.getWfApp());
        
        m.marshal(jaxbElem, System.out);
    }

    DynWfGen2(String jobTracker, String nameNode)
    {
        of = new ObjectFactory();
        wfApp = of.createWORKFLOWAPP();
        this.jobTracker = jobTracker;
        this.nameNode = nameNode;
    }
    
    WORKFLOWAPP getWfApp()
    {
        return wfApp;
    }
    
    ObjectFactory of;
    WORKFLOWAPP wfApp;
    String jobTracker;
    String nameNode;
    int totalIter = 2;
    int currentIter = 1;
    
    void createWfApp()
    {
        wfApp.setName("dyn-wf");
        addStartNode("call-dyn-wf_1");
        
        for(currentIter = 1; currentIter <= totalIter; currentIter++)
        {
            Map<String, String> propMap = new HashMap<String, String>();
            propMap.put("radius", "0.05");
            propMap.put("densThreshold", "10");
            addSunWorkflowAction("call-dyn-wf_" + currentIter, "dynWF.xml", 
                    propMap, "check-dynSWF_" + currentIter, "fail");

            propMap = new HashMap<String, String>();
            addJavaAction("check-dynSWF_" + currentIter, "com.practicalHadoop.strand.CheckDynSWF", 
                    propMap, "decide-continue_" + currentIter, "fail");

            Map<String, String> cases = new HashMap<String, String>();
            cases.put("report-success", "${wf:actionData('" + "check-dynSWF_" + currentIter + 
                    "')['done' == " + "true");
            
            String sDecisionDefault = "call-dyn-wf_" + (currentIter + 1);
            if(currentIter == totalIter)
                sDecisionDefault = "report-success";
                
            addDecisionNode("decide-continue_" + currentIter, cases, sDecisionDefault);
            
        }
        addEmailAction("report-failure", "fail", "fail", 
                "OoAdmin@company.com", "othersAdmin@company.com", 
                "cluster-wf failed",
                "cluster-wf failed, error message: /n/t/t [${wf:errorMessage(wf:lastErrorNode())}]");
        
        addEmailAction("report-success", "end", "fail", 
                "OoAdmin@company.com", "othersAdmin@company.com", 
                "cluster-wf failed",
                "cluster-wf finished");
        addKillNode("fail", "cluster-wf failed, error message: \n\t [${wf:errorMessage(wf:lastErrorNode())}]");
        addEndNode("end");
    }

    
    private void addStartNode(String startNodeName)
    {
        START start = of.createSTART();
        start.setTo(startNodeName);
        wfApp.setStart(start);
    }
    
    private void addDecisionNode(String name, Map<String, String> cases, String defaultElemTo)
    {
        DECISION decision = of.createDECISION();
        decision.setName(name);
        wfApp.getDecisionOrForkOrJoin().add(decision);

        SWITCH switchElem = of.createSWITCH();
        decision.setSwitch(switchElem);

        Set<Map.Entry<String, String>> entries = cases.entrySet();
        for(Entry<String, String> entry : entries)
        {
            CASE caseElem = of.createCASE();
            caseElem.setTo(entry.getKey());
            caseElem.setValue(entry.getValue());
            switchElem.getCase().add(caseElem);
        }
            
        DEFAULT defaultElem = of.createDEFAULT();
        defaultElem.setTo(defaultElemTo);
        switchElem.setDefault(defaultElem);
    }
    
    private void addKillNode(String name, String message)
    {
        KILL killNode = of.createKILL();
        killNode.setName(name);
        killNode.setMessage(message);
        wfApp.getDecisionOrForkOrJoin().add(killNode);
    }
    
    private void addEndNode(String name)
    {
        END endNode = of.createEND();
        endNode.setName(name);
        wfApp.setEnd(endNode);
    }

    
    private void addJavaAction(String name, String mainClass, Map<String, String> propMap,
            String okNodeName, String failNodeName)
    {
        ACTION action = of.createACTION();
        action.setName(name);
        JAVA javaAction = of.createJAVA();
        action.setJava(javaAction);
        
        javaAction.setJobTracker(jobTracker);
        javaAction.setNameNode(nameNode);
        javaAction.setMainClass("com.practicalHadoop.strand.CheckDynSWF");
        javaAction.setCaptureOutput(new FLAG());
        
        CONFIGURATION callSwfConf = makeConfiguration(propMap);
        javaAction.setConfiguration(callSwfConf);
        
        setOkTransition(action, okNodeName);
        setFailTransition(action, failNodeName);
        
        wfApp.getDecisionOrForkOrJoin().add(action);
    }
    
    private void addSunWorkflowAction(String name, String appPath, Map<String, String> propMap,
            String okNodeName, String failNodeName)
    {
        ACTION action = of.createACTION();
        action.setName(name);
        // create sub-workflow
        SUBWORKFLOW swf = of.createSUBWORKFLOW();
        action.setSubWorkflow(swf);
        
        swf.setAppPath(appPath);
        swf.setPropagateConfiguration(new FLAG());

        CONFIGURATION callSwfConf = makeConfiguration(propMap);
        swf.setConfiguration(callSwfConf);
        
        setOkTransition(action, okNodeName);
        setFailTransition(action, failNodeName);
        
        wfApp.getDecisionOrForkOrJoin().add(action);
    }
    
    private void addEmailAction(String actName, String okNodeName, String failNodeName, String mailTo, String cc, String subject, String body)
    {
        ACTION action = of.createACTION();
        action.setName(actName);
        
        com.practicalHadoop.oozieEmail.ObjectFactory emialOf = 
                new com.practicalHadoop.oozieEmail.ObjectFactory();
        
        com.practicalHadoop.oozieEmail.ACTION email = emialOf.createACTION();
        email.setTo(mailTo);
        email.setCc(cc);
        email.setSubject(subject);
        email.setBody(body);

        action.setAny(email);
        setOkTransition(action, okNodeName);
        setFailTransition(action, failNodeName);
        
        wfApp.getDecisionOrForkOrJoin().add(action);
    }

    
    private CONFIGURATION makeConfiguration(Map<String, String> propMap)
    {
        CONFIGURATION callSwfConf = of.createCONFIGURATION();

        Set<Map.Entry<String, String>> entries = propMap.entrySet();
        for(Entry<String, String> entry : entries)
        {
            String name = entry.getKey();
            String value = entry.getValue();
            CONFIGURATION.Property prop = new CONFIGURATION.Property();
            prop.setName(name);
            prop.setValue(value);
            callSwfConf.getProperty().add(prop);
        }
        return callSwfConf;
    }
    
    private void setOkTransition(ACTION act, String nodeName)
    {
        ACTIONTRANSITION okCallWfTrans =  of.createACTIONTRANSITION();
        okCallWfTrans.setTo(nodeName);
        act.setOk(okCallWfTrans);
    }
    
    private void setFailTransition(ACTION act, String nodeName)
    {
        ACTIONTRANSITION errorCallWfTrans =  of.createACTIONTRANSITION();
        errorCallWfTrans.setTo(nodeName);
        act.setError(errorCallWfTrans);
    }
}
