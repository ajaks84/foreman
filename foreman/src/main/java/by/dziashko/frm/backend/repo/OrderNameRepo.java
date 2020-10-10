package by.dziashko.frm.backend.repo;

import by.dziashko.frm.backend.entity.OrderName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderNameRepo extends JpaRepository<OrderName, Long> {

    @Query("select c from OrderName c " +
            "where lower(c.seller) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.client) like lower(concat('%', :searchTerm, '%'))") //
    List<OrderName> search(@Param("searchTerm") String searchTerm);

    OrderName getByOrderNumber(String orderNumber);
}
