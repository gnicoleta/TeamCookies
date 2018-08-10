insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (1,'Doe','John','john.doe@foo.com', '+40456789011', 'test', 'dojo', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (2,'Public','Mary','mary.public@foo.com', '+40456789011', 'test', 'puma', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (3,'Queue','Susan','susan.queue@foo.com', '+40456789011', 'test', 'qusa', 0);

insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (4,'Williams','David','david.williams@foo.com','+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'wida', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (5,'Johnson','Lisa','lisa.johnson@foo.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'joli', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (6,'Smith','Paul','paul.smith@foo.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'smpa', 0);

insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (7,'Adams','Carl','carl.adams@foo.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'adca', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (8,'Brown','Bill','bill.brown@foo.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'brobi', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (9,'Thomas','Susan','susan.thomas@foo.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'thsu', 0);

insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (10,'Davis','John','john.davis@foo.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'dajo', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (11,'Fowler','Mary','mary.fowler@foo.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'foma', 0);
insert into jbugger.user (ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILENUMBER, PASSWORD, USERNAME, USERSTATUS) VALUES (12,'Waters','David','david.waters@foo.com', '+40456789011', 'E00CF25AD42683B3DF678C61F42C6BDA', 'wada', 0);



insert into jbugger.role(ID, ROLE) VALUES (1, 0);
insert into jbugger.role(ID, ROLE) VALUES (2, 1);
insert into jbugger.role(ID, ROLE) VALUES (3, 2);

insert into jbugger.role(ID, ROLE) VALUES (4, 3);
insert into jbugger.role(ID, ROLE) VALUES (5, 4);



insert into jbugger.user_role(role_id, user_id) VALUES (1,1 );
insert into jbugger.user_role(role_id, user_id) VALUES (2,2 );
insert into jbugger.user_role(role_id, user_id) VALUES (3,3 );

insert into jbugger.user_role(role_id, user_id) VALUES (4,4 );
insert into jbugger.user_role(role_id, user_id) VALUES (5,5 );
insert into jbugger.user_role(role_id, user_id) VALUES (1,6 );

insert into jbugger.user_role(role_id, user_id) VALUES (2,7 );
insert into jbugger.user_role(role_id, user_id) VALUES (3,8 );
insert into jbugger.user_role(role_id, user_id) VALUES (4,9 );

insert into jbugger.user_role(role_id, user_id) VALUES (5,10);
insert into jbugger.user_role(role_id, user_id) VALUES (1,11);
insert into jbugger.user_role(role_id, user_id) VALUES (2,12);



