package br.unipar.devbackend.agendaenderecos.service;

import br.unipar.devbackend.agendaenderecos.model.Endereco;
import jakarta.xml.bind.JAXBException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class ViaCepService {

    public Endereco buscarCep(String cep) throws IOException, JAXBException {
       String url = "https://viacep.com.br/ws/" + cep + "/xml/";

       URL urlViaCep = new URL(url);
       HttpURLConnection connection = (HttpURLConnection) urlViaCep.openConnection();
       connection.setRequestMethod("GET");

        BufferedReader leitor = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String linha;
        StringBuilder resposta = new StringBuilder(); //resposta completa (construida)

        while ((linha = leitor.readLine()) != null) {
            resposta.append(linha);
        }
        leitor.close();
        return Endereco.unmarshalFromString(resposta.toString());
    }
}
