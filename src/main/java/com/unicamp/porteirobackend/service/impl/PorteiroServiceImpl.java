package com.unicamp.porteirobackend.service.impl;

import com.unicamp.porteirobackend.constants.Constants;
import com.unicamp.porteirobackend.dto.BookingDTO;
import com.unicamp.porteirobackend.dto.UserDTO;
import com.unicamp.porteirobackend.dto.VisitDTO;
import com.unicamp.porteirobackend.dto.VisitorDTO;
import com.unicamp.porteirobackend.dto.request.RegisterForm;
import com.unicamp.porteirobackend.entity.*;
import com.unicamp.porteirobackend.enums.EUserRole;
import com.unicamp.porteirobackend.enums.EVisitStatus;
import com.unicamp.porteirobackend.exception.BookingCreationException;
import com.unicamp.porteirobackend.exception.BookingUpdateException;
import com.unicamp.porteirobackend.repository.*;
import com.unicamp.porteirobackend.security.services.UserDetailsImpl;
import com.unicamp.porteirobackend.service.PorteiroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PorteiroServiceImpl implements PorteiroService {
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ResidentRepository residentRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Resident registerResident(RegisterForm form){
        if (form == null)
            return null;
        Resident resident = new Resident();
        resident.setName(form.getName());
        resident.setDocument(form.getDocument());
        resident.setPhoneNumber(form.getPhoneNumber());
        resident.setCreatedAt(new Date());
        resident.setUpdatedAt(new Date());
        resident.setOwner(form.isOwner());

        Set<Role> roles = new HashSet<>();
        roles.add(new Role(EUserRole.RES));

        User user = new User();
        user.setEmail(form.getEmail());
        user.setRoles(roles);
        //TODO: gerar username e senha p/ morador novo
        user.setUsername(form.getEmail());
        user.setPassword("123456");
        resident.setUser(user);

        Set<Visitor> visitors = new HashSet<>();
        form.getVisitors().forEach(v -> {
            Visitor visitor = new Visitor(v, resident);
            visitors.add(visitor);
        });
        resident.setVisitors(visitors);

        Address mailingAddress = new Address(form.getMailingAddress());
        resident.setMailAddress(mailingAddress);

        Apartment apartment = apartmentRepository.findByNumber(form.getApartment());
        if (apartment != null)
            resident.setApartments(Set.of(apartment));

        Set<EmergencyContact> emergencyContacts = new HashSet<>();
        form.getEmergencyContacts().forEach(e -> {
            EmergencyContact emergencyContact = new EmergencyContact(e, resident);
            emergencyContacts.add(emergencyContact);
        });
        resident.setEmergencyContacts(emergencyContacts);

        return resident;
    }

    @Override
    public Resident addVisitors(Integer residentId, List<Visitor> visitors) {
        Optional<Resident> residentOptional = residentRepository.findById(residentId);
        if (residentOptional.isEmpty())
            return null;
        Resident resident = residentOptional.get();
        for (Visitor v : visitors) {
            resident.getVisitors().add(v);
        }
        return resident;
    }

    @Override
    public List<Visitor> findVisitorsByResident(Integer residentId) {

        Optional<Resident> residentOptional = residentRepository.findById(residentId);
        if (residentOptional.isEmpty())
            return new ArrayList<>();
        Resident resident = residentOptional.get();
        return resident.getVisitors().stream().toList();
    }

    @Override
    public List<UserDTO> findAllUsers(){
        List<UserDTO> usersDTO = new ArrayList<>();
        List<User> users = userRepository.findAll();
        users.forEach(u -> {
            UserDTO dto = new UserDTO();
            dto.setId(u.getId());
            dto.setUsername(u.getUsername());
            dto.setEmail(u.getEmail());
            usersDTO.add(dto);
        });
        return usersDTO;
    }

    @Override
    public UserDTO getUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            return null;
        UserDTO dto = new UserDTO();
        dto.setId(userOptional.get().getId());
        dto.setUsername(userOptional.get().getUsername());
        dto.setEmail(userOptional.get().getEmail());
        return dto;
    }

    @Override
    public User getUser(UserDetailsImpl userDetails) {
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        return user.orElse(null);
    }

    @Override
    public boolean deleteUser(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            return false;
        userRepository.delete(userOptional.get());
        return true;
    }

    @Override
    public BookingDTO createBooking(BookingDTO bookingRequest, User user) {
        Date requestStartDate = bookingRequest.getReservedFrom();
        Date requestEndDate = bookingRequest.getReservedUntil();

        Place place = verifyPlaceForBooking(bookingRequest.getPlace().getId());
        Booking existingBookingForPlace = bookingRepository.findByPlace(place);
        if (existingBookingForPlace != null) {
            verifyBookingDates(requestStartDate, requestEndDate, existingBookingForPlace);
        }

        // Fetch resident by logged user
        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new BookingCreationException("Resident not found for user");

        Booking booking = new Booking();
        booking.setResident(resident);
        booking.setPlace(place);
        booking.setReservationFrom(bookingRequest.getReservedFrom());
        booking.setReservationTo(bookingRequest.getReservedUntil());
        bookingRepository.save(booking);

        return new BookingDTO(booking);
    }

    private Place verifyPlaceForBooking(Integer placeId) {
        // Check if desired place exists:
        Optional<Place> placeOptional = placeRepository.findById(placeId);
        if (placeOptional.isEmpty())
            throw new BookingCreationException("Desired place doesn't exist");

        // Check if desired place is available
        Place place = placeOptional.get();
        if (place.getBlocked())
            throw new BookingCreationException("Desired place is not available");
        return place;
    }

    private void verifyBookingDates(Date requestStartDate, Date requestEndDate, Booking existingBooking) {
        // Check if period is correct:
        if (!requestStartDate.before(requestEndDate))
            throw new BookingCreationException("Booking start date must be before end date");
        if (timeGreaterThan(requestStartDate, requestEndDate, Constants.BOOKING_MAX_HOURS))
            throw new BookingCreationException("Booking time must not exceed six hours");

        // Check if exists another booking for the desired place in the same period
        if (existingBooking.getReservationFrom() == null || existingBooking.getReservationTo() == null)
            return;

        if (existingBooking.getReservationTo().after(requestEndDate)
                || existingBooking.getReservationTo().after(requestStartDate)
                || existingBooking.getReservationFrom().before(requestStartDate)
                || existingBooking.getReservationFrom().before(requestEndDate))
            throw new BookingCreationException("Place is not available for chosen date");

    }
    @Override
    public BookingDTO updateBooking(Integer bookingId, BookingDTO bookingRequest, User user) {
        Date startDate = bookingRequest.getReservedFrom();
        Date endDate = bookingRequest.getReservedUntil();

        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        if (existingBooking.isEmpty())
            throw new BookingUpdateException("Booking with informed id doesn't exist: " + bookingId);

        Booking booking = existingBooking.get();
        if (!booking.getResident().getUser().equals(user))
            throw new BookingUpdateException("Cannot update another user's booking");

        booking.setReservationFrom(startDate);
        booking.setReservationTo(endDate);
        bookingRepository.save(booking);
        return new BookingDTO(booking);
    }

    private boolean timeGreaterThan(Date reservedFrom, Date reservedUntil, int maxHours) {
        long startTimeMillis = reservedFrom.getTime();
        long endTimeMillis = reservedUntil.getTime();
        long time = endTimeMillis - startTimeMillis;
        return (time*1000) / (60 * 60) > maxHours;
    }

    @Override
    public void deleteBooking(Integer bookingId, User user) {

        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        if (existingBooking.isEmpty())
            throw new BookingUpdateException("Booking with informed id doesn't exist: " + bookingId);

        Booking booking = existingBooking.get();
        if (!booking.getResident().getUser().equals(user) || !canUserDelete(user))
            throw new BookingUpdateException("Cannot delete another user's booking");

        bookingRepository.delete(booking);
    }

    private boolean canUserDelete(User user) {
        Set<Role> roles = user.getRoles();
        return roles.stream().anyMatch(r -> r.getName().equals(EUserRole.ADM)) ||
                roles.stream().anyMatch(r -> r.getName().equals(EUserRole.CON));
    }

    @Override
    public List<VisitDTO> getVisitsForUser(User user) {
        Set<Role> roles = user.getRoles();
        List<Visit> visits;
        List<VisitDTO> visitsDTO = new ArrayList<>();
        if (roles.stream().anyMatch(r -> r.getName().equals(EUserRole.ADM))){
            visits = visitRepository.findAll();
            for (Visit v : visits) {
                VisitDTO dto = new VisitDTO(v);
                visitsDTO.add(dto);
            }
        } else {
            Resident resident = residentRepository.findByUser_Id(user.getId());
            if (resident == null)
                return new ArrayList<>(); // Mudar para erro de permissão.
            Set<Visitor> visitors = resident.getVisitors();
            // Buscar visitas pending que deem match com a lista de visitors
            visits = visitRepository.findByVisitStatusAndVisitorIn(EVisitStatus.PENDING, visitors);
            for (Visit v : visits) {
                VisitDTO dto = new VisitDTO(v);
                visitsDTO.add(dto);
            }
        }
        return visitsDTO;
    }

    public VisitDTO getVisitById(Integer id, User user) {
        Set<Role> roles = user.getRoles();
        if (roles.stream().anyMatch(r -> r.getName().equals(EUserRole.ADM))) {
            Optional<Visit> visit = visitRepository.findById(id);
            return visit.map(VisitDTO::new).orElse(null);
        }

        Resident resident = residentRepository.findByUser_Id(user.getId());
        Optional<Visit> visitOptional = visitRepository.findById(id);
        if (visitOptional.isEmpty())
            return null;
        if (resident.getVisitors().contains(visitOptional.get().getVisitor()))
            return new VisitDTO(visitOptional.get());

        return null;
    }

    public VisitDTO createVisit(VisitDTO visitRequest, User user){
        Visit visit = new Visit();
        Optional<Visitor> visitor = visitorRepository.findById(visitRequest.getVisitor().getId());
        if (visitor.isEmpty())
            throw new BookingCreationException("The informed visitor is not registered");

        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new BookingCreationException("Current user is not a resident");

        if (!visitor.get().getResident().equals(resident))
            throw new BookingCreationException("Visitor not registered for this resident");

        visit.setVisitor(visitor.get());
        visit.setCreatedAt(new Date());
        visit.setUpdatedAt(null);
        visit.setVisitStatus(EVisitStatus.PENDING);
        visitRepository.save(visit);
        return new VisitDTO(visit);
    }

    public VisitDTO updateVisit(Integer id, VisitDTO visitRequest, User user) {
        Optional<Visit> visitOptional = visitRepository.findById(id);

        if (visitOptional.isEmpty())
            throw new BookingUpdateException("Visit with informed id not found: " + id);

        Visit visit = visitOptional.get();

        Optional<Visitor> visitor = visitorRepository.findById(visitRequest.getVisitor().getId());
        if (visitor.isPresent()){
            Resident resident = residentRepository.findByUser_Id(user.getId());
            if (resident == null)
                throw new BookingCreationException("Current user is not a resident.");
            if (!visitor.get().getResident().equals(resident))
                throw new BookingCreationException("Cannot update other resident's visit.");
            visit.setVisitor(visitor.get());
        }

        visit.setVisitStatus(visitRequest.getVisitStatus());
        visit.setUpdatedAt(new Date());
        visitRepository.save(visit);
        return new VisitDTO(visit);
    }

    public void deleteVisit(Integer id, User user){
        Optional<Visit> visitOptional = visitRepository.findById(id);

        if (visitOptional.isEmpty())
            throw new BookingUpdateException("Visit with informed id not found: " + id);

        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new BookingCreationException("Current user is not a resident.");

        Visit visit = visitOptional.get();
        if (!visit.getVisitor().getResident().equals(resident))
            throw new BookingCreationException("Cannot delete other resident's visit.");

        visitRepository.delete(visit);
    }

    public List<VisitDTO> findVisitByStatus(EVisitStatus status, User user) {
        if (user.getRoles().stream().anyMatch(r -> r.getName().equals(EUserRole.ADM))
                || user.getRoles().stream().anyMatch(r -> r.getName().equals(EUserRole.CON)))
            return visitRepository.findByVisitStatus(status);

        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new BookingCreationException("Current user is not a resident.");

        return visitRepository.findByVisitStatusAndVisitor_Resident(status, resident);
    }

}
