package cjit.fmss.webservice;

import com.cjit.fmss.api.hessian.secure.NoSSLCertificateCheckSocketFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class UrlConnectService
{
  private static Logger log = Logger.getLogger(UrlConnectService.class);

  public String getResult(String url) {System.out.println("cjit.fmss.webservice.UrlConnectService1:"+url);
    String result = "";
    try {
      if (url.toLowerCase().indexOf("https") == 0) {
        new NoSSLCertificateCheckSocketFactory()
          .resetHttpsProtocolSocketFactory(url);
      }

      HttpClient hc = new HttpClient();
      GetMethod gm = new GetMethod(url);System.out.println("cjit.fmss.webservice.UrlConnectService2"+url);
      hc.executeMethod(gm);
      result = new String(gm.getResponseBody(), "gbk");
    }
    catch (Throwable e) {
      log.warn(e.getMessage(), e);
    }
    return result;
  }
}