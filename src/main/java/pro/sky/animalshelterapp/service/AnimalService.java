package pro.sky.animalshelterapp.service;

import pro.sky.animalshelterapp.model.Animal;

public interface AnimalService {


    Animal getAnimalByName(Animal.AnimalTypes type);

}
