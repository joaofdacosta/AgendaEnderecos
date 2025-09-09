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

import java.io.StringReader;

@Getter
@Setter
@Entity
@ToString
@XmlRootElement(name = "xmlcep")
@XmlAccessorType(XmlAccessType.FIELD)

public class Endereco {

    @Id //anotacao para chave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //anotacao
    private Long id;  //chave primaria
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;

    @ManyToOne
    private Cliente clientes;

    public static Endereco unmarshalFromString(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Endereco.class); //cria o contexto
        Unmarshaller unmarshaller = context.createUnmarshaller(); //objeto que faz a conversão
        StringReader reader = new StringReader(xml); //lê a string
        return (Endereco) unmarshaller.unmarshal(reader); //converte a string em objeto e retorna
    }

}