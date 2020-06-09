ALTER TABLE category DROP COLUMN parent;
ALTER TABLE category add column parent INTEGER;
ALTER TABLE category add FOREIGN KEY (parent) REFERENCES category(id) ON DELETE CASCADE;