drop table if exists course_and_profession;

drop table if exists course_and_tag;

drop table if exists course;

drop table if exists course_provider;

drop table if exists refresh_token;

drop table if exists survey_question_answer;

drop table if exists survey_question;

drop table if exists survey;

drop table if exists tag;

alter table tempconfirmation
    drop constraint if exists fklw89u8at1trve48sg24p4oeu8;

alter table user_auth
    drop constraint if exists fkat1c6life5ma1ceq9jxk253cp;

drop table if exists user_info;

drop table if exists profession;

drop table if exists user_auth;

drop table if exists tempconfirmation;

create table course_provider
(
    id                bigint        not null
        primary key,
    cover_url         varchar(255)  not null,
    description       varchar(4096) not null,
    name              varchar(255)  not null,
    short_description varchar(255),
    url               varchar(255)  not null
);

create table course
(
    id                   bigint        not null
        primary key,
    cover_url            varchar(255)  not null,
    description          varchar(4096) not null,
    ends_at              timestamp     not null,
    internal_rating      integer       not null,
    is_advanced          boolean       not null,
    is_archived          boolean       not null,
    is_indefinite        boolean       not null,
    short_description    varchar(255),
    starts_at            timestamp     not null,
    title                varchar(255)  not null,
    url                  varchar(255)  not null,
    c_course_provider_id bigint        not null
        constraint fk5kggix8ruuokkvahqigu1o1p1
            references course_provider
);

create table profession
(
    id                bigint       not null
        primary key,
    cover_url         varchar(255),
    description       varchar(4096),
    name              varchar(255) not null
        constraint uk_l13yec50eb0ygdekleh9sm6h0
            unique,
    short_description varchar(255)
);

create table course_and_profession
(
    id                bigint not null
        primary key,
    cap_course_id     bigint not null
        constraint fkgtwcc379lmlhrkoqaxmxdaoc0
            references course,
    cap_profession_id bigint not null
        constraint fkloqx1vnm61jle8l9om0x1t460
            references profession
);

create table refresh_token
(
    refresh_token_id bigint       not null
        primary key,
    user_id          bigint       not null
        constraint uk_f95ixxe7pa48ryn1awmh2evt7
            unique,
    value            varchar(255) not null
        constraint uk_8rshch3e41dfdp17ljdetdplb
            unique
);

create table survey
(
    id                bigint        not null
        primary key,
    description       varchar(4096) not null,
    short_description varchar(255),
    title             varchar(255)  not null
);

create table survey_question
(
    id           bigint       not null
        primary key,
    text         varchar(255) not null,
    sq_survey_id bigint       not null
        constraint fk31dcrce2x77q3os965gc1m9br
            references survey
);

create table survey_question_answer
(
    id                bigint       not null
        primary key,
    text              varchar(255) not null,
    sqa_profession_id bigint       not null
        constraint fkk7swosd6nyhad9l7ywhn2omf7
            references profession,
    sqa_question_id   bigint       not null
        constraint fkdu6qr22nw549l4uguj271r5qw
            references survey_question
);

create table tag
(
    id    bigint       not null
        primary key,
    value varchar(255) not null
        constraint uk_rr0jdrb0km505m2m59c7kjlwa
            unique
);

create table course_and_tag
(
    id            bigint not null
        primary key,
    cat_course_id bigint not null
        constraint fkoy3of5u8me7jmcr9yfkchtxib
            references course,
    cat_tag_id    bigint not null
        constraint fkswpwirmcu4rec27s8qqu0tj9h
            references tag
);

create table tempconfirmation
(
    id         uuid      not null
        primary key,
    issued_at  timestamp not null,
    tc_user_id bigint
        constraint uk_lhmmesfljyrnjwg4suhesrwjb
            unique
);

create table user_auth
(
    id            bigint       not null
        primary key,
    archived_at   timestamp,
    email         varchar(255) not null
        constraint uk_pou0ngjxlvv2r6yd8td3idhqk
            unique,
    is_archived   boolean      not null,
    is_confirmed  boolean      not null,
    password      varchar(255) not null,
    regestered_at timestamp,
    role          integer      not null,
    tc_user_id    uuid
        constraint fketk75eu2q5bc7co924oi6ig5
            references tempconfirmation,
    ui_user_id    bigint
);

alter table tempconfirmation
    add constraint fklw89u8at1trve48sg24p4oeu8
        foreign key (tc_user_id) references user_auth
            on delete cascade;

create table user_info
(
    id               bigint       not null
        primary key,
    name             varchar(255) not null,
    surname          varchar(255) not null,
    ui_profession_id bigint
        constraint fkbr230wi4080leiwimmfu4ct36
            references profession,
    ui_user_id       bigint
        constraint fkovi0nnh1qqlqanxo5vehmugoj
            references user_auth
            on delete cascade
);

alter table user_auth
    add constraint fkat1c6life5ma1ceq9jxk253cp
        foreign key (ui_user_id) references user_info;