package com.tvpss.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.tvpss.model.Crew;

@Repository
@Transactional
public class CrewDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Crew> findByApplicationStatus(String status) {
        String query = "SELECT c FROM Crew c " +
                       "JOIN FETCH c.user u " +
                       "WHERE c.applicationStatus = :status";
        return entityManager.createQuery(query, Crew.class)
                .setParameter("status", status)
                .getResultList();
    }




//    public void updateApplicationStatus(int crewID, String newStatus) {
//        String query = "UPDATE Crew c SET c.applicationStatus = :newStatus WHERE c.crewID = :crewID";
//        int updatedCount = entityManager.createQuery(query)
//                .setParameter("newStatus", newStatus)
//                .setParameter("crewID", crewID)
//                .executeUpdate();
//        if (updatedCount == 0) {
//            System.out.println("No records updated for crewID: " + crewID);
//        }
//    }

    
    public Crew findCrewById(int crewID) {
        String query = "SELECT c FROM Crew c WHERE c.crewID = :crewID";
        return entityManager.createQuery(query, Crew.class)
                .setParameter("crewID", crewID)
                .getSingleResult();
    }

    public List<Crew> findAllApplicants() {
        String query = "SELECT * FROM Crew";
        return entityManager.createQuery(query, Crew.class).getResultList();
    }
    public Crew findCrewbyUserID(int userID) {
        String query = "SELECT c FROM Crew c WHERE c.user.userId = :userID";
        try {
            return entityManager.createQuery(query, Crew.class)
                    .setParameter("userID", userID)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Return null if no result is found
            return null;
        }
    }

    /**
     * Update the application status for a given Crew ID
     * 
     * @param crewID The ID of the Crew
     * @param status New status to set (e.g., Approved, Rejected)
     */
    public void updateApplicationStatus(int crewID, String status) {
        Crew crew = entityManager.find(Crew.class, crewID);
        if (crew != null) {
            crew.setApplicationStatus(status);
            entityManager.merge(crew); // Update the entity in the database
        } else {
            throw new RuntimeException("Crew with ID " + crewID + " not found.");
        }
    }

}
