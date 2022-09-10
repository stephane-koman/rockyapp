INSERT INTO roles(name, created_at)
VALUES ('ADMIN', current_timestamp);

INSERT INTO roles_permissions(role_id, permission_id)
(SELECT 1, id FROM permissions) ;

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1);