create table subject
(
    id          int auto_increment
        primary key,
    name        varchar(64) not null,
    description tinytext    null,
    type_id     int         not null,
    constraint subject_id_uindex
        unique (id),
    constraint subject_subject_type_id_fk
        foreign key (type_id) references subject_type (id)
            on update cascade on delete cascade
);

