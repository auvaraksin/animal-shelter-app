-- liquibase formatted sql

-- changeset Daimenss:1


CREATE TABLE animal
(
    id   SERIAL       NOT NULL,
    type VARCHAR(255) NOT NULL,
    CONSTRAINT animal_primary_key PRIMARY KEY (type)
);

INSERT INTO animal
values (1, 'DOG');

INSERT INTO animal
values (2, 'NO_ANIMAL');

CREATE TABLE client
(
    id                      serial       NOT NULL,
    chat_id                 BIGINT       NOT NULL,
    name                    varchar(255) NOT NULL,
    phone_number            varchar(255) NOT NULL,
    email                   varchar(255) NOT NULL,
    status                  varchar(255) NOT NULL,
    animal_type             varchar(255) REFERENCES animal (type),
    start_trial_date        DATE DEFAULT NULL,
    end_trial_date          DATE DEFAULT NULL,
    CONSTRAINT user_primary_key PRIMARY KEY (id)
);

CREATE TABLE reporting
(
    id          serial       NOT NULL,
    id_user     bigint       not null references client (id),
    report_text TEXT         NOT NULL,
    file_path   TEXT         NOT NULL,
    file_size   BIGINT       NOT NULL,
    preview     OID,
    sent_date   DATE,
    status      varchar(255) NOT NULL DEFAULT 'DECLINED',
    CONSTRAINT report_primary_key PRIMARY KEY (id)
);

-- changeset EvBak:1
ALTER TABLE chat_user DROP COLUMN email;
ALTER TABLE chat_user DROP COLUMN phone_number;

-- changeset EvBak:2
ALTER TABLE client DROP COLUMN email;

-- changeset EvBak:4
ALTER TABLE client DROP COLUMN start_trial_date;

-- changeset EvBak:5
ALTER TABLE client DROP COLUMN end_trial_date;
ALTER TABLE client DROP COLUMN status;

-- changeset EvBak:6
ALTER TABLE client DROP COLUMN phone_number;
ALTER TABLE client ALTER COLUMN name DROP NOT NULL;
ALTER TABLE client ALTER COLUMN contacts DROP NOT NULL;

--changeset EvBak:8
INSERT INTO message_source (digest,response_message)
VALUES ('info', 'Наш приют "Shelter" существует уже два года и предоставляет услуги по патронажу и усыновлению собак и кошек');

--changeset EvBak:9
INSERT INTO message_source
values ('adress', 'Наш приют расположен по адресу: РК, г. Астана, ул. Абая, 22, телефон 89999991111, email shelter@mail.ru Время работы: ежедневно с 9.00 до 18.00');

INSERT INTO message_source
values ('rules', 'На территории приюта нельзя пить, курить, кричать, пугать животных, приводить детей младше 7 лет');

INSERT INTO message_source
values ('animalMeetingRules', 'Приближайтесь к животному осторожно, без резких движений, разговаривайте спокойно, ласково, называйте по кличке');

INSERT INTO message_source
values ('documents', 'Для усыновления животного необходимы следующие документы: паспорт,заполенный договор, справка от врача об отсутствии аллергии');

INSERT INTO message_source
values ('transportingRules', 'Транспортировать животное можно только в специальной сумке или переноске');

INSERT INTO message_source
values ('puppyHome', 'Домик для щенка должен быть красивым и удобным, с приспособлением для точки зубов и когтей и игрушками');

INSERT INTO message_source
values ('dogHome', 'Дом для взрослой собаки должен быть оборудован спальным местом, поилкой, миской');

INSERT INTO message_source
values ('invalidDogHome', 'Дом для собаки с ограниченными возможностями должен иметь постоянный доступ к животному, желательно, быть траспортабельным');

INSERT INTO message_source
values ('kinolog', 'Кинологи советуют не сразу приступать к дрессировке, дать собаке сначала привыкнуть');

INSERT INTO message_source
values ('kinologList', 'Иванов Иван Иванович, Петров Петр Петрович');

INSERT INTO message_source
values ('rejection', 'Причиной отказа может быть: наличие аллергии, неблагоприятные жилищные условия, алкоголизм, и т.п. Ну, или если просто произведёте неблагоприятное впечатление');