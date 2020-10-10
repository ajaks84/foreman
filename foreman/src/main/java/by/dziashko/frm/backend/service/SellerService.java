package by.dziashko.frm.backend.service;

import by.dziashko.frm.backend.entity.Seller;
import by.dziashko.frm.backend.repo.SellerRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SellerService {

    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());
    private SellerRepo sellerRepo;

    public SellerService(SellerRepo sellerRepo) {
        this.sellerRepo = sellerRepo;
    }

    public List<Seller> findAll() {
        return sellerRepo.findAll();
    }

    public void save(Seller seller) {
        if (seller == null) {
            LOGGER.log(Level.SEVERE,
                    "Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        sellerRepo.save(seller);
    }

    public void delete(Seller seller) {
        sellerRepo.delete(seller);
    }

//    public Map<String, Integer> getStats() {
//        HashMap<String, Integer> stats = new HashMap<>();
//        findAll().forEach(seller -> stats.put(seller.getName(), seller.getOrderNames().size()));
//        return stats;
//    }

}