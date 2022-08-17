package com.speakeasy.repository.perfume;

import com.speakeasy.domain.perfume.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
