package com.customermanagement.CustomerManagement.web;

import com.customermanagement.CustomerManagement.model.Customer;
import com.customermanagement.CustomerManagement.model.CustomerRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api")
class CustomerController {

    private CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customers")
    Collection<Customer> customers() {
        return customerRepository.findAll();
    }

    @GetMapping("/customer/{id}")
    ResponseEntity<?> getCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/customer")
    ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) throws URISyntaxException {
        Customer result = customerRepository.save(customer);
        return ResponseEntity.created(new URI("/api/customer/" + result.getId()))
                .body(result);
    }

    @PutMapping("/customer/{id}")
    ResponseEntity<Customer> updateCustomer(@Valid @RequestBody Customer customer) {
        Customer result = customerRepository.save(customer);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("customer/excel")
    public String excelReader(@RequestParam("file") MultipartFile excel) {
        Customer cust = new Customer();
        ArrayList<String> str = new ArrayList<String>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excel.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);

            for(int i=0; i<sheet.getPhysicalNumberOfRows();i++) {
                XSSFRow row = sheet.getRow(i);
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    System.out.print(row.getCell(j) + " ");
                    str.add(String.valueOf(row.getCell(j)));
                    //cust.setName(String.valueOf(row.getCell(j)));
                }
                cust.setName(str.get(0));
                String sDate1 = str.get(1);
                Date date1;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                cust.setDateOfBirth(date1);
                cust.setNicNumber(str.get(2));
                customerRepository.save(cust);
                System.out.println("");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Success";
    }

}