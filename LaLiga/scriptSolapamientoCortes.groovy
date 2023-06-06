import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil
import groovy.util.XmlParser
import java.time.LocalDate
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
    def body = message.getBody(java.lang.String)
    def response = new XmlParser().parseText(body)

    //def roots = response.'**'.findAll { it.name() == 'root' }
    def parsedXml = new XmlSlurper().parseText(body)

        // Crear un nuevo XML
    def writer = new StringWriter()
    def xmlBuilder = new MarkupBuilder(writer)

    // Construir el nuevo XML
    xmlBuilder.EMPLEADOS {
            parsedXml.CompoundEmployee.person.employment_information.each { employmentInfo ->
                EMPLEADO {
                    CODEMP(employmentInfo.job_information.company.text())
                    //CODTRABA nombre???? crear 2 campos misma info
                    //no nesta en el excel CODTRABA_INTERNO -> user_id de employmentinformation
                    //campo si es principal o no lectura mapa
                    CODTRABA(parsedXml.CompoundEmployee.person.person_id_external.text())
                    GRUPOTC(employmentInfo.job_information.custom_string4.text())
                    TRIBUTA_REG_GEN(employmentInfo.job_information.custom_string8.text())
                    TRIBUTA_216(employmentInfo.job_information.custom_string9.text())
                    APELLIDOS(parsedXml.CompoundEmployee.person.personal_information.last_name.text())
                    NOMBRE(parsedXml.CompoundEmployee.person.personal_information.first_name.text())

                    // Agregar más propiedades aquí según sea necesario
                }
            }
        }
    
    // Imprimir el XML resultante
    //writer.toString()
    //def nodeAsText = XmlUtil.serialize(parent)
    message.setBody(writer.toString());
    return message;
}
