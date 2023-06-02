import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil
import groovy.util.XmlParser
import java.time.LocalDate

def Message processData(Message message) {
    def body = message.getBody(java.lang.String)
    def response = new XmlParser().parseText(body)
    def roots_aux=[];
    def roots = response.'**'.findAll { it.name() == 'root' }
    def modifiedXml = ""
    
    for (int i = 0; i < roots.size() - 1; i++) {
        def root1 = roots[i]
        def root2 = roots[i + 1]
        def fechaInicioCorte1 = root1.fecha_inicio_corte
        def fechaFinCorte1 = root1.fecha_fin_corte
        def fechaInicioCorte2 = root2.fecha_inicio_corte
        def fechaFinCorte2 = root2.fecha_fin_corte
        def ceco1 = root1.ceco.text()
        def ceco2 = root2.ceco.text()
        def id1 = root1.userId.text()
        def id2 = root2.userId.text()
        def fte1 = root1.fte.text()
        def fte2 = root2.fte.text()

        def overlap = doDateRangesOverlap(fechaInicioCorte1, fechaFinCorte1, fechaInicioCorte2, fechaFinCorte2)
        println "¿Hay alguna fecha que coincida entre los intervalos? ${overlap}"
        def consecutivos = fusionarConsecutivos(fechaFinCorte1, fechaInicioCorte2)
        println "¿Consecutivos? ${consecutivos}"

        if (overlap && ceco1 == ceco2 && id1 == id2 && fte1 == fte2) {
            println "iguales"
            //roots[i] = roots[i + 1]
            //roots.remove(i + 1)
            roots.remove(i)
            println "${roots}"

        }
        
        //def consecutivos = fusionarConsecutivos(fechaFinCorte1, fechaInicioCorte2)
         if (consecutivos && ceco1 == ceco2 && id1 == id2 && fte1 == fte2) {
            println "iguales2"
            //roots[i] = roots[i + 1]

            //roots[i].appendNode("fecha_inicio_corte",fechaInicioCorte1.text())
             roots[i+1].replaceNode {
                // Crea un nuevo nodo con los mismos elementos y valores del root2
                'root' {
                    
                    fecha_inicio_corte(fechaInicioCorte1)
                    fecha_fin_corte(fechaFinCorte2)
        
                    // Agrega los demás elementos que necesites
                }
                }
            roots.remove(i)
        }
        
        
        modifiedXml += XmlUtil.serialize(roots[i])
    }
    modifiedXml=modifiedXml.replace(/<?xml version="1.0" encoding="UTF-8"?>/,"");
    message.setBody(modifiedXml)
    return message
}

def doDateRangesOverlap(startDate1, endDate1, startDate2, endDate2) {
    def rangeStart1 = LocalDate.parse(startDate1.text())
    def rangeEnd1 = LocalDate.parse(endDate1.text())
    def rangeStart2 = LocalDate.parse(startDate2.text())
    def rangeEnd2 = LocalDate.parse(endDate2.text())

    return !(rangeEnd1.isBefore(rangeStart2) || rangeStart1.isAfter(rangeEnd2))
}

def fusionarConsecutivos(endDate1, startDate2) {
    def rangeEnd1 = LocalDate.parse(endDate1.text())
    def rangeStart2 = LocalDate.parse(startDate2.text())
    return rangeEnd1.plusDays(1).isEqual(rangeStart2)
}
