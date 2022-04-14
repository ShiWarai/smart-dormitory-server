create table resident
(
    resident_id int auto_increment
        primary key,
    surname     char(50)      not null,
    name        char(50)      not null,
    patronymic  char(50)      null,
    birthdate   date          not null,
    student_id  char(7)       null,
    room_number int           null,
    role        int default 0 not null,
    constraint resident_resident_id_uindex
        unique (resident_id),
    constraint resident_role_type
        foreign key (role) references role_type (id)
            on update cascade on delete set default,
    constraint resident_room_number_fk
        foreign key (room_number) references room (number)
            on update cascade on delete set null
)
    charset = utf8mb4;

