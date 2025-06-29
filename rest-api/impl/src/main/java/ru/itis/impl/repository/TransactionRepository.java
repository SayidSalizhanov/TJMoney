package ru.itis.impl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

    List<Transaction> findAllByUserAndGroupIsNull(User user);
    Page<Transaction> findAllByUserAndGroupIsNull(User user, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.group IS NULL AND t.dateTime >= :startDate")
    List<Transaction> findByUserWithPeriod(@Param("user") User user, @Param("startDate") LocalDateTime startDate);

    List<Transaction> findAllByGroup(Group group);
    Page<Transaction> findAllByGroup(Group group, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.group = :group AND t.dateTime >= :startDate")
    List<Transaction> findByGroupWithPeriod(@Param("group") Group group, @Param("startDate") LocalDateTime startDate);
}
