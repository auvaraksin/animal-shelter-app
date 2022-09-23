package pro.sky.animalshelterapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelterapp.model.MessageSource;
@Repository
public interface MessageSourseRepository extends JpaRepository<MessageSource,String> {
}
