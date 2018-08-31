package com.cjit.webService.client;

/**
 * InvoicePrintServiceService.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.7.3
 * Built on : May 30, 2016 (04:08:57 BST)
 */

/*
 * InvoicePrintServiceService java interface
 */

public interface InvoicePrintServiceService {

	/**
	 * Auto generated method signature
	 * 
	 * @param submitData0
	 * 
	 */

	public SubmitDataResponse submitData(

			SubmitData submitData0) throws java.rmi.RemoteException;

	/**
	 * Auto generated method signature for Asynchronous Invocations
	 * 
	 * @param submitData0
	 * 
	 */
	public void startsubmitData(

			SubmitData submitData0,

			final InvoicePrintServiceServiceCallbackHandler callback)

			throws java.rmi.RemoteException;

	//
}
