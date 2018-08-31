/**
 * TaxInvoiceWebServicePortServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sinosoft.lis.TaxInvoiceWebService;

public class TaxInvoiceWebServicePortServiceLocator extends org.apache.axis.client.Service implements com.sinosoft.lis.TaxInvoiceWebService.TaxInvoiceWebServicePortService {

    public TaxInvoiceWebServicePortServiceLocator() {
    }


    public TaxInvoiceWebServicePortServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TaxInvoiceWebServicePortServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TaxInvoiceWebService
    private java.lang.String TaxInvoiceWebService_address = "http://TaxInvoiceWebService.lis.sinosoft.com";

    public java.lang.String getTaxInvoiceWebServiceAddress() {
        return TaxInvoiceWebService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TaxInvoiceWebServiceWSDDServiceName = "TaxInvoiceWebService";

    public java.lang.String getTaxInvoiceWebServiceWSDDServiceName() {
        return TaxInvoiceWebServiceWSDDServiceName;
    }

    public void setTaxInvoiceWebServiceWSDDServiceName(java.lang.String name) {
        TaxInvoiceWebServiceWSDDServiceName = name;
    }

    public com.sinosoft.lis.TaxInvoiceWebService.TaxInvoiceWebServicePort getTaxInvoiceWebService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TaxInvoiceWebService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTaxInvoiceWebService(endpoint);
    }

    public com.sinosoft.lis.TaxInvoiceWebService.TaxInvoiceWebServicePort getTaxInvoiceWebService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.sinosoft.lis.TaxInvoiceWebService.TaxInvoiceWebServiceSoapBindingStub _stub = new com.sinosoft.lis.TaxInvoiceWebService.TaxInvoiceWebServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getTaxInvoiceWebServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTaxInvoiceWebServiceEndpointAddress(java.lang.String address) {
        TaxInvoiceWebService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.sinosoft.lis.TaxInvoiceWebService.TaxInvoiceWebServicePort.class.isAssignableFrom(serviceEndpointInterface)) {
                com.sinosoft.lis.TaxInvoiceWebService.TaxInvoiceWebServiceSoapBindingStub _stub = new com.sinosoft.lis.TaxInvoiceWebService.TaxInvoiceWebServiceSoapBindingStub(new java.net.URL(TaxInvoiceWebService_address), this);
                _stub.setPortName(getTaxInvoiceWebServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TaxInvoiceWebService".equals(inputPortName)) {
            return getTaxInvoiceWebService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://TaxInvoiceWebService.lis.sinosoft.com", "TaxInvoiceWebServicePortService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://TaxInvoiceWebService.lis.sinosoft.com", "TaxInvoiceWebService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TaxInvoiceWebService".equals(portName)) {
            setTaxInvoiceWebServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
