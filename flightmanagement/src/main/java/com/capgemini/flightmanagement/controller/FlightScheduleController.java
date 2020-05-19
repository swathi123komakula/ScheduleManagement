package com.capgemini.flightmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.flightmanagement.entity.Airport;
import com.capgemini.flightmanagement.entity.Flight;
import com.capgemini.flightmanagement.entity.FlightSchedule;
import com.capgemini.flightmanagement.exception.FlightScheduleNotFoundException;
import com.capgemini.flightmanagement.exception.InvalidFlightScheduleDate;
import com.capgemini.flightmanagement.service.IAirportService;
import com.capgemini.flightmanagement.service.IFlightScheduleService;
import com.capgemini.flightmanagement.service.IFlightService;



@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/flightSchedule")
public class FlightScheduleController {
	
	private static final Logger log=LoggerFactory.getLogger(FlightScheduleController.class);
	
	@Autowired
	private IFlightScheduleService flightScheduleServive;
	
	@Autowired
	private IFlightService flightService;
	
	@Autowired
	private IAirportService airportService;

	@PostMapping(path="/add")
	public String add(@RequestBody FlightSchedule flightSchedule)
	{
		
		log.debug("Inside add method in controller class");
		
		String validate=flightScheduleServive.validateScheduledFlight(flightSchedule);
		
		if("valid data".equals(validate)) {
			
			Optional<Flight> FlightOpt=flightService.viewFlight(flightSchedule.getFlight().getFlightNumber());
			flightSchedule.setAvailableSeats(FlightOpt.get().getSeatCapacity());
			flightScheduleServive.scheduleFlight(flightSchedule);
			validate="Added successfully";
		}
		
		
		return validate;
		
	}
	
	@GetMapping("/viewByAirport")
	public List<FlightSchedule> getFlightOnDate(@RequestParam("source")String source,@RequestParam("destination") String destination,@RequestParam("date")String Date)throws InvalidFlightScheduleDate{
		
		Airport airport1=airportService.viewAirport(source).get();
		Airport airport2=airportService.viewAirport(destination).get();
		LocalDate date=LocalDate.parse(Date);
		
		/*if(date.compareTo(LocalDate.now())<1) {
			throw new InvalidFlightScheduleDate();
		}*/
		
		List<FlightSchedule> FlightScheduleList=flightScheduleServive.viewScheduledFlights(airport1,airport2,date);
		
		return FlightScheduleList;
	}
	@GetMapping("/delete")
	public String delete(@RequestParam("id") int id) throws FlightScheduleNotFoundException {
		
		log.debug("Inside delete method in controller class");
		
		Optional<FlightSchedule> flightScheduleOpt=flightScheduleServive.viewScheduledFlights(id);
		
		if(!flightScheduleOpt.isPresent()) {
			throw new FlightScheduleNotFoundException();
		}
		
		
		flightScheduleServive.deleteScheduledFlight(id);
		
		return "Deleted succesfully";
	}
	
	@PostMapping("/update")
	public String update(@RequestBody FlightSchedule flightSchedule) {
		
		log.debug("Inside update method in controller class");
		
		flightScheduleServive.modifyScheduledFlight(flightSchedule);
		
		return "updated";
		
	}
	@GetMapping("/viewById")
	public FlightSchedule getFlightScheduleById(@RequestParam("id")int id) throws FlightScheduleNotFoundException{
		
		log.debug("Inside getFlightScheduleById in controller class");
		
		Optional<FlightSchedule> flightScheduleOpt=flightScheduleServive.viewScheduledFlights(id);
		
		if(!flightScheduleOpt.isPresent()) {
			throw new FlightScheduleNotFoundException();
		}
		
		FlightSchedule flightSchedule=flightScheduleOpt.get();
		
		return flightSchedule;
	}
	
	@GetMapping("/viewAll")
	public List<FlightSchedule> viewAll(){
		List<FlightSchedule> flightScheduleList=flightScheduleServive.viewScheduledFlights();
		return flightScheduleList;
	}
	
	
}

