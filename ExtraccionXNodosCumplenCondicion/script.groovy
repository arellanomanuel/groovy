import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.XmlUtil;
import groovy.util.*;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String);
    def response = new XmlParser().parseText(body);
    //def finished = "N"
    def nodeToExtract = response.'**'.find { it.name() == 'endDate' && it.text() =='9999-12-31T00:00:00.000' };
    parent = nodeToExtract.parent()
    def nodeAsText = XmlUtil.serialize(parent)
    
    while(nodeToExtract != null){
        parent.remove(nodeToExtract)
        nodeToExtract = response.'**'.find { it.name() == 'endDate' && it.text() =='9999-12-31T00:00:00.000' };
        if(nodeToExtract != null){
        parent = nodeToExtract.parent()
        nodeAsText += XmlUtil.serialize(parent)
        }
    }
    message.setBody(nodeAsText);
    return message;
}
