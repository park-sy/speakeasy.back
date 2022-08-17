package com.speakeasy.repository.perfume;

import com.speakeasy.domain.perfume.Perfume;
import com.speakeasy.domain.perfume.PerfumeImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfumeImgRepository extends JpaRepository<PerfumeImages, Long> {
    List<PerfumeImages> findByPerfume(Perfume perfume);
}
