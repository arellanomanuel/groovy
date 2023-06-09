import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil
import groovy.util.XmlParser
import java.time.LocalDate
import groovy.xml.MarkupBuilder
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
                    CODTRABA(emp.person.person_id_external.text())
                    CODTRABA_INTERNO(empInf.user_id.text())
                    N_MATRICULA(emp.person.person_id_external.text())
                   empInf.job_information.each { jobInfo ->
                        if (jobInfo.end_date.text()=="9999-12-31") {                    
                            CODEMP(jobInfo.company.text())
                            GRUPOTC(jobInfo.custom_string4.text())
                            TRIBUTA_REG_GEN(jobInfo.custom_string8.text())
                            TRIBUTA_REG_FORAL(jobInfo.custom_string8.text())
                            REGIMEN_FORAL(jobInfo.custom_string8.text())
                            TRIBUTA_216(jobInfo.custom_string9.text())
                            CECO(jobInfo.department.text())
                            CECO(jobInfo.custom_string101.text())
                            
                            CODIGO_CATEGORIA(jobInfo.job_code.text())
                            PUESTO(jobInfo.job_title.text())
                           

                            TIPO_CONTRATO(jobInfo.custom_string10.text())
                            JORNADA_DIARIA(jobInfo.custom_double1.text())
                            TIPO_TRABAJADOR(jobInfo.employee_class.text())
                            GRUPO_COTIZACION(jobInfo.custom_string3.text())
                            SITU_ESP_COTIZACION(jobInfo.custom_string6.text())
                            COD_CENTRO(jobInfo.custom_string6.text())
                            CAUSA(jobInfo.event_reason.text())
                           
                            
                            NOMBRE_DEPARTAMENTO(jobInfo.department.text())
                            CONVENIO_VACACIONES(jobInfo.custom_string12.text())
                            FECHA_FIN_CONTRATO(jobInfo.contract_end_date.text())
                        }
                    }


                    emp.person.personal_information.each { perInfo ->
                        if (perInfo.end_date.text()=="9999-12-31") {
                            APELLIDOS(perInfo.last_name.text())
                            NOMBRE(perInfo.first_name.text())
                            SEXO(perInfo.gender.text())
                            NACIONALIDAD(perInfo.nationality.text())
                            GRADO_DISCAPACIDAD(perInfo.personal_information_esp.custom_string1.text())
                            AYUDA_DESPLAZAMIENTO(perInfo.personal_information_esp.custom_string2.text())
                            SITUACION_FISCAL(perInfo.marital_status.text())
                            NIF_CONYUGE(perInfo.personal_information_esp.genericString1.text())
                        }
                    }
                    emp.person.address_information.each { addInfo ->
                        if (addInfo.address_type.text() == "home" && addInfo.end_date.text()=="9999-12-31") {
                            DIRECCION(addInfo.address1.text())
                            MUNICIPIO(addInfo.city.text())
                            PROVINCIA(addInfo.state.text())
                            CODIGO_POSTAL(addInfo.zip_code.text())                    
                        }
                    }
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
                    emp.person.email_information.each { emailInfo ->
                    if (emailInfo.email_type.text() == "B") {
                            DIRECCION_EMAIL(emailInfo.email_address.text())
                        }
                    }
                    emp.person.national_id_card.each { idCard ->
                        if (idCard.card_type.text() == "NAF") {
                            COD_DOCUMENTO_IDENTIF(idCard.card_type.text())
                            N_AFILIACION(idCard.national_id.text())
                        }
                        if (idCard.card_type.text() == "DNI") {
                            COD_DOCUMENTO_IDENTIF(idCard.card_type.text())
                            DOCUMENTO_IDENTIFI(idCard.national_id.text())
                        }
                        
                    }

                    FECHA_NACIMIENTO(emp.person.date_of_birth.text())
                    //TO DO: sin ejemplos de empleados con iban me falta PaymentInformationV3 FILTRAR CUSTOMPAYTYPE ==MAIN
                    empInf.PaymentInformationV3.each { payInfo ->
                        if (payInfo.effectiveEndDate.text() == "9999-12-31") {
                            payInfo.PaymentInformationDetailV3.each { payDeInfo ->
                            if (payDeInfo.customPayType.text() == "MAIN") {
                                IBAN(payDeInfo.iban.text())
                                CUENTA_BANCARIA(payDeInfo.accountNumber.text())
                                }
                            }
                        }
                    }
                    FECHA_ALTA(empInf.start_date.text())
                    FECHA_ANTIGUEDAD(empInf.seniorityDate.text())
                    FECHA_BAJA(empInf.end_date.text())
                   
                    emp.person.email_information.each { emailInfo ->
                        if (emailInfo.email_taype.text() == "B") {
                        DIRECCION_EMAIL(emailInfo.email_address.text())
                        }
                    }
                    
                 
                    //TO DO: HIJOS Y ASCENDENTES
                                        
                    HIJOS{
                    emp.person.dependent_information.each { depInfo ->
                        if (depInfo.dependent_relation_information.custom_string1.text()=="D") {
                        HIJO{
                            F_NACI_HIJO(depInfo.date_of_birth.text())
                            CARGO_HIJO(depInfo.dependent_relation_information.custom_string1.text())
                            GRADO_HIJO(depInfo.dependent_relation_information.dependent_personal_information_esp.custom_string1.text())
                            AYUDA_HIJO(depInfo.dependent_relation_information.dependent_personal_information_esp.custom_string2.text())
                            ADOPCION_HIJO(depInfo.dependent_relation_information.dependent_personal_information_esp.genericDate2.text())

                                    }
                                }
                            }
                        }
                    

                    ASCENDENTES{
                    emp.person.dependent_information.each { depInfo ->
                    if (depInfo.dependent_relation_information.custom_string1.text()=="A") {
                        ASCENDENTE{
                            F_NACI_ASCEN(depInfo.date_of_birth.text())
                            CARGO_HIJO(depInfo.dependent_relation_information.custom_string1.text())
                            GRADO_ASCEN(depInfo.dependent_relation_information.dependent_personal_information_esp.custom_string1.text())
                            AYUDA_ASCEN(depInfo.dependent_relation_information.dependent_personal_information_esp.custom_string2.text())

                                }
                            }   
                        }
                    }
                    def lis = emp.'**'.findAll { it.name() == 'last_modified_on' }
                    def fechaMasReciente = encontrarFechaMasReciente(lis)
                    //println "${fechaMasReciente}"
                    FECHA_CAMBIO(fechaMasReciente)
                    empInf.compensation_information.each { comInfo ->
                        if (comInfo.end_date.text()=="9999-12-31") {
                            VARIABLE_ANIO_ANT(comInfo.custom_string46.text())
                            VARIABLE_PREVISTA(comInfo.custom_string1.text())
                            comInfo.paycompensation_recurring.each { pComInfo ->
                                if (pComInfo.end_date.text()=="9999-12-31" && pComInfo.pay_component.text()=="0YA") {
                                    SALARIO_BRUTO_ANUAL(pComInfo.paycompvalue.text())
                                }
                                if (pComInfo.end_date.text()=="9999-12-31" && pComInfo.pay_component.text()=="4") {    
                                    SEGURO_MEDICO(pComInfo.paycompvalue.text())
                                }
                                if (pComInfo.end_date.text()=="9999-12-31" && pComInfo.pay_component.text()=="011") {
                                    TELETRABAJO(pComInfo.paycompvalue.text())
                                }
                                if (pComInfo.end_date.text()=="9999-12-31" && pComInfo.pay_component.text()=="PorcVariable") {
                                    PORCEN_VARIABLE(pComInfo.paycompvalue.text())}
                                if (pComInfo.end_date.text()=="9999-12-31" && pComInfo.pay_component.text()=="Variable") {
                                    VARIABLE(pComInfo.paycompvalue.text())}
                            }
                            
                        }
                    }
                    // Agregar más propiedades aquí según sea necesario
                
            }
                 
            }
        }
    }
    // XML resultante
    message.setBody(writer.toString());
    return message;
}
def String encontrarFechaMasReciente(List<String> fechas) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    LocalDateTime fechaMasReciente = null
    
    for (String fechaStr : fechas) {
        LocalDateTime fecha = LocalDateTime.parse(fechaStr, formatter)
        
        if (fechaMasReciente == null || fecha.isAfter(fechaMasReciente)) {
            fechaMasReciente = fecha
        }
    }
    
    if (fechaMasReciente != null) {
        return fechaMasReciente.format(formatter)
    } else {
        return null
    }
}
