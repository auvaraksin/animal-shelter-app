package pro.sky.animalshelterapp.Service.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.Service.UserService;
import pro.sky.animalshelterapp.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {
    //  Класс  UserServiceImpl будет реализовать интерфейс UserService

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }
}