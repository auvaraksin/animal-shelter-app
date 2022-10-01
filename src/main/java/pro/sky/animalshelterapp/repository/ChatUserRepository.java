package pro.sky.animalshelterapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelterapp.model.ChatUser;
//Репозиторий для User
@Repository
public interface ChatUserRepository extends JpaRepository <ChatUser,Long> {
}
