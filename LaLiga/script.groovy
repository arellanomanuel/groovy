import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.XmlUtil;
import groovy.util.*;
import java.time.LocalDate;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String)
    def response = new XmlParser().parseText(body)
    


    def fechaInicioCorte1 = '2022-02-01'
    def fechaFinCorte1 = '2022-02-01'
    def fechaInicioCorte2 = '2022-02-01'
    def fechaFinCorte2 = '2022-04-04'

    def overlap = doDateRangesOverlap(fechaInicioCorte1, fechaFinCorte1, fechaInicioCorte2, fechaFinCorte2)
    println "¿Hay alguna fecha que coincida entre los intervalos? ${overlap}"
    
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
    return message
}

def doDateRangesOverlap(startDate1, endDate1, startDate2, endDate2) {
    def rangeStart1 = LocalDate.parse(startDate1)
    def rangeEnd1 = LocalDate.parse(endDate1)
    def rangeStart2 = LocalDate.parse(startDate2)
    def rangeEnd2 = LocalDate.parse(endDate2)
    
    return !(rangeEnd1.isBefore(rangeStart2) || rangeStart1.isAfter(rangeEnd2))
}
