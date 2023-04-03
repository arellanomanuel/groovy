import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.XmlUtil;
import groovy.util.*;

def Message processData(Message message) {
    def response=message.getBody(java.lang.String);
    response=response.replace(/<?xml version="1.0" encoding="UTF-8"?>/,"");
    message.setBody(response);
    return message;
}
