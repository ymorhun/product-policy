CREATE DATABASE productPolicy;

USE productPolicy;

create table "role" (
role_id SERIAL  PRIMARY KEY,
title varchar(255)
);

create table "user" (
user_id SERIAL primary key,
first_name varchar(32) NOT NULL,
last_name varchar(32) NOT NULL,
email varchar(32) NOT NULL UNIQUE,
password varchar(32) NOT NULL
);

create table user_role(
user_id integer NOT NULL REFERENCES "user",
role_id integer NOT NULL REFERENCES "role",
PRIMARY KEY (user_id, role_id)
);

create table product_policy (
product_policy_id SERIAL PRIMARY KEY,
date_initiation DATE,
date_creation DATE,
"name" varchar(64),
description text,
duration integer,
initiator integer REFERENCES "user"(user_id),
person_creates_decision integer REFERENCES "user"(user_id)
);

create table policy_expert (
user_id integer NOT NULL REFERENCES "user",
product_policy_id integer NOT NULL REFERENCES product_policy,
expert_priority integer,
PRIMARY KEY (user_id, product_policy_id)
);

create table alternative (
alternative_id SERIAL PRIMARY KEY,
title varchar(64),
alternative_type varchar(64)
);

create table alternative_level (
alternative_id integer NOT NULL REFERENCES alternative,
product_policy_id integer NOT NULL REFERENCES product_policy,
"level" integer,
PRIMARY KEY (alternative_id, product_policy_id)
);

create table comparison_matrix (
comparison_matrix_id SERIAL PRIMARY KEY,
date_initiation DATE,
date_creation DATE,
user_id integer REFERENCES "user",
product_policy_id integer NOT NULL REFERENCES product_policy,
alternative_id integer NOT NULL REFERENCES alternative
);

create table comparison_matrix_element (
comparison_matrix_id integer REFERENCES comparison_matrix,
"row" integer,
"column" integer,
"value" numeric(15,3),
first_alternative_id integer NOT NULL REFERENCES alternative(alternative_id),
second_alternative_id integer NOT NULL REFERENCES alternative(alternative_id),
PRIMARY KEY (comparison_matrix_id, "row", "column")
);

create table criteria (
alternative_id integer NOT NULL PRIMARY KEY REFERENCES alternative
);

create table product (
alternative_id integer NOT NULL PRIMARY KEY REFERENCES alternative,
product_group_id integer NOT NULL REFERENCES criteria(alternative_id)
);

create table values_fabric (
alternative_id integer NOT NULL REFERENCES product,
product_policy_id integer NOT NULL REFERENCES product_policy,
volume numeric(15,3),
price numeric(15,3),
PRIMARY KEY (alternative_id, product_policy_id)
);

create table unit (
unit_id SERIAL NOT NULL PRIMARY KEY,
title varchar(32)
);

create table material (
material_id SERIAL NOT NULL PRIMARY KEY,
unit_id integer REFERENCES unit,
title varchar(128)
);

create table product_policy_material (
product_policy_id integer NOT NULL REFERENCES product_policy,
material_id integer NOT NULL REFERENCES material,
volume numeric(15,3),
max_increase numeric(15,3),
max_decrease numeric(15,3),
PRIMARY KEY (product_policy_id, material_id)
);

create table production_cost (
alternative_id integer REFERENCES product,
material_id integer REFERENCES material,
volume_per_piece numeric(15,3),
product_policy_id integer REFERENCES product_policy
PRIMARY KEY (alternative_id, material_id)
);
