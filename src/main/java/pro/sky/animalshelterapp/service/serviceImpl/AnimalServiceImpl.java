package pro.sky.animalshelterapp.service.serviceImpl;

import org.springframework.stereotype.Service;
import pro.sky.animalshelterapp.model.Animal;
import pro.sky.animalshelterapp.repository.AnimalRepository;
import pro.sky.animalshelterapp.service.AnimalService;


@Service
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;


    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Override
    public Animal getAnimalByName(Animal.AnimalTypes type) {

        return animalRepository.getAnimalBy(type);
    }
}
