package pro.sky.animalshelterapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelterapp.model.Client;
@Repository
public interface ClientUserRepository extends JpaRepository <Client, Long> {
}
