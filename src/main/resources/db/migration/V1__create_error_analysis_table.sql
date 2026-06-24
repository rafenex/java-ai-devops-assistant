CREATE TABLE error_analysis (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    context TEXT,
    stacktrace TEXT NOT NULL,
    probable_cause TEXT NOT NULL,
    where_to_look TEXT NOT NULL,
    suggested_fix TEXT NOT NULL,
    risk TEXT NOT NULL,
    category VARCHAR(100) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);