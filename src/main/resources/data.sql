-- INITIAL DATA
INSERT INTO person (person_type, first_name, last_name, email, phone_number, update_date, creation_date) VALUES ('INTERVIEWER', 'Pedro', 'Vareta', 'pv@mail.com', '+351-910-000-000', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO person (person_type, first_name, last_name, email, phone_number, update_date, creation_date) VALUES ('INTERVIEWER', 'Alberto', 'Mendes', 'am@mail.com', '+351-910-000-001', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO person (person_type, first_name, last_name, email, phone_number, update_date, creation_date) VALUES ('CANDIDATE', 'Tadeu', 'Ferreira', 'tf@mail.com', '+351-910-000-002', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO schedule (person_id, schedule_day, schedule_hour, update_date, creation_date) VALUES (1L, DATEADD('DAY',1, CURRENT_DATE), 12, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO schedule (person_id, schedule_day, schedule_hour, update_date, creation_date) VALUES (1L, DATEADD('DAY',3, CURRENT_DATE), 18, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO schedule (person_id, schedule_day, schedule_hour, update_date, creation_date) VALUES (2L, DATEADD('DAY',2, CURRENT_DATE), 11, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO schedule (person_id, schedule_day, schedule_hour, update_date, creation_date) VALUES (2L, DATEADD('DAY',3, CURRENT_DATE), 18, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO schedule (person_id, schedule_day, schedule_hour, update_date, creation_date) VALUES (3L, DATEADD('DAY',3, CURRENT_DATE), 12, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO schedule (person_id, schedule_day, schedule_hour, update_date, creation_date) VALUES (3L, DATEADD('DAY',2, CURRENT_DATE), 11, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO schedule (person_id, schedule_day, schedule_hour, update_date, creation_date) VALUES (3L, DATEADD('DAY',3, CURRENT_DATE), 18, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO schedule (person_id, schedule_day, schedule_hour, update_date, creation_date) VALUES (3L, DATEADD('DAY',4, CURRENT_DATE), 9, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
