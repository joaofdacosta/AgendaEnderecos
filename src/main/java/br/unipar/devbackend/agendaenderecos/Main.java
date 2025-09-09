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

public class Main {
    public static void main(String[] args) throws IOException, JAXBException {
        EntityManagerUtil.getEmf();

        String cepParaBuscar = "85805020";
        Endereco endereco;

        // Crie uma nova instância de EntityManager para a busca
        EntityManager emBusca = EntityManagerUtil.getEm();
        EnderecoDAO enderecoDAOBusca = new EnderecoDAO(emBusca);
        List<Endereco> enderecosExistentes = enderecoDAOBusca.findByCep(cepParaBuscar);

        // Feche o EntityManager da busca, pois não será mais usado
        if (emBusca.isOpen()) {
            emBusca.close();
        }

        EntityManager em = EntityManagerUtil.getEm();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (!enderecosExistentes.isEmpty()) {
                // Se o CEP já existe, anexe a entidade à nova transação
                endereco = em.merge(enderecosExistentes.get(0));
                System.out.println("CEP " + cepParaBuscar + " encontrado no banco de dados. Cliente será associado a este endereço.");
            } else {
                ViaCepService viaCepService = new ViaCepService();
                endereco = viaCepService.buscarCep(cepParaBuscar);
                em.persist(endereco);
                System.out.println("CEP " + cepParaBuscar + " buscado na API e preparado para gravação.");
            }

            Cliente cliente = new Cliente();
            cliente.setNome("Quarto Cliente"); // Altere o nome para testar
            cliente.setEmail("quarto.cliente@email.com");
            cliente.setDataNascimento(new Date());

            cliente.getEnderecos().add(endereco);
            endereco.setClientes(cliente);

            em.persist(cliente);

            transaction.commit();
            System.out.println("Cliente " + cliente.getNome() + " associado com sucesso ao endereço do CEP " + endereco.getCep());

        } catch (Exception ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Ocorreu um erro na operação: " + ex.getMessage());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
            EntityManagerUtil.closeEmf();
        }
    }
}