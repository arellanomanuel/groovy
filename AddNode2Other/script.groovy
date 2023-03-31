import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.XmlUtil;
import groovy.util.*;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String);
    def root = new XmlParser().parseText(body)
    root.data.row.each { row ->
        row.appendNode("Campo5", "añadido en todos")
        row.appendNode("Campo6", "añadido en todos")
    }

    /*La linea siguiente sirve para devolver un String del nodo*/
    message.setBody(XmlUtil.serialize(root));
    return message;
}
