package com.speakeasy.repository.perfume;

import com.speakeasy.domain.perfume.PerfumeVote;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumeVoteRepository extends JpaRepository<PerfumeVote, Long>,PerfumeVoteRepositoryCustom {

}
