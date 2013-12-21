package com.practicalHadoop.ooziewf.builder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import com.practicalHadoop.ooziewf.ACTION;
import com.practicalHadoop.ooziewf.ACTIONTRANSITION;
import com.practicalHadoop.ooziewf.CONFIGURATION;
import com.practicalHadoop.ooziewf.END;
import com.practicalHadoop.ooziewf.FLAG;
import com.practicalHadoop.ooziewf.JAVA;
import com.practicalHadoop.ooziewf.KILL;
import com.practicalHadoop.ooziewf.ObjectFactory;
import com.practicalHadoop.ooziewf.START;
import com.practicalHadoop.ooziewf.SUBWORKFLOW;
import com.practicalHadoop.ooziewf.WORKFLOWAPP;


public class DynWfGen {
	
	public static void main(String[] args) throws JAXBException
	{
		ObjectFactory of = new ObjectFactory();
		
		// create workflow object
		WORKFLOWAPP wfApp = of.createWORKFLOWAPP();
		wfApp.setName("dyn-wf");
		
		START start = of.createSTART();
		start.setTo("call-dyn-wf_1");
		wfApp.setStart(start);
		
		// create first action: sub-workflow
		ACTION callWf = of.createACTION();
		callWf.setName("call-dyn-wf_1");
		// create sub-workflow
		SUBWORKFLOW swf = of.createSUBWORKFLOW();
		swf.setAppPath("dynWF.xml");
		
		swf.setPropagateConfiguration(new FLAG());
		CONFIGURATION callSwfConf = of.createCONFIGURATION();
		CONFIGURATION.Property prop1 = new CONFIGURATION.Property();
		prop1.setName("radius");
		prop1.setValue("0.05");
		callSwfConf.getProperty().add(prop1);
		CONFIGURATION.Property prop2 = new CONFIGURATION.Property();
		prop2.setName("densThreshold");
		prop2.setValue("10");
		callSwfConf.getProperty().add(prop2);
		swf.setConfiguration(callSwfConf);
		
		callWf.setSubWorkflow(swf);
		
		ACTIONTRANSITION okCallWfTrans =  of.createACTIONTRANSITION();
		okCallWfTrans.setTo("check-dynSWF");
		callWf.setOk(okCallWfTrans);
		ACTIONTRANSITION errorCallWfTrans =  of.createACTIONTRANSITION();
		errorCallWfTrans.setTo("fail");
		callWf.setError(errorCallWfTrans);
		
		wfApp.getDecisionOrForkOrJoin().add(callWf);

		// create second action  - java, check-dynSWF
		ACTION checkSwfResults = of.createACTION();
		checkSwfResults.setName("check-dynSWF_1");
		
		JAVA javaCheckSwfResults = of.createJAVA();
		javaCheckSwfResults.setJobTracker("${jobTracker}");
		javaCheckSwfResults.setNameNode("${nameNode}");
		javaCheckSwfResults.setMainClass("com.practicalHadoop.strand.CheckDynSWF");
		javaCheckSwfResults.setCaptureOutput(new FLAG());
		checkSwfResults.setJava(javaCheckSwfResults);

		ACTIONTRANSITION okCheckWf =  of.createACTIONTRANSITION();
		okCheckWf.setTo("decide-continue");
		checkSwfResults.setOk(okCheckWf);
		ACTIONTRANSITION errorCheckWf =  of.createACTIONTRANSITION();
		errorCheckWf.setTo("fail");
		checkSwfResults.setError(errorCallWfTrans);
		
		wfApp.getDecisionOrForkOrJoin().add(checkSwfResults);
		
		// add decision node
		// add 'report-failure' email action
		// add 'report-success' email action
		
		KILL killNode = of.createKILL();
		killNode.setName("fail");
		killNode.setMessage("cluster-wf failed, error message: \n\t [${wf:errorMessage(wf:lastErrorNode())}]");
		wfApp.getDecisionOrForkOrJoin().add(killNode);
		
		END end = of.createEND();
		end.setName("end");
		wfApp.setEnd(end);
		
		JAXBContext jc = JAXBContext.newInstance("com.practicalHadoop.ooziewf");
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		JAXBElement<WORKFLOWAPP> jaxbElem = new JAXBElement<WORKFLOWAPP>(
				new QName("local","workflow-app"),
				WORKFLOWAPP.class, 
				wfApp);
		
		m.marshal(jaxbElem, System.out);
	}
}
