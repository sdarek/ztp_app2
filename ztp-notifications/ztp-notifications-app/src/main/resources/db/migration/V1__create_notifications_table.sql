CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    content VARCHAR(1000) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    recipient VARCHAR(255) NOT NULL,
    recipient_timezone VARCHAR(100) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    planned_send_at TIMESTAMP WITH TIME ZONE NOT NULL,
    retry_count INTEGER NOT NULL
);
