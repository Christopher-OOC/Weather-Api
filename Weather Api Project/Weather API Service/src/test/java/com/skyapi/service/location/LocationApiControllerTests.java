package com.skyapi.service.location;

import java.util.Collections;

import java.util.List;
  
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.common.Location;
import com.skyapi.service.exception.LocationNotFoundException;
import com.skyapi.service.service.LocationService;


@SpringBootTest
@AutoConfigureMockMvc
public class LocationApiControllerTests {
	
	private static final String END_POINT_PATH = "/v1/locations";
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@MockBean
	private LocationService service;
	
	@Test
	public void testAddShouldReturn400BadRequest() {
		Location location = new Location();
		String bodyContent = null;
		
		try {
			bodyContent = mapper.writeValueAsString(location);
			mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest())
				.andDo(print());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testAddShouldReturn201Created() throws Exception {
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegoinName("New York");
		location.setCountryCode("US");
		location.setCountryName("United State of America");
		location.setEnabled(true);
		
		Mockito.when(service.add(location)).thenReturn(location);
	
		String bodyContent = mapper.writeValueAsString(location);
		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
			.andExpect(status().isCreated())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code", is("NYC_USA")))
			.andDo(print()); 
	}
	
	@Test
	public void testListShouldReturn204NoContent() throws Exception {
		Mockito.when(service.list()).thenReturn(Collections.emptyList());
		
		System.out.println(service.list());
		
		mockMvc.perform(get(END_POINT_PATH))
			.andExpect(status().isNoContent())
			.andDo(print());
	}
	
	@Test
	public void testListShouldReturn200Ok() throws Exception {
		Location location1 = new Location();
		location1.setCode("NYC_USA");
		location1.setCityName("New York City");
		location1.setRegoinName("New York");
		location1.setCountryCode("US");
		location1.setCountryName("United State of America");
		location1.setEnabled(true);
		
		Location location2 = new Location();
		location2.setCode("NYC_USA1");
		location2.setCityName("New York City1");
		location2.setRegoinName("New York1");
		location2.setCountryCode("US1");
		location2.setCountryName("United State of America");
		location2.setEnabled(true);
		
		Mockito.when(service.list()).thenReturn(List.of(location1, location2));
		
		mockMvc.perform(get(END_POINT_PATH).contentType("application/json"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$[0].code", is("NYC_USA")))
			.andExpect(jsonPath("$[0].country_code", is("US")))
			.andExpect(jsonPath("$[1].code", is("NYC_USA1")))
			.andExpect(jsonPath("$[1].country_code", is("US1")))
			.andDo(print()); 
	}
	
	@Test
	public void testShouldReturn405MethodNotAllowed() throws Exception {
		String requestURI = END_POINT_PATH + "/ABCDEF";
		
		mockMvc.perform(post(requestURI))
			.andExpect(status().isMethodNotAllowed())
			.andDo(print());
		
	}
	
	@Test
	public void testShouldReturn404NotFound() throws Exception {
		String requestURI = END_POINT_PATH + "/ABCDEF";
		
		mockMvc.perform(get(requestURI))
			.andExpect(status().isNotFound())
			.andDo(print());
		
	}
	
	@Test
	public void testShouldReturn200Ok() throws Exception {
		String code = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + code;
		
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegoinName("New York");
		location.setCountryCode("US");
		location.setCountryName("United State of America");
		location.setEnabled(true);
		
		Mockito.when(service.get(code)).thenReturn(location);
		
		mockMvc.perform(get(requestURI))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code", is("NYC_USA")))
			.andDo(print());
	}
	
	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegoinName("New York");
		location.setCountryCode("US");
		location.setCountryName("United State of America");
		location.setEnabled(true);
		
		Mockito.when(service.update(location)).thenThrow(new LocationNotFoundException("No Location Found!"));
		
		String bodyContent = mapper.writeValueAsString(location);
		
		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
			.andExpect(status().isNotFound())
			.andDo(print());
	
	}
	
	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {
		Location location = new Location();
		location.setCityName("New York City");
		location.setRegoinName("New York");
		location.setCountryCode("US");
		location.setCountryName("United State of America");
		location.setEnabled(true);
		
		Mockito.when(service.update(location)).thenReturn(location);
		
		String bodyContent = mapper.writeValueAsString(location);
		
		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
	
	@Test
	public void testUpdateShouldReturn200Ok() throws Exception {
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegoinName("New York");
		location.setCountryCode("US");
		location.setCountryName("United State of America");
		location.setEnabled(true);
		
		Mockito.when(service.update(location)).thenReturn(location);
	
		String bodyContent = mapper.writeValueAsString(location);
		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.code", is("NYC_USA")))
			.andDo(print()); 
	}
	
	@Test
	public void testDeleteShouldReturn404NotFound() throws Exception {
		String code = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + code;
		
		Mockito.doThrow(LocationNotFoundException.class).when(service).delete(code);
		
		mockMvc.perform(delete(requestURI))
			.andExpect(status().isNotFound())
			.andDo(print());
	}
	
	@Test
	public void testDeleteShouldReturn204NoContent() throws Exception {
		String code = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + code;
		
		Mockito.doNothing().when(service).delete(code);
		
		mockMvc.perform(delete(requestURI))
			.andExpect(status().isNoContent())
			.andDo(print());
	}

}
