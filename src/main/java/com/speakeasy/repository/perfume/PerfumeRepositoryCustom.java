package com.speakeasy.repository.perfume;

import com.speakeasy.domain.perfume.Perfume;
import com.speakeasy.request.perfume.PerfumeSearch;
import com.speakeasy.response.perfume.PerfumeResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PerfumeRepositoryCustom {


    List<PerfumeResponse> getList(PerfumeSearch perfumeSearch);

    void updateView(Long perfumeId);


    @Transactional
    void updatePoint(Long perfumeId);
}
