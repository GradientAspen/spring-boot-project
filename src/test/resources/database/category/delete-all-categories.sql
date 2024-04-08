delete from books_categories where category_id in (select id from categories);
delete from categories;
