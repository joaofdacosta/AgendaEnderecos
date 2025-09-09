package br.unipar.devbackend.agendaenderecos;

import br.unipar.devbackend.agendaenderecos.dao.ClienteDAO;
import br.unipar.devbackend.agendaenderecos.dao.EnderecoDAO;
import br.unipar.devbackend.agendaenderecos.model.Cliente;
import br.unipar.devbackend.agendaenderecos.model.Endereco;
import br.unipar.devbackend.agendaenderecos.service.ViaCepService;
import br.unipar.devbackend.agendaenderecos.utils.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, JAXBException {
        EntityManagerUtil.getEmf();
        Scanner scanner = new Scanner(System.in);

        //Pede o CEP do usuário
        System.out.println("Por favor, digite o CEP:");
        String cepParaBuscar = scanner.nextLine();

        EntityManager em = EntityManagerUtil.getEm();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Endereco endereco;

            //Consulta o CEP no banco de dados
            EnderecoDAO enderecoDAO = new EnderecoDAO(em);
            List<Endereco> enderecosExistentes = enderecoDAO.findByCep(cepParaBuscar);

            if (!enderecosExistentes.isEmpty()) {
                // Se o CEP existe, use o endereço encontrado
                endereco = enderecosExistentes.get(0);
                System.out.println("Endereço com o CEP " + cepParaBuscar + " encontrado no banco de dados.");
            } else {
                // Se o CEP não existe, busque na API do ViaCEP
                System.out.println("CEP não encontrado no banco. Buscando na API do ViaCEP...");
                ViaCepService viaCepService = new ViaCepService();
                endereco = viaCepService.buscarCep(cepParaBuscar);

                // Exibe o endereço encontrado antes de salvar
                System.out.println("Endereço encontrado na API: " + endereco.getLogradouro() + ", " + endereco.getBairro() + " - " + endereco.getLocalidade() + "/" + endereco.getUf());

                // Grava o novo endereço no banco.
                em.persist(endereco);
                System.out.println("✅ Novo endereço preparado para gravação no banco de dados.");
            }

            // Solicita e associa um novo cliente
            System.out.println("\n--- Cadastro do Novo Cliente ---");
            System.out.println("Nome do cliente:");
            String nomeCliente = scanner.nextLine();

            System.out.println("Email do cliente:");
            String emailCliente = scanner.nextLine();

            Cliente cliente = new Cliente();
            cliente.setNome(nomeCliente);
            cliente.setEmail(emailCliente);
            cliente.setDataNascimento(new Date()); // Exemplo de data

            // Adiciona a entidade Endereco na lista de endereços do cliente
            cliente.getEnderecos().add(endereco);

            // Define o cliente para o objeto Endereco
            endereco.setClientes(cliente);

            // Grava o novo cliente no banco
            em.persist(cliente);

            transaction.commit();
            System.out.println("\n Cliente '" + cliente.getNome() + "' associado com sucesso ao endereço do CEP " + endereco.getCep() + ".");

        } catch (Exception ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println(" Ocorreu um erro na operação: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
            EntityManagerUtil.closeEmf();
            scanner.close();
        }
    }
}