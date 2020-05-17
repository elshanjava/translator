package com.example.translator.repository;

import com.example.translator.model.TranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepo extends JpaRepository<TranslationEntity, Long> {
}
