package pro.sky.animalshelterapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="animal")
public class Animal implements Serializable {

    public enum AnimalTypes {

        DOG,
        CAT,
        NO_ANIMAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AnimalTypes type;

    @OneToMany(mappedBy = "animal",cascade = CascadeType.ALL)
    @JsonIgnore
    public Set<ChatUser> chatUsers;

    public Animal() {
    }

    public Animal(long id, AnimalTypes type) {
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AnimalTypes getType() {
        return type;
    }

    public Set<ChatUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(Set<ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal)) return false;
        Animal animal = (Animal) o;
        return id.equals(animal.id) && type == animal.type && Objects.equals(chatUsers, animal.chatUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, chatUsers);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", type=" + type +
                ", chatUsers=" + chatUsers +
                '}';
    }
}

