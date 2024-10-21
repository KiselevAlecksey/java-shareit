MERGE INTO users AS target
USING (SELECT 'ivan@example.com' AS email, 'Иван Иванов' AS name UNION ALL
       SELECT 'petr@example.com', 'Петр Петров' UNION ALL
       SELECT 'anna@example.com', 'Анна Сидорова') AS source
ON target.email = source.email
WHEN MATCHED THEN
    UPDATE SET target.name = source.name
WHEN NOT MATCHED THEN
    INSERT (name, email)
    VALUES (source.name, source.email);

MERGE INTO items AS target
USING (VALUES
    ('Книги', 'Сборник рассказов', 1, TRUE, 1),
    ('Ноутбук', 'MacBook Pro', 2, FALSE, NULL),
    ('Велосипед', 'Горный велосипед', 3, TRUE, 3)
) AS source (name, description, owner_id, available, request_id)
ON target.name = source.name
WHEN MATCHED THEN
    UPDATE SET description = source.description,
               owner_id = source.owner_id,
               available = source.available,
               request_id = source.request_id
WHEN NOT MATCHED THEN
    INSERT (name, description, owner_id, available, request_id)
    VALUES (source.name, source.description, source.owner_id, source.available, source.request_id);

MERGE INTO bookings AS target
USING (VALUES
    (1, 1, TRUE, 'Бронь книги', 2, '2023-10-01 12:00:00', '2023-10-15 12:00:00', '2023-10-02 12:00:00', 'APPROVED'),
    (2, 2, FALSE, 'Бронь ноутбука', 3, '2023-11-05 08:00:00', '2023-11-20 16:00:00', '2023-11-04 18:00:00', 'WAITING'),
    (3, 3, TRUE, 'Бронь велосипеда', 1, '2023-12-02 09:00:00', '2023-12-07 17:00:00', '2023-12-01 13:00:00', 'REJECTED')
) AS source (owner_id, item_id, available, content, booker_id, start_booking, end_booking, confirm_time, status)
ON target.content = source.content
WHEN MATCHED THEN
    UPDATE SET owner_id = source.owner_id,
               item_id = source.item_id,
               available = source.available,
               booker_id = source.booker_id,
               start_booking = source.start_booking,
               end_booking = source.end_booking,
               confirm_time = source.confirm_time,
               status = source.status
WHEN NOT MATCHED THEN
    INSERT (owner_id, item_id, available, content, booker_id, start_booking, end_booking, confirm_time, status)
    VALUES (source.owner_id, source.item_id,
     source.available, source.content,
     source.booker_id, source.start_booking,
     source.end_booking, source.confirm_time, source.status);

MERGE INTO comments AS target
USING (VALUES
    ('Отличная книга!', 2, 1, '2023-10-16 10:00:00'),
    ('Очень удобный ноутбук.', 3, 2, '2023-11-21 11:00:00'),
    ('Велосипедом доволен, спасибо!', 1, 3, '2023-12-08 12:00:00')
) AS source (content, author_id, item_id, created_date)
ON target.content = source.content
WHEN MATCHED THEN
    UPDATE SET author_id = source.author_id,
               item_id = source.item_id,
               created_date = source.created_date
WHEN NOT MATCHED THEN
    INSERT (content, author_id, item_id, created_date)
    VALUES (source.content, source.author_id, source.item_id, source.created_date);

MERGE INTO item_requests AS target
USING (VALUES
    ('Хочу почитать эту книгу.', 2, 1, '2023-09-29 15:00:00'),
    ('Нужно взять ноутбук на неделю.', 3, 2, '2023-11-03 19:00:00'),
    ('Планирую покататься на выходных.', 1, 3, '2023-11-28 22:00:00')
) AS source (description, requestor_id, item_id, created_date)
ON target.description = source.description
WHEN MATCHED THEN
    UPDATE SET requestor_id = source.requestor_id,
               item_id = source.item_id,
               created_date = source.created_date
WHEN NOT MATCHED THEN
    INSERT (description, requestor_id, item_id, created_date)
    VALUES (source.description, source.requestor_id, source.item_id, source.created_date);