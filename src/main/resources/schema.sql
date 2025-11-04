CREATE TABLE IF NOT EXISTS videos  (
	"id"	INTEGER,
	"youtube_link"	TEXT NOT NULL,
	"duration_seconds"	INTEGER,
	"scheduled_date"	TEXT,
	"scheduled_time"	TEXT,
	PRIMARY KEY("id")
);