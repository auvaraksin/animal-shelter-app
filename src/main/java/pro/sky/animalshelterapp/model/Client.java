package pro.sky.animalshelterapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Client {
    @Id
    @GeneratedValue
    private Long id;
    private Long chatId;
    private String first_name;
    private String name;

    private String contacts;
    private String animal_type;
    private int successReportNumber;
    private boolean clientStatus;
    private LocalDate dateOfStart;
    private LocalDate dateOfEnd;

    public Client() {}

    public Client(Long chatId, String first_name, String name,String contacts) {
        this.chatId = chatId;
        this.first_name = first_name;
        this.name = name;
        this.contacts = contacts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getAnimal_type() {
        return animal_type;
    }

    public void setAnimal_type(String animal_type) {
        this.animal_type = animal_type;
    }

    public int getSuccessReportNumber() {
        return successReportNumber;
    }

    public void setSuccessReportNumber(int successReportNumber) {
        this.successReportNumber = successReportNumber;
    }

    public boolean isStatus() {
        return clientStatus;
    }

    public void setStatus(boolean clientStatus) {
        this.clientStatus = clientStatus;
    }

    public LocalDate getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(LocalDate dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public LocalDate getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(LocalDate dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return successReportNumber == client.successReportNumber && clientStatus == client.clientStatus && Objects.equals(id, client.id) && Objects.equals(chatId, client.chatId) && Objects.equals(first_name, client.first_name) && Objects.equals(contacts, client.contacts)
                && Objects.equals(animal_type, client.animal_type) && Objects.equals(name,client.name) && Objects.equals(dateOfStart, client.dateOfStart) && Objects.equals(dateOfEnd, client.dateOfEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, first_name, contacts, animal_type, name, successReportNumber, clientStatus, dateOfStart, dateOfEnd);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", first_name='" + first_name + '\'' +
                ", contacts='" + contacts + '\'' +
                ", animal_type='" + animal_type + '\'' +
                ", successReportNumber=" + successReportNumber +
                ", status=" + clientStatus +
                ", dateOfStart=" + dateOfStart +
                ", dateOfEnd=" + dateOfEnd +
                '}';
    }
}

