package com.pao.project.elearning.service;
import com.pao.project.elearning.exception.UserNegasitException;
import com.pao.project.elearning.model.Cursant;
import com.pao.project.elearning.model.Instructor;
import com.pao.project.elearning.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private static UserService instance;

    private List<Cursant> cursanti;
    private List<Instructor> instructori;
    private Map<Integer, User> usersById;

    private UserService() {
        cursanti = new ArrayList<>();
        instructori = new ArrayList<>();
        usersById = new HashMap<>();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }

        return instance;
    }

    public void adaugaCursant(Cursant cursant) {
        if (usersById.containsKey(cursant.getId())) {
            throw new IllegalArgumentException("Exista deja un user cu id-ul " + cursant.getId() + ".");
        }

        cursanti.add(cursant);
        usersById.put(cursant.getId(), cursant);
    }

    public void adaugaInstructor(Instructor instructor) {
        if (usersById.containsKey(instructor.getId())) {
            throw new IllegalArgumentException("Exista deja un user cu id-ul " + instructor.getId() + ".");
        }

        instructori.add(instructor);
        usersById.put(instructor.getId(), instructor);
    }

    public User cautaUserDupaId(int id) throws UserNegasitException {
        User user = usersById.get(id);

        if (user == null) {
            throw new UserNegasitException("Userul cu id-ul " + id + " nu a fost gasit.");
        }

        return user;
    }

    public Cursant cautaCursantDupaId(int id) throws UserNegasitException {
        for (Cursant cursant : cursanti) {
            if (cursant.getId() == id) {
                return cursant;
            }
        }

        throw new UserNegasitException("Cursantul cu id-ul " + id + " nu a fost gasit.");
    }

    public Instructor cautaInstructorDupaId(int id) throws UserNegasitException {
        for (Instructor instructor : instructori) {
            if (instructor.getId() == id) {
                return instructor;
            }
        }

        throw new UserNegasitException("Instructorul cu id-ul " + id + " nu a fost gasit.");
    }

    public void stergeCursantDupaId(int id) throws UserNegasitException {
        Cursant cursantGasit = null;

        for (Cursant cursant : cursanti) {
            if (cursant.getId() == id) {
                cursantGasit = cursant;
                break;
            }
        }

        if (cursantGasit == null) {
            throw new UserNegasitException("Cursantul cu id-ul " + id + " nu a fost gasit.");
        }

        cursanti.remove(cursantGasit);
        usersById.remove(id);
    }

    public void stergeInstructorDupaId(int id) throws UserNegasitException {
        Instructor instructorGasit = null;

        for (Instructor instructor : instructori) {
            if (instructor.getId() == id) {
                instructorGasit = instructor;
                break;
            }
        }

        if (instructorGasit == null) {
            throw new UserNegasitException("Instructorul cu id-ul " + id + " nu a fost gasit.");
        }

        instructori.remove(instructorGasit);
        usersById.remove(id);
    }

    public List<Cursant> getCursanti() {
        return cursanti;
    }

    public List<Instructor> getInstructori() {
        return instructori;
    }
}