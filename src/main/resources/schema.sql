-- Use the 'rest-api' database
\c rest-api;

-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS task_management_system;

-- Set the schema search path
SET search_path TO task_management_system;

-- Creating the 'users' table
CREATE TABLE task_management_system.users (
    id UUID NOT NULL,
    full_name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    "password" VARCHAR NOT NULL,
    "role" VARCHAR NOT NULL,
    CONSTRAINT email_password_unique UNIQUE (email, "password"),
    CONSTRAINT user_pk PRIMARY KEY (id)
);

-- Creating the 'tasks' table
CREATE TABLE task_management_system.tasks (
    id UUID NOT NULL,
    title VARCHAR NOT NULL,
    description VARCHAR NULL,
    status VARCHAR NOT NULL,
    priority VARCHAR NOT NULL,
    id_creator UUID NOT NULL,
    id_executor UUID NOT NULL,
    CONSTRAINT task_pk PRIMARY KEY (id)
);

-- Adding foreign key constraints to 'tasks' table
ALTER TABLE task_management_system.tasks
    ADD CONSTRAINT task_creator_fk FOREIGN KEY (id_creator) REFERENCES task_management_system.users(id);

ALTER TABLE task_management_system.tasks
    ADD CONSTRAINT task_executor_fk FOREIGN KEY (id_executor) REFERENCES task_management_system.users(id);

-- Creating the 'task_comment' table
CREATE TABLE task_management_system.task_comment (
    id UUID NOT NULL,
    description VARCHAR NOT NULL,
    id_author UUID NOT NULL,
    id_task UUID NOT NULL,
    CONSTRAINT comment_pk PRIMARY KEY (id)
);

-- Adding foreign key constraint to 'task_comment' table
ALTER TABLE task_management_system.task_comment
    ADD CONSTRAINT comment_task_fk FOREIGN KEY (id_task) REFERENCES task_management_system.tasks(id);






-- INSERT

INSERT INTO task_management_system.users (id, full_name, email, "password", "role") VALUES
('ecf72b35-4151-4439-a5a1-408d2ce330c5', 'John Doe', 'john.doe@example.com', 'password123', 'ADMIN'),
('a88589c6-0f3a-47fc-8a43-78f9f9bb78ff', 'Jane Smith', 'jane.smith@example.com', 'password456', 'USER'),
('2658929f-d34c-4f4c-96be-1c5653297406', 'Alice Johnson', 'alice.johnson@example.com', 'password789', 'USER'),
('aa1fca78-b7a7-4235-8355-fe7937a3e4cd', 'Bob Brown', 'bob.brown@example.com', 'password321', 'ADMIN'),
('892a0f4d-3615-43fd-b3d2-90171fac84df', 'Charlie Davis', 'charlie.davis@example.com', 'password654', 'USER');

INSERT INTO task_management_system.tasks (id, title, description, status, priority, id_creator, id_executor) VALUES
('0f55f5d5-62dd-4575-9fbc-ec54587b4c6b', 'Task 2', 'Description of Task 2', 'in progress', 'mid', 'ecf72b35-4151-4439-a5a1-408d2ce330c5', 'a88589c6-0f3a-47fc-8a43-78f9f9bb78ff'),
('92492d18-c388-4184-8785-cc1bb5f4ca09', 'Task 3', 'Description of Task 3', 'completed', 'low', 'ecf72b35-4151-4439-a5a1-408d2ce330c5', '2658929f-d34c-4f4c-96be-1c5653297406'),
('b7509aed-ecf6-4e10-bf02-8c39854cf0f3', 'Task 4', 'Description of Task 4', 'pending', 'high', 'aa1fca78-b7a7-4235-8355-fe7937a3e4cd', '2658929f-d34c-4f4c-96be-1c5653297406'),
('c35d78d6-6423-4d57-bb77-f26e5813aa15', 'Task 1', 'Description of Task 1', 'pending', 'high', 'ecf72b35-4151-4439-a5a1-408d2ce330c5', 'a88589c6-0f3a-47fc-8a43-78f9f9bb78ff'),
('3df93891-5afd-4c34-82d4-5e7b881da16c', 'Task 5', 'Description of Task 5', 'in progress', 'mid', 'aa1fca78-b7a7-4235-8355-fe7937a3e4cd', '892a0f4d-3615-43fd-b3d2-90171fac84df');

INSERT INTO task_management_system.task_comment (id, description, id_author, id_task) VALUES
('06d469ba-2950-426b-96f4-85ca3256c769', 'This is the first comment.', 'a88589c6-0f3a-47fc-8a43-78f9f9bb78ff', '0f55f5d5-62dd-4575-9fbc-ec54587b4c6b'),
('052b00b8-8b4b-4fc4-8eea-80a60d6591ca', 'This is the second comment.', '2658929f-d34c-4f4c-96be-1c5653297406', 'b7509aed-ecf6-4e10-bf02-8c39854cf0f3'),
('58c2a6e9-f093-461b-a867-c90fae960866', 'This is the third comment.', '2658929f-d34c-4f4c-96be-1c5653297406', 'b7509aed-ecf6-4e10-bf02-8c39854cf0f3'),
('5f596092-1225-4061-b66a-6f87a0a5dee7', 'test comment to the 4th task from admin', 'aa1fca78-b7a7-4235-8355-fe7937a3e4cd', '0f55f5d5-62dd-4575-9fbc-ec54587b4c6b'),
('130a78c8-8980-40a2-9687-6a718eee225a', 'test comment from executor', 'a88589c6-0f3a-47fc-8a43-78f9f9bb78ff', '0f55f5d5-62dd-4575-9fbc-ec54587b4c6b');