package io.github.bshweb.backend.repository;

import io.github.bshweb.backend.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {
    @Query("""
        select distinct b
        from Board b
        left join fetch b.stages
        where b.id = :id
    """)
    Optional<Board> findBoardWithStagesById(@Param("id") UUID id);
}
