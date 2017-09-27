package cir.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import cir.entity.Author;

@Repository
public interface AuthorDao extends PagingAndSortingRepository<Author, Long>{

	Author findByName(String name);

	@Override
	@SuppressWarnings("unchecked")
	Author save(Author author);
}
