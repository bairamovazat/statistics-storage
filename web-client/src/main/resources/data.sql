INSERT INTO user_role(role)
  SELECT 'USER' WHERE NOT EXISTS (
      SELECT role FROM user_role WHERE role='USER'
  );

INSERT INTO user_role(role)
  SELECT 'ADMIN' WHERE NOT EXISTS (
      SELECT role FROM user_role WHERE role='ADMIN'
  );

INSERT INTO user_role(role)
  SELECT 'CREATOR' WHERE NOT EXISTS (
      SELECT role FROM user_role WHERE role='CREATOR'
  );