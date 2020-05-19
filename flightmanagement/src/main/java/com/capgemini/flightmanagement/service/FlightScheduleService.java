package com.capgemini.flightmanagement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.flightmanagement.entity.Airport;
import com.capgemini.flightmanagement.entity.Flight;
import com.capgemini.flightmanagement.entity.FlightSchedule;
import com.capgemini.flightmanagement.entity.Schedule;
import com.capgemini.flightmanagement.repository.IAirportRepository;
import com.capgemini.flightmanagement.repository.IFlightRepository;
import com.capgemini.flightmanagement.repository.IFlightScheduleRepository;
import com.capgemini.flightmanagement.repository.IScheduleRepository;



@Service
public class FlightScheduleService implements IFlightScheduleService{
	
	private static final Logger log=LoggerFactory.getLogger(FlightScheduleService.class);
	
	@Autowired
	IFlightScheduleRepository flightScheduleRepository;
	
	@Autowired
	IFlightRepository flightRepository;
	
	@Autowired
	IScheduleRepository scheduleRepository;
	
	@Autowired
	IAirportRepository airportReposidtory;
	
	@Override
	@Transactional
	public void scheduleFlight(FlightSchedule flightSchedule) {
		
		log.debug("Inside scheduleFlight function");
		
		scheduleRepository.save(flightSchedule.getSchedule());
		
		flightScheduleRepository.save(flightSchedule);
	}
	
	@Override
	@Transactional
	public Optional<FlightSchedule> viewScheduledFlights(int id){
		
		log.debug("Inside viewScheduledFlights by id function in FlightScheduleController");
		
		Optional<FlightSchedule> flightScheduleOpt=flightScheduleRepository.findById(id);
		
		return flightScheduleOpt;
	}
	
	@Override
	@Transactional
	public List<FlightSchedule> viewScheduledFlights(Airport arrival, Airport destination, LocalDate date) {
		
		
		
		log.debug("Inside viewScheduledFlights by parameters function in FlightSchedule Service");
		
		List<FlightSchedule> FlightScheduleList=flightScheduleRepository.getFlightScheduleByAirport(arrival, destination);
		
		List<FlightSchedule> FlightsOnScheduleList=new ArrayList<>();
	
		
		for (FlightSchedule flightSchedule : FlightScheduleList) {
			Schedule schedule=flightSchedule.getSchedule();
			if(schedule.getDepartureTime().toLocalDate().equals(date)) {
				FlightsOnScheduleList.add(flightSchedule);
			}
		}
		
		return FlightsOnScheduleList;
		
				
	}
	
	@Override
	@Transactional
	public void deleteScheduledFlight(int id) {
		
		log.debug("Inside deleteScheduledFlight function in FlightScheduleService class");
		
		int scheduleId=flightScheduleRepository.findById(id).get().getSchedule().getScheduleId();
		
		flightScheduleRepository.deleteById(id);
		scheduleRepository.deleteById(scheduleId);
	}
	
	@Override
	@Transactional
	public void modifyScheduledFlight(FlightSchedule flightSchedule) {
		
		log.debug("Inside modifyScheduledFlight function in FlightScheduleService class");
		
		scheduleRepository.save(flightSchedule.getSchedule());
		
		flightScheduleRepository.save(flightSchedule);
		
	}
	
	@Override
	@Transactional
	public List<FlightSchedule> viewScheduledFlights(){
		
		log.debug("Inside viewScheduledFlights function in FlightScheduleService class");
		
		List<FlightSchedule> flightScheduleList=flightScheduleRepository.findAll();
		
		return flightScheduleList;
				
		
	}
	
	@Override
	@Transactional
	public String validateScheduledFlight(FlightSchedule flightSchedule){
		
		int id=flightSchedule.getScheduleFlightId();
		Flight flight=flightSchedule.getFlight();
		Schedule schedule=flightSchedule.getSchedule();
		
		
		Optional<FlightSchedule> FlightScheduleOpt=flightScheduleRepository.findById(id);
		
		if(FlightScheduleOpt.isPresent()) {
			return "Flight Schedule with this ID already exists!!";
		}
		
		
		
		Optional<Flight> FlightOpt=flightRepository.findById(flight.getFlightNumber());
		
		if(!FlightOpt.isPresent()) {
			return "No Flight with flight Number "+flight.getFlightNumber()+" exists!!";
		}
		
		if(("cancelled").equalsIgnoreCase(FlightOpt.get().getStatus())) {
			return "Cancelled Flights cannot be added";
		}
		
		
		
		return "valid data";
		
		
	}
	

}

