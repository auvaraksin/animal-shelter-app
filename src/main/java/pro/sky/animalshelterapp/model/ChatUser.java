package pro.sky.animalshelterapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

//Модель таблицы, куда будут попадать все первоначально общающиеся с ботом
@Entity
public class ChatUser {
    @Id
    @GeneratedValue
    private Long id;
    private Long chatId;
    private String first_name;
    /*На первом этапе бот тоже может записать данные пользователя
    * Поэтому тут тоже можно запросить телефон-емайл*/
    private String phoneNumber;
    private String email;

    public ChatUser() {
    }

    public ChatUser(long chatId, String first_name) {
        this.chatId = chatId;
        this.first_name = first_name;
    }
    public Long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public String getFirst_name(){return first_name;}

    public String getPhoneNumber() {return phoneNumber;}

    public String getEmail() {return email;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUser user1 = (ChatUser) o;
        return chatId == user1.chatId && id.equals(user1.id)&&first_name.equals(user1.first_name)&&phoneNumber.equals(user1.phoneNumber)&&email.equals(user1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, first_name, phoneNumber, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", first_name="+first_name+
                ", phoneNumber="+phoneNumber+
                ", email = "+email+'}';
    }
}

