CREATE KEYSPACE nicecase WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE nicecase.human (
    id TIMEUUID,
    firstname TEXT,
    lastname TEXT,
    gender TEXT,
    address0 TEXT,
    address1 TEXT,
    city TEXT,
    stateprov TEXT,
    zippostal TEXT,
    country TEXT,
    phone TEXT,
    "isGoodPerson" BOOLEAN, // Case sensitive column name - generally not recommended, but possible.
    PRIMARY KEY(id)
);

CREATE INDEX good_person ON nicecase.human ( "isGoodPerson" ); // We want to be able to find good people quickly

CREATE INDEX person_state ON nicecase.human ( stateprov ); // Maybe we need good people by state?

CREATE INDEX ON nicecase.human ( firstname ); // Let Cassandra use the default name for the index.  Good people tend to be named "Brad" - let's find them fast too!

CREATE TABLE nicecase.goodhuman (
    human_id TIMEUUID,
    PRIMARY KEY(human_id)
);

CREATE TABLE nicecase.badhuman (
    human_id TIMEUUID,
    PRIMARY KEY(human_id)
);

CREATE TABLE nicecase.goodbrad (
    human_id TIMEUUID,
    firstname TEXT,
    PRIMARY KEY(human_id)
);