CREATE TABLE DATASETS (
    id bigserial PRIMARY KEY,
    title varchar(50) NOT NULL,
    description varchar(255),
    user_id bigint NOT NULL,
    is_trained_on boolean,
    accident_severity_prediction_classifier bytea,
    casualties_number_prediction_classifier bytea,
    FOREIGN KEY(user_id) REFERENCES users(id)
);