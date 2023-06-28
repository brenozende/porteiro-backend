package com.unicamp.porteirobackend.service.impl;

import com.unicamp.porteirobackend.constants.Constants;
import com.unicamp.porteirobackend.dto.*;
import com.unicamp.porteirobackend.dto.request.RegisterForm;
import com.unicamp.porteirobackend.entity.*;
import com.unicamp.porteirobackend.enums.EUserRole;
import com.unicamp.porteirobackend.enums.EVisitStatus;
import com.unicamp.porteirobackend.exception.PorteiroException;
import com.unicamp.porteirobackend.repository.*;
import com.unicamp.porteirobackend.security.services.UserDetailsImpl;
import com.unicamp.porteirobackend.service.PorteiroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PorteiroServiceImpl implements PorteiroService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmergencyContactRepository emergencyContactRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CommunicationsRepository communicationsRepository;
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

    @Autowired
    PasswordEncoder encoder;

    @Override
    public ApartmentDTO createApartment(ApartmentDTO apartmentRequest) {
        Apartment apartment = new Apartment();
        apartment.setBlock(apartmentRequest.getBlock());
        apartment.setFloor(apartmentRequest.getFloor());
        apartment.setRealEstate(apartmentRequest.getRealEstate());
        apartment.setNumber(apartmentRequest.getNumber());

        Optional<Address> addressOptional = addressRepository.findById(apartmentRequest.getAddress().getId());

        if (addressOptional.isPresent())
            apartment.setAddress(addressOptional.get());
        else {
            Address address = new Address(apartmentRequest.getAddress());
            addressRepository.save(address);
        }

        apartmentRepository.save(apartment);
        return new ApartmentDTO(apartment);
    }

    @Override
    public ApartmentDTO updateApartment(Integer id, ApartmentDTO apartmentRequest) {
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(id);
        if (apartmentOptional.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Apartment not found with id: " + id);
        Apartment apartment = apartmentOptional.get();
        apartment.setNumber(apartmentRequest.getNumber());
        apartment.setBlock(apartmentRequest.getBlock());
        apartment.setFloor(apartmentRequest.getFloor());
        apartment.setRealEstate(apartmentRequest.getRealEstate());

        Optional<Address> addressOptional = addressRepository.findById(apartmentRequest.getAddress().getId());
        if (addressOptional.isPresent())
            apartment.setAddress(addressOptional.get());
        else {
            Address address = new Address(apartmentRequest.getAddress());
            addressRepository.save(address);
            apartment.setAddress(address);
        }

        apartmentRepository.save(apartment);
        return new ApartmentDTO(apartment);
    }

    @Override
    public List<ApartmentDTO> getApartments() {
        List<Apartment> apartments = apartmentRepository.findAll();
        if (apartments.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "No apartment found.");

        return apartments.stream().map(ApartmentDTO::new).toList();
    }

    @Override
    public ApartmentDTO getApartmentById(Integer id) {
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(id);
        if (apartmentOptional.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Apartment not found with id: " + id);

        return new ApartmentDTO(apartmentOptional.get());
    }

    @Override
    public void deleteApartment(Integer id) {
        Optional<Apartment> apartmentOptional = apartmentRepository.findById(id);
        if (apartmentOptional.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Apartment not found with id: " + id);

        apartmentRepository.delete(apartmentOptional.get());
    }

    @Override
    public ResidentDTO registerResident(RegisterForm form){
        validateRegisterForm(form);

        User user = getUser();
        form.setFilledBy(user.getUsername());

        Resident resident = new Resident();
        resident.setName(form.getName());
        resident.setDocument(form.getDocument());
        resident.setPhoneNumber(form.getPhoneNumber());
        resident.setCreatedAt(new Date());
        resident.setUpdatedAt(null);
        resident.setOwner(form.isOwner());

        Optional<Role> roleOptional = roleRepository.findByName(EUserRole.RES);
        Set<Role> roles = new HashSet<>();
        roles.add(roleOptional.orElse(null));

        User residentUser = new User();
        residentUser.setEmail(form.getEmail());
        residentUser.setRoles(roles);
        //TODO: gerar username e senha p/ morador novo
        residentUser.setUsername(form.getEmail());
        Optional<User> existingUser = userRepository.findByUsername(form.getEmail());
        if (existingUser.isPresent())
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Username or email already taken");
        residentUser.setPassword(encoder.encode("123456"));
        userRepository.save(residentUser);
        resident.setUser(residentUser);

        Set<Visitor> visitors = new HashSet<>();
        form.getVisitors().forEach(v -> {
            Visitor visitor = new Visitor(v, resident);
            visitors.add(visitor);
        });
        resident.setVisitors(visitors);

        Address mailingAddress = null;
        if (form.getMailingAddress() != null){
            if (form.getMailingAddress().getId() != null)
                mailingAddress = addressRepository.findById(form.getMailingAddress().getId()).orElse(null);
            else {
                mailingAddress = new Address(form.getMailingAddress());
                addressRepository.save(mailingAddress);
            }
        }

        resident.setMailAddress(mailingAddress);

        Apartment apartment = apartmentRepository.findByNumber(form.getApartment());
        if (apartment == null)
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Must provide a valid apartment number");
        resident.setApartments(Set.of(apartment));

        if (form.getEmergencyContacts() != null) {
            Set<EmergencyContact> emergencyContacts = new HashSet<>();
            form.getEmergencyContacts().forEach(e -> {
                EmergencyContact emergencyContact = new EmergencyContact(e, resident);
                emergencyContacts.add(emergencyContact);
            });
            emergencyContactRepository.saveAll(emergencyContacts);
            resident.setEmergencyContacts(emergencyContacts);
        }

        residentRepository.save(resident);
        visitorRepository.saveAll(visitors);
        return new ResidentDTO(resident);
    }

    private boolean stringNullOrEmpty(String string) {
        return string == null || string.isEmpty() || string.isBlank();
    }

    private void validateRegisterForm(RegisterForm form) {
        if (form == null)
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Form must be filled");
        if (stringNullOrEmpty(form.getName()))
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Name must be provided");
        if (!validDocument(form.getDocument()))
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Document must be not empty and 11 characters long");
        if (stringNullOrEmpty(form.getEmail()))
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "E-mail must be provided");
        if (form.getApartment() == null)
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Apartment number must be provided");
    }

    private boolean validDocument(String document) {
        return document != null && document.length() == 11;
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        if (bookings.isEmpty())
            return new ArrayList<>();
        return bookings.stream().map(BookingDTO::new).toList();
    }

    @Override
    public BookingDTO getBookingById(Integer id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.map(BookingDTO::new).orElse(null);
    }

    @Override
    public ResidentDTO addVisitors(Integer residentId, List<VisitorDTO> visitors) {
        Optional<Resident> residentOptional = residentRepository.findById(residentId);
        if (residentOptional.isEmpty())
            return null;
        Resident resident = residentOptional.get();
        for (VisitorDTO v : visitors) {
            Visitor visitor = new Visitor(v, resident);
            resident.getVisitors().add(visitor);
            visitorRepository.save(visitor);
        }
        residentRepository.save(resident);
        return new ResidentDTO(resident);
    }

    @Override
    public List<VisitorDTO> findVisitorsByResident(Integer residentId) {

        Optional<Resident> residentOptional = residentRepository.findById(residentId);
        if (residentOptional.isEmpty())
            return new ArrayList<>();
        Resident resident = residentOptional.get();
        return resident.getVisitors().stream().map(VisitorDTO::new).toList();
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
    public User getUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    public BookingDTO createBooking(BookingDTO bookingRequest) {

        User user = getUser();
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
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Resident not found for user");

        Booking booking = new Booking();
        booking.setResident(resident);
        booking.setPlace(place);
        booking.setReservationFrom(bookingRequest.getReservedFrom());
        booking.setReservationTo(bookingRequest.getReservedUntil());
        booking = bookingRepository.save(booking);

        return new BookingDTO(booking);
    }

    private Place verifyPlaceForBooking(Integer placeId) {
        // Check if desired place exists:
        Optional<Place> placeOptional = placeRepository.findById(placeId);
        if (placeOptional.isEmpty())
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Desired place doesn't exist");

        // Check if desired place is available
        Place place = placeOptional.get();
        if (place.getBlocked())
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Desired place is not available");
        return place;
    }

    private void verifyBookingDates(Date requestStartDate, Date requestEndDate, Booking existingBooking) {
        // Check if period is correct:
        if (!requestStartDate.before(requestEndDate))
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Booking start date must be before end date");
        if (timeGreaterThan(requestStartDate, requestEndDate, Constants.BOOKING_MAX_HOURS))
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Booking time must not exceed six hours");

        // Check if exists another booking for the desired place in the same period
        if (existingBooking.getReservationFrom() == null || existingBooking.getReservationTo() == null)
            return;

        if (existingBooking.getReservationTo().after(requestEndDate)
                || existingBooking.getReservationTo().after(requestStartDate)
                || existingBooking.getReservationFrom().before(requestStartDate)
                || existingBooking.getReservationFrom().before(requestEndDate))
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Place is not available for chosen date");

    }
    @Override
    public BookingDTO updateBooking(Integer bookingId, BookingDTO bookingRequest) {
        User user = getUser();

        Date startDate = bookingRequest.getReservedFrom();
        Date endDate = bookingRequest.getReservedUntil();

        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        if (existingBooking.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND ,"Booking with informed id doesn't exist: " + bookingId);

        Booking booking = existingBooking.get();
        if (!booking.getResident().getUser().equals(user))
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Cannot update another user's booking");

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
    public void deleteBooking(Integer bookingId) {
        User user = getUser();

        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        if (existingBooking.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Booking with informed id doesn't exist: " + bookingId);

        Booking booking = existingBooking.get();
        if (!booking.getResident().getUser().equals(user) || !canUserDelete(user))
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Cannot delete another user's booking");

        bookingRepository.delete(booking);
    }

    private boolean canUserDelete(User user) {
        Set<Role> roles = user.getRoles();
        return roles.stream().anyMatch(r -> r.getName().equals(EUserRole.ADM)) ||
                roles.stream().anyMatch(r -> r.getName().equals(EUserRole.CON));
    }

    @Override
    public List<CommunicationDTO> getAllCommunications() {
        List<Communications> communications = communicationsRepository.findAll();
        if (communications.isEmpty())
            return new ArrayList<>();
        return communications.stream().map(CommunicationDTO::new).toList();
    }

    @Override
    public CommunicationDTO createCommunication(CommunicationDTO request){
        User user = getUser();

        Communications communications = new Communications();
        communications.setMessage(request.getMessage());
        communications.setDate(new Date());
        communications.setFromUser(user);
        communications = communicationsRepository.save(communications);
        return new CommunicationDTO(communications);
    }

    @Override
    public CommunicationDTO getCommunicationById(Integer id) {
        Optional<Communications> communications = communicationsRepository.findById(id);
        return communications.map(CommunicationDTO::new).orElse(null);
    }

    @Override
    public CommunicationDTO updateCommunication(Integer id, CommunicationDTO communicationRequest) {
        Optional<Communications> optionalCommunications = communicationsRepository.findById(id);
        if (optionalCommunications.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Communication not found with id: " + id);

        Communications existingCommunication = optionalCommunications.get();
        User user = getUser();
        if (!existingCommunication.getFromUser().equals(user))
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Cannot update other user's communication");

        existingCommunication.setMessage(communicationRequest.getMessage());
        existingCommunication.setDate(new Date());
        communicationsRepository.save(existingCommunication);
        return new CommunicationDTO(existingCommunication);
    }

    @Override
    public void deleteCommunication(Integer id) {
        User user = getUser();

        Optional<Communications> optionalCommunications = communicationsRepository.findById(id);
        if (optionalCommunications.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Communication not found with id: " + id);
        Communications existingCommunication = optionalCommunications.get();

        if (!existingCommunication.getFromUser().equals(user))
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Cannot delete other user's communication");

        communicationsRepository.delete(existingCommunication);
    }

    @Override
    public List<PlaceDTO> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        if (places.isEmpty())
            return new ArrayList<>();
        return places.stream().map(PlaceDTO::new).toList();
    }

    @Override
    public PlaceDTO createPlace(PlaceDTO placeRequest) {
        Place place = new Place();
        place.setBlocked(false);
        place.setName(placeRequest.getName());
        place = placeRepository.save(place);
        return new PlaceDTO(place);
    }

    @Override
    public PlaceDTO getPlaceById(Integer id) {
        Optional<Place> place = placeRepository.findById(id);
        return place.map(PlaceDTO::new).orElse(null);
    }

    @Override
    public PlaceDTO updatePlace(Integer id, PlaceDTO placeUpdateRequest) {
        Optional<Place> optionalPlace = placeRepository.findById(id);
        if (optionalPlace.isEmpty())
            return null;
        Place place = optionalPlace.get();
        place.setName(placeUpdateRequest.getName());
        place.setBlocked(placeUpdateRequest.getBlocked());
        return new PlaceDTO(place);
    }

    @Override
    public void deletePlace(Integer id) {
        Optional<Place> placeOptional = placeRepository.findById(id);
        if (placeOptional.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Place not found with id: " + id);
        Place place = placeOptional.get();
        placeRepository.delete(place);
    }

    @Override
    public List<VisitDTO> getVisits() {
        User user = getUser();
        Set<Role> roles = user.getRoles();
        List<Visit> visits;
        if (roles.stream().anyMatch(r -> r.getName().equals(EUserRole.ADM))){
            visits = visitRepository.findByVisitStatus(EVisitStatus.PENDING);
        } else {
            Resident resident = residentRepository.findByUser_Id(user.getId());
            if (resident == null)
                throw new PorteiroException(HttpStatus.FORBIDDEN, "Current user is not resident or admin.");
            Set<Visitor> visitors = resident.getVisitors();
            visits = visitRepository.findByVisitStatusAndVisitorIn(EVisitStatus.PENDING, visitors);
        }
        if (visits.isEmpty())
            return new ArrayList<>();
        return visits.stream().map(VisitDTO::new).toList();
    }

    @Override
    public VisitDTO getVisitById(Integer id) {
        User user = getUser();
        Set<Role> roles = user.getRoles();
        if (roles.stream().anyMatch(r -> r.getName().equals(EUserRole.ADM))) {
            Optional<Visit> visit = visitRepository.findById(id);
            return visit.map(VisitDTO::new).orElse(null);
        }

        Resident resident = residentRepository.findByUser_Id(user.getId());
        Optional<Visit> visitOptional = visitRepository.findById(id);
        if (visitOptional.isEmpty())
            return null;
        if (!resident.getVisitors().contains(visitOptional.get().getVisitor()))
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Visitor is not registered.");

        return new VisitDTO(visitOptional.get());
    }

    @Override
    public VisitDTO createVisit(VisitDTO visitRequest){
        User user = getUser();
        Visit visit = new Visit();
        Optional<Visitor> visitor = visitorRepository.findById(visitRequest.getVisitor().getId());
        if (visitor.isEmpty())
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "The informed visitor is not registered");

        Resident resident = residentRepository.findByUser_Id(user.getId());

        // Se user for porteiro, precisa informar resident na request
        if (resident == null && (user.getRoles().stream().anyMatch(r -> r.getName().equals(EUserRole.CON))
                || user.getRoles().stream().anyMatch(r -> r.getName().equals(EUserRole.ADM))))
            resident = residentRepository.findById(visitRequest.getVisitor().getResident().getId()).orElse(null);

        if (resident == null) {
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Resident not found.");
        }

        if (!visitor.get().getResident().equals(resident))
            throw new PorteiroException(HttpStatus.BAD_REQUEST, "Visitor not registered for this resident");

        visit.setVisitor(visitor.get());
        visit.setCreatedAt(new Date());
        visit.setUpdatedAt(null);
        visit.setVisitStatus(EVisitStatus.PENDING);
        visitRepository.save(visit);
        return new VisitDTO(visit);
    }

    @Override
    public VisitDTO updateVisit(Integer id, VisitDTO visitRequest) {
        User user = getUser();
        Optional<Visit> visitOptional = visitRepository.findById(id);

        if (visitOptional.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Visit with informed id not found: " + id);

        Visit visit = visitOptional.get();

        Optional<Visitor> visitor = visitorRepository.findById(visitRequest.getVisitor().getId());
        if (visitor.isPresent()){
            Resident resident = residentRepository.findByUser_Id(user.getId());
            if (resident == null)
                throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Current user is not a resident.");
            if (!visitor.get().getResident().equals(resident))
                throw new PorteiroException(HttpStatus.FORBIDDEN, "Cannot update other resident's visit.");
            visit.setVisitor(visitor.get());
        }

        visit.setVisitStatus(visitRequest.getVisitStatus());
        visit.setUpdatedAt(new Date());
        visitRepository.save(visit);
        return new VisitDTO(visit);
    }

    @Override
    public void deleteVisit(Integer id){
        User user = getUser();
        Optional<Visit> visitOptional = visitRepository.findById(id);

        if (visitOptional.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "Visit with informed id not found: " + id);

        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Current user is not a resident.");

        Visit visit = visitOptional.get();
        if (!visit.getVisitor().getResident().equals(resident))
            throw new PorteiroException(HttpStatus.FORBIDDEN, "Cannot delete other resident's visit.");

        visitRepository.delete(visit);
    }

    @Override
    public List<VisitDTO> findVisitByStatus(EVisitStatus status) {
        User user = getUser();
        if (user.getRoles().stream().anyMatch(r -> r.getName().equals(EUserRole.ADM))
                || user.getRoles().stream().anyMatch(r -> r.getName().equals(EUserRole.CON))) {
            List<Visit> visits = visitRepository.findByVisitStatus(status);
            if (visits.isEmpty())
                throw new PorteiroException(HttpStatus.NOT_FOUND, "No visits found with status: " + status);
            return visits.stream().map(VisitDTO::new).toList();
        }

        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Current user is not a resident.");

        List<Visit> visits = visitRepository.findByVisitStatusAndVisitor_Resident(status, resident);
        if (visits.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "No visits found with status: " + status);

        return visits.stream().map(VisitDTO::new).toList();
    }

    @Override
    public List<VisitorDTO> getAllVisitors() {
        User user = getUser();
        if (user.getRoles().stream().anyMatch(r -> r.getName().equals(EUserRole.ADM))
                || user.getRoles().stream().anyMatch(r -> r.getName().equals(EUserRole.CON))) {
            List<Visitor> visitors = visitorRepository.findAll();
            if (visitors.isEmpty())
                throw new PorteiroException(HttpStatus.NOT_FOUND, "No visitors found");
            return visitors.stream().map(VisitorDTO::new).toList();
        }

        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Current user is not a resident.");

        List<Visitor> visitors = visitorRepository.findByResident(resident);
        if (visitors.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "No visitors found");

        return visitors.stream().map(VisitorDTO::new).toList();
    }

    @Override
    public List<ResidentDTO> getAllResidents() {
        List<Resident> residents = residentRepository.findAll();
        return residents.stream().map(ResidentDTO::new).toList();
    }

    @Override
    public VisitorDTO getVisitorById(Integer id) {
        User user = getUser();

        Optional<Visitor> visitorOptional = visitorRepository.findById(id);
        if (visitorOptional.isEmpty())
            return null;
        Visitor visitor = visitorOptional.get();

        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Current user is not a resident.");

        if (!visitor.getResident().equals(resident))
            throw new PorteiroException(HttpStatus.FORBIDDEN, "Cannot see other resident's visitors");

        return new VisitorDTO(visitor);
    }

    @Override
    public VisitorDTO createVisitor(VisitorDTO visitorRequest) {
        User user = getUser();

        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Current user is not a resident.");

        Visitor visitor = new Visitor();
        visitor.setResident(resident);
        visitor.setName(visitorRequest.getName());
        visitor.setDocument(visitorRequest.getDocument());
        visitor.setCreatedAt(new Date());
        visitor.setUpdatedAt(null);
        visitor.setRelationship(visitorRequest.getRelationship());
        visitor.setAuthType(visitorRequest.getAuthType());
        visitor = visitorRepository.saveAndFlush(visitor);
        return new VisitorDTO(visitor);
    }

    @Override
    public VisitorDTO updateVisitor(Integer id, VisitorDTO visitorRequest) {
        User user = getUser();

        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Current user is not a resident.");

        Optional<Visitor> visitorOptional = visitorRepository.findById(id);

        if (visitorOptional.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "No visitor registered with id: " + id);

        Visitor visitor = visitorOptional.get();

        if (!visitor.getResident().equals(resident))
            throw new PorteiroException(HttpStatus.FORBIDDEN, "Cannot update other resident's visitor");

        visitor.setName(visitorRequest.getName());
        visitor.setDocument(visitorRequest.getDocument());
        visitor.setUpdatedAt(new Date());
        visitor.setAuthType(visitorRequest.getAuthType());
        visitor.setRelationship(visitorRequest.getRelationship());
        visitorRepository.save(visitor);
        return new VisitorDTO(visitor);
    }

    @Override
    public void deleteVisitor(Integer id) {
        User user = getUser();

        Resident resident = residentRepository.findByUser_Id(user.getId());
        if (resident == null)
            throw new PorteiroException(HttpStatus.UNAUTHORIZED, "Current user is not a resident.");

        Optional<Visitor> visitorOptional = visitorRepository.findById(id);

        if (visitorOptional.isEmpty())
            throw new PorteiroException(HttpStatus.NOT_FOUND, "No visitor registered with id: " + id);

        Visitor visitor = visitorOptional.get();

        if (!visitor.getResident().equals(resident))
            throw new PorteiroException(HttpStatus.FORBIDDEN, "Cannot delete other resident's visitor");

        visitorRepository.delete(visitor);
    }

}
