package com.example.Airport_Management.passager;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PassagerService {
    @Autowired
    private PassagerRepository passagerRepository;

    @Autowired(required = false)
    private TwilioService twilioService;

    // Regex pour valider l'email
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    // Regex pour valider le numéro de téléphone (format tunisien)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{8}$");

    public Passager addPassager(Passager passager) {
        // Validation des champs obligatoires
        validatePassager(passager);
        
        // Vérifier si l'email existe déjà
        Optional<Passager> existingPassager = passagerRepository.findByEmail(passager.getEmail());
        if (existingPassager.isPresent()) {
            throw new IllegalArgumentException("Un passager avec cet email existe déjà");
        }
        
        // Sauvegarder le passager
        Passager savedPassager = passagerRepository.save(passager);

        // Envoyer un SMS seulement si le service Twilio est disponible
        if (twilioService != null) {
            try {
                String messageBody = String.format("Bienvenue %s %s ! Votre compte a été créé avec succès sur notre application BagSeek.",
                        passager.getPrenom(), passager.getNom());
                String formattedNumber = "+216" + passager.getNumero();
                twilioService.sendSMS(formattedNumber, messageBody);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi du SMS: " + e.getMessage());
            }
        }

        return savedPassager;
    }
    
    /**
     * Valide les données du passager
     * @param passager Le passager à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validatePassager(Passager passager) {
        // Vérifier le nom
        if (!StringUtils.hasText(passager.getNom()) || passager.getNom().length() < 2) {
            throw new IllegalArgumentException("Le nom doit contenir au moins 2 caractères");
        }
        
        // Vérifier le prénom
        if (!StringUtils.hasText(passager.getPrenom()) || passager.getPrenom().length() < 2) {
            throw new IllegalArgumentException("Le prénom doit contenir au moins 2 caractères");
        }
        
        // Vérifier l'email
        if (!StringUtils.hasText(passager.getEmail()) || !EMAIL_PATTERN.matcher(passager.getEmail()).matches()) {
            throw new IllegalArgumentException("L'email n'est pas valide");
        }
        
        // Vérifier le numéro de téléphone
        String numeroStr = String.valueOf(passager.getNumero());
        if (!PHONE_PATTERN.matcher(numeroStr).matches()) {
            throw new IllegalArgumentException("Le numéro de téléphone doit contenir 8 chiffres");
        }
        
        String password = passager.getPassword();
        if (!StringUtils.hasText(password) || 
            password.length() < 6 || 
            !password.matches(".*[A-Z].*") ||  // Au moins une majuscule
            !password.matches(".*[a-z].*")) {  // Au moins une minuscule
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères, une majuscule et une minuscule");
        }
        
        // Vérifier le type
        if (passager.getType() == null) {
            throw new IllegalArgumentException("Le type de passager est obligatoire");
        }
    }

    public Passager updatePassager(String id, Passager passager) {
        if (passagerRepository.findById(id).isPresent()) {
            Passager existingPassager = passagerRepository.findById(id).get();
            existingPassager.setNom(passager.getNom());
            existingPassager.setPrenom(passager.getPrenom());
            existingPassager.setEmail(passager.getEmail());
            existingPassager.setNumero(passager.getNumero());
            existingPassager.setNotifications(passager.getNotifications());
            existingPassager.setHistoriqueDeclarations(passager.getHistoriqueDeclarations());
            return passagerRepository.save(existingPassager);
        } else {
            return null;
        }
    }

    public String deletePassager(String id) {
        if (passagerRepository.findById(id).isPresent()) {
            passagerRepository.deleteById(id);
            return "Passager supprimé avec succés";
        } else {
            return "Passager non supprimé";
        }
    }

    public List<Passager> getAll() {
        return passagerRepository.findAll();
    }

    public Passager getPassagerById(String id) {
        return passagerRepository.findById(id).orElse(null);
    }

    public List<Passager> getPassagerByNom(String nom) {
        return passagerRepository.findByNomContainingIgnoreCase(nom);
    }

    public Optional<Passager> getPassagerByEmail(String email) {
        return passagerRepository.findByEmail(email);
    }

    public Passager updatePassager(Passager passager) {
        return passagerRepository.save(passager); // Sauvegarde directement l'objet
    }
}