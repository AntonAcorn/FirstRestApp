package ru.acorn.FirstRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.acorn.FirstRestApp.models.Person;
@Repository
public interface PeopleRepository extends JpaRepository <Person, Integer> {
}
