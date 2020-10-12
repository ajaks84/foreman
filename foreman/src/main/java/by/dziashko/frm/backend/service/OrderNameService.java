package by.dziashko.frm.backend.service;

import by.dziashko.frm.backend.entity.OrderName;
import by.dziashko.frm.backend.entity.Seller;
import by.dziashko.frm.backend.repo.OrderNameRepo;
import by.dziashko.frm.backend.repo.SellerRepo;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderNameService {
    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());
    private OrderNameRepo orderNameRepo;
    private SellerRepo sellerRepo;

    public OrderNameService(OrderNameRepo orderNameRepo,SellerRepo sellerRepo ) {
        this.orderNameRepo = orderNameRepo;
        this.sellerRepo = sellerRepo;
    }

    public List<OrderName> findAll() {
        return orderNameRepo.findAll();
    }

    public OrderName find(Long id) {
        return orderNameRepo.getOne(id);
    }

    public OrderName find(String s) {
        return orderNameRepo.getByOrderNumber(s);
    }

    public List<OrderName> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return orderNameRepo.findAll();
        } else {
            return orderNameRepo.search(stringFilter);
        }
    }

    public List<OrderName> filterReady(Boolean value) {
        if (value == true) {
            return orderNameRepo.getByOrderReadiness(OrderName.Readiness.Ready);
        } else {
            return orderNameRepo.findAll();
        }
    }


    public long count() {
        return orderNameRepo.count();
    }

    public void delete(OrderName orderName) {
        orderNameRepo.delete(orderName);
    }

    public void save(OrderName orderName) {
        if (orderName == null) {
            LOGGER.log(Level.SEVERE,
                    "OrderName is null. Are you sure you have connected your form to the application?");
            return;
        }
        orderNameRepo.save(orderName);
    }

    @PostConstruct
    public void populateTestData() {

        if (sellerRepo.count() == 0) {
            sellerRepo.saveAll(
                    Stream.of("Jacek", "Jan")
                            .map(Seller::new)
                            .collect(Collectors.toList()));
        }

        if (orderNameRepo.count() == 0) {
        Random r = new Random(0);
        List<Seller> sellers = sellerRepo.findAll();
        OrderName orderName1 = new OrderName();
        orderName1.setSeller(sellers.get(r.nextInt(sellers.size())));
        orderName1.setClient("Client 1");
        orderName1.setOrderNumber("134");
        orderName1.setOrderDate("11.09.2020");
        orderName1.setOrderDeadLine("11.12.2020");
        orderName1.setOrderDelay("0");
        orderName1.setOrderReadiness(OrderName.Readiness.Ready);
        orderNameRepo.save(orderName1);
            OrderName orderName2 = new OrderName();
            orderName2.setSeller(sellers.get(r.nextInt(sellers.size())));
            orderName2.setClient("Client 2");
            orderName2.setOrderNumber("135");
            orderName2.setOrderDate("11.07.2020");
            orderName2.setOrderDeadLine("11.10.2021");
            orderName2.setOrderDelay("0");
            orderName2.setOrderReadiness(OrderName.Readiness.NotReady);
            orderNameRepo.save(orderName2);
            OrderName orderName3 = new OrderName();
            orderName3.setSeller(sellers.get(r.nextInt(sellers.size())));
            orderName3.setClient("Client 3");
            orderName3.setOrderNumber("136");
            orderName3.setOrderDate("8.07.2020");
            orderName3.setOrderDeadLine("9.6.2021");
            orderName3.setOrderDelay("0");
            orderName3.setOrderReadiness(OrderName.Readiness.NotReady);
            orderNameRepo.save(orderName3);
    }}
}
