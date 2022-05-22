--DROP TABLE IF EXISTS room_type;
--DROP TABLE IF EXISTS room;
--DROP TABLE IF EXISTS role_type;
--DROP TABLE IF EXISTS resident;
--DROP TABLE IF EXISTS object_type;
--DROP TABLE IF EXISTS status_type;
--DROP TABLE IF EXISTS object;
--DROP TABLE IF EXISTS reservation;

create table if not exists room_type
(
    id   serial
    constraint room_type_pk
    primary key,
    name varchar(32) not null
    );

alter table room_type
    owner to shiwarai;

create unique index if not exists room_type_id_uindex
    on room_type (id);

create unique index if not exists room_type_name_uindex
    on room_type (name);

create table if not exists room
(
    number  integer not null,
    floor   integer not null,
    type_id integer not null
    constraint room_room_type_id_fk
    references room_type
    on update cascade on delete restrict
);

alter table room
    owner to shiwarai;

create unique index if not exists room_number_uindex
    on room (number);

create table if not exists role_type
(
    id   integer     not null
    constraint role_type_pk
    primary key,
    name varchar(16) not null
    );

alter table role_type
    owner to shiwarai;

create table if not exists resident
(
    id          serial
    constraint resident_pk
    primary key,
    surname     varchar(50)       not null,
    name        varchar(50)       not null,
    patronymic  varchar(50),
    birthdate   date              not null,
    student_id  char(7),
    pin_code    char(4),
    room_number integer
    constraint resident_room_number_fk
    references room (number)
    on update cascade on delete set null,
    role_id     integer default 0 not null
    constraint resident_role_type_id_fk
    references role_type
    on update cascade on delete set default
    );

alter table resident
    owner to shiwarai;

create unique index if not exists resident_id_uindex
    on resident (id);

create unique index if not exists role_type_id_uindex
    on role_type (id);

create unique index if not exists role_type_name_uindex
    on role_type (name);

create table if not exists object_type
(
    id   serial
    constraint object_type_pk
    primary key,
    name varchar(32) not null
    );

alter table object_type
    owner to shiwarai;

create unique index if not exists object_type_id_uindex
    on object_type (id);

create unique index if not exists object_type_name_uindex
    on object_type (name);

create table if not exists status_type
(
    id   integer not null
    constraint status_pk
    primary key,
    name varchar(16)
    );

alter table status_type
    owner to shiwarai;

create table if not exists object
(
    id          serial
    constraint object_pk
    primary key,
    name        varchar(64) not null,
    description text,
    type_id     integer     not null
    constraint object_object_type_id_fk
    references object_type
    on update cascade on delete restrict,
    status_id   integer default 0
    constraint object_status_id_fk
    references status_type
    on update cascade on delete set default
    );

alter table object
    owner to shiwarai;

create unique index if not exists object_id_uindex
    on object (id);

create unique index if not exists status_id_uindex
    on status_type (id);

create unique index if not exists status_name_uindex
    on status_type (name);

create table if not exists reservation
(
    id                serial
    constraint reservation_pk
    primary key,
    object_id         integer   not null
    constraint reservation_object_id_fk
    references object
    on update cascade on delete cascade,
    resident_id       integer   not null
    constraint reservation_resident_id_fk
    references resident
    on update cascade on delete cascade,
    reason            text,
    start_reservation timestamp not null,
    end_reservation   timestamp not null
);

alter table reservation
    owner to shiwarai;

create unique index if not exists reservation_id_uindex
    on reservation (id);