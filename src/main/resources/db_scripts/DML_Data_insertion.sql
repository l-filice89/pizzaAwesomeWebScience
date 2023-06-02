INSERT INTO pizzas(name, ingredients, price)
VALUES ('pepperoni', 'pepperoni, cheese, tomato sauce', 12.50),
       ('margherita', 'cheese, tomato sauce', 10.00),
       ('hawaiian', 'ham, cheese, pineapple, tomato sauce', 13.50),
       ('vegetarian', 'cheese, tomato sauce, mushrooms, peppers, onions', 12.00),
       ('meat feast', 'pepperoni, ham, chicken, cheese, tomato sauce', 14.00),
       ('four seasons', 'pepperoni, ham, mushrooms, peppers, onions, cheese, tomato sauce', 14.00),
       ('garlic bread', 'garlic butter, cheese, tomato sauce', 8.00),
       ('garlic bread with cheese', 'garlic butter, cheese, tomato sauce', 9.00);

INSERT INTO status
VALUES ('ordered'),
        ('preparing'),
        ('baking'),
        ('boxing'),
        ('delivering'),
        ('delivered'),
        ('cancelled');