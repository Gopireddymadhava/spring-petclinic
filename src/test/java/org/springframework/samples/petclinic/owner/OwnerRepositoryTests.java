package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class OwnerRepositoryTests {

	@Autowired
	private OwnerRepository ownerRepository;

	// Test de findById
	@Test
	void findById_shouldReturnOwner_whenIdExists() {
		Optional<Owner> owner = ownerRepository.findById(1);

		assertThat(owner).isNotNull();
		assertThat(owner.get().getFirstName());

	}

	// Test de findByLastNameStartingWith

}
