package BusinessLayer;

/**
 * interfata generica pentru validatori
 * permite definirea unor reguli de validare pentru diferite tipuri de obiecte
 *
 * @param <T> tipul obiectului care va fi validat
 */
public interface Validator<T> {
    /**
     * valideaza obiectul primit ca parametru
     * @param t (obiectul care trebuie validat)
     */
    public void validate(T t);
}

