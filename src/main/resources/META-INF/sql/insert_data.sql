insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (100,'Doe','John','john.doe@msggroup.com', '+40456789011', 'test', 'dojo', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (200,'Public','Mary','mary.public@msggroup.com', '+40456789011', 'test', 'puma', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (300,'Queue','Susan','susan.queue@msggroup.com', '+40456789011', 'test', 'qusa', 0);

insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (400,'Williams','David','david.williams@msggroup.com','+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'wida', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (500,'Johnson','Lisa','lisa.johnson@msggroup.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'joli', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (600,'Smith','Paul','paul.smith@msggroup.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'smpa', 0);

insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (700,'Adams','Carl','carl.adams@msggroup.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'adca', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (800,'Brown','Bill','bill.brown@msggroup.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'brobi', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (900,'Thomas','Susan','susan.thomas@msggroup.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'thsu', 0);

insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (101,'Davis','John','john.davis@msggroup.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'dajo', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (110,'Fowler','Mary','mary.fowler@msggroup.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'foma', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (120,'Waters','David','david.waters@msggroup.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'wada', 0);

insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (130,'admin','admin','admin@msggroup.com', '+40456789011', 'admin', 'admin', 0);


insert into jbugger.role(ID, ROLE, ROLE_STRING) VALUES (100, 0,'ADM');
insert into jbugger.role(ID, ROLE, ROLE_STRING) VALUES (200, 1,'PM');
insert into jbugger.role(ID, ROLE, ROLE_STRING) VALUES (300, 2,'TM');

insert into jbugger.role(ID, ROLE, ROLE_STRING) VALUES (400, 3,'DEV');
insert into jbugger.role(ID, ROLE, ROLE_STRING) VALUES (500, 4,'TEST');

insert into jbugger.rights(ID, TYPE, TYPE_STRING) VALUES (100, 0,'PERMISSION_MANAGEMENT');
insert into jbugger.rights(ID, TYPE, TYPE_STRING) VALUES (200, 1,'USER_MANAGEMENT');
insert into jbugger.rights(ID, TYPE, TYPE_STRING) VALUES (300, 2,'BUG_MANAGEMENT');
insert into jbugger.rights(ID, TYPE, TYPE_STRING) VALUES (400, 3,'BUG_CLOSE');
insert into jbugger.rights(ID, TYPE, TYPE_STRING) VALUES (500, 4,'BUG_EXPORT_PDF');

insert into jbugger.role_right(role_id, right_id) VALUES (100,100 );
insert into jbugger.role_right(role_id, right_id) VALUES (100,200 );

insert into jbugger.role_right(role_id, right_id) VALUES (200,300 );
insert into jbugger.role_right(role_id, right_id) VALUES (200,400 );
insert into jbugger.role_right(role_id, right_id) VALUES (200,500 );

insert into jbugger.role_right(role_id, right_id) VALUES (300,300 );
insert into jbugger.role_right(role_id, right_id) VALUES (300,400 );
insert into jbugger.role_right(role_id, right_id) VALUES (300,500 );

insert into jbugger.role_right(role_id, right_id) VALUES (400,300 );
insert into jbugger.role_right(role_id, right_id) VALUES (400,400 );
insert into jbugger.role_right(role_id, right_id) VALUES (400,500 );

insert into jbugger.role_right(role_id, right_id) VALUES (500,300 );
insert into jbugger.role_right(role_id, right_id) VALUES (500,400 );
insert into jbugger.role_right(role_id, right_id) VALUES (500,500 );


insert into jbugger.user_role(role_id, user_id) VALUES (100,100 );
insert into jbugger.user_role(role_id, user_id) VALUES (200,200 );
insert into jbugger.user_role(role_id, user_id) VALUES (300,300 );

insert into jbugger.user_role(role_id, user_id) VALUES (400,400 );
insert into jbugger.user_role(role_id, user_id) VALUES (500,500 );
insert into jbugger.user_role(role_id, user_id) VALUES (100,600 );

insert into jbugger.user_role(role_id, user_id) VALUES (200,700 );
insert into jbugger.user_role(role_id, user_id) VALUES (300,800 );
insert into jbugger.user_role(role_id, user_id) VALUES (400,900 );

insert into jbugger.user_role(role_id, user_id) VALUES (500,101);
insert into jbugger.user_role(role_id, user_id) VALUES (100,110);
insert into jbugger.user_role(role_id, user_id) VALUES (200,120);

insert into jbugger.user_role(role_id, user_id) VALUES (100,130);
insert into jbugger.user_role(role_id, user_id) VALUES (200,130);
insert into jbugger.user_role(role_id, user_id) VALUES (300,130);
insert into jbugger.user_role(role_id, user_id) VALUES (400,130);







