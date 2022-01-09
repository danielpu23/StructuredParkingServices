DROP DATABASE IF exists StructuredParkingServices;
CREATE DATABASE StructuredParkingServices;
USE StructuredParkingServices;

/* Delete the tables if they already exist */
drop table if exists Clients;
drop table if exists Cars;
drop table if exists Valets;
drop table if exists ParkingSpots;
drop table if exists Transactions;
drop table if exists TransArchive;

/* Create the tables */
create table Clients(cID int primary key auto_increment, name text);
ALTER TABLE Clients AUTO_INCREMENT = 1000;

create table Cars(cID int, licensePlate varchar(7) primary key, make text, model text, foreign key(cID) references Clients(cID) on delete cascade);

create table Valets(vID int primary key, name text);

create table ParkingSpots(sID int primary key auto_increment, vID int, licensePlate varchar(7) unique, foreign key(licensePlate) references Cars(licensePlate) on delete cascade, foreign key(vID) references Valets(vID) on delete set null);

create table Transactions(tID int primary key auto_increment, cID int, licensePlate varchar(7), bill int, startDate datetime, datePaid datetime);

/* Archive table*/
create table TransArchive(tID int primary key auto_increment, cID int, licensePlate varchar(7), bill int, updatedAt datetime);

/* Triggers */
drop trigger if exists insertTransactionTime;
DELIMITER $$
create trigger insertTransactionTime
before insert on Transactions
for each row
begin
    if time(new.startDate) < '06:00:00' or time(new.startDate) > '23:00:00'
    then set new.startDate = null;
    update ParkingSpots set vID = null, licensePlate = null
    where licensePlate = new.licensePlate;
    end if;
end $$

drop trigger if exists updateTransactionTime;
DELIMITER $$
create trigger updateTransactionTime
after update on Transactions
for each row
begin
	if time(new.datePaid) < '06:00:00' or time(new.datePaid) > '23:00:00'
	then update Transactions set datePaid = null and bill = 0 where tID = new.tID;
	end if;
end $$

/* Stored Procedure */
drop procedure if exists ArchiveTrans;
DELIMITER $$
create procedure ArchiveTrans(in cutOff datetime)
begin
	insert into TransArchive select tID, cID, licensePlate, bill, datePaid from Transactions
	where Transactions.datePaid <= cutOff and Transactions.datePaid is not null;
	delete from Transactions where Transactions.datePaid <= cutOff;
end $$

/* Populate Schema initially */
insert into Clients(cID, name) values (null, 'Jason Williams');
insert into Clients(cID, name) values (null, 'Mary Anderson');
insert into Clients(cID, name) values (null, 'Annie Joseph');
insert into Clients(cID, name) values (null, 'Timmy Stevens');
insert into Clients(cID, name) values (null, 'Gordon Ford');
insert into Cars (cID, licensePlate, make, model) values (1000, 'ABCDEFG', 'Toyota', 'Camry');
insert into Cars (cID, licensePlate, make, model) values (1001, '1234567', 'Toyota', 'Sienna');
insert into Cars (cID, licensePlate, make, model) values (1002, '1111111', 'Honda', 'CRV');
insert into Cars (cID, licensePlate, make, model) values (1003, '2222222', 'Ford', 'F150');
insert into Cars (cID, licensePlate, make, model) values (1004, '3333333', 'Nissan', 'Rogue');
insert into Valets(vID, name) values (1, 'Joe Smith');
insert into Valets(vID, name) values (2, 'Bill Johnson');
insert into ParkingSpots values(NULL, 1, '1111111');
insert into ParkingSpots values(NULL, NULL, NULL);
insert into ParkingSpots values(NULL, NULL, NULL);
insert into ParkingSpots values(NULL, NULL, NULL);
insert into ParkingSpots values(NULL, NULL, NULL);
insert into ParkingSpots values(NULL, NULL, NULL);
insert into ParkingSpots values(NULL, NULL, NULL);
insert into ParkingSpots values(NULL, NULL, NULL);
insert into ParkingSpots values(NULL, NULL, NULL);
insert into ParkingSpots values(NULL, NULL, NULL); 
insert into Transactions (tID, cID, licensePlate, bill, startDate, datePaid) values (null, 1000, 'ABCDEFG', 100, '2021-12-03 09:33:22','2021-12-03 13:45:52'); 
insert into Transactions (tID, cID, licensePlate, bill, startDate, datePaid) values (null, 1001, '1234567', 60, '2021-12-03 12:11:17','2021-12-03 14:23:34'); 
insert into Transactions (tID, cID, licensePlate, bill, startDate, datePaid) values (null, 1002, '1111111', 0, '2021-12-03 20:45:32', null); 
