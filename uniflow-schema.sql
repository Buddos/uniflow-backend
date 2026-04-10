-- Create Database (Run this separately if database does not exist)
-- CREATE DATABASE uniflow;

-- Connect to the database before running the following scripts:
-- \c uniflow

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    department VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    phone_number VARCHAR(50),
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE course_units (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255),
    department VARCHAR(255),
    credits INTEGER,
    is_core BOOLEAN DEFAULT TRUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE venues (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    capacity INTEGER,
    building VARCHAR(255),
    floor INTEGER,
    equipment_office_name VARCHAR(255),
    distance_from_office_meters INTEGER,
    location VARCHAR(255),
    resource_home VARCHAR(255),
    has_projector BOOLEAN DEFAULT FALSE,
    has_whiteboard BOOLEAN DEFAULT TRUE,
    has_ac BOOLEAN DEFAULT FALSE,
    status VARCHAR(50) DEFAULT 'AVAILABLE',
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for @ElementCollection in Venue.java
CREATE TABLE venue_equipment (
    venue_id BIGINT NOT NULL,
    equipment VARCHAR(255),
    FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE
);

CREATE TABLE timetable_entries (
    id BIGSERIAL PRIMARY KEY,
    venue_id BIGINT,
    course_unit_id BIGINT,
    lecturer_id BIGINT,
    day_of_week VARCHAR(20),
    start_time TIME,
    end_time TIME,
    academic_year VARCHAR(50),
    semester VARCHAR(50),
    cohort VARCHAR(100),
    expected_students INTEGER,
    is_makeup_class BOOLEAN DEFAULT FALSE,
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    color_code VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE SET NULL,
    FOREIGN KEY (course_unit_id) REFERENCES course_units(id) ON DELETE CASCADE,
    FOREIGN KEY (lecturer_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    venue_id BIGINT,
    timetable_entry_id BIGINT,
    booked_by_id BIGINT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    purpose VARCHAR(255),
    status VARCHAR(50) DEFAULT 'CONFIRMED',
    booking_type VARCHAR(50) DEFAULT 'REGULAR',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE,
    FOREIGN KEY (timetable_entry_id) REFERENCES timetable_entries(id) ON DELETE SET NULL,
    FOREIGN KEY (booked_by_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE course_unit_requests (
    id BIGSERIAL PRIMARY KEY,
    course_unit_id BIGINT,
    requesting_department VARCHAR(255),
    providing_department VARCHAR(255),
    expected_students INTEGER,
    status VARCHAR(50) DEFAULT 'PENDING',
    rejection_reason TEXT,
    comments TEXT,
    semester VARCHAR(50),
    is_auto_fallback BOOLEAN DEFAULT FALSE,
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP,
    FOREIGN KEY (course_unit_id) REFERENCES course_units(id) ON DELETE CASCADE
);

CREATE TABLE academic_trips (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    destination VARCHAR(255),
    cohort VARCHAR(100),
    start_date DATE,
    end_date DATE,
    department VARCHAR(255),
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    description TEXT,
    number_of_students INTEGER,
    coordinator VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE equipment (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    type VARCHAR(100),
    serial_number VARCHAR(100),
    home_department VARCHAR(255),
    current_venue VARCHAR(255),
    status VARCHAR(50) DEFAULT 'AVAILABLE',
    qr_code VARCHAR(255),
    description TEXT,
    last_maintenance TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    title VARCHAR(255),
    message TEXT,
    type VARCHAR(50),
    is_read BOOLEAN DEFAULT FALSE,
    action_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
