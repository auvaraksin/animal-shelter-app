package pro.sky.animalshelterapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.animalshelterapp.model.Animal;
import pro.sky.animalshelterapp.model.ChatUser;
import pro.sky.animalshelterapp.service.ChatUserService;

import java.util.Collection;

@RestController
@RequestMapping("/user")
public class ChatUserController {

    private final ChatUserService chatUserService;

    public ChatUserController(ChatUserService chatUserService) {
        this.chatUserService = chatUserService;
    }

    @Operation(
            tags = "Усыновители",
            summary = "Добавление нового усыновителя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавляемый усыновитель\n" +
                            "\nОбязательные поля для добавления: name, phoneNumber, email, status, animal->type, startTrialDate, endTrialDate\n" +
                            "\nДоступные статусы: ADOPTER_ON_TRIAL, ADOPTER_TRIAL_FAILED, OWNER\n" +
                            "\nДоступные типы животных (ОБЯЗАТЕЛЬНО также выбирать в списке выше): DOG"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Новый усыновитель добавлен"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ChatUser> createChatUser(@RequestBody ChatUser chatUser,
                                               @Parameter(description = "Тип животного")
                                           @RequestParam Animal.AnimalTypes type) {
        if (chatUser != null) {
            ChatUser newChatUser = chatUserService.createChatUserByVolunteer(chatUser, type);
            return ResponseEntity.ok(newChatUser);
        } else return ResponseEntity.noContent().build();
    }

    @Operation(
            tags = "Усыновители",
            summary = "Редактирование данных пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Редактируемый пользователь\n" +
                            "\nПоля для редактирования: name, phoneNumber, email, status, animal->type, startTrialDate, endTrialDate\n" +
                            "\nДоступные статусы: GUEST, ADOPTER_ON_TRIAL, ADOPTER_TRIAL_FAILED, OWNER\n" +
                            "\nДоступные типы животных (ОБЯЗАТЕЛЬНО также выбирать в списке выше): DOG, NO_ANIMAL"
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные пользователя изменены"
                    )
            }
    )
    @PutMapping
    public ResponseEntity<ChatUser> editChatUser(@RequestBody ChatUser chatUser,
                                         @Parameter(description = "Тип животного")
                                         @RequestParam Animal.AnimalTypes type) {
        if (chatUser == null) {
            return ResponseEntity.notFound().build();
        }
        ChatUser editedChatUser = chatUserService.createChatUserByVolunteer(chatUser, type);
        return ResponseEntity.ok(editedChatUser);
    }

    @Operation(
            tags = "Усыновители",
            summary = "Поиск пользователя по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденный пользователь"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ChatUser> getChatUser(@Parameter(description = "ID пользователя")
                                        @PathVariable Long id) {
        ChatUser chatUser = chatUserService.getChatUserById(id);
        if (chatUserService.getChatUserById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chatUser);
    }

    @Operation(
            tags = "Усыновители",
            summary = "Удаление пользователя из базы данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь удален"
                    )
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity deleteChatUser(@Parameter(description = "ID пользователя")
                                     @PathVariable Long id) {
        chatUserService.deleteChatUserById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            tags = "Усыновители",
            summary = "Получение списка всех пользователей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список всех пользователей"
                    )
            }
    )
    @GetMapping("/all")
    public ResponseEntity<Collection<ChatUser>> getAllChatUsers() {
        Collection<ChatUser> chatUsers = chatUserService.getAllChatUsers();
        if (chatUsers == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chatUsers);
    }
}