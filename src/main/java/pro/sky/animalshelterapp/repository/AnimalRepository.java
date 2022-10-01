package pro.sky.animalshelterapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.animalshelterapp.model.Animal;


public interface AnimalRepository extends JpaRepository<Animal, Animal.AnimalTypes> {

    @Query("SELECT a FROM Animal a WHERE a.type = ?1")
    Animal getAnimalBy(Animal.AnimalTypes type);

}
