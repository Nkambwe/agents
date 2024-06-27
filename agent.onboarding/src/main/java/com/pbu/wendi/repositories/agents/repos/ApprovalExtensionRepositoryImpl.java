package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Approval;
import com.pbu.wendi.utils.enums.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class ApprovalExtensionRepositoryImpl implements ApprovalExtensionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void updateApproval(Approval approval) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE Approval a SET ");

        if (approval.getRecruitedBy() != null && !approval.getRecruitedBy().isEmpty()) {
            queryBuilder.append("a.recruitedBy = :recruitedBy, ");
        }
        if (approval.getRecruitedOn() != null) {
            queryBuilder.append("a.recruitedOn = :recruitedOn, ");
        }
        if (approval.getRecruitStatus()  != Status.UNKNOWN) {
            queryBuilder.append("a.recruitStatus = :recruitStatus, ");
        }
        if (approval.getRecruitComment() != null && !approval.getRecruitComment().isEmpty()) {
            queryBuilder.append("a.recruitComment = :recruitComment, ");
        }
        if (approval.getCreatedBy() != null && !approval.getCreatedBy().isEmpty()) {
            queryBuilder.append("a.createdBy = :createdBy, ");
        }
        if (approval.getCreatedOn() != null) {
            queryBuilder.append("a.createdOn = :createdOn, ");
        }
        if (approval.getCreatedStatus() != Status.UNKNOWN) {
            queryBuilder.append("a.createdStatus = :createdStatus, ");
        }
        if (approval.getCreatedComment() != null && !approval.getCreatedComment().isEmpty()) {
            queryBuilder.append("a.createdComment = :createdComment, ");
        }
        if (approval.getReviewedBy() != null && !approval.getReviewedBy().isEmpty()) {
            queryBuilder.append("a.reviewedBy = :reviewedBy, ");
        }
        if (approval.getReviewedOn() != null) {
            queryBuilder.append("a.reviewedOn = :reviewedOn, ");
        }
        if (approval.getReviewComment() != null && !approval.getReviewComment().isEmpty()) {
            queryBuilder.append("a.reviewComment = :reviewComment, ");
        }
        if (approval.getApprovedBy() != null && !approval.getApprovedBy().isEmpty()) {
            queryBuilder.append("a.approvedBy = :approvedBy, ");
        }
        if (approval.getApprovedOn() != null) {
            queryBuilder.append("a.approvedOn = :approvedOn, ");
        }
        if (approval.getApproveStatus() != Status.UNKNOWN) {
            queryBuilder.append("a.approveStatus = :approveStatus, ");
        }
        if (approval.getApproveComment() != null && !approval.getApproveComment().isEmpty()) {
            queryBuilder.append("a.approveComment = :approveComment, ");
        }

        // Remove the last comma and space
        queryBuilder.setLength(queryBuilder.length() - 2);

        queryBuilder.append(" WHERE a.id = :id");

        Query query = entityManager.createQuery(queryBuilder.toString());

        if (approval.getRecruitedBy() != null && !approval.getRecruitedBy().isEmpty()) {
            query.setParameter("recruitedBy", approval.getRecruitedBy());
        }
        if (approval.getRecruitedOn() != null) {
            query.setParameter("recruitedOn", approval.getRecruitedOn());
        }
        if (approval.getRecruitStatus()  != Status.UNKNOWN) {
            query.setParameter("recruitStatus", approval.getRecruitStatus());
        }
        if (approval.getRecruitComment() != null && !approval.getRecruitComment().isEmpty()) {
            query.setParameter("recruitComment", approval.getRecruitComment());
        }
        if (approval.getCreatedBy() != null && !approval.getCreatedBy().isEmpty()) {
            query.setParameter("createdBy", approval.getCreatedBy());
        }
        if (approval.getCreatedOn() != null) {
            query.setParameter("createdOn", approval.getCreatedOn());
        }
        if (approval.getCreatedStatus()  != Status.UNKNOWN) {
            query.setParameter("createdStatus", approval.getCreatedStatus());
        }
        if (approval.getCreatedComment() != null && !approval.getCreatedComment().isEmpty()) {
            query.setParameter("createdComment", approval.getCreatedComment());
        }
        if (approval.getReviewedBy() != null && !approval.getReviewedBy().isEmpty()) {
            query.setParameter("reviewedBy", approval.getReviewedBy());
        }
        if (approval.getReviewedOn() != null) {
            query.setParameter("reviewedOn", approval.getReviewedOn());
        }
        if (approval.getReviewComment() != null && !approval.getReviewComment().isEmpty()) {
            query.setParameter("reviewComment", approval.getReviewComment());
        }
        if (approval.getApprovedBy() != null && !approval.getApprovedBy().isEmpty()) {
            query.setParameter("approvedBy", approval.getApprovedBy());
        }
        if (approval.getApprovedOn() != null) {
            query.setParameter("approvedOn", approval.getApprovedOn());
        }
        if (approval.getApproveStatus()  != Status.UNKNOWN) {
            query.setParameter("approveStatus", approval.getApproveStatus());
        }
        if (approval.getApproveComment() != null && !approval.getApproveComment().isEmpty()) {
            query.setParameter("approveComment", approval.getApproveComment());
        }

        query.setParameter("id", approval.getId());

        query.executeUpdate();
    }
}

