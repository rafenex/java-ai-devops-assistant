ALTER TABLE error_analysis
ALTER COLUMN probable_cause DROP NOT NULL;

ALTER TABLE error_analysis
ALTER COLUMN where_to_look DROP NOT NULL;

ALTER TABLE error_analysis
ALTER COLUMN suggested_fix DROP NOT NULL;

ALTER TABLE error_analysis
ALTER COLUMN risk DROP NOT NULL;

ALTER TABLE error_analysis
ALTER COLUMN category DROP NOT NULL;