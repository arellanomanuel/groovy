import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.XmlUtil;
import groovy.util.*;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String)
    def response = new XmlParser().parseText(body)
    def nodeToExtract1 = response.'**'.find { it.name() == 'd:status' }
    def nodeToExtract2 = response.'**'.find { it.name() == 'd:message' }
    message.setProperty("OK_KO", nodeToExtract1.text());
    message.setProperty("TXT_ERROR", nodeToExtract2.text());
    //def nodeAsText = XmlUtil.serialize(nodeToExtract1) + XmlUtil.serialize(nodeToExtract2)
    //message.setBody(nodeAsText)
    return message
}
