package kz.university.iam.dao;

/** Деректер қорымен жұмыс кезіндегі қате. */
public class DaoException extends RuntimeException {
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
