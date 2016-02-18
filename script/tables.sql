
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
	

	
	
