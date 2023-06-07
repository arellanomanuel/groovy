import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil
import groovy.util.XmlParser
import java.time.LocalDate
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
    def body = message.getBody(java.lang.String)
    def response = new XmlParser().parseText(body)
    def parsedXml = new XmlSlurper().parseText(body)

    // Crear un nuevo XML
    def writer = new StringWriter()
    def xmlBuilder = new MarkupBuilder(writer)

    // Construir el nuevo XML
    xmlBuilder.EMPLEADOS {
            parsedXml.CompoundEmployee.each { emp ->
                 emp.person.employment_information.each { empInf ->
                 EMPLEADO{
                    CODEMP(empInf.job_information.company.text())
                    //CODTRABA nombre???? crear 2 campos misma info
                    //no nesta en el excel CODTRABA_INTERNO -> user_id de employmentinformation
                    //campo si es principal o no lectura mapa
                    CODTRABA(emp.person.person_id_external.text())
                    N_MATRICULA(emp.person.person_id_external.text())
                    GRUPOTC(empInf.job_information.custom_string4.text())
                    TRIBUTA_REG_GEN(empInf.job_information.custom_string8.text())
                    TRIBUTA_REG_FORAL(empInf.job_information.custom_string8.text())
                    REGIMEN_FORAL(empInf.job_information.custom_string8.text())
                    TRIBUTA_216(empInf.job_information.custom_string9.text())
                    APELLIDOS(emp.person.personal_information.last_name.text())
                    NOMBRE(emp.person.personal_information.first_name.text())
                    //TO DO: solo del que tiene el tipo home
                    DIRECCION(emp.person.address_information.address1.text())
                    MUNICIPIO(emp.person.address_information.city.text())
                    PROVINCIA(emp.person.address_information.state.text())
                    //TO DO: pasar despues tlfs
                    CODIGO_POSTAL(emp.person.address_information.zip_code.text())                    
                    //TO DO: email solo business
                    
                    emp.person.phone_information.each { phoneInfo ->
                        if (phoneInfo.phone_type.text() == "C") {
                            TELEFONOC(phoneInfo.phone_number.text())
                        }
                        if (phoneInfo.phone_type.text() == "H") {
                            TELEFONOH(phoneInfo.phone_number.text())
                        }
                        if (phoneInfo.phone_type.text() == "P") {
                            TELEFONOP(phoneInfo.phone_number.text())
                        }
                        if (phoneInfo.phone_type.text() == "B") {
                            //xmlBuilder.TELEFONOB(phoneInfo.phone_number.text())
                            TELEFONOB(phoneInfo.phone_number.text())
                        }
                    }
                    //TO DO: sin ejemplos de empleados con national_id_card
                    //TO DO: if N_AFILIACION == NAF ????
                    N_AFILIACION(emp.person.national_id_card.card_type.text())
                    DOCUMENTO_IDENTIFI(emp.person.national_id_card.national_id.text())
                    //TO DO: if N_AFILIACION == COD_DOCUMENTO_IDENTIF ????
                    COD_DOCUMENTO_IDENTIF(emp.person.national_id_card.card_type.text())

                    FECHA_NACIMIENTO(emp.person.date_of_birth.text())
                    CECO(empInf.job_information.department.text())
                    //TO DO: O2cecos mismo nombre
                    CECO(empInf.job_information.custom_string101.text())
                    SEXO(emp.person.personal_information.gender.text())
                    CODIGO_CATEGORIA(empInf.job_information.job_code.text())
                    PUESTO(empInf.job_information.job_title.text())
                    //TO DO: sin ejemplos de empleados con iban
                    IBAN(empInf.PaymentInformationV3.PaymentInformationDetailV3.iban.text())
                    CUENTA_BANCARIA(empInf.PaymentInformationV3.PaymentInformationDetailV3.accountNumber.text())

                    TIPO_CONTRATO(empInf.job_information.custom_string10.text())
                    JORNADA_DIARIA(empInf.job_information.custom_double1.text())
                    FECHA_ALTA(empInf.start_date.text())

                    // Agregar más propiedades aquí según sea necesario
                
            }
                 
            }
        }
    }
    // XML resultante
    message.setBody(writer.toString());
    return message;
}
