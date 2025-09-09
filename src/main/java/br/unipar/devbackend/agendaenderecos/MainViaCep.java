package br.unipar.devbackend.agendaenderecos;
import br.unipar.devbackend.agendaenderecos.model.Endereco;
import br.unipar.devbackend.agendaenderecos.service.ViaCepService;
import jakarta.xml.bind.JAXBException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;



public class MainViaCep {
    public static void main(String[] args) throws IOException, JAXBException {
        String cep = "85811030";
        String url = "https://viacep.com.br/ws/" + cep + "/xml/";

        ViaCepService viaCep = new ViaCepService();
        Endereco endereco = viaCep.buscarCep(cep);

        System.out.println("CEP: " + endereco.getCep() + ", Logradouro: "  + endereco.getLogradouro() +
                ", Bairro: " + endereco.getBairro() + ", Localidade: " + endereco.getLocalidade() +
                "/ " + endereco.getUf());


        //Buscar um cep qualquer
        // verificar se esse cep existe no banco de dados
            // se existir, pede pra adicionar um cliente para ele
            // se não exisitir, grava o novo cep com data e hora da gravação.


    }
}
