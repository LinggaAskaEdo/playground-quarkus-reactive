-- Cart
insert into product(title, description, created_at, updated_at) values ('Product1', 'description', NOW(), NOW());
insert into product(title, description, created_at, updated_at) values ('Product2', 'description', NOW(), NOW());
insert into product(title, description, created_at, updated_at) values ('Product3','description', NOW(), NOW());
insert into product(title, description, created_at, updated_at) values ('Product4', 'description', NOW(), NOW());

insert into shopping_cart(cart_total, name) values (0, 'MyCart');

-- Book
insert into author (author_name) values ('Neil Gaiman');
insert into book (book_name, author_id) values ('Libro 1', 1);
insert into book (book_name, author_id) values ('Libro 2', 1);

-- Movie
insert into movie(title, director, releaseDate) values ('Reservoir Dogs', 'Quentin Tarantino', '1993-02-26');
insert into movie(title, director, releaseDate) values ('Pulp Fiction', 'Quentin Tarantino', '1994-11-25');
insert into movie(title, director, releaseDate) values ('The Mask', 'Chuck Russel', '1994-12-25');

insert into actor(name) values ('Quentin Tarantino');
insert into actor(name) values ('John Travolta');
insert into actor(name) values ('Samuel L Jackson');
insert into actor(name) values ('Jim Carrey');

insert into actors_movies(movie_id, actor_id) values (2, 1);
insert into actors_movies(movie_id, actor_id) values (2, 2);
insert into actors_movies(movie_id, actor_id) values (2, 3);
insert into actors_movies(movie_id, actor_id) values (1, 2);
insert into actors_movies(movie_id, actor_id) values (3, 4);