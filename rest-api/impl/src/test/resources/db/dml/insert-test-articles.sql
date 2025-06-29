INSERT INTO articles (title, content, author, published_at)
VALUES
    ('Как стать программистом', '10 шагов для начала карьеры в IT.', 'John Doe', '2024-01-15 09:30:00'),
    ('Путешествие в Японию', 'Советы по бюджетному путешествию.', 'Alice Smith', '2024-02-20 14:15:00'),
    ('Рецепт идеального борща', 'Секреты украинской кухни.', 'Мария Иванова', '2024-03-05 18:45:00'),
    ('Квантовые вычисления', 'Основы для начинающих.', 'Dr. Quantum', '2024-04-10 10:00:00'),
    ('Искусство минимализма', 'Как избавиться от лишнего.', 'Anna Green', '2024-05-01 12:00:00'),
    ('Очень длинный заголовок статьи, который проверяет ограничения поля title ' ||
     'и демонстрирует, как система обрабатывает большие объемы текста.',
     'Содержание статьи с большим количеством символов... (500+ символов) ' ||
     'Этот текст имитирует реальные данные и помогает проверить работу приложения с длинными строками.',
     'Long Text Tester', '2024-06-01 20:00:00'),
    ('Тест спецсимволов &%$#@!', 'Проверка: <script>alert("XSS")</script> & SQL-инъекции;', 'Security Expert', '2024-07-12 00:00:00');
