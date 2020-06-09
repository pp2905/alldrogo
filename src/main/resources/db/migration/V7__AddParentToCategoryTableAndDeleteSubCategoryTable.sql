ALTER TABLE category add parent INTEGER NOT NULL REFERENCES category(id);
ALTER TABLE auction DROP COLUMN sub_category_id;
DROP TABLE sub_category;