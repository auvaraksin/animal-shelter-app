package pro.sky.animalshelterapp.Service.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.Service.UserService;
import pro.sky.animalshelterapp.repository.ChatUserRepository;


@Service
public class UserServiceImpl implements UserService {
    //  Класс  UserServiceImpl будет реализовать интерфейс UserService

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final ChatUserRepository repository;

    public UserServiceImpl(ChatUserRepository repository) {
        this.repository = repository;
    }
}