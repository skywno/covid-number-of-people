package com.example.covid.repository;

import com.example.covid.domain.AdminLocationMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLocationRepository extends JpaRepository <AdminLocationMap, Long>{
}
