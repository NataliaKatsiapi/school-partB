create schema privateschool;
use privateschool;

-- tables creation
create table user(
	username varchar(20) not null,
    password varchar(30) not null
);
select * from user;

create user 'admin' identified by '1234';
grant all on privateschool.* to 'admin';
insert into user
values ('admin', '1234');


create table trainer (
	tid int primary key not null auto_increment,
    tfname varchar(20) not null,
    tlname varchar(25) not null,
    tsubject varchar(20) not null
);

create table ctype(
	typeid integer not null primary key auto_increment,
    tname varchar(10) not null 
);


create table course (
	cid integer primary key not null auto_increment,
    ctitle varchar(20) not null,
    cstream varchar(20) not null,
    cstart_date date not null,
    cend_date date not null,
    ctype integer not null,
    
    constraint typefk foreign key (ctype) references ctype(typeid)
);

create table trainer_course(
	tsid integer primary key not null auto_increment,
    tid integer not null,
    cid integer not null,
    
    constraint trfk foreign key (tid) references trainer(tid),
    constraint coufk foreign key (cid) references course(cid)
);

create table student(
	sid integer primary key not null auto_increment,
    sfname varchar(20) not null,
    slname varchar(25) not null,
    sfees float(6,2) not null,
   sdob date not null 
); 

drop table student;

create table course_student(
	csid integer primary key not null auto_increment,
    cid integer not null,
    sid integer not null,
    
    constraint cfk foreign key (cid) references course(cid),
    constraint stfk foreign key (sid) references student(sid)
);

drop table assignment;
create table assignment(
	aid integer primary key not null auto_increment,
    atitle varchar(20) not null,
    adescr varchar(35) not null,
    asub_date date
); 


alter table assignment
add column cid integer not null,
add constraint corsefk foreign key (cid) references course(cid);

create table student_assignment(
	said integer primary key not null auto_increment,
    oralmark float,
    totalmark float,
    sid integer not null,
    aid integer not null,
    
    constraint sfk foreign key (sid) references student(sid),
	constraint afk foreign key (aid) references assignment(aid)
);


-- inserts
insert into student(sfname,slname,sfees,sdob)
values ('Maria', 'Papadopoulou', 2500, 20000513);

insert into student(sfname,slname,sfees,sdob)
values ('Giorgos', 'Kyriakou', 1990, 19700112),
		('Marios', 'Georgiou', 3000, 19850828),
        ('Chris', 'Sideris', 2000, 19981125),
        ('Chrysi', 'Apostolou', 1700, 19901217);
 
select * from course;
insert into ctype(tname)
values('part'),
		('full');
        
insert into course(ctitle, cstream, cstart_date, cend_date, ctype)
values ('Java', 'CB8', 20190513, 20200301, 2),
		('C#', 'CB7', 20180810, 20200112, 1),
        ('Python', 'CB6', 20190412, 20200228, 1);
        
 select * from trainer;       
insert into trainer(tfname, tlname, tsubject) 
values ('Eleni', 'Stroumpouli', 'DataBases'),
		('Dimitris', 'Efthimiou', 'Introduction'), 
        ('Fotis', 'Fotiou', 'Web Design'), 
        ('Niki', 'Spyraki', 'Backend');
        
select * from assignment;
insert into assignment(atitle, adescr, asub_date, cid)
values ('Private School', 'a private school application', 20191223, (select cid from course where ctitle = 'Python')),
		('Private School', 'a private school application', 20190713, (select cid from course where ctitle = 'Java')),
		('Food', 'a web application for food ordering', 20191112, (select cid from course where ctitle = 'C#')),
        ('eshop', 'an online shop', 20191222, (select cid from course where ctitle = 'Python')),
        ('Footbal Game', 'calculating points',20191227, (select cid from course where ctitle = 'Java'));
        
select * from course_student;
insert into course_student(sid, cid)
values (1,(select cid from course where ctitle = 'Python')),
		(1, (select cid from course where ctitle = 'Java')),
        (2,(select cid from course where ctitle = 'Java')),
        (3, (select cid from course where ctitle = 'Java')),
        (3, (select cid from course where ctitle = 'Python')),
        (3, (select cid from course where ctitle = 'C#')),
		(4, (select cid from course where ctitle = 'Java')),
        (4, (select cid from course where ctitle = 'Python')),
        (5, (select cid from course where ctitle = 'C#'));
        
        select * from trainer_course;
        
        insert into trainer_course(tid, cid)
        values (1,(select cid from course where ctitle = 'Python')),
			   (2,(select cid from course where ctitle = 'Java')),
               (2,(select cid from course where ctitle = 'Python')),
               (3,(select cid from course where ctitle = 'C#')),
               (4,(select cid from course where ctitle = 'Java'));
               
	
    insert into student_assignment(sid, aid, oralMark, totalMark)
    values  (1, 6, 89, 76.5),
			(1, 7, 54, 70),
            (2, 7, 60, 88),
            (3, 9, 87, 45),
            (4, 10, 34, 98),
            (4, 7, 56.9, 59),
            (4, 6, 45, 78),
            (4, 9, 67, 96),
            (5, 8, 100, 89);
   -- δεν είναι απαραίτητο ότι οι μαθητές έχουν κάνει όλες τις εργασίες τους         
            
            
            
            
-- queries
select * 
from student;

select * 
from trainer;

select *
from assignment;

select *
from course as c, ctype as t
where c.ctype = t.typeid;

select s.*, c.*, t.tname
from student as s, course as c, course_student as cs, ctype as t
where cs.cid = c.cid
and cs.sid = s.sid
and c.ctype = t.typeid;

select  t.*, c.*, ct.tname
from course as c, trainer as t, trainer_course as tc, ctype as ct
where tc.tid = t.tid
and tc.cid = c.cid
and ct.typeid = ctype;

select a.*, c.*, ct.tname
from course as c, assignment as a, ctype as ct
where c.cid = a.cid
and ct.typeid = ctype;

select *
from course as c, assignment as a, student as s, student_assignment as sa, ctype as ct
where c.cid = a.cid
and sa.sid = s.sid
and sa.aid = a.aid
and ct.typeid = c.ctype;
 
select s.*, count(cs.sid) as NumberOfCourses
from course_student as cs, student as s
where cs.sid = s.sid
group by cs.sid
having count(cs.sid) > 1;
