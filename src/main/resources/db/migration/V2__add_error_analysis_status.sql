ALTER TABLE error_analysis
ADD COLUMN status VARCHAR(30) NOT NULL DEFAULT 'DONE';

ALTER TABLE error_analysis
ADD COLUMN processed_at TIMESTAMP;

ALTER TABLE error_analysis
ADD COLUMN error_message TEXT;