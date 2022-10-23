package pro.sky.animalshelterapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelterapp.interfaces.ClientService;

import java.time.LocalDate;

@RestController
@RequestMapping("clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/find-all-records")
    public ResponseEntity getClientsList() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/find-client-data-by-name")
    public ResponseEntity getClientByName(@RequestParam String name) {
        return ResponseEntity.ok(clientService.findByName(name));
    }

    @GetMapping("/find-chatId-by-client-name")
    public ResponseEntity getChatIdByName(@RequestParam String name) {
        return ResponseEntity.ok(clientService.findChatIdByName(name));
    }

    @GetMapping("/find-clients-by-status")
    public ResponseEntity getClientsListByStatus(@RequestParam Boolean status) {
        return ResponseEntity.ok(clientService.findByStatus(status));
    }

    @PutMapping("/update-client-status")
    public ResponseEntity updateClientStatus(@RequestParam String name,
                                             @RequestParam Boolean status,
                                             @RequestParam(required = false) String dateOfStart,
                                             @RequestParam(required = false) String dateOfEnd) {
        if (dateOfStart != null && dateOfEnd != null) {
            return ResponseEntity.ok(clientService.updateClientStatus(name, status, LocalDate.parse(dateOfStart), LocalDate.parse(dateOfEnd)));
        }
        return ResponseEntity.ok(clientService.updateClientStatus(name, status));
    }

    @PutMapping("set-animal-type")
    public ResponseEntity setAnimalType(@RequestParam String name,
                                        @RequestParam String animalType) {
        if (animalType.toUpperCase().equals("DOG")) {
            return ResponseEntity.ok(clientService.setAnimalType(name, animalType.toUpperCase()));
        }
        if (animalType.toUpperCase().equals("CAT")) {
            return ResponseEntity.ok(clientService.setAnimalType(name, animalType.toUpperCase()));
        }
        return ResponseEntity.notFound().build();
    }
}
