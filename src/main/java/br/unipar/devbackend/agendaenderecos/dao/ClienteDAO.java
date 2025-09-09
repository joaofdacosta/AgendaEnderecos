package br.unipar.devbackend.agendaenderecos.dao;

import br.unipar.devbackend.agendaenderecos.model.Cliente;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClienteDAO {

    EntityManager em;

    public ClienteDAO(EntityManager em) {
        this.em = em;
    }

    public Cliente gravar(Cliente cliente) {
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            System.out.println("Cliente gravado com sucesso.");
            return cliente;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Algo de errado não deu certo ao gravar " + ex.getMessage());
            return null;
        } finally {
            if (em.isOpen()) {
                em.close();
                System.out.println("EntityManager fechado.");
            }
        }
    }

    public Cliente editar(Cliente cliente) {
        try {
            em.getTransaction().begin();
            em.merge(cliente);
            em.getTransaction().commit();
            System.out.println("Cliente editado com sucesso.");
            return cliente;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Algo de errado não deu certo ao editar: " + ex.getMessage());
            return null;

        } finally {
            if (em.isOpen()) {
                em.close();
                System.out.println("EntityManager fechado.");
            }

        }

    }

    public Cliente findById(Long id) {
        return em.find(Cliente.class, id);
    }

    public List<Cliente> findAll() {
        return em.createQuery("SELECT t FROM Cliente t ", Cliente.class)
                .getResultList();
    }

    public Boolean delete(Cliente cliente) {
        try {
            em.getTransaction().begin();
            em.remove(cliente);
            em.getTransaction().commit();
            System.out.println("Cliente removido com sucesso!");
            return true;
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Algo de errado não deu certo ao remover " + ex.getMessage());
            return false;

        } finally {
            if (em.isOpen()) {
                em.close();
                System.out.println("EntityManager fechado.");
            }
        }
    }
}