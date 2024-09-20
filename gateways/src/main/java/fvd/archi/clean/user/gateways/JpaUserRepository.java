package fvd.archi.clean.user.gateways;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface JpaUserRepository extends JpaRepository<UserDataMapper, String> {
}