package pro.sky.animalshelterapp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.animalshelterapp.controllers.ClientController;
import pro.sky.animalshelterapp.interfaces.ClientService;
import pro.sky.animalshelterapp.models.Client;
import pro.sky.animalshelterapp.repositories.ClientRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static pro.sky.animalshelterapp.TestConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalShelterAppApplicationTests {

    @LocalServerPort
    private int port;

    @InjectMocks
    private ClientController clientController;

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    void setup() {
        DEFAULTURL = "http://localhost:" + port + "/clients";
        Client client1 = new Client(1234567L, "John", "John Smith", "+71234567890");
        client1.setStatus(true);
        Client client2 = new Client(7654321L, "Anna", "Anna Gordon", "+70987654321");
        client2.setStatus(true);
        List<Client> clients = Arrays.asList(client1, client2);
        clientService.createClient(client1);
        when(clientRepository.findAll()).thenReturn(clients);
        when(clientRepository.findByName("John")).thenReturn(client1);
        when(clientRepository.findByClientStatus(true)).thenReturn(clients);
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertNotNull(clientController);
    }

    @Test
    public void testGetClientsList() throws Exception {
        Assertions.assertNotNull(this.restTemplate.getForObject(DEFAULTURL + DEFAULTURLFINDALLRECORDS, String.class));
        String expectedResponse = "[{\"id\":null,\"chatId\":1234567,\"first_name\":\"John\",\"name\":\"John Smith\",\"contacts\":\"+71234567890\",\"animal_type\":null,\"dateOfStart\":null,\"dateOfEnd\":null,\"status\":true}," +
                "{\"id\":null,\"chatId\":7654321,\"first_name\":\"Anna\",\"name\":\"Anna Gordon\",\"contacts\":\"+70987654321\",\"animal_type\":null,\"dateOfStart\":null,\"dateOfEnd\":null,\"status\":true}]";
        ResponseEntity<String> responseOk
                = this.restTemplate.getForEntity(DEFAULTURL + DEFAULTURLFINDALLRECORDS, String.class);
        Assertions.assertEquals(HttpStatus.OK, responseOk.getStatusCode());
        Assertions.assertEquals(expectedResponse, responseOk.getBody().toString());
        ResponseEntity<String> responseNotFound
                = this.restTemplate.getForEntity(DEFAULTURL + DEFAULTURLFINDALLRECORDS + "wrong", String.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseNotFound.getStatusCode());
    }

    @Test
    public void testFindClientDataByName() throws Exception {
        Assertions.assertNotNull(this.restTemplate.getForObject(DEFAULTURL + DEFAULTURLFINDBYNAMEJOHN, String.class));
        String expectedResponse = "{\"id\":null,\"chatId\":1234567,\"first_name\":\"John\",\"name\":\"John Smith\",\"contacts\":\"+71234567890\",\"animal_type\":null,\"dateOfStart\":null,\"dateOfEnd\":null,\"status\":true}";
        ResponseEntity<String> responseOk
                = this.restTemplate.getForEntity(DEFAULTURL + DEFAULTURLFINDBYNAMEJOHN, String.class);
        Assertions.assertEquals(HttpStatus.OK, responseOk.getStatusCode());
        Assertions.assertEquals(expectedResponse, responseOk.getBody().toString());
    }

    @Test
    public void testFindChatIdByClientName() throws Exception {
        Assertions.assertNotNull(this.restTemplate.getForObject(DEFAULTURL + DEFAULTURLFINDCHATIDBYNAMEJOHN, String.class));
        String expectedResponse = String.valueOf(DEFAULTCHATID);
        ResponseEntity<String> responseOk
                = this.restTemplate.getForEntity(DEFAULTURL + DEFAULTURLFINDCHATIDBYNAMEJOHN, String.class);
        Assertions.assertEquals(HttpStatus.OK, responseOk.getStatusCode());
        Assertions.assertEquals(expectedResponse, responseOk.getBody().toString());
        ResponseEntity<String> responseInternalServerError
                = this.restTemplate.getForEntity(DEFAULTURL + DEFAULTURLFINDCHATIDBYNAMEJOHN + "wrong", String.class);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseInternalServerError.getStatusCode());

    }

    @Test
    public void testFindClientsByStatus() throws Exception {
        Assertions.assertNotNull(this.restTemplate.getForObject(DEFAULTURL + DEFAULTURLFINDBYSTATUSTRUE, String.class));
        String expectedResponse = "[{\"id\":null,\"chatId\":1234567,\"first_name\":\"John\",\"name\":\"John Smith\",\"contacts\":\"+71234567890\",\"animal_type\":null,\"dateOfStart\":null,\"dateOfEnd\":null,\"status\":true}," +
                "{\"id\":null,\"chatId\":7654321,\"first_name\":\"Anna\",\"name\":\"Anna Gordon\",\"contacts\":\"+70987654321\",\"animal_type\":null,\"dateOfStart\":null,\"dateOfEnd\":null,\"status\":true}]";
        ResponseEntity<String> responseOk
                = this.restTemplate.getForEntity(DEFAULTURL + DEFAULTURLFINDBYSTATUSTRUE, String.class);
        Assertions.assertEquals(HttpStatus.OK, responseOk.getStatusCode());
        Assertions.assertEquals(expectedResponse, responseOk.getBody().toString());
    }

}
