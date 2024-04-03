DELETE FROM books_categories WHERE category_id IN (SELECT id FROM categories);
DELETE FROM categories;