package pro.sky.animalshelterapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelterapp.models.MessageSource;
@Repository
public interface MessageSourceRepository extends JpaRepository<MessageSource,String> {
}
