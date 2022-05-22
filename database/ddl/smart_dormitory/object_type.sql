create table object_type
(
    id   int auto_increment
        primary key,
    name varchar(32) null,
    constraint subject_type_id_uindex
        unique (id),
    constraint subject_type_name_uindex
        unique (name)
);

