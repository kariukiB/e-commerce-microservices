package com.kariuki.ecommerce.customer;

import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomerDto request) {
        if (request == null){
            return null;
        }
        return Customer.builder()
                .address(request.address())
                .email(request.email())
                .id(request.id())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}
