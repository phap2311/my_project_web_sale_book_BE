package com.example.book_storemanagement.service.bill;

import com.example.book_storemanagement.model.dto.BillDTO;
import com.example.book_storemanagement.model.entity.Bill;
import com.example.book_storemanagement.repository.IBillRepository;
import com.example.book_storemanagement.service.cart.ICartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService implements IBillService {
    @Autowired
    private IBillRepository iBillRepository;
    @Autowired
    private ICartService iCartService;

    @Override
    public Bill save(Bill bill, Long accountId) {
       /// iBillRepository.saveBill(bill, accountId);
       /// String billCode = bill.getCode()    ;
        String billCode = generateFiveDigitInteger();
        bill.setCode(billCode);
        iBillRepository.saveBill(bill, accountId);
        iCartService.updateCartWithBill(accountId, billCode);
        return bill;
    }
    @Override
    public Long getLastInsertedId() {
        return iBillRepository.getLastInsertedId();
    }

    public String generateFiveDigitInteger() {
        int randomNumber = (int) (Math.random() * 90000) + 10000;
        return String.valueOf(randomNumber);
    }

    @Override
    public Optional<Bill> findById(Long id) {
        return iBillRepository.findById(id);
    }

    @Override
    public Bill findByCodeBill(String code) {
        return iBillRepository.findByCode(code);
    }

    @Override
    public void update( Long accountId, Long billId) {
        iBillRepository.updateBill(accountId, billId);
    }

    @Override
    public List<Bill> getAllBillByAccount(Long accountId) {
        return iBillRepository.getAllBillByAccount(accountId);
    }


}
