CREATE TABLE IF NOT EXISTS videos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    youtube_link TEXT NOT NULL,
    duration_seconds INTEGER NOT NULL,
    scheduled_date TEXT NOT NULL,
    scheduled_time TEXT NOT NULL
);