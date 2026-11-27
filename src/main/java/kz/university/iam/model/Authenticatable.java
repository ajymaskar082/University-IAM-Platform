package kz.university.iam.model;

// >>> [ТАЛАП: ООП] БАСТАЛАДЫ — ИНТЕРФЕЙС: Authenticatable
/**
 * Жүйеге кіре алатын және ресурстарға қолжетімділігі тексерілетін нысан.
 */
public interface Authenticatable {

    String getUsername();

    /** Берілген ресурсқа (мысалы, "grades") қолжетімділік бар ма? */
    boolean canAccess(String resource);
}
// <<< [ТАЛАП: ООП] АЯҚТАЛДЫ — ИНТЕРФЕЙС: Authenticatable
