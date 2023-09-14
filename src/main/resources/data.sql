-- INITIAL DATA
INSERT INTO person (id, person_type, first_name, last_name, email, phone_number, update_date, creation_date) VALUES (NEXT VALUE FOR PERSON_SEQ, 'INTERVIEWER', 'Pedro', 'Vareta', 'pv@mail.com', '+351910000000', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO person (id, person_type, first_name, last_name, email, phone_number, update_date, creation_date) VALUES (NEXT VALUE FOR PERSON_SEQ, 'INTERVIEWER', 'Alberto', 'Mendes', 'am@mail.com', '+351910000001', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO person (id, person_type, first_name, last_name, email, phone_number, update_date, creation_date) VALUES (NEXT VALUE FOR PERSON_SEQ, 'CANDIDATE', 'Tadeu', 'Ferreira', 'tf@mail.com', '+351910000002', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());