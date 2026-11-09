
package edu.farmingdale.csc311.clubdirectory.club;

import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubRepository repository;

    public ClubController(ClubRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Club> getAllClubs(
            @RequestParam(required = false) String category) {

        if (category != null) {
            return repository.findByCategoryIgnoreCase(category);
        }

        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Club getClubById(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No club with id " + id
                ));
    }

    @PostMapping
    public ResponseEntity<Club> addClub(
            @Valid @RequestBody Club club) {

        Club newClub = new Club(
                club.getName(),
                club.getCategory(),
                club.getMeetingDay(),
                club.getRoom()
        );

        Club savedClub = repository.save(newClub);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedClub);
    }
}
