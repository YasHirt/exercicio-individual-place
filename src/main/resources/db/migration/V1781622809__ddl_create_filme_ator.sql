CREATE TABLE filme_ator (
filme_id BIGINT NOT NULL,
ator_id BIGINT NOT NULL,
PRIMARY KEY (filme_id, ator_id),
FOREIGN KEY (filme_id) REFERENCES filme(id) ON DELETE CASCADE,
FOREIGN KEY (ator_id) REFERENCES ator(id) ON DELETE CASCADE
);