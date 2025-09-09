package br.unipar.devbackend.agendaenderecos.model;

import jakarta.persistence.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorOrder;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.StringReader;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@XmlRootElement(name = "xmlcep")
@XmlAccessorType(XmlAccessType.FIELD)
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;

    //campo para data e hora
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRegistro;

    @ManyToOne
    private Cliente clientes;

    public static Endereco unmarshalFromString(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Endereco.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return (Endereco) unmarshaller.unmarshal(reader);
    }
}