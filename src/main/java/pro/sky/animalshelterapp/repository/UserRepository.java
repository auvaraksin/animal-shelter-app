package pro.sky.animalshelterapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelterapp.model.User;
//Репозиторий для User
@Repository
public interface UserRepository extends JpaRepository <User,Long> {
}
