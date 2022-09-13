
-- Create table
create table APP_USER
(
  USER_ID           BIGINT not null,
  USER_NAME         VARCHAR(20) not null,
  ENCRYTED_PASSWORD VARCHAR(128) not null
);
--  
alter table APP_USER
  add constraint APP_USER_PK primary key (USER_ID);

alter table APP_USER
  add constraint APP_USER_UNIQUE unique (USER_NAME);


-- Create table
create table APP_ROLE
(
  ROLE_ID   BIGINT not null,
  ROLE_NAME VARCHAR(20) not null
);
--  
alter table APP_ROLE
  add constraint APP_ROLE_PK primary key (ROLE_ID);

alter table APP_ROLE
  add constraint APP_ROLE_UNIQUE unique (ROLE_NAME);


-- Create table
create table USER_ROLE
(
  ID      BIGINT not null,
  USER_ID BIGINT not null,
  ROLE_ID BIGINT not null
);
--  
alter table USER_ROLE
  add constraint USER_ROLE_PK primary key (ID);

alter table USER_ROLE
  add constraint USER_ROLE_UNIQUE unique (USER_ID, ROLE_ID);

alter table USER_ROLE
  add constraint USER_ROLE_FK1 foreign key (USER_ID)
  references APP_USER (USER_ID);

alter table USER_ROLE
  add constraint USER_ROLE_FK2 foreign key (ROLE_ID)
  references APP_ROLE (ROLE_ID);

 
-------------------------------------- Pass: 123456

insert into App_User (USER_ID, USER_NAME, ENCRYTED_PASSWORD, ENABLED)
values (2, 'user1', '$2a$10$.SpVyirwnaZjhlXB6OatuOG36JQMBTNnIwPZ7X1tgDkDVZzZm13Um');

insert into App_User (USER_ID, USER_NAME, ENCRYTED_PASSWORD, ENABLED)
values (1, 'admin1', '$2a$10$.SpVyirwnaZjhlXB6OatuOG36JQMBTNnIwPZ7X1tgDkDVZzZm13Um');

---

insert into app_role (ROLE_ID, ROLE_NAME)
values (1, 'ROLE_ADMIN');

insert into app_role (ROLE_ID, ROLE_NAME)
values (2, 'ROLE_USER');

---

insert into user_role (ID, USER_ID, ROLE_ID)
values (1, 1, 1);

insert into user_role (ID, USER_ID, ROLE_ID)
values (2, 1, 2);

insert into user_role (ID, USER_ID, ROLE_ID)
values (3, 2, 2);

---

create table ACCOUNTS
(
  ID                BIGINT not null,
  ROLE_NAME         VARCHAR(20) not null,
  USER_NAME         VARCHAR(20) not null,
  ENCRYTED_PASSWORD VARCHAR(128) not null
);
--  
alter table ACCOUNTS
  add constraint ACCOUNTS_PK primary key (ID);

---

Commit;
