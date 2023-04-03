import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.XmlUtil;
import groovy.util.*;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String);
    def response = new XmlParser().parseText(body);
    // mismo efecto que la linea 11
    // def nodeToDel = response.'**'.find { it.name() == 'book' };
    def nodeToDel = response.value.books.book.find { it.name() == 'book' };
    def parent = nodeToDel.parent()
    parent.remove(nodeToDel)
    def nodeAsText = XmlUtil.serialize(response)
    message.setBody(nodeAsText);
    return message;
}
