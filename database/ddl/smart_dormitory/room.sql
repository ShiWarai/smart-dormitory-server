create table room
(
    number        int           not null
        primary key,
    floor         int           not null,
    max_residents int           null,
    type_id       int default 0 not null,
    constraint room_room_type_id_fk
        foreign key (type_id) references room_type (id)
            on update cascade on delete set default
)
    charset = utf8mb4;

