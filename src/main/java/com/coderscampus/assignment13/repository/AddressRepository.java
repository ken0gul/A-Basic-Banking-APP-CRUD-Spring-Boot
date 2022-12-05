package com.coderscampus.assignment13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coderscampus.assignment13.domain.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long>{

	
	@Query("select a from Address a where a.userId= :userId")
	Address findAddressById(Long userId);
}
