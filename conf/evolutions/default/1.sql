# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table assets (
  id                            bigint auto_increment not null,
  asset_type                    integer,
  price                         double,
  serial_number                 varchar(255),
  model_number                  varchar(255),
  warranty_expires_at           datetime(6),
  is_available                  tinyint(1) default 0,
  manufacturer_id               bigint,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint ck_assets_asset_type check (asset_type in (0,1,2,3,4,5)),
  constraint pk_assets primary key (id)
);

create table employees (
  id                            varchar(255) not null,
  name                          varchar(255),
  email                         varchar(255),
  mobile                        varchar(255),
  designation                   varchar(255),
  department                    integer,
  hashed_password               varchar(255),
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint ck_employees_department check (department in (0,1,2,3,4)),
  constraint pk_employees primary key (id)
);

create table employee_assets (
  id                            bigint auto_increment not null,
  employee_id                   varchar(255),
  asset_id                      bigint,
  reason                        varchar(255),
  status                        integer,
  requested_at                  datetime(6),
  expected_issue_date           datetime(6),
  issued_at                     datetime(6),
  returned_at                   datetime(6),
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint ck_employee_assets_status check (status in (0,1,2,3)),
  constraint pk_employee_assets primary key (id)
);

create table manufacturing_companies (
  id                            bigint auto_increment not null,
  registered_name               varchar(255),
  contact_person                varchar(255),
  contact_email                 varchar(255),
  contact_number                varchar(255),
  licence_number                varchar(255),
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint pk_manufacturing_companies primary key (id)
);

create table purchase_orders (
  id                            bigint auto_increment not null,
  asset_id                      bigint,
  purchased_by_id               varchar(255),
  requested_at                  datetime(6),
  expected_delivery             datetime(6),
  delivered_at                  datetime(6),
  purchase_status               integer,
  created_at                    datetime(6) not null,
  updated_at                    datetime(6) not null,
  constraint ck_purchase_orders_purchase_status check (purchase_status in (0,1,2,3)),
  constraint uq_purchase_orders_asset_id unique (asset_id),
  constraint pk_purchase_orders primary key (id)
);

alter table assets add constraint fk_assets_manufacturer_id foreign key (manufacturer_id) references manufacturing_companies (id) on delete restrict on update restrict;
create index ix_assets_manufacturer_id on assets (manufacturer_id);

alter table employee_assets add constraint fk_employee_assets_employee_id foreign key (employee_id) references employees (id) on delete restrict on update restrict;
create index ix_employee_assets_employee_id on employee_assets (employee_id);

alter table employee_assets add constraint fk_employee_assets_asset_id foreign key (asset_id) references assets (id) on delete restrict on update restrict;
create index ix_employee_assets_asset_id on employee_assets (asset_id);

alter table purchase_orders add constraint fk_purchase_orders_asset_id foreign key (asset_id) references assets (id) on delete restrict on update restrict;

alter table purchase_orders add constraint fk_purchase_orders_purchased_by_id foreign key (purchased_by_id) references employees (id) on delete restrict on update restrict;
create index ix_purchase_orders_purchased_by_id on purchase_orders (purchased_by_id);


# --- !Downs

alter table assets drop foreign key fk_assets_manufacturer_id;
drop index ix_assets_manufacturer_id on assets;

alter table employee_assets drop foreign key fk_employee_assets_employee_id;
drop index ix_employee_assets_employee_id on employee_assets;

alter table employee_assets drop foreign key fk_employee_assets_asset_id;
drop index ix_employee_assets_asset_id on employee_assets;

alter table purchase_orders drop foreign key fk_purchase_orders_asset_id;

alter table purchase_orders drop foreign key fk_purchase_orders_purchased_by_id;
drop index ix_purchase_orders_purchased_by_id on purchase_orders;

drop table if exists assets;

drop table if exists employees;

drop table if exists employee_assets;

drop table if exists manufacturing_companies;

drop table if exists purchase_orders;

