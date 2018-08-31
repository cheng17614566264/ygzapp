
/**
 * CemReceiverServer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.3  Built on : May 30, 2016 (04:08:57 BST)
 */

package com.cjit.webService.client.service.cemreceiverserver._1_0;

/*
 *  CemReceiverServer java interface
 */

public interface CemReceiverServer {

	/**
	 * Auto generated method signature 费控ws服务类
	 * 
	 * @param cemReceiver0
	 * 
	 */

	public CemReceiverResponse cemReceiver(

			CemReceiver cemReceiver0) throws java.rmi.RemoteException;

	/**
	 * Auto generated method signature for Asynchronous Invocations 费控ws服务类
	 * 
	 * @param cemReceiver0
	 * 
	 */
	public void startcemReceiver(

			CemReceiver cemReceiver0,

			final CemReceiverServerCallbackHandler callback)

			throws java.rmi.RemoteException;

	//
}
