create table resident
(
    id         int auto_increment
        primary key,
    surname    varchar(50)   not null,
    name       varchar(50)   not null,
    patronymic varchar(50)   null,
    birthdate  date          not null,
    student_id char(7)       null,
    pin_code   char(4)       null,
    room_id    int           null,
    role_id    int default 0 not null,
    constraint resident_resident_id_uindex
        unique (id),
    constraint resident_role_type
        foreign key (role_id) references role_type (id)
            on update cascade on delete set default,
    constraint resident_room_number_fk
        foreign key (room_id) references room (number)
            on update cascade on delete set null
)
    charset = utf8mb4;

