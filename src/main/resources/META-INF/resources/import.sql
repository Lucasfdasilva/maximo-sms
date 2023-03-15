create table tipo_usuario(
descricao varchar,
codigo INTEGER
);
insert into tipo_usuario(descricao, codigo) VALUES ('Cliente', 1);
insert into tipo_usuario(descricao, codigo) VALUES ('Funcionario', 2);
insert into tipo_usuario(descricao, codigo) VALUES ('ADM', 3);

create table status_usuario(
descricao varchar,
codigo INTEGER
);
insert into status_usuario(descricao, codigo) VALUES ('Ativo', 1);
insert into status_usuario(descricao, codigo) VALUES ('Desativado', 2);
insert into status_usuario(descricao, codigo) VALUES ('Bloqueado', 3);

