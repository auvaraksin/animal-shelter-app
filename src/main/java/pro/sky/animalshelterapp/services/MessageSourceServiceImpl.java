package pro.sky.animalshelterapp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.exeptions.EmptyMessageValueExeption;
import pro.sky.animalshelterapp.interfaces.MessageSourceService;
import pro.sky.animalshelterapp.repositories.MessageSourceRepository;

@Service
public class MessageSourceServiceImpl implements MessageSourceService {
    private final MessageSourceRepository messageSourceRepository;
    private static final Logger logger = LoggerFactory.getLogger(MessageSourceServiceImpl.class);

    MessageSourceServiceImpl(MessageSourceRepository messageSourceRepository) {
        this.messageSourceRepository = messageSourceRepository;
    }

    public String findById(String digest) {
        logger.info("Method to read the record in the DB in table 'MessageSource' was invoked ");
        return messageSourceRepository.findById(digest).orElseThrow(EmptyMessageValueExeption::new).getResponseMessage();
    }
}
