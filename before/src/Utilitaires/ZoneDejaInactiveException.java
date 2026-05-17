package before.src.Utilitaires;

// Exception levée si on tente de suspendre une zone déjà suspendue
public class ZoneDejaInactiveException extends RuntimeException {

    public ZoneDejaInactiveException(String message) {
        super(message);
    }
}
