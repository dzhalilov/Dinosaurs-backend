create table if not exists course_transition
(
    id              uuid      not null
        primary key,
    ct_course_id    bigint
        constraint fk_course_id
            unique
        constraint fk5kggix8ruuokkvahqigu1o1p1
            references course
            on delete no action,
    tc_user_id      bigint
        constraint fk_user_id
            unique
        constraint fketk75eu2q5bc7co924oi6ig5
            references user_auth
            on delete no action,
    transitioned_at timestamp not null
);
