package com.unicamp.porteirobackend.service;

import com.unicamp.porteirobackend.dto.BookingDTO;
import com.unicamp.porteirobackend.dto.UserDTO;
import com.unicamp.porteirobackend.dto.request.RegisterForm;
import com.unicamp.porteirobackend.entity.Resident;
import com.unicamp.porteirobackend.entity.User;
import com.unicamp.porteirobackend.entity.Visitor;
import com.unicamp.porteirobackend.security.services.UserDetailsImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PorteiroService {

    boolean deleteUser(int id);

    List<UserDTO> findAllUsers();

    UserDTO getUserById(int id);

    Resident registerResident(RegisterForm form);

    Resident addVisitors(Integer residentId, List<Visitor> visitors);

    List<Visitor> findVisitorsByResident(Integer residentId);

    User getUser(UserDetailsImpl userDetails);

    BookingDTO createBooking(BookingDTO booking, User user);

    BookingDTO updateBooking(Integer bookingId, BookingDTO bookingRequest, User user);

    void deleteBooking(Integer id, User user);
}
