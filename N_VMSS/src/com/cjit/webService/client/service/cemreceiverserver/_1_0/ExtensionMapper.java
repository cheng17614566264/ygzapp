
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.3  Built on : May 30, 2016 (04:09:26 BST)
 */

        
            package com.cjit.webService.client.service.cemreceiverserver._1_0;

import com.cjit.webService.client.service.cemreceiverserver.CemReceiverResponse_CType;
import com.cjit.webService.client.service.cemreceiverserver.CemReceiver_CType;

/**
            *  ExtensionMapper class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://service.kurrent.com/cemReceiverServer".equals(namespaceURI) &&
                  "cemReceiver_CType".equals(typeName)){
                   
                            return  CemReceiver_CType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://service.kurrent.com/cemReceiverServer".equals(namespaceURI) &&
                  "cemReceiverResponse_CType".equals(typeName)){
                   
                            return  CemReceiverResponse_CType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    