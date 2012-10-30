create table project (
  id                        bigint not null,
  name                      varchar(255),
  folder                    varchar(255),
  constraint pk_project primary key (id))
;

create table task (
  id                        bigint not null,
  title                     varchar(255),
  done                      boolean,
  due_date                  timestamp,
  folder                    varchar(255),
  project_id                bigint,
  constraint pk_task primary key (id))
;

create sequence project_seq;

create sequence task_seq;

alter table task add constraint fk_task_project_1 foreign key (project_id) references project (id);
create index ix_task_project_1 on task (project_id);


