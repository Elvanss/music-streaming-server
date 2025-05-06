-- USERS
CREATE TABLE users (
    user_id UUID DEFAULT gen_random_uuid() NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    profile_image BYTEA
);

-- USER SETTINGS
CREATE TABLE user_audit_data (
    user_id UUID PRIMARY KEY,
    is_email_verified BOOLEAN DEFAULT FALSE,
    is_phone_verified BOOLEAN DEFAULT FALSE,
    is_account_locked BOOLEAN DEFAULT FALSE,
    language VARCHAR(10),
    dark_mode BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING_VERIFICATION'
);

-- ARTISTS
CREATE TABLE artists (
    artist_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    genre VARCHAR(50),
    image BYTEA
);

-- ALBUMS
CREATE TABLE albums (
    album_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    artist_id INT,
    name VARCHAR(100) NOT NULL,
    release_date DATE,
    image BYTEA
);

-- TRACKS
CREATE TABLE tracks (
    track_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    album_id INT,
    name VARCHAR(100) NOT NULL,
    duration INT,
    path VARCHAR(255),
    is_explicit BOOLEAN DEFAULT FALSE
);

-- PLAYLISTS
CREATE TABLE playlists (
    playlist_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT,
    name VARCHAR(100),
    image BYTEA,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PLAYLIST TRACKS
CREATE TABLE playlist_tracks (
    playlist_id INT,
    track_id INT,
    track_order INT,
    PRIMARY KEY (playlist_id, track_id)
);

-- USER LIKES
CREATE TABLE user_likes (
    user_id INT,
    track_id INT,
    liked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(user_id, track_id)
);

-- FOLLOWERS
CREATE TABLE followers (
    user_id INT,
    artist_id INT,
    PRIMARY KEY(user_id, artist_id)
);

-- SUBSCRIPTION PLANS
CREATE TABLE subscription_plans (
    plan_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50),
    price DECIMAL(10,2),
    description TEXT
);

-- USER SUBSCRIPTIONS
CREATE TABLE user_subscriptions (
    user_id INT,
    plan_id INT,
    subscribed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    PRIMARY KEY(user_id, plan_id)
);


-- PAYMENTS
CREATE TABLE payments (
    payment_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT,
    method VARCHAR(50),
    amount DECIMAL(10,2),
    paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- PLAY HISTORY
CREATE TABLE user_play_history (
    user_id INT,
    track_id INT,
    play_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(user_id, track_id, play_date)
);

-- USER SIMILARITY – For future recommendation systems
CREATE TABLE user_song_similarity (
    user_id INT,
    track_id INT, -- consistent with other tables
    similarity_score FLOAT,
    PRIMARY KEY(user_id, track_id)
);

-- RELATIONSHIP STATEMENT
ALTER TABLE user_audit_data
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id)
REFERENCES users(user_id)
ON DELETE CASCADE;

-- Add check constraint for status
ALTER TABLE user_audit_data
ADD CONSTRAINT status_check
CHECK (status IN ('ACTIVE', 'DISABLED', 'PENDING_VERIFICATION', 'LOCKED', 'DELETED'));

-- Add check constraint for role
ALTER TABLE user_audit_data
ADD CONSTRAINT role_check
CHECK (role IN ('ROLE_TESTER', 'ROLE_USER', 'ROLE_ARTIST', 'ROLE_ADMIN'));

ALTER TABLE user_settings
ADD CONSTRAINT fk_user_settings_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE albums
ADD CONSTRAINT fk_albums_artist FOREIGN KEY (artist_id) REFERENCES artists(artist_id) ON DELETE SET NULL;

ALTER TABLE tracks
ADD CONSTRAINT fk_tracks_album FOREIGN KEY (album_id) REFERENCES albums(album_id) ON DELETE SET NULL;

CREATE INDEX idx_tracks_name ON tracks(name);

ALTER TABLE playlists
ADD CONSTRAINT fk_playlists_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE playlist_tracks
ADD CONSTRAINT fk_playlist_tracks_playlist FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id) ON DELETE CASCADE;

ALTER TABLE playlist_tracks
ADD CONSTRAINT fk_playlist_tracks_track FOREIGN KEY (track_id) REFERENCES tracks(track_id) ON DELETE CASCADE;

ALTER TABLE user_likes
ADD CONSTRAINT fk_user_likes_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE user_likes
ADD CONSTRAINT fk_user_likes_track FOREIGN KEY (track_id) REFERENCES tracks(track_id) ON DELETE CASCADE;

ALTER TABLE followers
ADD CONSTRAINT fk_followers_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE followers
ADD CONSTRAINT fk_followers_artist FOREIGN KEY (artist_id) REFERENCES artists(artist_id) ON DELETE CASCADE;
ALTER TABLE user_subscriptions
ADD CONSTRAINT fk_user_subscriptions_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE user_subscriptions
ADD CONSTRAINT fk_user_subscriptions_plan FOREIGN KEY (plan_id) REFERENCES subscription_plans(plan_id) ON DELETE CASCADE;

ALTER TABLE payments
ADD CONSTRAINT fk_payments_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE user_play_history
ADD CONSTRAINT fk_play_history_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE user_play_history
ADD CONSTRAINT fk_play_history_track FOREIGN KEY (track_id) REFERENCES tracks(track_id) ON DELETE CASCADE;

ALTER TABLE user_song_similarity
ADD CONSTRAINT fk_similarity_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE user_song_similarity
ADD CONSTRAINT fk_similarity_track FOREIGN KEY (track_id) REFERENCES tracks(track_id) ON DELETE CASCADE;

-- You may also add a FK for song_id if needed, depending on whether it refers to tracks(track_id):
-- ALTER TABLE user_song_similarity
-- ADD CONSTRAINT fk_similarity_song FOREIGN KEY (song_id) REFERENCES tracks(track_id) ON DELETE CASCADE;
