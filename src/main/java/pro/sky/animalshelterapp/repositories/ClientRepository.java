package pro.sky.animalshelterapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelterapp.models.Client;

import java.util.Collection;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByName(String name);
    Collection<Client> findByClientStatus(Boolean status);
}
