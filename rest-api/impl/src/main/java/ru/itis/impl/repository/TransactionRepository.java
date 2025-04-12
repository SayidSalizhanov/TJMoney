package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.Transaction;
import ru.itis.impl.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.group IS NULL")
    List<Transaction> findByUser(@Param("user") User user);

    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.group IS NULL AND t.dateTime >= :startDate")
    List<Transaction> findByUserWithPeriod(@Param("user") User user, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT t FROM Transaction t WHERE t.group = :group")
    List<Transaction> findByGroup(@Param("group") Group group);

    @Query("SELECT t FROM Transaction t WHERE t.group = :group AND t.dateTime >= :startDate")
    List<Transaction> findByGroupWithPeriod(@Param("group") Group group, @Param("startDate") LocalDateTime startDate);

    @Modifying
    @Query("UPDATE Transaction t SET t.amount = :amount, t.category = :category, t.type = :type, t.description = :description WHERE t.id = :id")
    void update(@Param("amount") Integer amount, @Param("category") String category, @Param("type") String type, @Param("description") String description, @Param("id") Long id);
}
