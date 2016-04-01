
drop table transaction;

create table transaction (
	transaction_id varchar(50) primary key, 
	card_no varchar(30) not null,
	channel_id varchar(50) not null,
	upc varchar(13) not null,
	load_value double not null,
	card_status char(4),
	transaction_time datetime,
	remark varchar(256)
);


drop table card;
create table card (
	card_no varchar(30) not null,
	card_upc varchar(13) not null,
	face_value double,
	max_face_value double,
	card_type char(4),
	card_status char(4),
	create_time datetime,
	last_update_time datetime,
	assignedDp integer,
	assignedStore integer,
	remark varchar(256),
	primary key ( card_no, card_upc)
);

drop table card_transaction;
create table card_transaction (
	transaction_id varchar(50) primary key,
	card_no varchar(30) not null,
	card_upc varchar(13) not null,
	type integer not null,
	load_value double,
	channel_id integer,
	transaction_time datetime,
	remark varchar (256)
)
	

	
	
