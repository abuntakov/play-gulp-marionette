DROP TABLE IF EXISTS contacts;
CREATE TABLE contacts (
	id bigint NOT NULL PRIMARY KEY DEFAULT nextval('common_seq'::regclass)
	,email text NOT NULL
	,first_name text
	,last_name text
	,location geometry NOT NULL
	,boundary geometry
	,numbers integer[] NOT NULL
	,categories json
);
