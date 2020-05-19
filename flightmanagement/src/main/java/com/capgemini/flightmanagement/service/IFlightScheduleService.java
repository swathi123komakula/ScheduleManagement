package com.capgemini.flightmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.capgemini.flightmanagement.entity.Airport;
import com.capgemini.flightmanagement.entity.FlightSchedule;




public interface IFlightScheduleService {
	
	public void scheduleFlight(FlightSchedule flightSchedule);
	
	public Optional<FlightSchedule> viewScheduledFlights(int id);
	
	public List<FlightSchedule> viewScheduledFlights(Airport arrival,Airport destination,LocalDate date);
	
	public void deleteScheduledFlight(int id);
	
	public void modifyScheduledFlight(FlightSchedule flightSchedule);
	
	public List<FlightSchedule> viewScheduledFlights();
	
	public String validateScheduledFlight(FlightSchedule flightSchedule);

}

