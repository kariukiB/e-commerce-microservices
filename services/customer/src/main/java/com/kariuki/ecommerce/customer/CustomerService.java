package com.kariuki.ecommerce.customer;

import com.kariuki.ecommerce.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerDto request) {
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(CustomerDto request) {
        var customer = repository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Cannot update customer:: No customer found for provided ID:: %s", request.id())));
        mergeCustomer(customer, request);
        repository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerDto request) {
        if (StringUtils.isNotEmpty(request.firstName())){
            customer.setFirstName(request.firstName());
        }
        if (StringUtils.isNotEmpty(request.lastName())){
            customer.setLastName(request.lastName());
        }
        if (StringUtils.isNotEmpty(request.email())){
            customer.setEmail(request.email());
        }
        if ((request.address() != null)){
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {

        return repository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return repository.findById(customerId)
                .map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("No customer found with the provided ID:: %s", customerId)));
    }

    public void deleteCustomer(String customerId) {
        repository.deleteById(customerId);
    }
}
