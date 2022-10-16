package pro.sky.animalshelterapp.interfaces;

import pro.sky.animalshelterapp.models.Client;

import java.time.LocalDate;
import java.util.Collection;

public interface ClientService {
    void createClient(Client client);

    Client updateClientStatus(String name, Boolean status, LocalDate dateOfStart, LocalDate dateOfEnd);

    Client updateClientStatus(String name, Boolean status);

    Client setAnimalType(String name, String animalType) throws RuntimeException;

    Collection<Client> findAll();

    Client findByName(String name);

    Long findChatIdByName(String name);

    Collection<Client> findByStatus(Boolean status);

}
