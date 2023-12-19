package com.tree.test.repository;

import com.tree.test.model.dao.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {
//    @Query(value = "SELECT * FROM statement WHERE account_id = :accountId", nativeQuery = true)
//    List<Statement> getStatementsByAccountIdNative(@Param("accountId") Long accountId);
//    @Query("SELECT s FROM Statement s WHERE s.account.id = :accountId")
//    List<Statement> findStatementsByAccount_Id(@Param("accountId") Long accountId);

    List<Statement> findByAccountId(Long accountId);


}
