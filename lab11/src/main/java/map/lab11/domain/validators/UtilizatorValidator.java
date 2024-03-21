package map.lab11.domain.validators;

import map.lab11.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        //TODO: implement method validate
        String errors = "";
        if(entity.getFirstName().isEmpty() || entity.getFirstName().isBlank()){
            errors += "Numele nu poate fi gol!\n";
        }
        if(entity.getLastName().isEmpty() || entity.getLastName().isBlank()){
            errors += "Prenumele nu poate fi gol!\n";
        }
        if(entity.getUsername().isEmpty() || entity.getFirstName().isBlank()){
            errors += "Username-ul nu poate fi gol!\n";
        }
        if(entity.getPassword().isEmpty() || entity.getLastName().isBlank()){
            errors += "Trebuie sa introduceti o parola!\n";
        }
        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }
}
