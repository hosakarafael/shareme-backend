package com.rafaelhosaka.shareme.applicationuser;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role,String> {
    Role findByName(String name);
}
