package com.skyapi.service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skyapi.common.Location;
import com.skyapi.service.exception.LocationNotFoundException;
import com.skyapi.service.repository.LocationRepository;

@Service
@Transactional
public class LocationService {
	private LocationRepository repo;

	public LocationService(LocationRepository repo) {
		super();
		this.repo = repo;
	}
	
	public Location add(Location location) {
		return repo.save(location);
	}
	
	public List<Location> list() {
		return repo.findUntrashed();
	}
	
	public Location get(String code) {
		return repo.findByCode(code);
	}
	
	public Location update(Location locationInRequest) throws LocationNotFoundException {
		String code = locationInRequest.getCode();
		
		Location locationInDB = repo.findByCode(code);
		
		if (locationInDB == null) {
			throw new LocationNotFoundException("No location found with the given code: " + code);
		}
		
		locationInDB.setCityName(locationInRequest.getCityName());
		locationInDB.setRegoinName(locationInRequest.getRegoinName());
		locationInDB.setCountryCode(locationInRequest.getCountryCode());
		locationInDB.setCountryName(locationInRequest.getCountryName());
		locationInDB.setEnabled(locationInRequest.isEnabled());
	
		return repo.save(locationInDB);
	}
	
	public void delete(String code) throws LocationNotFoundException { 
		Location location = repo.findByCode(code);
		
		if (location == null) {
			throw new LocationNotFoundException("");
		}
		
		repo.trashByCode(code);
	}
	
}
