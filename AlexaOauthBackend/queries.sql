create table tvsm_otp(
    id int primary key auto_increment,
    mobile_number varchar(13) not null,
    otp varchar(6) not null,
    num_sent int not null default 0,
    expire datetime not null,
    used tinyint(1) default 0,
    created datetime not null,
    modified datetime not null
)