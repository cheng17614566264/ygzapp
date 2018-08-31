
/**
 * CemReceiverServerCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.3  Built on : May 30, 2016 (04:08:57 BST)
 */

package com.cjit.webService.client.service.cemreceiverserver._1_0;

/**
 * CemReceiverServerCallbackHandler Callback class, Users can extend this class
 * and implement their own receiveResult and receiveError methods.
 */
public abstract class CemReceiverServerCallbackHandler {

	protected Object clientData;

	/**
	 * User can pass in any object that needs to be accessed once the
	 * NonBlocking Web service call is finished and appropriate method of this
	 * CallBack is called.
	 * 
	 * @param clientData
	 *            Object mechanism by which the user can pass in user data that
	 *            will be avilable at the time this callback is called.
	 */
	public CemReceiverServerCallbackHandler(Object clientData) {
		this.clientData = clientData;
	}

	/**
	 * Please use this constructor if you don't want to set any clientData
	 */
	public CemReceiverServerCallbackHandler() {
		this.clientData = null;
	}

	/**
	 * Get the client data
	 */

	public Object getClientData() {
		return clientData;
	}

	/**
	 * auto generated Axis2 call back method for cemReceiver method override
	 * this method for handling normal response from cemReceiver operation
	 */
	public void receiveResultcemReceiver(CemReceiverResponse result) {
	}

	/**
	 * auto generated Axis2 Error handler override this method for handling
	 * error response from cemReceiver operation
	 */
	public void receiveErrorcemReceiver(java.lang.Exception e) {
	}

}
