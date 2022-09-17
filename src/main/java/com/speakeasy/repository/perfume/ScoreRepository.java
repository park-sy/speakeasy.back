package com.speakeasy.repository.perfume;

import com.speakeasy.domain.perfume.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
