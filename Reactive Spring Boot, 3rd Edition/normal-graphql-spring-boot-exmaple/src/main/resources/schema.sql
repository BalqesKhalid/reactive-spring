CREATE TABLE authors(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255),
	age INT
);
	
CREATE TABLE books(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255),
	pages INT,
	author_id INT,
	CONSTRAINT fk_books_authors FOREIGN KEY(author_id) REFERENCES authors(id)
);
