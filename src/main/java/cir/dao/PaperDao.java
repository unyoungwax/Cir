package cir.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import cir.entity.Paper;

@Repository
public interface PaperDao extends PagingAndSortingRepository<Paper, Long> {

	Paper findByTitle(String title);

	@Override
	@SuppressWarnings("unchecked")
	Paper save(Paper paper);
}
