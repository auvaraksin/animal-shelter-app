package pro.sky.animalshelterapp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.interfaces.ClientService;
import pro.sky.animalshelterapp.models.Client;
import pro.sky.animalshelterapp.repositories.ClientRepository;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void createClient(Client client) {
        logger.info("Method to create a new record in the DB in table 'Client' was invoked");
        clientRepository.save(client);
    }

    public Client updateClientStatus(String name, Boolean status, LocalDate dateOfStart, LocalDate dateOfEnd) {
        logger.info("Method to update the record in the DB in table 'Client' was invoked. Set client's status and trial period");
        Client client = clientRepository.findByName(name);
        client.setStatus(status);
        client.setDateOfStart(dateOfStart);
        client.setDateOfEnd(dateOfEnd);
        clientRepository.save(client);
        return client;
    }

    public Client updateClientStatus(String name, Boolean status) {
        logger.info("Method to update the record in the DB in table 'Client' was invoked. Set client's status");
        Client client = clientRepository.findByName(name);
        client.setStatus(status);
        clientRepository.save(client);
        return client;
    }

    public Client setAnimalType(String name, String animalType) {
        logger.info("Method to update the record in the DB in table 'Client' was invoked. Set animal type");
        Client client = clientRepository.findByName(name);
        if (animalType.equals("cat")||animalType.equals("dog")||animalType.equals("кошка")||animalType.equals("собака")){
        client.setAnimal_type(animalType);
        clientRepository.save(client);
        return client;}
        else throw new RuntimeException();
    }

    public Collection<Client> findAll() {
        logger.info("Method to show all records in the DB in table 'Client' was invoked");
        return clientRepository.findAll();
    }

    public Client findByName(String name) {
        logger.info("Method to find the record in the DB in table 'Client' was invoked");
        return clientRepository.findByName(name);
    }

    public Long findChatIdByName(String name) {
        logger.info("Method to find the data in the DB in table 'Client' was invoked");
        return clientRepository.findByName(name).getChatId();
    }

    public Collection<Client> findByStatus(Boolean status) {
        logger.info("Method to find records in the DB in table 'Client' was invoked");
        return clientRepository.findByClientStatus(status);
    }
}
