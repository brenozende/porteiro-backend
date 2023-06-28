package com.unicamp.porteirobackend.service;

import com.unicamp.porteirobackend.dto.*;
import com.unicamp.porteirobackend.dto.request.RegisterForm;
import com.unicamp.porteirobackend.entity.Resident;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.entity.Visitor;
import com.unicamp.porteirobackend.enums.EVisitStatus;
import com.unicamp.porteirobackend.security.services.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PorteiroService {

    boolean deleteUser(int id);

    List<UserDTO> findAllUsers();

    UserDTO getUserById(int id);

    ResidentDTO registerResident(RegisterForm form);

    ResidentDTO addVisitors(Integer residentId, List<VisitorDTO> visitors);

    List<VisitorDTO> findVisitorsByResident(Integer residentId);

    User getUser();

    BookingDTO createBooking(BookingDTO booking);

    BookingDTO updateBooking(Integer bookingId, BookingDTO bookingRequest);

    void deleteBooking(Integer id);

    VisitDTO getVisitById(Integer id);

    VisitDTO createVisit(VisitDTO visitRequest);

    VisitDTO updateVisit(Integer id, VisitDTO visitRequest);

    void deleteVisit(Integer id);

    List<VisitDTO> findVisitByStatus(EVisitStatus status);

    List<BookingDTO> getAllBookings();

    BookingDTO getBookingById(Integer id);

    List<CommunicationDTO> getAllCommunications();

    CommunicationDTO createCommunication(CommunicationDTO request);

    CommunicationDTO getCommunicationById(Integer id);

    CommunicationDTO updateCommunication(Integer id, CommunicationDTO communicationRequest);

    void deleteCommunication(Integer id);

    List<PlaceDTO> getAllPlaces();

    PlaceDTO createPlace(PlaceDTO placeRequest);

    PlaceDTO getPlaceById(Integer id);

    PlaceDTO updatePlace(Integer id, PlaceDTO placeUpdateRequest);

    void deletePlace(Integer id);

    List<VisitDTO> getVisits();

    List<VisitorDTO> getAllVisitors();

    List<ResidentDTO> getAllResidents();

    VisitorDTO getVisitorById(Integer id);

    VisitorDTO createVisitor(VisitorDTO visitorRequest);

    VisitorDTO updateVisitor(Integer id, VisitorDTO visitorRequest);

    void deleteVisitor(Integer id);

    ApartmentDTO createApartment(ApartmentDTO apartmentRequest);

    ApartmentDTO updateApartment(Integer id, ApartmentDTO apartmentRequest);

    List<ApartmentDTO> getApartments();

    ApartmentDTO getApartmentById(Integer id);

    void deleteApartment(Integer id);
}
