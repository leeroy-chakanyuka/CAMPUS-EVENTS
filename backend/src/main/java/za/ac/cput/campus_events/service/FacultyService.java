package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.Faculty;
import za.ac.cput.campus_events.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService implements IFacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {

        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty save(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Optional<Faculty> findById(Long id) {
        return facultyRepository.findById(id);
    }

    @Override
    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public void deactivate(Long facultyId) {
        // Optional<Faculty> facultyOptional = facultyRepository.findById(facultyId);

        // if (facultyOptional.isPresent()) {
        //     Faculty faculty = facultyOptional.get();
        //     faculty.setStatus("Inactive");
        //     facultyRepository.save(faculty);
        // } else {
        //     throw new RuntimeException("Faculty not found with id: " + facultyId);
        // }
    }


    @Override
    public <T> T create(T t) {
        return null;
    }

    @Override
    public <T> T read(Long id) {
        return null;
    }

    @Override
    public <T> T update(T t) {
        return null;
    }

    @Override
    public <T> void delete(T t) {

    }

}
