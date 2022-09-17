package com.speakeasy.repository.perfume;

import com.speakeasy.domain.perfume.PerfumeScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumeScoreRepository extends JpaRepository<PerfumeScore, Long> {
}
