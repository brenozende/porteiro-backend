package com.unicamp.porteirobackend.service.impl;

import com.unicamp.porteirobackend.constants.Constants;
import com.unicamp.porteirobackend.dto.BookingDTO;
import com.unicamp.porteirobackend.dto.UserDTO;
import com.unicamp.porteirobackend.dto.request.RegisterForm;
import com.unicamp.porteirobackend.entity.*;
import com.unicamp.porteirobackend.enums.EUserRole;
import com.unicamp.porteirobackend.exception.BookingCreationException;
import com.unicamp.porteirobackend.exception.BookingUpdateException;
import com.unicamp.porteirobackend.repository.*;
import com.unicamp.porteirobackend.security.services.UserDetailsImpl;
import com.unicamp.porteirobackend.service.PorteiroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PorteiroServiceImpl implements PorteiroService {
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
        verifyBookingDates(requestStartDate, requestEndDate, place);

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

    private void verifyBookingDates(Date requestStartDate, Date requestEndDate, Place place) {
        // Check if period is correct:
        if (!requestStartDate.before(requestEndDate))
            throw new BookingCreationException("Booking start date must be before end date");
        if (timeGreaterThan(requestStartDate, requestEndDate, Constants.BOOKING_MAX_HOURS))
            throw new BookingCreationException("Booking time must not exceed six hours");

        // Check if exists another booking for the desired place
        if (requestStartDate.after(place.getFreeUntil()))
            throw new BookingCreationException("Booking start time must be before the place's free end period");
        if (requestStartDate.before(place.getFreeFrom()))
            throw new BookingCreationException("Booking start time must be after the place's free start period");
        if (requestEndDate.after(place.getFreeFrom()))
            throw new BookingCreationException("Booking end time must be after the place's free end period");
        if (requestEndDate.before(place.getFreeUntil()))
            throw new BookingCreationException("Booking end time must be before the place's free start period");
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
}
