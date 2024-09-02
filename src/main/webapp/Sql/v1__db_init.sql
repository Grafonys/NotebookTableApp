drop table if exists notebooks_t;

create table notebooks_t (
                           ID serial primary key,
                           Brand varchar(50) NOT NULL,
                           Name varchar(20) NOT NULL,
                           PageAmount int NOT NULL CHECK (PageAmount > 4),
                           Cover varchar(20) NOT NULL CHECK (Cover = 'soft' OR Cover = 'hard'),
                           Country varchar(50) NOT NULL,
                           PageType varchar(20) NOT NULL CHECK (PageType = 'lined' OR PageType = 'squared' OR PageType = 'bold')
);

insert into notebooks_t (Brand, Name, PageAmount, Cover, Country, PageType)
values
    ('Moleskine', 'Classic', 96, 'soft', 'Italy', 'squared'),
    ('Moleskine', 'Classic', 96, 'hard', 'Italy', 'squared'),
    ('Moleskine', 'Cashier', 48, 'soft', 'Italy', 'lined'),
    ('Moleskine', 'Hello-Kitty', 18, 'soft', 'Italy', 'squared'),
    ('Moleskine', 'ART', 48, 'soft', 'Italy', 'bold'),
    ('Moleskine', 'PRO', 96, 'hard', 'Italy', 'squared'),
    ('Rhodia', 'Rhodiarama', 80, 'hard', 'France', 'bold'),
    ('Rhodia', 'Heritage escher', 32, 'soft', 'France', 'squared'),
    ('Rhodia', 'Business', 90, 'soft', 'France', 'bold'),
    ('Rhodia', 'Rhodiactive', 80, 'soft', 'France', 'squared'),
    ('Rhodia', 'Webnotebook', 96, 'hard', 'France', 'lined'),
    ('Rhodia', 'Heritage moucheture', 32, 'hard', 'France', 'squared'),
    ('Ciak', 'Techno', 96, 'hard', 'Italy', 'squared'),
    ('Ciak', 'Duo', 96, 'hard', 'Italy', 'squared'),
    ('Ciak', 'Eco', 36, 'soft', 'Italy', 'bold'),
    ('Ciak', 'Titan', 96, 'hard', 'Italy', 'squared'),
    ('Blankster', 'Megapolis', 96, 'hard', 'Sweden', 'lined'),
    ('Fenimore', 'Isometric', 24, 'hard', 'GB', 'bold'),
    ('Fenimore', 'Black and Red Lines', 24, 'hard', 'GB', 'squared'),
    ('Zakrtka', 'A5', 24, 'soft', 'Ukraine', 'bold'),
    ('Zakrtka', 'Compact', 12, 'soft', 'Ukraine', 'lined'),
    ('Zakrtka', 'Update', 36, 'soft', 'Ukraine', 'lined');