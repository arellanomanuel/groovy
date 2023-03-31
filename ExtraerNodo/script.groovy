import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.XmlUtil;
import groovy.util.*;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String);
    def response = new XmlParser().parseText(body);
    def nodeToSerialize = response.'**'.find { it.name() == 'author' };
    def nodeAsText = XmlUtil.serialize(nodeToSerialize)
    message.setBody(nodeAsText);
    return message;
}
